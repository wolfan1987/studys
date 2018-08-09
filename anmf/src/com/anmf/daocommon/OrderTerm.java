package com.anmf.daocommon;
/**
 * 排序条件集
 * 
 * @author Administrator
 * 
 */
public class OrderTerm {
	/**
	 * 排序方式
	 */
	private OrderWise wise;

	/**
	 * 列名 ,对象属性名
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
