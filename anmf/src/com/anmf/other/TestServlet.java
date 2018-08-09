package com.anmf.other;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anmf.dbms.MySqlConnector;
import com.anmf.exception.DBMSException;

public class TestServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		// Connection con = ConnectionFactory.getConnection("jdbc/antest");
		// Session session=HibernateSessionFactory.getSession();
		// Connection conn=session.connection();
		
		PreparedStatement pstm;
		try {
			Connection connn = MySqlConnector.getInstance()
			.getConnectionFromMSsql();
			pstm = connn.prepareStatement("select rid,rname from test;");
			java.sql.ResultSet rs = pstm.executeQuery();
//			while (rs.next()) {
//				System.out.println(rs.getInt(1));
//				System.out.println(rs.getString(2));
//			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DBMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.write("<li><font size='3'>��Ŀ����<font><ul><li><a href='showSumup.do?sumup=initItem&mpid=13'target='bottnmframe'><font size='2'>��Ŀ�ܽ�</font> </a></li><li><a href='ItemWorkFlow/AddItemhicth.jsp?mpid=5'target='bottnmframe'><font size='2'>��Ŀ����</font> </a></li><li><a href='showBar.do?pengke=init' target='bottnmframe'><font size='2'>��ͬ����</font> </a></li><li><a href='showItem.do?method=queryItemhitch&mpid=7'target='bottnmframe'><font size='2'>״̬����</font> </a><ul><li><a href='getAllUser.do?anmto=getAlluser&mpid=15'target='bottnmframe'><font size='2'>�鿴�û�</font> </a></li><li><a href='getAllUser.do?anmto=getAlluser&mpid=15'target='bottnmframe'><font size='2'>�鿴�û�</font> </a></li><li><a href='UserManager/addRoles.jsp?mpid=16'target='bottnmframe'><font size='2'>��ӽ�ɫ</font> </a></li></ul></li></ul></li>");
		out.flush();
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
