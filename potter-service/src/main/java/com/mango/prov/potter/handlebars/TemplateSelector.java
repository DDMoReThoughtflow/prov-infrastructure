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
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.github.jknack.handlebars.io.TemplateLoader;
import com.github.jknack.handlebars.io.TemplateSource;
import com.mango.prov.messaging.Parcel;

@Component
public class TemplateSelector {

	private Logger LOGGER = LoggerFactory.getLogger(TemplateSelector.class);

	private TemplateLoader templateLoader;

	private List<TemplateSelectorStrategy> templateSelectorStrategyChain;

	@Autowired
	public TemplateSelector(TemplateLoader templateLoader, List<TemplateSelectorStrategy> strategies) {
		this.templateLoader = templateLoader;
		this.templateSelectorStrategyChain = strategies;
	}

	public TemplateSource getTemplateName(Message<Parcel> message) {
		TemplateSource template = null;
		for(TemplateSelectorStrategy selector: templateSelectorStrategyChain) {
			String name = selector.getTemplateName(message);
			template = resolveTemplateSource(name);
			if(template != null) {
				break;
			}
		}
		return template;
	}

	private TemplateSource resolveTemplateSource(String name) {
		TemplateSource template = null;
		if(StringUtils.isNotBlank(name)) {
			try {
				template = templateLoader.sourceAt(name);
			} catch (IOException e) {
				LOGGER.debug("Template {} does not exist.", name);
			}
		}
		return template;
	}


}
