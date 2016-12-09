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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@BasePathAwareController
@RequestMapping(value = "/activityRecords")
public class ActivityRecordResourceController implements ResourceProcessor<Resource<ActivityRecord>> {

	@Autowired
	private ActivityRecordPublishingService publisher;

	@Autowired
	private EntityLinks entityLinks;

	//TODO should add on a global exception handler.
	@RequestMapping(value = "/publish", method = RequestMethod.GET)
	public ResponseEntity<Resource<Object>> lookup(@RequestParam("activityId") String activityId) {
		try {
			boolean published = publisher.publish(activityId);
			if(published) {
				return new ResponseEntity<Resource<Object>>(HttpStatus.OK);
			} else {
				return new ResponseEntity<Resource<Object>>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (IllegalArgumentException e){
			return new ResponseEntity<Resource<Object>>(new Resource<Object>(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public Resource<ActivityRecord> process(Resource<ActivityRecord> resource) {

		LinkBuilder lb = entityLinks.linkFor(ActivityRecord.class, "name");
		resource.add(new Link(lb.toString() + "/publish{?activityId}", "publish"));
		return resource;
	}

}
