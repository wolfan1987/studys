package com.anmf.daocommon;
/**
 * 查询时使用的 条件
 * 
 * @author Administrator
 * 
 */
public class Condition {
	/**
	 * 条件名[键]
	 */
	private String property;

	/**
	 * 操作符
	 */
	private Operater op;

	/**
	 * 条件值[值]
	 */
	private Object value;

	public Condition() {
		super();
	}

	public Condition(String property, Operater op, Object value) {
		super();
		this.property = property;
		this.op = op;
		this.value = value;
	}

	public Operater getOp() {
		return op;
	}

	public void setOp(Operater op) {
		this.op = op;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
