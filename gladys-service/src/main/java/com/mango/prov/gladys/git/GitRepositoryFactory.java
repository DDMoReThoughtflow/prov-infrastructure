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
import java.nio.file.Files;
import java.util.UUID;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Component;

@Component
public class GitRepositoryFactory {

	public GitRepository clone(String repositoryUrl) throws IOException {

		CloneCommand cloneCommand = Git.cloneRepository();
		File tempGitDirectory = Files.createTempDirectory(UUID.randomUUID().toString()).toFile();
		cloneCommand.setDirectory(tempGitDirectory);
		cloneCommand.setURI(repositoryUrl);
		cloneCommand.setCloneAllBranches(true);
		cloneCommand.setBare(true);
		cloneCommand.setRemote("origin");

		Git git;
		try {
			git = cloneCommand.call();
			return new GitRepository(git, tempGitDirectory);
		} catch (GitAPIException e) {
			throw new IOException("Could not clone Git repository at " + repositoryUrl, e);
		}

	}

}
