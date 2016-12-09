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
package com.mango.prov.potter;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.integration.support.json.JsonObjectMapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.CompositeTemplateLoader;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.mango.prov.potter.handlebars.ActivityRecordTemplateSelectorStrategy;
import com.mango.prov.potter.handlebars.GitTemplateSelectorStrategy;
import com.mango.prov.potter.handlebars.MetaCategoryTemplateSelectorStrategy;
import com.mango.prov.potter.handlebars.TemplateSelectorStrategy;

/**
 * Main Spring configuration
 *
 */
@Configuration
@ImportResource("classpath:META-INF/spring/spring-integration-context.xml")
@EnableIntegration
public class PotterConfiguration {

	//TODO this needs to be configurable
	public static final String FILESYSTEM_TEMPLATE_LOCATION = "templates";
	public static final String CLASSPATH_TEMPLATE_LOCATION = "/templates";

	@Bean(name="jacksonJodaTimeObjectMapper")
	public JsonObjectMapper getJacksonJodaTimeObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
	    mapper.registerModule(new JodaModule());
	    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	    return new Jackson2JsonObjectMapper(mapper);
	}

	@Bean
	public TemplateLoader createHandlebarsTemplateLoader() {
		//TODO make the templates folder configurable
		FileTemplateLoader fileTemplateLoader = new FileTemplateLoader(FILESYSTEM_TEMPLATE_LOCATION);
		ClassPathTemplateLoader classPathTemplateLoader = new ClassPathTemplateLoader(CLASSPATH_TEMPLATE_LOCATION);
		return new CompositeTemplateLoader(fileTemplateLoader, classPathTemplateLoader);
	}


//	@Bean
//	public List<TemplateSelectorStrategy> getTemplateSelectorStrategies() {
//		List<TemplateSelectorStrategy> strategies = Arrays.asList(new MetaCategoryTemplateSelectorStrategy(), new GitTemplateSelectorStrategy(), new ActivityRecordTemplateSelectorStrategy());
//		return strategies;
//	}


}
