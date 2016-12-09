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
package com.mango.prov.spring;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.GenericMessage;

import com.mango.prov.messaging.Meta;
import com.mango.prov.messaging.Parcel;

/**
 * Intercepts the retrieved message and ensures that it is structured appropriately
 * for the JSON message schema.
 *
 */
public class ProvPayloadMessageInterceptor implements ChannelInterceptor {

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		Object payload = message.getPayload();
		payload = this.decoratePayload(payload);
		return new GenericMessage<Object>(payload, message.getHeaders());
	}

	@Override
	public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
		//Do nothing.
	}

	@Override
	public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
		//Do nothing
	}

	@Override
	public boolean preReceive(MessageChannel channel) {
		//Do nothing, need to return true to ensure that the message is retrieved.
		return true;
	}

	@Override
	public Message<?> postReceive(Message<?> message, MessageChannel channel) {
		Object payload = message.getPayload();
		payload = this.decoratePayload(payload);
		return new GenericMessage<Object>(payload, message.getHeaders());
	}

	@Override
	public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
		//Do nothing
	}

	private Object decoratePayload(Object payload) {
		if(isPayloadAlreadyDecorated(payload)) {
			return payload;
		}
		Parcel parcel = new Parcel();
		Meta meta = new Meta();
		parcel.setMeta(meta);
		parcel.setPayload(payload);
		return parcel;
	}

	private boolean isPayloadAlreadyDecorated(Object payload) {
		if(payload instanceof Parcel) {
			return true;
		}
		return false;
	}

}
