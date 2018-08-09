package com.anmf.daocommon;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * 该类以辅助Ajax技术
 * 
 * @author Administrator
 * 
 */
public class MyPrintWriter {
	/**
	 * 得到输出流Text格式
	 * 
	 * @param response
	 * @return
	 */
	public static PrintWriter getWriterForText(HttpServletResponse response) {
		try {
			response.setContentType("text/html;charset=utf-8");
			response.setHeader("pragma", "no-cache");
			response.setHeader("cache-control", "no-cache");
			response.setDateHeader("expires", 0);

			PrintWriter out = response.getWriter();
			return out;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 得到输出流XML格式
	 * 
	 * @param response
	 * @return
	 */
	public static PrintWriter getWriterForXML(HttpServletResponse response) {
		try {
			response.setContentType("text/xml;charset=utf-8");
			response.setHeader("pragma", "no-cache");
			response.setHeader("cache-control", "no-cache");
			response.setDateHeader("expires", 0);

			PrintWriter out = response.getWriter();
			return out;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
