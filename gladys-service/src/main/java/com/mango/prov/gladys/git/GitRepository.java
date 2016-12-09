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
package com.mango.prov.gladys.git;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import com.mango.prov.gladys.vcs.VcsCommitChangeSet;
import com.mango.prov.gladys.vcs.VcsCommitHistoryPair;

public class GitRepository {

	private Git git;
	private File workingDirectory;

	GitRepository(Git git, File workingDirectory) {
		this.git = git;
		this.workingDirectory = workingDirectory;
	}

	public VcsCommitChangeSet describeCommitsBetween(String commitIdStr, String previousCommitIdStr) throws IOException {
		GitCommitChangeSetGenerator generator = new GitCommitChangeSetGenerator();
		return generator.generateChangeSet(git, commitIdStr, previousCommitIdStr);
	}

	public Collection<VcsCommitHistoryPair> getCommitsBetween(String latestCommitId, String previousCommitId) throws IOException {
		Set<VcsCommitHistoryPair> commitIds = new LinkedHashSet<>();
		Repository repository = null;
		RevWalk walk = null;
		try {
			repository = git.getRepository();
			walk = new RevWalk(repository);
			RevCommit commit = walk.parseCommit(ObjectId.fromString(latestCommitId));
			Deque<RevCommit> commitDeque = new LinkedList<>();
			commitDeque.push(commit);
			RevCommit  currentCommit = null;
			while((currentCommit = commitDeque.pollFirst()) != null) {
				RevCommit[] parentCommits = currentCommit.getParents();
				if(parentCommits != null && parentCommits.length > 0) {
					for(RevCommit parentCommit: parentCommits) {
						String parentCommitId = ObjectId.toString(parentCommit);
						String currentCommitId = ObjectId.toString(currentCommit);
						commitIds.add(new VcsCommitHistoryPair(currentCommitId, parentCommitId));
						if(!parentCommitId.equals(previousCommitId)) {
							commitDeque.add(walk.parseCommit(parentCommit));
						}
					}
				}
			}
		} catch (MissingObjectException | IncorrectObjectTypeException e) {
			throw new IOException("Could not walk Git repository structure", e);
		} finally {
			walk.close();
			repository.close();
		}
		return commitIds;
	}

	public void closeAndDelete() throws IOException {
		close();
		FileUtils.forceDelete(workingDirectory);
	}

	public void close() throws IOException {
		git.close();
	}

}
