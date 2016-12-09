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
package com.mango.prov.potter.handlebars;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.mango.prov.messaging.Meta;
import com.mango.prov.messaging.Parcel;

public abstract class AbstractJsonPathTemplateSelectorStrategy implements TemplateSelectorStrategy {

	private final Logger LOGGER = LoggerFactory.getLogger(GitTemplateSelectorStrategy.class);
	private String jsonPath;

	public AbstractJsonPathTemplateSelectorStrategy(String jsonPath) {
		this.jsonPath = jsonPath;
	}

	@Override
	public String getTemplateName(Message<Parcel> message) {
		String name = this.getTemplateNameFromParcel(message);
		if(StringUtils.isBlank(name)) {
			name = this.getTemplateNameFromMeta(message);
		}
		return StringUtils.trimToNull(name);
	}

	public String getTemplateNameFromParcel(Message<Parcel> message) {
		Parcel parcel = message.getPayload();
		String value = null;
		try {
			Object payload = parcel.getPayload();
			if(payload instanceof String) {
				value = JsonPath.read((String) payload, jsonPath);
			} else {
				value = JsonPath.read(payload, jsonPath);
			}
		} catch (PathNotFoundException e) {
			LOGGER.debug("Path {} not found on payload in parcel {}", jsonPath, parcel);
		}

		return StringUtils.trimToNull(value);
	}

	public String getTemplateNameFromMeta(Message<Parcel> message) {
		Parcel parcel = message.getPayload();
		String value = null;
		try {
			Meta meta = parcel.getMeta();
			if(meta != null) {
				Map<String, Object> additionalProperties = meta.getAdditionalProperties();
				if(additionalProperties != null) {
					value = JsonPath.read(additionalProperties, jsonPath);
				}
			}
		} catch (PathNotFoundException e) {
			LOGGER.debug("Path {} not found on meta in parcel {}", jsonPath, parcel);
		}

		return StringUtils.trimToNull(value);
	}



}
