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

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import com.mango.prov.messaging.Meta;
import com.mango.prov.messaging.Parcel;


public class MetaCategoryTemplateSelectorStrategyTest {

	private TemplateSelectorStrategy selector = null;

	@Before
	public void setUp() {
		selector = new MetaCategoryTemplateSelectorStrategy();
	}

	@Test
	public void testGetGitNameFromPayload() {
		Parcel parcel = new Parcel();
		Meta meta = new Meta();
		meta.setAdditionalProperty("category", "banana");
		parcel.setMeta(meta);
		Message<Parcel> message = new GenericMessage<>(parcel);
		String templateName = selector.getTemplateName(message);
		Assert.assertNotNull(templateName);
		Assert.assertEquals("banana", templateName);
	}


	@Test
	public void testGetMissingNameFromPayload() {
		Parcel parcel = new Parcel();
		Map<String, String> payload = new HashMap<>();
		parcel.setPayload(payload);
		payload.put("banana", "apple");
		Message<Parcel> message = new GenericMessage<>(parcel);
		String templateName = selector.getTemplateName(message);
		Assert.assertNull(templateName);
	}

}
