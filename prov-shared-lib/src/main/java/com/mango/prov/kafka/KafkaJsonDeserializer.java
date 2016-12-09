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

import java.io.IOException;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

public class KafkaJsonDeserializer implements Deserializer<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaJsonDeserializer.class);

    private ObjectMapper mapper;

    private Class<?> targetClass;

    public KafkaJsonDeserializer() {
		this(new ObjectMapper());
    }

    public KafkaJsonDeserializer(Class<?> targetClass) {
		this(new ObjectMapper(), targetClass);
    }

    public KafkaJsonDeserializer(ObjectMapper mapper) {
    	this(mapper, Map.class);
    }

    public KafkaJsonDeserializer(ObjectMapper mapper, Class<?> targetClass) {
    	this.mapper = mapper;
    	mapper.registerModule(new JodaModule());
	    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	    this.targetClass = targetClass;
    }

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public void close() {

	}

	@Override
	public Object deserialize(String topic, byte[] data) {
		return this.deserialize(data);
	}

	public Object deserialize(byte[] data) {
        try {
			return mapper.readValue(data, targetClass);
		} catch (IOException e) {
			LOGGER.error("Could not deserialize class {} from data {}", targetClass, data, e);
		}
        return null;
	}

}
