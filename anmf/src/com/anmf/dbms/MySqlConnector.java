package com.anmf.dbms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.anmf.exception.DBMSException;

/**
 * 获得mysql数据库连接的实现类
 * 
 * @author Administrator
 * 
 */
public class MySqlConnector extends DBConnector {
	private static MySqlConnector mysql = new MySqlConnector();

	private MySqlConnector() {
	}

	/**
	 * 创建MySql连接对象的实例只有一个,即单例模式的应用
	 * 
	 * @return
	 */
	public static MySqlConnector getInstance() {
		return mysql;
	}

	@Override
	public Connection getConnectionFromMSsql() throws DBMSException {
		Connection conn = null;
		String url = "jdbc:mysql://localhost:3306/antest?characterEncoding=gb2312";
		String driver = "com.mysql.jdbc.Driver";
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, "root", "admin");
			return conn;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Connection getConnectionFromMysql() throws DBMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Connection getConnectionFromOracle() throws DBMSException {
		// TODO Auto-generated method stub
		return null;
	}

}
