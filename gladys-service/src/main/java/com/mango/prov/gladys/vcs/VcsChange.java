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

public class VcsChange {

	private VcsChangeType changeType;

	private String oldPath;

	private String newPath;

	private String checksum;

	private String checksumType;

	private String previousRevision;

	private String path;

	public VcsChangeType getChangeType() {
		return changeType;
	}

	public void setChangeType(VcsChangeType changeType) {
		this.changeType = changeType;
	}

	public String getOldPath() {
		return oldPath;
	}

	public void setOldPath(String oldUri) {
		this.oldPath = oldUri;
	}

	public String getNewPath() {
		return newPath;
	}

	public void setNewPath(String newUri) {
		this.newPath = newUri;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return this.path;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public String getChecksumType() {
		return checksumType;
	}

	public void setChecksumType(String checksumType) {
		this.checksumType = checksumType;
	}

	public String getPreviousRevision() {
		return previousRevision;
	}

	public void setPreviousRevision(String previousRevision) {
		this.previousRevision = previousRevision;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((changeType == null) ? 0 : changeType.hashCode());
		result = prime * result + ((checksum == null) ? 0 : checksum.hashCode());
		result = prime * result + ((checksumType == null) ? 0 : checksumType.hashCode());
		result = prime * result + ((newPath == null) ? 0 : newPath.hashCode());
		result = prime * result + ((oldPath == null) ? 0 : oldPath.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + ((previousRevision == null) ? 0 : previousRevision.hashCode());
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
		VcsChange other = (VcsChange) obj;
		if (changeType != other.changeType)
			return false;
		if (checksum == null) {
			if (other.checksum != null)
				return false;
		} else if (!checksum.equals(other.checksum))
			return false;
		if (checksumType == null) {
			if (other.checksumType != null)
				return false;
		} else if (!checksumType.equals(other.checksumType))
			return false;
		if (newPath == null) {
			if (other.newPath != null)
				return false;
		} else if (!newPath.equals(other.newPath))
			return false;
		if (oldPath == null) {
			if (other.oldPath != null)
				return false;
		} else if (!oldPath.equals(other.oldPath))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		if (previousRevision == null) {
			if (other.previousRevision != null)
				return false;
		} else if (!previousRevision.equals(other.previousRevision))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "VcsChange [changeType=" + changeType + ", oldPath=" + oldPath + ", newPath=" + newPath + ", checksum="
				+ checksum + ", checksumType=" + checksumType + ", previousRevision=" + previousRevision + ", path="
				+ path + "]";
	}



}
