package com.anmf.command;

import java.util.List;

import com.anmf.brdelegate.TestDelegate;
import com.anmf.exception.BRDelegateException;
import com.anmf.exception.CommandException;
import com.anmf.exception.FactoryException;
import com.anmf.factory.BRDelegateFactory;
import com.anmf.viewhelper.IViewHelper;

public class TestCommand implements ICommand {

	public String compute(IViewHelper ivhelper) throws CommandException {
		try {
			/**
			 * ͨ��ҵ�����������һ��ҵ��������
			 */
			TestDelegate testDelegate = (TestDelegate) BRDelegateFactory
					.getInstance().createDelegate("testdelegate");
			/**
			 * ��ҵ��������ȥ��dao����������Daoȥִ�����ݿ�Ĳ���,Ȼ�󽫷��ص����ݷ���������
			 */
			List info = testDelegate.searchAll();
			ivhelper.getRequest().setAttribute("info", info);
			return "/display.jsp";
		} catch (FactoryException e) {
			e.printStackTrace();
			throw new CommandException(e);
		} catch (BRDelegateException e) {
			e.printStackTrace();
			throw new CommandException(e);
		}
	}

}
