package com.browserstack.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.browserstack.service.api.ICommandService;

/**
 * This contains end point which will send data.
 * @author raghunandan.gupta
 *
 */
@RestController
public class TailCommandController {

	@Autowired
	private ICommandService commandService;

	@RequestMapping(value = "/showLog", produces = MediaType.TEXT_PLAIN_VALUE, method = RequestMethod.GET)
	public String getLastLines(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		return commandService.executeCommand();
	}

}
