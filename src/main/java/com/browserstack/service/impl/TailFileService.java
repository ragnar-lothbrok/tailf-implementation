package com.browserstack.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

import com.browserstack.service.api.ITailFileService;

/**
 * Service to send last 10 lines to subscribers for a file
 * 
 * @author anujjalan
 *
 */
@Service
public class TailFileService implements ITailFileService {

	private final static Logger logger = LoggerFactory.getLogger(TailFileService.class);

	@Autowired
	private SimpMessagingTemplate template;

	@Value("${tail.file.name:/tmp/file}")
	private String tailFileName;

	private Queue<String> tailfMessages = new CircularFifoQueue<String>(10);
	private Queue<String> tailMessages = new CircularFifoQueue<String>(10);

	private BufferedReader bf;

	private String lastMessage;

	@Override
	public String getTailedLines() {
		StringBuilder sb = new StringBuilder();
		List<String> messageList = new ArrayList<>();
		for (String message : tailMessages) {
			messageList.add(message);
			sb.append(message);
			sb.append("\n");
		}
		return sb.toString();
	}

	@PostConstruct
	public void init() throws Exception {
		try {
			bf = new BufferedReader(new FileReader(new File(tailFileName)));
			String line;
			while ((line = bf.readLine()) != null) {
				tailfMessages.add(line);
				tailMessages.add(line);
			}
		} catch (FileNotFoundException e) {
			logger.error("Error while initializing file");
			throw new Exception();
		}
	}

	@Scheduled(fixedDelay = 100)
	public void publishUpdates() {
		String line;
		int count = 0;
		try {
			if (count < 10) {
				while ((line = bf.readLine()) != null) {
					tailfMessages.add(line);
					tailMessages.add(line);
					count++;
				}
			}
		} catch (IOException e) {
			logger.error("Error while reading file");
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			if (tailfMessages.peek() != null) {
				String message = tailfMessages.poll();
				sb.append(message);
				sb.append("\n");
			}
		}
		String tailedLines = sb.toString();
		if (tailedLines != null && !tailedLines.equals(lastMessage)) {
			lastMessage = tailedLines;
			template.convertAndSend("/topic/tail", tailedLines);
		}
	}

}
