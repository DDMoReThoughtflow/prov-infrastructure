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
package com.mango.prov.kafka;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

public class KafkaJsonSerializer implements Serializer<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaJsonSerializer.class);

    private ObjectMapper mapper;

    public KafkaJsonSerializer() {
		this(new ObjectMapper());
    }

    public KafkaJsonSerializer(ObjectMapper mapper) {
    	this.mapper = mapper;
    	mapper.registerModule(new JodaModule());
	    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public byte[] serialize(String topic, Object data) {
		return this.serialize(data);
	}

	public byte[] serialize(Object data) {
		try {
			if(data != null) {
				return mapper.writeValueAsBytes(data);
			}
		} catch (JsonProcessingException e) {
			LOGGER.error("Json processing failed for object: {}", data, e);
		}
		return "".getBytes();
	}

	@Override
	public void close() {

	}

}
