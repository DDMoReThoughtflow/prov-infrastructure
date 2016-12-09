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

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.mango.prov.messaging.Parcel;
import com.mango.prov.potter.Application;
import com.mango.prov.potter.PotterConfiguration;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class HandlebarsTemplateTransformerTest {

	@Autowired
	private HandlebarsTemplateTransformer transformer;


	@Test
	public void testAddedFile() throws JsonParseException, JsonMappingException, IOException {
		runTest("/add_message.json");
	}

	@Test
	public void testModifiedFile() throws JsonParseException, JsonMappingException, IOException {
		runTest("/modify_message.json");
	}


	@Test
	public void testDeletedFile() throws JsonParseException, JsonMappingException, IOException {
		runTest("/delete_message.json");
	}

	@Test
	public void testRenamedFile() throws JsonParseException, JsonMappingException, IOException {
		runTest("/rename_message.json");
	}

	@Test
	public void testCopiedFile() throws JsonParseException, JsonMappingException, IOException {
		runTest("/copied_message.json");
	}

	@Test
	public void testAddModifyFile() throws JsonParseException, JsonMappingException, IOException {
		runTest("/add_modify_message.json");
	}

	@Test
	public void testModifyAddFile() throws JsonParseException, JsonMappingException, IOException {
		runTest("/modify_add_message.json");
	}

	private void runTest(String testConfig) throws JsonParseException, JsonMappingException, IOException {
		Message<Parcel> message = loadMessage(testConfig);
		Message<Parcel> result = transformer.transform(message);
		assertTransform(testConfig, Objects.toString(result.getPayload().getPayload()));
	}

	private Message<Parcel> loadMessage(String testData) throws JsonParseException, JsonMappingException, IOException {
		String jsonInString = new Scanner(HandlebarsTemplateTransformerTest.class.getResourceAsStream(testData), "UTF-8").useDelimiter("\\A").next();
		ObjectMapper mapper = new ObjectMapper();
		Map json = mapper.readValue(jsonInString, Map.class);
		Map headers = (Map) json.get("headers");
		Map payload = (Map) json.get("payload");
		Parcel parcel = new Parcel();
		parcel.setPayload(payload);
		Message<Parcel> message =  new GenericMessage<Parcel>(parcel, headers);
		return message;
	}

	private void assertTransform(String testData, String actual) throws JsonParseException, JsonMappingException, IOException {
		String jsonInString = new Scanner(HandlebarsTemplateTransformerTest.class.getResourceAsStream(testData), "UTF-8").useDelimiter("\\A").next();
		ObjectMapper mapper = new ObjectMapper();
		Map json = mapper.readValue(jsonInString, Map.class);
		Map<?,?> expected = (Map<?,?>) json.get("expected");
		// convert the expected to a string
		String expectedString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
		//convert actual to object then back to string
		Map actualMap = mapper.readValue(actual, Map.class);
		String actualString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(actualMap);
		Assert.assertEquals(expectedString, actualString);
	}


}
