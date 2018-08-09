package com.anmf.factory;

import java.util.HashMap;
import java.util.Map;

import com.anmf.brdelegate.IDelegate;
import com.anmf.brdelegate.ReadConfigFile;
import com.anmf.command.ICommand;
import com.anmf.dao.IDao;
import com.anmf.exception.BRDelegateException;
import com.anmf.exception.FactoryException;

/**
 * DAO工厂实现类
 * 
 * @author Administrator
 * 
 */
public class DaoFactory implements IFactory {
	private static DaoFactory daoFactory = new DaoFactory();

	/**
	 * 将构造方法设为私有,使整个系统中只有一个DAO工厂
	 * 
	 * 
	 */
	private DaoFactory() {
	}

	public static DaoFactory getInstance() {
		return daoFactory;
	}

	private static Map<String, String> mapXmlConfig = new HashMap<String, String>();
	private static Map<String, IDao> mapDaoCache = new HashMap<String, IDao>();

	public ICommand createCommand(String type) throws FactoryException {
		return null;
	}

	public IDao createDao(String type) throws FactoryException {
		try {
			/**
			 * 如果已存在此type DAO对象则直接返回
			 */
			if (mapDaoCache.containsKey(type)) {
				return mapDaoCache.get(type);
			}
			/**
			 * 读取配置文件,按类名实现化对象
			 */
			mapXmlConfig = ReadConfigFile.readConfig();
			String strClassName = mapXmlConfig.get(type);
			IDao dao = (IDao) Class.forName(strClassName).newInstance();
			mapDaoCache.put(type, dao);
			return dao;
		} catch (BRDelegateException e) {
			e.printStackTrace();
			throw new FactoryException(e);
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new FactoryException(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new FactoryException(e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new FactoryException(e);
		}

	}

	public IDelegate createDelegate(String type) throws FactoryException {
		return null;
	}

}
