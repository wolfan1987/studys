package com.anmf.viewhelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anmf.command.ICommand;
import com.anmf.exception.CommandException;
import com.anmf.exception.FactoryException;
import com.anmf.factory.CommandFactory;

/**
 * ��ͼЭ�����ʵ����
 * 
 * @author Administrator
 * 
 */
public class ViewHelper implements IViewHelper {

	private HttpServletRequest request = null;
	private HttpServletResponse response = null;

	/**
	 * ���������Ӧ������з�װ
	 * 
	 * @param request
	 * @param response
	 */
	public ViewHelper(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	/**
	 * ����һ���������
	 */
	public ICommand getCommand() throws CommandException {
		/**
		 * ��������url
		 */
		String strUrl = request.getRequestURI();
		/**
		 * ���ж�ε���.do����ʱ����.do��������
		 */
		if (request.getAttribute("cmd") != null) {
			strUrl = (String) request.getAttribute("cmd");
		}
		/**
		 * �����ַ�������ʼλ��
		 */
		int iStart = strUrl.lastIndexOf("/") == -1 ? 0 : strUrl
				.lastIndexOf("/") + 1;
		/**
		 * �����ַ����Ľ���λ��
		 */
		int iEnd = strUrl.lastIndexOf(".do") == -1 ? strUrl.length() : strUrl
				.lastIndexOf(".do");
		/**
		 * �õ������������ַ���
		 */
		String strCmd = strUrl.substring(iStart, iEnd);

		try {
			/**
			 * �õ��������
			 */
			return CommandFactory.getInstance().createCommand(strCmd);
		} catch (FactoryException e) {
			e.printStackTrace();
			throw new CommandException(e);
		}
	}

	/**
	 * ��������еĲ�����Ϣ
	 */
	public Map<String, Object> getRequestData() {
		Map<String, String[]> mapPara = request.getParameterMap();
		Map<String, Object> mapData = new HashMap<String, Object>();
		Set<String> setKey = mapPara.keySet();
		for (String key : setKey) {
			String[] strArray = mapPara.get(key);
			if (strArray != null && strArray.length == 1) {
				mapData.put(key, strArray[0]);
			} else {
				mapData.put(key, strArray);
			}
		}
		return mapData;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

}
