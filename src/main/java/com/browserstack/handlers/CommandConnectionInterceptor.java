package com.browserstack.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.stereotype.Component;

@Deprecated
@Component
public class CommandConnectionInterceptor extends ChannelInterceptorAdapter {

	private final static Logger LOGGER = LoggerFactory.getLogger(CommandConnectionInterceptor.class);

	@Override
	public void postSend(Message<?> message, MessageChannel channel, boolean sent) {

		StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);

		if (sha.getCommand() == null) {
			return;
		}

		String sessionId = sha.getSessionId();

		switch (sha.getCommand()) {
		case CONNECT:
			LOGGER.debug("STOMP Connect [sessionId: " + sessionId + "]");
			break;
		case CONNECTED:
			LOGGER.debug("STOMP Connected [sessionId: " + sessionId + "]");
			break;
		case DISCONNECT:
			LOGGER.debug("STOMP Disconnect [sessionId: " + sessionId + "]");
			break;
		default:
			break;

		}
	}
}
