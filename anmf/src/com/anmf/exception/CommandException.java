package com.anmf.exception;

/**
 * ��������쳣
 * @author Administrator
 *
 */
public class CommandException extends ANMFException {

	public CommandException(Exception exception) {
		super(exception);
		// TODO Auto-generated constructor stub
	}

	public CommandException(Exception exception, String message) {
		super(exception, message);
		// TODO Auto-generated constructor stub
	}

	public CommandException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
