package com.anmf.dbms;

import java.sql.Connection;
import java.sql.SQLException;

import com.anmf.exception.DBMSException;

/**
 * 使操作在事务下进行的类
 * 
 * @author Administrator
 * 
 */
public class TransactionProcess {
	private Connection conn = null;

	/**
	 * 确定是否在事务的控制事进行操作
	 * 
	 * @param isAutoCommit
	 * @throws DBMSException
	 */
	public TransactionProcess(boolean isAutoCommit) throws DBMSException {
		conn = MySqlConnector.getInstance().getConnectionFromMSsql();
		try {
			conn.setAutoCommit(isAutoCommit);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 返回一个数据库连接
	 * 
	 * @return
	 */

	public Connection getConnection() {
		return conn;
	}

	/**
	 * 提交事务
	 */
	public void commit() {
		if (conn != null) {
			try {
				conn.commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 回滚事务
	 */
	public void rollback() {
		if (conn != null) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 关闭连接
	 */

	public void close() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
