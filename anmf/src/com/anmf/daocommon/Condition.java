package com.anmf.daocommon;
/**
 * ��ѯʱʹ�õ� ����
 * 
 * @author Administrator
 * 
 */
public class Condition {
	/**
	 * ������[��]
	 */
	private String property;

	/**
	 * ������
	 */
	private Operater op;

	/**
	 * ����ֵ[ֵ]
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
