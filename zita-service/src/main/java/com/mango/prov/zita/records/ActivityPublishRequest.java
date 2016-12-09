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
package com.mango.prov.zita.records;

import java.util.List;

public class ActivityPublishRequest {

	private String activityId;

	private List<ActivityRecord> activityRecords;

	public ActivityPublishRequest(String activityId, List<ActivityRecord> activityRecords) {
		this.activityId = activityId;
		this.activityRecords = activityRecords;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public List<ActivityRecord> getActivityRecords() {
		return activityRecords;
	}

	public void setActivityRecords(List<ActivityRecord> activityRecords) {
		this.activityRecords = activityRecords;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activityId == null) ? 0 : activityId.hashCode());
		result = prime * result + ((activityRecords == null) ? 0 : activityRecords.hashCode());
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
		ActivityPublishRequest other = (ActivityPublishRequest) obj;
		if (activityId == null) {
			if (other.activityId != null)
				return false;
		} else if (!activityId.equals(other.activityId))
			return false;
		if (activityRecords == null) {
			if (other.activityRecords != null)
				return false;
		} else if (!activityRecords.equals(other.activityRecords))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ActivityPublishRequest [activityId=" + activityId + ", activityRecords=" + activityRecords + "]";
	}



}
