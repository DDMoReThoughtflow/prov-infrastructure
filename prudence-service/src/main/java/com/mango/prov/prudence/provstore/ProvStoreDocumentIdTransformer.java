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
package com.mango.prov.prudence.provstore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

/**
 * Resolves the document name to the ProvStore document id.
 * If there is no document of that name then it will create one.
 *
 */
@Component
public class ProvStoreDocumentIdTransformer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProvStoreDocumentIdTransformer.class);

	@Value("${provstore.urls.base:https://provenance.ecs.soton.ac.uk/store/}api/v0/documents/")
	private String provStoreQueryDocumentByNameApiUrl = "https://provenance.ecs.soton.ac.uk/store/api/v0/documents/";

	@Value("${provstore.urls.base:https://provenance.ecs.soton.ac.uk/store/}api/v0/documents/")
	private String provStoreCreateDocumentApiUrl = "https://provenance.ecs.soton.ac.uk/store/api/v0/documents/";



	@Value("${provstore.credentials.username}")
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	@Value("${provstore.credentials.apikey}")
	private String apiKey;

	@Transformer
	public String transform(Message message) throws JsonProcessingException {
		String documentName = getDocumentName(message);
		LOGGER.debug("Document name resolved to {}", documentName);
		if(documentName == null) {
			throw new RuntimeException("Could not resolve document name.");
		}
		String documentId = getDocumentId(documentName);
		if(StringUtils.isEmpty(documentId)) {
			LOGGER.debug("Creating new document with name {}", documentName);
			documentId = createEmptyDocument(documentName);
		}
		LOGGER.debug("Document id resolved to {}", documentId);
		return documentId;
	}

	private String createEmptyDocument(String documentName) throws JsonProcessingException {
		String id = null;
		Map content = createEmptyDocumentMap(documentName);
		HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.set("Authorization", "ApiKey " + username + ":" + apiKey);

	    UriComponentsBuilder builder =UriComponentsBuilder.fromHttpUrl(provStoreCreateDocumentApiUrl);

	    String contentString = new ObjectMapper().writeValueAsString(content);

	    HttpEntity<?> entity = new HttpEntity<>(contentString, headers);

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.POST, entity, String.class);
		String body = response.getBody();
		LOGGER.debug("Response from creating document: {}", body);
		id = Integer.toString((int) JsonPath.read(body, "$.id"));
		return id;
	}

	private Map createEmptyDocumentMap(String documentName) {
		Map<String, Object> document = new HashMap<>();
		Map<String, Object> content = new HashMap<>();
		content.put("prefix", new HashMap());
		document.put("content", content);
		document.put("rec_id", documentName);
		return document;
	}

	private String getDocumentId(String documentName) {
		String id = null;
		HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.set("Authorization", "ApiKey " + username + ":" + apiKey);

	    UriComponentsBuilder builder =UriComponentsBuilder.fromHttpUrl(provStoreQueryDocumentByNameApiUrl)
	    	.queryParam("document_name", documentName);

	    HttpEntity<?> entity = new HttpEntity<>(headers);

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
		String body = response.getBody();
		LOGGER.debug("Response from querying for document with name {} document: {}", documentName, body);
		int count = JsonPath.read(body, "$.meta.total_count");
		if(count != 0) {
			id = Integer.toString((int) JsonPath.read(body, "$.objects[0].id"));
		}
		return id;
	}

	private String getDocumentName(Message message) {
		MessageHeaders headers = message.getHeaders();
		return headers.get("documentName", String.class);
	}




}
