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

public class VcsCommitHistoryPair {

	private String currentCommit;
	private String previousCommit;

	public VcsCommitHistoryPair(String currentCommit, String previousCommit) {
		this.currentCommit = currentCommit;
		this.previousCommit = previousCommit;
	}

	public String getPreviousCommit() {
		return previousCommit;
	}


	public String getCurrentCommit() {
		return currentCommit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currentCommit == null) ? 0 : currentCommit.hashCode());
		result = prime * result + ((previousCommit == null) ? 0 : previousCommit.hashCode());
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
		VcsCommitHistoryPair other = (VcsCommitHistoryPair) obj;
		if (currentCommit == null) {
			if (other.currentCommit != null)
				return false;
		} else if (!currentCommit.equals(other.currentCommit))
			return false;
		if (previousCommit == null) {
			if (other.previousCommit != null)
				return false;
		} else if (!previousCommit.equals(other.previousCommit))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "VcsCommitHistoryPair [currentCommit=" + currentCommit + ", previousCommit=" + previousCommit + "]";
	}



}
