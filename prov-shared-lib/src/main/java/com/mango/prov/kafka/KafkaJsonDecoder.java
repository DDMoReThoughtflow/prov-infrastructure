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

import kafka.serializer.Decoder;

public class KafkaJsonDecoder implements Decoder<Object> {

	private KafkaJsonDeserializer deserializer;

	public KafkaJsonDecoder() {
		this(Map.class);
	}

	public KafkaJsonDecoder(Class<?> targetClass) {
		this(new KafkaJsonDeserializer(targetClass));
	}

	public KafkaJsonDecoder(KafkaJsonDeserializer deserializer) {
		this.deserializer = deserializer;
	}

	@Override
	public Object fromBytes(byte[] data) {
		return deserializer.deserialize(data);
	}

}
