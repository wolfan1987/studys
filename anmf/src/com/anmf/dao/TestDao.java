package com.anmf.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.anmf.exception.DaoException;
import com.anmf.vo.AbstractVo;
import com.anmf.vo.TestVo;

public class TestDao extends TransactionDao implements IDao<AbstractVo> {

	public boolean delete(AbstractVo vo) throws DaoException {
		// TODO Auto-generated method stub
		return false;
	}

	public AbstractVo findByPk(AbstractVo vo) throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<AbstractVo> getAll() throws DaoException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<AbstractVo> listObj = new ArrayList<AbstractVo>();
		TestVo tv = null;
		/**
		 * 获得连接
		 */
		conn = this.getTp().getConnection();
		try {
			pstmt = conn.prepareStatement("select rid,rname from test;");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tv = new TestVo();
				tv.setRid(rs.getInt(1));
				tv.setRname(rs.getString(2));
				listObj.add(tv);
			}
			return listObj;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
	}

	public boolean save(AbstractVo vo) throws DaoException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean update(AbstractVo vo) throws DaoException {
		// TODO Auto-generated method stub
		return false;
	}

}
