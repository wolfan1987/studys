package com.anmf.daocommon;
/**
 * ����������
 * 
 * @author Administrator
 * 
 */
public class OrderTerm {
	/**
	 * ����ʽ
	 */
	private OrderWise wise;

	/**
	 * ���� ,����������
	 */
	private String name;

	public OrderTerm(OrderWise wise, String name) {
		super();
		this.wise = wise;
		this.name = name;
	}

	public OrderTerm() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public OrderWise getWise() {
		return wise;
	}

	public void setWise(OrderWise wise) {
		this.wise = wise;
	}

}
