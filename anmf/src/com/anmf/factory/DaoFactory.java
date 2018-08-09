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
 * DAO����ʵ����
 * 
 * @author Administrator
 * 
 */
public class DaoFactory implements IFactory {
	private static DaoFactory daoFactory = new DaoFactory();

	/**
	 * �����췽����Ϊ˽��,ʹ����ϵͳ��ֻ��һ��DAO����
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
			 * ����Ѵ��ڴ�type DAO������ֱ�ӷ���
			 */
			if (mapDaoCache.containsKey(type)) {
				return mapDaoCache.get(type);
			}
			/**
			 * ��ȡ�����ļ�,������ʵ�ֻ�����
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
