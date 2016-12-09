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
package com.mango.prov.spring;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class MapMessageSplitter {

	public Object splitMessage(Message<?> message) {
		Object payload = message.getPayload();
		if(payload instanceof Map) {
			payload = flattenMap((Map) payload);
		}
		return payload;
	}

	private Collection<Object> flattenMap(Map<Object, Object> map) {
		Collection<Object> payloads = new ArrayList<>();
		for(Entry<Object, Object> entry: map.entrySet()) {
			Object value = entry.getValue();
			if(value instanceof Collection) {
				payloads.addAll((Collection) value);
			} else if(value instanceof Map) {
				payloads.addAll(flattenMap((Map) value));
			} else {
				payloads.add(value);
			}
		}
		return payloads;
	}

}
