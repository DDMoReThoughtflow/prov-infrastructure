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

import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.mango.prov.messaging.Parcel;

@Component
public class GitTemplateSelectorStrategy extends AbstractJsonPathTemplateSelectorStrategy {

	private static final String JSON_PATH = "$.repositoryType";

	public GitTemplateSelectorStrategy() {
		super(JSON_PATH);
	}

	@Override
	public String getTemplateName(Message<Parcel> message) {
		String name = super.getTemplateNameFromParcel(message);
		if(StringUtils.isNotBlank(name)) {
			name = name + "/commit";
		}
		return name;
	}

}
