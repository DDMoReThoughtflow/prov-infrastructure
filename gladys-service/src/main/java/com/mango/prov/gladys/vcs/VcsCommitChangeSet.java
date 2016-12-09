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
package com.mango.prov.gladys.vcs;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;

public class VcsCommitChangeSet {

	private Committer committer;

	private String message;

	private DateTime commitDate;

	private String commitId;

	private String repositoryUri;

	private String repositoryType;

	private String branchName;

	private List<VcsChange> files = new LinkedList<>();

	public Committer getCommitter() {
		return committer;
	}

	public void setCommitter(Committer committer) {
		this.committer = committer;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public DateTime getCommitDate() {
		return commitDate;
	}

	public void setCommitDate(DateTime commitDate) {
		this.commitDate = commitDate;
	}

	public String getCommitId() {
		return commitId;
	}

	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}

	public String getRepositoryUri() {
		return repositoryUri;
	}

	public void setRepositoryUri(String originUri) {
		this.repositoryUri = originUri;
	}

	public String getRepositoryType() {
		return repositoryType;
	}

	public void setRepositoryType(String repositoryType) {
		this.repositoryType = repositoryType;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public List<VcsChange> getFiles() {
		return files;
	}

	public void setFiles(List<VcsChange> files) {
		this.files = new LinkedList<>(files);
	}

	public void addFiles(List<VcsChange> files) {
		this.files.addAll(files);
	}

	public void addFile(VcsChange change) {
		this.files.add(change);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((branchName == null) ? 0 : branchName.hashCode());
		result = prime * result + ((commitDate == null) ? 0 : commitDate.hashCode());
		result = prime * result + ((commitId == null) ? 0 : commitId.hashCode());
		result = prime * result + ((committer == null) ? 0 : committer.hashCode());
		result = prime * result + ((files == null) ? 0 : files.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((repositoryType == null) ? 0 : repositoryType.hashCode());
		result = prime * result + ((repositoryUri == null) ? 0 : repositoryUri.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VcsCommitChangeSet other = (VcsCommitChangeSet) obj;
		if (branchName == null) {
			if (other.branchName != null)
				return false;
		} else if (!branchName.equals(other.branchName))
			return false;
		if (commitDate == null) {
			if (other.commitDate != null)
				return false;
		} else if (!commitDate.equals(other.commitDate))
			return false;
		if (commitId == null) {
			if (other.commitId != null)
				return false;
		} else if (!commitId.equals(other.commitId))
			return false;
		if (committer == null) {
			if (other.committer != null)
				return false;
		} else if (!committer.equals(other.committer))
			return false;
		if (files == null) {
			if (other.files != null)
				return false;
		} else if (!files.equals(other.files))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (repositoryType == null) {
			if (other.repositoryType != null)
				return false;
		} else if (!repositoryType.equals(other.repositoryType))
			return false;
		if (repositoryUri == null) {
			if (other.repositoryUri != null)
				return false;
		} else if (!repositoryUri.equals(other.repositoryUri))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "VcsCommitChangeSet [committer=" + committer + ", message=" + message + ", commitDate=" + commitDate
				+ ", commitId=" + commitId + ", repositoryUri=" + repositoryUri + ", repositoryType=" + repositoryType
				+ ", branchName=" + branchName + ", files=" + files + "]";
	}




}
