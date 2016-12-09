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

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "activityId", "type", "status" })
@Document
public class ActivityRecord {

	@Id
	private String id;
	/**
	 *
	 * (Required)
	 *
	 */
	@JsonProperty("activityId")
	@NotNull
	private String activityId;
	/**
	 *
	 * (Required)
	 *
	 */
	@JsonProperty("type")
	@NotNull
	private ActivityRecord.Type type;

	@JsonProperty("status")
	@NotNull
	private Status status = Status.NEW;

	@JsonProperty("date")
	private DateTime date;

	@JsonProperty("creationDate")
	@NotNull
	@CreatedDate
	private DateTime creationDate;

	@JsonProperty("modifiedDate")
	@NotNull
	@LastModifiedDate
	private DateTime modifiedDate;

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 *
	 * (Required)
	 *
	 * @return The activityId
	 */
	@JsonProperty("activityId")
	public String getActivityId() {
		return activityId;
	}

	/**
	 *
	 * (Required)
	 *
	 * @param activityId
	 *            The activityId
	 */
	@JsonProperty("activityId")
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	/**
	 *
	 * (Required)
	 *
	 * @return The type
	 */
	@JsonProperty("type")
	public ActivityRecord.Type getType() {
		return type;
	}

	/**
	 *
	 * (Required)
	 *
	 * @param type
	 *            The type
	 */
	@JsonProperty("type")
	public void setType(ActivityRecord.Type type) {
		this.type = type;
	}

	/**
	 *
	 * (Required)
	 *
	 * @return The status
	 */
	@JsonProperty("status")
	public ActivityRecord.Status getStatus() {
		return status;
	}

	/**
	 *
	 * (Required)
	 *
	 * @param status
	 *            The status
	 */
	@JsonProperty("status")
	public void setStatus(ActivityRecord.Status status) {
		this.status = status;
	}

	/**
	 *
	 * @return The date
	 */
	@JsonProperty("date")
	public DateTime getDate() {
		return date;
	}

	/**
	 *
	 * @param date
	 *            The date
	 */
	@JsonProperty("date")
	public void setDate(DateTime date) {
		this.date = date;
	}

	@JsonProperty("creationDate")
	public DateTime getCreatedDate() {
		return creationDate;
	}

	@JsonIgnore
	public void setCreatedDate(DateTime creationDate) {
		this.creationDate = creationDate;
	}

	@JsonProperty("modifiedDate")
	public DateTime getModifiedDate() {
		return modifiedDate;
	}

	@JsonIgnore
	public void setModifiedDate(DateTime modifiedDate) {
		this.modifiedDate = creationDate;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	public static enum Type {

		INPUT("INPUT"), OUTPUT("OUTPUT"), START("START"), END("END"), PLAN("PLAN"), AGENT("AGENT"), USER("USER"), OTHER("OTHER");
		private final String value;
		private final static Map<String, ActivityRecord.Type> CONSTANTS = new HashMap<String, ActivityRecord.Type>();

		static {
			for (ActivityRecord.Type c : values()) {
				CONSTANTS.put(c.value, c);
			}
		}

		private Type(String value) {
			this.value = value;
		}

		@JsonValue
		@Override
		public String toString() {
			return this.value;
		}

		@JsonCreator
		public static ActivityRecord.Type fromValue(String value) {
			ActivityRecord.Type constant = CONSTANTS.get(value);
			if (constant == null) {
				throw new IllegalArgumentException(value);
			} else {
				return constant;
			}
		}

	}

	public static enum Status {

		NEW("NEW"), PUBLISHED("PUBLISHED");
		private final String value;
		private final static Map<String, ActivityRecord.Status> CONSTANTS = new HashMap<>();

		static {
			for (ActivityRecord.Status c : values()) {
				CONSTANTS.put(c.value, c);
			}
		}

		private Status(String value) {
			this.value = value;
		}

		@JsonValue
		@Override
		public String toString() {
			return this.value;
		}

		@JsonCreator
		public static ActivityRecord.Status fromValue(String value) {
			ActivityRecord.Status constant = CONSTANTS.get(value);
			if (constant == null) {
				throw new IllegalArgumentException(value);
			} else {
				return constant;
			}
		}
	}

}
