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
			 * 通过业务代理工厂产生一个业务代理对象
			 */
			TestDelegate testDelegate = (TestDelegate) BRDelegateFactory
					.getInstance().createDelegate("testdelegate");
			/**
			 * 用业务代理对象去向dao发出请求，由Dao去执行数据库的操作,然后将返回的数据放入请求中
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
