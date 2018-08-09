package com.anmf.command;

import com.anmf.exception.CommandException;
import com.anmf.viewhelper.IViewHelper;

public interface ICommand {
	public String compute(IViewHelper ivhelper)throws CommandException;

}
