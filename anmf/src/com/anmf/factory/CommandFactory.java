package com.anmf.factory;

import java.util.HashMap;
import java.util.Map;

import com.anmf.brdelegate.IDelegate;
import com.anmf.brdelegate.ReadConfigFile;
import com.anmf.command.ICommand;
import com.anmf.dao.IDao;
import com.anmf.exception.BRDelegateException;
import com.anmf.exception.FactoryException;

public class CommandFactory implements IFactory {

	private static CommandFactory commandFactory = new CommandFactory();
	/**
	 * 存放配置文件信息
	 */
	private static Map<String, String> mapXmlConfig = new HashMap<String, String>();
	/**
	 * 对cmd对象进行缓存
	 */
	private static Map<String, ICommand> cmdCache = new HashMap<String, ICommand>();

	private CommandFactory() {
	}

	public static CommandFactory getInstance() {
		return commandFactory;
	}

	public ICommand createCommand(String type) throws FactoryException {
		if (cmdCache.containsKey(type)) {
			return cmdCache.get(type);
		}
		
		try {
			// 从xml文件中读取命令
			mapXmlConfig = ReadConfigFile.readConfig();
			String clazzName = mapXmlConfig.get(type);
			ICommand cmd = (ICommand) Class.forName(clazzName).newInstance();
			cmdCache.put(type, cmd);
			return cmd;
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new FactoryException(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new FactoryException(e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new FactoryException(e);
		} catch (BRDelegateException e) {
			e.printStackTrace();
			throw new FactoryException(e);
		}
	}

	public IDao createDao(String type) throws FactoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public IDelegate createDelegate(String type) throws FactoryException {
		// TODO Auto-generated method stub
		return null;
	}

}
