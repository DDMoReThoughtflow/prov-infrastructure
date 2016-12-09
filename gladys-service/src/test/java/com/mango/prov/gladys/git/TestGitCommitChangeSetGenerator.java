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
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

import com.mango.prov.gladys.vcs.VcsChange;
import com.mango.prov.gladys.vcs.VcsChangeType;
import com.mango.prov.gladys.vcs.VcsCommitChangeSet;


public class TestGitCommitChangeSetGenerator {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	public GitRepositoryFactory factory = new GitRepositoryFactory();

	public GitRepository repository;



	@Before
	public void extractGitRepository() throws IOException {
		File tempFolder = folder.getRoot();
		//move the archive to the temp folder.
		InputStream in = TestGitCommitChangeSetGenerator.class.getClassLoader().getResourceAsStream("static-project-for-testing-git-libs.git.tgz");
		File archive = new File(tempFolder, "static-project-for-testing-git-libs.git.tgz");
		try {
			FileUtils.copyInputStreamToFile(in, archive);

			File destination = new File(tempFolder, "repo");
			Archiver archiver = ArchiverFactory.createArchiver("tar", "gz");
			archiver.extract(archive, destination);

		} finally {
			IOUtils.closeQuietly(in);
		}
		String repositoryUrl = tempFolder + "/repo/static-project-for-testing-git-libs.git";
		repository = factory.clone(repositoryUrl);
	}

	@After
	public void closeRepository() throws IOException {
		repository.close();
	}

	@Test
	public void testSingleFileChange() throws IOException {
		VcsCommitChangeSet changeSet = repository.describeCommitsBetween("83af5c689115bc5ece9e699babe69336185c9756", "30102da0f790d6a4c79809e25305f7b36f29679d");
		List<VcsChange> changedFiles = changeSet.getFiles();
		Assert.assertEquals(1, changedFiles.size());
		VcsChange change = changedFiles.get(0);
		Assert.assertEquals(VcsChangeType.MODIFY, change.getChangeType());
		Assert.assertEquals("scientist.gemspec", change.getPath());
		Assert.assertEquals("scientist.gemspec", change.getNewPath());
		Assert.assertEquals("scientist.gemspec", change.getOldPath());
		Assert.assertEquals("30102da0f790d6a4c79809e25305f7b36f29679d", change.getPreviousRevision());
		Assert.assertEquals("22af2cc5713abd9b3dee8dc34d58e5b86541b7ff", change.getChecksum());

	}

}
