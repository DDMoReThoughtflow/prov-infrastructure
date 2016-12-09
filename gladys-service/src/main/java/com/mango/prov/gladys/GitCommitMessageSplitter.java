/*******************************************************************************
* Copyright (C) 2016 Mango Business Solutions Ltd, [http://www.mango-solutions.com]
*
* This program is free software: you can redistribute it and/or modify it under
* the terms of the GNU Affero General Public License as published by the
* Free Software Foundation, version 3.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
* or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
* for more details.
*
* You should have received a copy of the GNU Affero General Public License along
* with this program. If not, see <http://www.gnu.org/licenses/agpl-3.0.html>.
*******************************************************************************/
package com.mango.prov.gladys;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Splitter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import com.mango.prov.gladys.git.GitRepository;
import com.mango.prov.gladys.git.GitRepositoryFactory;
import com.mango.prov.gladys.vcs.VcsCommitHistoryPair;


/**
 * Receives the commit message, interrogates the commit and creates an event for
 * every file in the commit
 *
 */
@Component
public class GitCommitMessageSplitter  {

	@Autowired
	private GitRepositoryFactory repositoryFactory = null;


	@Splitter
	public Object splitMessage(Message<Object> message) throws IOException, InvalidRemoteException, TransportException, GitAPIException {
		MessageHeaders headers = message.getHeaders();

		String repoUrl = headers.get("vcs-repo").toString();
		String latestCommit = headers.get("vcs-latest-commit").toString();
		String previousCommit = headers.get("vcs-previous-commit").toString();
		String branchName = headers.get("vcs-branch-name").toString();

		Collection<VcsCommitHistoryPair> commitIdList = getParentCommitIds(repoUrl, latestCommit, previousCommit, branchName);
		Collection<Message<Object>> newMessages = new LinkedList<>();
		for(VcsCommitHistoryPair commitId: commitIdList) {
			Map<String, Object> newHeaders = new HashMap<>(headers);
			newHeaders.put("vcs-latest-commit", commitId.getCurrentCommit());
			newHeaders.put("vcs-previous-commit", commitId.getPreviousCommit());
			//TODO I don't like this I think each of the messages will be sharing a payload object rather than getting a copy of it.
			Message<Object> newMessage = new GenericMessage(message.getPayload(), newHeaders);
			newMessages.add(newMessage);
		}
		return newMessages;
	}

	private Collection<VcsCommitHistoryPair> getParentCommitIds(String repoUrl, String latestCommit, String previousCommit, String branchName) throws IOException, InvalidRemoteException, TransportException, GitAPIException {
		Collection<VcsCommitHistoryPair> commitIds = new LinkedHashSet<>();
		GitRepository gitRepository = repositoryFactory.clone(repoUrl);

		try {
			commitIds = gitRepository.getCommitsBetween(latestCommit, previousCommit);
		} finally {
			gitRepository.closeAndDelete();
		}
		return commitIds;
	}

	public GitRepositoryFactory getRepositoryFactory() {
		return repositoryFactory;
	}

	public void setRepositoryFactory(GitRepositoryFactory repositoryFactory) {
		this.repositoryFactory = repositoryFactory;
	}

}
