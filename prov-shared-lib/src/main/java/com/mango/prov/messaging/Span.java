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

package com.mango.prov.messaging;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Span
 * <p>
 * A span contains the tracking information for a request moving through the system.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "id",
    "in",
    "out",
    "ip",
    "name"
})
public class Span {

    /**
     * The Trace ID
     * <p>
     * The trace id for the span.
     *
     */
    @JsonProperty("id")
    private String id;
    /**
     * In Date Time
     * <p>
     * The date time for the start of the span formatted as an ISO 8601 date time string
     *
     */
    @JsonProperty("in")
    private DateTime in;
    /**
     * Out Date Time
     * <p>
     * The date time for the end of the span formatted as an ISO 8601 date time string
     *
     */
    @JsonProperty("out")
    private DateTime out;
    /**
     * Host
     * <p>
     * The hostname / ip address of where the span was executed.
     *
     */
    @JsonProperty("ip")
    private String ip;
    /**
     * Process
     * <p>
     * The name of the process that encompassed the span.
     *
     */
    @JsonProperty("name")
    private String name;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * The Trace ID
     * <p>
     * The trace id for the span.
     *
     * @return
     *     The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * The Trace ID
     * <p>
     * The trace id for the span.
     *
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * In Date Time
     * <p>
     * The date time for the start of the span formatted as an ISO 8601 date time string
     *
     * @return
     *     The in
     */
    @JsonProperty("in")
    public DateTime getIn() {
        return in;
    }

    /**
     * In Date Time
     * <p>
     * The date time for the start of the span formatted as an ISO 8601 date time string
     *
     * @param in
     *     The in
     */
    @JsonProperty("in")
    public void setIn(DateTime in) {
        this.in = in;
    }

    /**
     * Out Date Time
     * <p>
     * The date time for the end of the span formatted as an ISO 8601 date time string
     *
     * @return
     *     The out
     */
    @JsonProperty("out")
    public DateTime getOut() {
        return out;
    }

    /**
     * Out Date Time
     * <p>
     * The date time for the end of the span formatted as an ISO 8601 date time string
     *
     * @param out
     *     The out
     */
    @JsonProperty("out")
    public void setOut(DateTime out) {
        this.out = out;
    }

    /**
     * Host
     * <p>
     * The hostname / ip address of where the span was executed.
     *
     * @return
     *     The ip
     */
    @JsonProperty("ip")
    public String getIp() {
        return ip;
    }

    /**
     * Host
     * <p>
     * The hostname / ip address of where the span was executed.
     *
     * @param ip
     *     The ip
     */
    @JsonProperty("ip")
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Process
     * <p>
     * The name of the process that encompassed the span.
     *
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Process
     * <p>
     * The name of the process that encompassed the span.
     *
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }


    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((additionalProperties == null) ? 0 : additionalProperties.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((in == null) ? 0 : in.hashCode());
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((out == null) ? 0 : out.hashCode());
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
		Span other = (Span) obj;
		if (additionalProperties == null) {
			if (other.additionalProperties != null)
				return false;
		} else if (!additionalProperties.equals(other.additionalProperties))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (in == null) {
			if (other.in != null)
				return false;
		} else if (!in.equals(other.in))
			return false;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (out == null) {
			if (other.out != null)
				return false;
		} else if (!out.equals(other.out))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Span [id=" + id + ", in=" + in + ", out=" + out + ", ip=" + ip + ", name=" + name
				+ ", additionalProperties=" + additionalProperties + "]";
	}


}
