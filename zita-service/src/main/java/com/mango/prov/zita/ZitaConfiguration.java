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
package com.mango.prov.zita;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

/**
 * Main Spring configuration
 *
 */
@Configuration
@EnableMongoAuditing
public class ZitaConfiguration {

	@Bean
	public RestTemplate restTemplate() {
	    final RestTemplate restTemplate = new RestTemplate();

	    //find and replace Jackson message converter with our own
	    for (int i = 0; i < restTemplate.getMessageConverters().size(); i++) {
	        final HttpMessageConverter<?> httpMessageConverter = restTemplate.getMessageConverters().get(i);
	        if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter){
	            restTemplate.getMessageConverters().set(i, mappingJackson2HttpMessageConverter());
	        }
	    }

	    return restTemplate;
	}

	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
	    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
	    converter.setObjectMapper(myObjectMapper());
	    return converter;
	}

	@Bean
	public ObjectMapper myObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
	    mapper.registerModule(new JodaModule());
	    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	    return mapper;
	}



	/**
	 * Null auditor, this should be replaced when auth is introduced.
	 * @return a default AuditorAware implementation that returns a static "UNDEFINED" for the current auditor.
	 */
	@Bean
	public AuditorAware<Object> getAuditor() {
		return new AuditorAware<Object>() {

			@Override
			public Object getCurrentAuditor() {
				return "UNDEFINED";
			}

		};
	}
}
