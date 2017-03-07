package com.browserstack.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Queue;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.browserstack.constants.CommandConstants;
import com.browserstack.service.api.ICommandService;

/**
 * This will send 10 lines only
 * 
 * @author raghunandan.gupta
 *
 */
@Service
public class TailCommandServiceImpl implements ICommandService {

	private final static Logger LOGGER = LoggerFactory.getLogger(TailCommandServiceImpl.class);

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Value("${tail.file.name:/Users/raghunandan.gupta/Documents/gitrepo/browser-stack-assignment/src/main/resources/server.log}")
	private String logFileName;

	private Queue<String> allMessages = new CircularFifoQueue<String>(10);

	private Queue<String> lastNMessage = new CircularFifoQueue<String>(10);

	private BufferedReader fileReader;

	public String executeCommand() {
		StringBuilder buffer = new StringBuilder();
		for (String message : allMessages) {
			buffer.append(message);
			buffer.append("<br />");
		}
		return buffer.toString();
	}

	/**
	 * This will read last CommandConstants.MAX_LINES_READ lines
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void init() throws Exception {
		try {
			fileReader = new BufferedReader(new FileReader(new File(logFileName)));
			String line;
			while ((line = fileReader.readLine()) != null) {
				if (line.trim().length() > 0) {
					allMessages.add(line);
					lastNMessage.add(line);
				}
			}
		} catch (FileNotFoundException e) {
			LOGGER.error("Error while opening file {} ", e.getMessage());
			throw new Exception();
		}
	}

	@Scheduled(fixedDelay = 100)
	public void sendUpdates() {
		String line = null;
		int lineCount = 0;
		try {
			while ((line = fileReader.readLine()) != null) {
				if (line.trim().length() > 0) {
					allMessages.add(line);
					lastNMessage.add(line);
				}
				lineCount++;
				if (lineCount == CommandConstants.MAX_LINES_READ) {
					break;
				}
			}
		} catch (IOException e) {
			LOGGER.error("Exception occured while reading data from file = {} ", e.getMessage());
		}
		// LOGGER.info("Total messages : "+allMessages.size());
		StringBuilder lastNLines = new StringBuilder();
		for (int i = 0; i < CommandConstants.MAX_LINES_READ; i++) {
			if (allMessages.peek() != null) {
				String message = allMessages.remove();
				lastNLines.append(message + "<br />");
			}
		}
		if (lastNLines.length() > 0) {
			messagingTemplate.convertAndSend("/topic/log", lastNLines);
		}
	}

	@Override
	public void sendLastNLines() {
		for (int i = 0; i < CommandConstants.MAX_LINES_READ; i++) {
			if (lastNMessage.peek() != null) {
				String message = lastNMessage.remove();
				allMessages.add(message);
			}
		}
	}

}
