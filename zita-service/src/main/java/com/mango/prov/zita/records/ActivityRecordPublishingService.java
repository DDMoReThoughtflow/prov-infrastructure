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
package com.mango.prov.zita.records;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class ActivityRecordPublishingService {

	@Value("${services.kev.header.key:x-kev-topic}")
	private final String topicHeader = "x-kev-topic";

	@Value("${services.kev.header.value:activityrecords}")
	private final String topic = "activityrecords";

	@Autowired
	private ActivityRecordRepository repository;

	@Autowired
	private RestTemplate restTemplate;

	private final Logger LOGGER = LoggerFactory.getLogger(ActivityRecordPublishingService.class);

	@Value("${services.kev.url:http://localhost:10050/topics/post}")
	private String kevUrl = "http://localhost:10050/topics/post";

	public boolean publish(String activityId) {

		LOGGER.info("Publishing activities with ID {}", activityId);
		List<ActivityRecord> records = getRecordsForActivity(activityId);
		if(records == null || records.isEmpty()) {
			throw new IllegalArgumentException("No resources matching that activityId");
		}
		ActivityPublishRequest request = new ActivityPublishRequest(activityId, records);
		HttpEntity<ActivityPublishRequest> entity = createHttpEntity(request);
	    LOGGER.info("Posting request {}", entity);
		ResponseEntity<String> response = restTemplate.postForEntity(kevUrl, entity, String.class);
		LOGGER.info("Received response {}", response);
		//TODO need to add error handling on to kev.
		if(!HttpStatus.OK.equals(response.getStatusCode())) {
			return false;
		}
		setRecordsAsPublished(records);
		return true;
	}

	private HttpEntity<ActivityPublishRequest> createHttpEntity(ActivityPublishRequest request) {
		HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.set(topicHeader, topic);

	    HttpEntity<ActivityPublishRequest> entity = new HttpEntity<>(request, headers);
		return entity;
	}

	private void setRecordsAsPublished(List<ActivityRecord> records) {
		// TODO Auto-generated method stub
		for(ActivityRecord record: records) {
			record.setStatus(ActivityRecord.Status.PUBLISHED);
		}

		repository.save(records);
	}

	private List<ActivityRecord> getRecordsForActivity(String activityId) {
		return repository.findAllByActivityId(activityId, new Sort(new Order(Direction.ASC, "creationDate")));
	}
}
