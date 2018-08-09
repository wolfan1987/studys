package com.anmf.factory;

import com.anmf.brdelegate.IDelegate;
import com.anmf.command.ICommand;
import com.anmf.dao.IDao;
import com.anmf.exception.FactoryException;
/**
 * �����࣬���ڲ�����ͬ�Ķ�����:dao   delegate   command
 * @author Administrator
 *
 */
public interface IFactory {

	
	public IDao createDao(String type) throws FactoryException;

	public IDelegate createDelegate(String type) throws FactoryException;

	public ICommand createCommand(String type) throws FactoryException;
}
