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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Jackson2Helper;
import com.github.jknack.handlebars.JsonNodeValueResolver;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.context.FieldValueResolver;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.helper.AssignHelper;
import com.github.jknack.handlebars.helper.StringHelpers;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.github.jknack.handlebars.io.TemplateSource;
import com.mango.prov.messaging.Parcel;

@Component
public class HandlebarsTemplateTransformer {

	private static final String MODEL_HEADERS = "headers";

	private static final String MODEL_PAYLOAD = "payload";

	private static final String TEMPLATE_NAME_HEADER = "templateName";

	private static final Logger LOGGER = LoggerFactory.getLogger(HandlebarsTemplateTransformer.class);

	@Autowired
	private TemplateLoader templateLoader;

	@Autowired
	private TemplateSelector selector;

	@Transformer
	public Message<Parcel> transform(Message<Parcel> message) throws IOException {
		MessageHeaders headers = message.getHeaders();
		Parcel parcel = message.getPayload();
		Map<Object, Object> model = createModel(headers, parcel.getPayload());
		TemplateSource templateSource = resolveTemplateName(message);
		Handlebars handlebars = getHandlebars();
		Template template = handlebars.compile(templateSource);
		Context context = createContext(model);
		String value = template.apply(context);
		parcel.setPayload(value);
		LOGGER.debug("Used {} template to transform {} into {} ", templateSource, message, value);
		return message;
	}

	private Map<Object, Object> createModel(MessageHeaders headers, Object payload) {
		Map<Object, Object> model = new HashMap<>();
		model.put(MODEL_PAYLOAD, payload);
		model.put(MODEL_HEADERS, headers);
		return model;
	}

	private Context createContext(Map<Object, Object> model) {
		Context context = Context.newBuilder(model)
				.resolver(
						MapValueResolver.INSTANCE,
						JavaBeanValueResolver.INSTANCE,
						FieldValueResolver.INSTANCE,
						JsonNodeValueResolver.INSTANCE)
				.build();
		return context;
	}

	private Handlebars getHandlebars() {
		Handlebars handlebars = new Handlebars(templateLoader);
		handlebars.registerHelper("json", Jackson2Helper.INSTANCE);
		handlebars.registerHelpers(StringHelpers.class);
		handlebars.registerHelper("assign", new AssignHelper());
		return handlebars;
	}

	private TemplateSource resolveTemplateName(Message<Parcel> message) {
		return selector.getTemplateName(message);
	}

	public TemplateLoader getTemplateLoader() {
		return templateLoader;
	}

	public void setTemplateLoader(TemplateLoader templateLoader) {
		this.templateLoader = templateLoader;
	}

}
