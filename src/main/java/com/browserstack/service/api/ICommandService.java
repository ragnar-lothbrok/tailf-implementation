package com.browserstack.service.api;

/**
 * Contains generic method only.
 * @author raghunandan.gupta
 *
 */
public interface ICommandService {

	public String executeCommand();

	void sendLastNLines();

}
