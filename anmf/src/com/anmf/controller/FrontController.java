package com.anmf.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anmf.command.ICommand;
import com.anmf.exception.CommandException;
import com.anmf.viewhelper.IViewHelper;
import com.anmf.viewhelper.ViewHelper;

/**
 * 控制器类，用于处理所有的请求
 * 
 * @author Administrator
 * 
 */
@SuppressWarnings("serial")
public class FrontController extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String strPage = null;
		/**
		 * 封装request和response对象
		 */
		IViewHelper vhelper = new ViewHelper(request, response);
		try {
			/**
			 * 通过协作对象得到一个命令对象
			 */
			ICommand command = vhelper.getCommand();
			/**
			 * 命令对象将协作对象的数据传过去，由代理对象去处理，返回的是请求路径的字符串
			 */
			strPage = command.compute(vhelper);
		} catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * 当请求转发的还是一个.do动作时，继续调用请求方法,即多次提交.do动作
		 */
		if (strPage.indexOf(".do") > -1) {
			request.setAttribute("cmd", strPage);
			doGet(request, response);
		}
		/**
		 * 将请求转发
		 */
		request.getRequestDispatcher(strPage).forward(request, response);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);

	}

}
