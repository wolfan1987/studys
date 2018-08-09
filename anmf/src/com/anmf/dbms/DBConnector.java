package com.anmf.dbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.anmf.exception.DBMSException;

/**
 * 抽象一个获得数据库
 * 
 * @author Administrator
 * 
 */
public abstract class DBConnector {
	/*
	 * 抽象方法用于获得MySql数据库的连接
	 */
	public abstract Connection getConnectionFromMysql() throws DBMSException;

	/*
	 * 抽象方法用于获得Oralce数据库的连接
	 */
	public abstract Connection getConnectionFromOracle() throws DBMSException;

	/*
	 * 抽象方法用于获得MSsql数据库的连接
	 */
	public abstract Connection getConnectionFromMSsql() throws DBMSException;

	/**
	 * 释放资源
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
