package com.anmf.dbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.anmf.exception.DBMSException;

/**
 * ����һ��������ݿ�
 * 
 * @author Administrator
 * 
 */
public abstract class DBConnector {
	/*
	 * ���󷽷����ڻ��MySql���ݿ������
	 */
	public abstract Connection getConnectionFromMysql() throws DBMSException;

	/*
	 * ���󷽷����ڻ��Oralce���ݿ������
	 */
	public abstract Connection getConnectionFromOracle() throws DBMSException;

	/*
	 * ���󷽷����ڻ��MSsql���ݿ������
	 */
	public abstract Connection getConnectionFromMSsql() throws DBMSException;

	/**
	 * �ͷ���Դ
	 * 
	 * @param rs
	 * @param stmt
	 * @param pstmt
	 */
	public void close(ResultSet rs, Statement stmt, PreparedStatement pstmt) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

}
