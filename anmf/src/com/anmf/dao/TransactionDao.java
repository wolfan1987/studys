package com.anmf.dao;

import com.anmf.dbms.TransactionProcess;

public class TransactionDao {

	private TransactionProcess tp=null;

	public TransactionProcess getTp() {
		return tp;
	}

	public void setTp(TransactionProcess tp) {
		this.tp = tp;
	}
	
}
