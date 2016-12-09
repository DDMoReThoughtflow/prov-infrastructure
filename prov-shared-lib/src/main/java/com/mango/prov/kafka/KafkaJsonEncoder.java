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

import kafka.serializer.Encoder;

public class KafkaJsonEncoder implements Encoder<Object> {

	private KafkaJsonSerializer serializer;

	public KafkaJsonEncoder() {
		this.serializer = new KafkaJsonSerializer();
	}

	@Override
	public byte[] toBytes(Object data) {
		return serializer.serialize(data);
	}

}
