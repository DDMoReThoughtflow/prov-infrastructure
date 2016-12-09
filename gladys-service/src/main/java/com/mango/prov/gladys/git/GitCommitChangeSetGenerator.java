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

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.mango.prov.gladys.vcs.Committer;
import com.mango.prov.gladys.vcs.VcsChange;
import com.mango.prov.gladys.vcs.VcsChangeType;
import com.mango.prov.gladys.vcs.VcsCommitChangeSet;

class GitCommitChangeSetGenerator {

	private static final String GIT_NULL = "/dev/null";
	private static final String GIT_REPOSITORY_TYPE = "git";

	private static final Logger LOGGER = LoggerFactory.getLogger(GitCommitChangeSetGenerator.class);

	VcsCommitChangeSet generateChangeSet(Git git, String commitIdStr, String previousCommitIdStr) throws IOException {

		VcsCommitChangeSet commitChangeSet = new VcsCommitChangeSet();
		ObjectId commitId = ObjectId.fromString(commitIdStr);
		ObjectId previousCommitId = ObjectId.fromString(previousCommitIdStr);
		Repository repository = git.getRepository();
		RevWalk walk = new RevWalk(repository);

		try (ObjectReader reader = repository.newObjectReader()) {
			RevCommit commit = walk.parseCommit(commitId);
			RevCommit previousCommit = walk.parseCommit(previousCommitId);

			setCommitter(commitChangeSet, commit);
			setCommitMessage(commitChangeSet, commit);
			setCommitId(commitChangeSet, commitIdStr);
			setCommitDate(commitChangeSet, commit);
			setRepositoryUri(commitChangeSet, git);
			setRepositoryType(commitChangeSet);
			setBranchName(commitChangeSet);
			setChanges(commitChangeSet, git, reader, commit, previousCommit);


		} finally {
			walk.close();
		}
		return commitChangeSet;
	}

	private void setChanges(VcsCommitChangeSet commitChangeSet, Git git, ObjectReader reader, RevCommit commit, RevCommit previousCommit) throws IOException {
		CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
		newTreeIter.reset(reader, commit.getTree());
		CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
		oldTreeIter.reset(reader, previousCommit.getTree());

		// finally get the list of changed files
		try {
			List<DiffEntry> diffs = git.diff()
			                    .setNewTree(newTreeIter)
			                    .setOldTree(oldTreeIter)
			                    .call();
			for (DiffEntry diff : diffs) {
				VcsChange change = describeDiff(git, diff, commit, previousCommit);
				commitChangeSet.addFile(change);
			}
		} catch (GitAPIException e) {
			throw new IOException("Could not read Git repository", e);
		}
	}

	private void setBranchName(VcsCommitChangeSet commitChangeSet) {
		// TODO Need to get the branch that I am supposedly walking.

	}

	private void setRepositoryType(VcsCommitChangeSet commitChangeSet) {
		commitChangeSet.setRepositoryType(GIT_REPOSITORY_TYPE);
	}

	private void setRepositoryUri(VcsCommitChangeSet commitChangeSet, Git git) {
		String origin = this.getOriginUrl(git);
		commitChangeSet.setRepositoryUri(origin);
	}

	private void setCommitDate(VcsCommitChangeSet commitChangeSet, RevCommit commit) {
		PersonIdent authorIdent = commit.getAuthorIdent();
		Date authorDate = authorIdent.getWhen();
		TimeZone authorTimeZone = authorIdent.getTimeZone();
		DateTimeZone timeZone = DateTimeZone.forTimeZone(authorTimeZone);
		DateTime dateTime = new DateTime(authorDate, timeZone);
		commitChangeSet.setCommitDate(dateTime);
	}

	private void setCommitId(VcsCommitChangeSet commitChangeSet, String commitIdStr) {
		commitChangeSet.setCommitId(commitIdStr);

	}

	private void setCommitMessage(VcsCommitChangeSet commitChangeSet, RevCommit commit) {
		String message = commit.getFullMessage();
		if(!StringUtils.isEmpty(message)) {
			commitChangeSet.setMessage(message);
		}

	}

	private void setCommitter(VcsCommitChangeSet commitChangeSet, RevCommit commit) {
		PersonIdent person = commit.getCommitterIdent();
		String name = person.getName();
		String email = person.getEmailAddress();
		Committer committer = new Committer();
		committer.setEmail(email);
		committer.setName(name);
		commitChangeSet.setCommitter(committer);
	}


	private VcsChange describeDiff(Git git, DiffEntry diff, RevCommit commitId, RevCommit previousCommitId) throws NoHeadException, GitAPIException, CorruptObjectException, IOException {

		ChangeType changeType = diff.getChangeType();

		String oldPath = normalizePath(diff.getOldPath());
		String newPath = normalizePath(diff.getNewPath());

		String previousRevisionId = null;
		Iterable<RevCommit> commits = git.log().add(commitId).addPath(newPath != null? newPath: oldPath).call();
		if(commits != null && commits.iterator().hasNext()) {
			RevCommit previousCommit = commits.iterator().next();
			if(previousCommit != null) {
				previousRevisionId = ObjectId.toString(previousCommit);
			}
		}

		String checksum = getChecksum(git, commitId, newPath);

		VcsChange gitChange = new VcsChange();
		gitChange.setPreviousRevision(previousRevisionId);
		gitChange.setNewPath(newPath);
		gitChange.setOldPath(oldPath);
		gitChange.setPath(newPath != null? newPath: oldPath);
		gitChange.setChangeType(VcsChangeType.convert(changeType));

		//TODO get the SHA-1 of the files.
		gitChange.setChecksum(checksum);
		gitChange.setChecksumType("SHA1");
		return gitChange;
	}

	private String getChecksum(Git git, RevCommit commit, String path) throws IOException {
		String checksum = null;
		if(!StringUtils.isEmpty(path)) {
	        RevTree tree = commit.getTree();

	        // now try to find a specific file
	        try (TreeWalk treeWalk = new TreeWalk(git.getRepository())) {
	            treeWalk.addTree(tree);
	            treeWalk.setRecursive(true);
	            treeWalk.setFilter(PathFilter.create(path));
	            if (!treeWalk.next()) {
	                LOGGER.info("Did not find {} in repository", path);
	            }

	            ObjectId objectId = treeWalk.getObjectId(0);
	            ObjectLoader loader = git.getRepository().open(objectId);
	            // and then one can the loader to read the file
	            try (InputStream objectStream = loader.openStream()) {
	            	checksum = DigestUtils.sha1Hex(objectStream);
	            }
	        }
		}
		return checksum;
	}

	private String normalizePath(String path) {
		if(StringUtils.isEmpty(path) || GIT_NULL.equals(path)) {
			return null;
		}
		return path;
	}

	public String getOriginUrl(Git git) {
		Config config = git.getRepository().getConfig();
		return config.getString("remote", "origin", "url");

	}
}
