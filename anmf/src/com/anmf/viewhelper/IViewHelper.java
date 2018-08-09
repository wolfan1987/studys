package com.anmf.viewhelper;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anmf.command.ICommand;
import com.anmf.exception.CommandException;

/**
 * ��ͼЭ����ӿ�,�����ڻ���������ݺͽ�����ת��
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
