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
 * 视图协作类的实现类
 * 
 * @author Administrator
 * 
 */
public class ViewHelper implements IViewHelper {

	private HttpServletRequest request = null;
	private HttpServletResponse response = null;

	/**
	 * 将请求和响应对象进行封装
	 * 
	 * @param request
	 * @param response
	 */
	public ViewHelper(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	/**
	 * 返回一个命令对象
	 */
	public ICommand getCommand() throws CommandException {
		/**
		 * 获得请求的url
		 */
		String strUrl = request.getRequestURI();
		/**
		 * 当有多次调用.do操作时，此.do操作优先
		 */
		if (request.getAttribute("cmd") != null) {
			strUrl = (String) request.getAttribute("cmd");
		}
		/**
		 * 命令字符串的起始位置
		 */
		int iStart = strUrl.lastIndexOf("/") == -1 ? 0 : strUrl
				.lastIndexOf("/") + 1;
		/**
		 * 命令字符串的结束位置
		 */
		int iEnd = strUrl.lastIndexOf(".do") == -1 ? strUrl.length() : strUrl
				.lastIndexOf(".do");
		/**
		 * 得到完整的命令字符串
		 */
		String strCmd = strUrl.substring(iStart, iEnd);

		try {
			/**
			 * 得到命令对象
			 */
			return CommandFactory.getInstance().createCommand(strCmd);
		} catch (FactoryException e) {
			e.printStackTrace();
			throw new CommandException(e);
		}
	}

	/**
	 * 获得请求中的参数信息
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
