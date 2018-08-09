package com.anmf.factory;

import java.util.HashMap;
import java.util.Map;

import com.anmf.brdelegate.IDelegate;
import com.anmf.brdelegate.ReadConfigFile;
import com.anmf.command.ICommand;
import com.anmf.dao.IDao;
import com.anmf.exception.BRDelegateException;
import com.anmf.exception.FactoryException;

public class BRDelegateFactory implements IFactory {

	private static BRDelegateFactory brdeFactory = new BRDelegateFactory();

	private BRDelegateFactory() {

	}

	public static BRDelegateFactory getInstance() {
		return brdeFactory;
	}

	private static Map<String, String> mapXmlConfig = new HashMap<String, String>();
	private static Map<String, IDelegate> mapDelegateCache = new HashMap<String, IDelegate>();

	public ICommand createCommand(String type) throws FactoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public IDao createDao(String type) throws FactoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public IDelegate createDelegate(String type) throws FactoryException {
		if (mapDelegateCache.containsKey(type)) {
			return mapDelegateCache.get(type);
		}
		try {
			mapXmlConfig = ReadConfigFile.readConfig();
			String strClassName = mapXmlConfig.get(type);
			IDelegate delegate = (IDelegate) Class.forName(strClassName)
					.newInstance();
			mapDelegateCache.put(type, delegate);
			return delegate;
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

}
