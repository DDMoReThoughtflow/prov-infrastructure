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
package com.mango.prov.kev;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class HeadersToPayloadTransformer {

	@Value("${transformers.headersToPayload.pattern:.*}")
	private String headerPattern;

	@Value("${transformers.headersToPayload.originalPayloadKey:}")
	private String originalPayloadKey;

	@Value("${transformers.headersToPayload.originalHeaderKey:}")
	private String originalHeadersKey;

	@Transformer
	public Map<String, Object> transform(Message message) {
		MessageHeaders headers = message.getHeaders();
		Map<String, Object> newPayload = new HashMap<>();

		Pattern pattern = Pattern.compile(headerPattern);
		for(Map.Entry<String, Object> entry: headers.entrySet()) {
			if(pattern.matcher(entry.getKey()).matches()) {
				newPayload.put(entry.getKey(), entry.getValue());
			}
		}
		if(!StringUtils.isEmpty(originalPayloadKey.trim())) {
			newPayload.put(originalPayloadKey.trim(), message.getPayload());

		}
		if(!StringUtils.isEmpty(originalHeadersKey.trim())) {
			Map<String, Object> allHeaders = new HashMap<>(headers);
			newPayload.put(originalHeadersKey.trim(), allHeaders);

		}
		return newPayload;
	}

	public String getHeaderPattern() {
		return headerPattern;
	}

	public void setHeaderPattern(String headerPattern) {
		this.headerPattern = headerPattern;
	}

	public String getOriginalPayloadKey() {
		return originalPayloadKey;
	}

	public void setOriginalPayloadKey(String originalPayloadKey) {
		this.originalPayloadKey = originalPayloadKey;
	}

	public String getOriginalHeadersKey() {
		return originalHeadersKey;
	}

	public void setOriginalHeadersKey(String originalHeadersKey) {
		this.originalHeadersKey = originalHeadersKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((headerPattern == null) ? 0 : headerPattern.hashCode());
		result = prime * result + ((originalHeadersKey == null) ? 0 : originalHeadersKey.hashCode());
		result = prime * result + ((originalPayloadKey == null) ? 0 : originalPayloadKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HeadersToPayloadTransformer other = (HeadersToPayloadTransformer) obj;
		if (headerPattern == null) {
			if (other.headerPattern != null)
				return false;
		} else if (!headerPattern.equals(other.headerPattern))
			return false;
		if (originalHeadersKey == null) {
			if (other.originalHeadersKey != null)
				return false;
		} else if (!originalHeadersKey.equals(other.originalHeadersKey))
			return false;
		if (originalPayloadKey == null) {
			if (other.originalPayloadKey != null)
				return false;
		} else if (!originalPayloadKey.equals(other.originalPayloadKey))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "HeadersToPayloadTransformer [headerPattern=" + headerPattern + ", originalPayloadKey="
				+ originalPayloadKey + ", originalHeadersKey=" + originalHeadersKey + "]";
	}

}
