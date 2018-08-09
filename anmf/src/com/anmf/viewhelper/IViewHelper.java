package com.anmf.viewhelper;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anmf.command.ICommand;
import com.anmf.exception.CommandException;

/**
 * 视图协作类接口,它用于获得请求数据和将数据转发
 * 
 * @author Administrator
 * 
 */
public interface IViewHelper {
	public HttpServletRequest getRequest();

	public HttpServletResponse getResponse();

	public Map<String, Object> getRequestData();

	public ICommand getCommand() throws CommandException;

}
