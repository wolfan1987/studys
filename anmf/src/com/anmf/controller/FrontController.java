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
 * �������࣬���ڴ������е�����
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
		 * ��װrequest��response����
		 */
		IViewHelper vhelper = new ViewHelper(request, response);
		try {
			/**
			 * ͨ��Э������õ�һ���������
			 */
			ICommand command = vhelper.getCommand();
			/**
			 * �������Э����������ݴ���ȥ���ɴ������ȥ�������ص�������·�����ַ���
			 */
			strPage = command.compute(vhelper);
		} catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * ������ת���Ļ���һ��.do����ʱ�������������󷽷�,������ύ.do����
		 */
		if (strPage.indexOf(".do") > -1) {
			request.setAttribute("cmd", strPage);
			doGet(request, response);
		}
		/**
		 * ������ת��
		 */
		request.getRequestDispatcher(strPage).forward(request, response);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);

	}

}
