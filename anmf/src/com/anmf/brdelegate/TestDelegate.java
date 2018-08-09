package com.anmf.brdelegate;

import java.util.List;

import com.anmf.dao.TestDao;
import com.anmf.dbms.TransactionProcess;
import com.anmf.exception.BRDelegateException;
import com.anmf.exception.DBMSException;
import com.anmf.exception.DaoException;
import com.anmf.exception.FactoryException;
import com.anmf.factory.DaoFactory;

public class TestDelegate implements IDelegate {

	public List searchAll() throws BRDelegateException {
		TransactionProcess trans = null;
		try {
			/**
			 * 实例化且设置事务
			 */
			trans = new TransactionProcess(true);
			/**
			 * 实例化Dao对象
			 */
			TestDao dao = (TestDao) DaoFactory.getInstance().createDao(
					"testdao");
			/**
			 * 设置Dao对象的事务
			 */
			dao.setTp(trans);
			return dao.getAll();
		} catch (DBMSException e) {
			e.printStackTrace();
			throw new BRDelegateException(e);
		} catch (FactoryException e) {
			e.printStackTrace();
			throw new BRDelegateException(e);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new BRDelegateException(e);
		}
	}
}
