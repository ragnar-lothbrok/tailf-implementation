package com.browserstack.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.browserstack.service.api.ITailFileService;

@Controller
public class TailFileController {

	@Autowired
	private ITailFileService tailFileService;

	@RequestMapping(value = "/log", produces = MediaType.TEXT_PLAIN_VALUE, method = RequestMethod.GET)
	@ResponseBody
	public String getLastLines(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		return tailFileService.getTailedLines();
	}
	

}
