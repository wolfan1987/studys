package com.anmf.factory;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.hibernate.Session;

import com.anmf.resource.HibernateSessionFactory;

public class ConnectionFactory {

	/**
	 * 从连接池中得到数据库连接
	 * 
	 * @param strPoolName
	 *            数据源名
	 * @return Connection
	 */
	public static Connection getConnection(String strPoolName) {
		Connection conn = null;
		InitialContext ctx;
		try {
			ctx = new InitialContext();
			/**
			 * 通过JNDI获得连接
			 */
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/"
					+ strPoolName);
			conn = ds.getConnection();
			return conn;
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

}
