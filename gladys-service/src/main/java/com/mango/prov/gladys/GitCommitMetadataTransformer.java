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

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import com.mango.prov.gladys.git.GitRepository;
import com.mango.prov.gladys.git.GitRepositoryFactory;
import com.mango.prov.gladys.vcs.VcsCommitChangeSet;
import com.mango.prov.messaging.Parcel;

@Component
public class GitCommitMetadataTransformer {


	@Autowired
	private GitRepositoryFactory repositoryFactory = null;


	@Transformer
	public Message<Parcel> transform(Message<Parcel> message) throws IOException, InvalidRemoteException, TransportException, GitAPIException {
		MessageHeaders headers = message.getHeaders();

		String repoUrl = headers.get("vcs-repo").toString();
		String latestCommit = headers.get("vcs-latest-commit").toString();
		String previousCommit = headers.get("vcs-previous-commit").toString();
		String branchName = headers.get("vcs-branch-name").toString();
		VcsCommitChangeSet change = describeCommit(repoUrl, latestCommit, previousCommit, branchName);
		message.getPayload().setPayload(change);
		return message;

	}

	private VcsCommitChangeSet describeCommit(String repoUrl, String commit, String previousCommit, String branchName) throws IOException, InvalidRemoteException, TransportException, GitAPIException {
		GitRepository gitRepository = repositoryFactory.clone(repoUrl);

		try {
			return gitRepository.describeCommitsBetween(commit, previousCommit);
		} finally {
			gitRepository.closeAndDelete();
		}
	}

	public GitRepositoryFactory getRepositoryFactory() {
		return repositoryFactory;
	}

	public void setRepositoryFactory(GitRepositoryFactory repositoryFactory) {
		this.repositoryFactory = repositoryFactory;
	}



}
