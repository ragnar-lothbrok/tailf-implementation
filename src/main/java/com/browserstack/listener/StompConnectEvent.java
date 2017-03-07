package com.browserstack.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import com.browserstack.service.api.ICommandService;

/**
 * When new session will be created. onApplicationEvent method will be called.
 * @author raghunandan.gupta
 *
 */
@Component
public class StompConnectEvent implements ApplicationListener<SessionConnectEvent> {

	private final static Logger LOGGER = LoggerFactory.getLogger(StompConnectEvent.class);

	@Autowired
	private ICommandService commandService;

	public void onApplicationEvent(SessionConnectEvent event) {
		StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

		LOGGER.debug("Connect event [sessionId: " + sha.getSessionId());

		commandService.sendLastNLines();
	}

}