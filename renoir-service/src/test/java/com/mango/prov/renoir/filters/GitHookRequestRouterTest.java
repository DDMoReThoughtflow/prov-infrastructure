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
package com.mango.prov.renoir.filters;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.integration.test.matcher.PayloadMatcher.hasPayload;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.router.RecipientListRouter;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 *  Tests git hook request router.
 */
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class GitHookRequestRouterTest {

    @Autowired
    MessageChannel inboundChannel;

    @Autowired
    QueueChannel testGithubHookRequestChannel;

    @Autowired
    QueueChannel testBitbucketHookRequestChannel;

    @Autowired
    QueueChannel testGitlabHookRequestChannel;

    @Autowired
    QueueChannel testRenoirNativeRequestChannel;

    @Autowired
    RecipientListRouter gitHookRequestRouter;

    @Test
    public void testGithubHookRequestChannel() {
        String payload = "mock-content";
        Message<String> message = MessageBuilder.withPayload(payload).setHeader("x-github-event", "push").build();
        inboundChannel.send(message);
        Message<?> outMessage = testGithubHookRequestChannel.receive(0);
        assertNotNull("Expected an output message", outMessage);
        assertThat(outMessage, hasPayload(payload));
    }

    @Test
    public void testBitbucketHookRequestChannel() {
        String payload = "mock-content";
        Message<String> message = MessageBuilder.withPayload(payload).setHeader("x-event-key", "repo:push").build();
        inboundChannel.send(message);
        Message<?> outMessage = testBitbucketHookRequestChannel.receive(0);
        assertNotNull("Expected an output message", outMessage);
        assertThat(outMessage, hasPayload(payload));
    }

    @Test
    public void testGitlabHookRequestChannel() {
        String payload = "mock-content";
        Message<String> message = MessageBuilder.withPayload(payload).setHeader("x-gitlab-event", "Push Hook").build();
        inboundChannel.send(message);
        Message<?> outMessage = testGitlabHookRequestChannel.receive(0);
        assertNotNull("Expected an output message", outMessage);
        assertThat(outMessage, hasPayload(payload));
    }


    @Test
    public void testGitlabHookRequestChannelCamelCaseHeader() {
        String payload = "mock-content";
        Message<String> message = MessageBuilder.withPayload(payload).setHeader("X-Gitlab-Event", "Push Hook").build();
        inboundChannel.send(message);
        Message<?> outMessage = testGitlabHookRequestChannel.receive(0);
        assertNotNull("Expected an output message", outMessage);
        assertThat(outMessage, hasPayload(payload));
    }

    @Test
    public void testGitlabSystemHookRequestChannelRouter() {

    	Map<String, Object> payload = new HashMap<>();

    	payload.put("payload", "{'object_kind' : 'push', 'event_name' : 'push'}");
        Message<Map<String,Object>> message = MessageBuilder.withPayload(payload).setHeader("X-Gitlab-Event", "System Hook").build();
        inboundChannel.send(message);
        Message<?> outMessage = testGitlabHookRequestChannel.receive(0);
        assertNotNull("Expected an output message", outMessage);
        assertThat(outMessage, hasPayload(payload));
    }

    @Test
    public void testRenoirNativeRequestChannel() {
        String payload = "mock-content";
        Message<String> message = MessageBuilder.withPayload(payload).setHeader("x-renoir-vcs-event", "push").build();
        inboundChannel.send(message);
        Message<?> outMessage = testRenoirNativeRequestChannel.receive(0);
        assertNotNull("Expected an output message", outMessage);
        assertThat(outMessage, hasPayload(payload));
    }
}
