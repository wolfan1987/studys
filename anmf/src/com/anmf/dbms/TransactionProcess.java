package com.anmf.dbms;

import java.sql.Connection;
import java.sql.SQLException;

import com.anmf.exception.DBMSException;

/**
 * ʹ�����������½��е���
 * 
 * @author Administrator
 * 
 */
public class TransactionProcess {
	private Connection conn = null;

	/**
	 * ȷ���Ƿ�������Ŀ����½��в���
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
	 * ����һ�����ݿ�����
	 * 
	 * @return
	 */

	public Connection getConnection() {
		return conn;
	}

	/**
	 * �ύ����
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
	 * �ع�����
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
	 * �ر�����
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
