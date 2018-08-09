package com.zjht.soft.merchant.socket.business;

/**
 * 交易类型
 *<br>
 * Created by 黄灿贤 on 2017年9月25日
 * @version 1.0-SNAPSHOT
 */
public enum BizTypeEnum {

	PAY("841100", " 扫码付（被扫）"), 
	CANCEL("841510", "撤销申请"),
	//QUERY("841120", "交易查询"),
	QUERY("841110", "交易查询"),
	//REVOKE("841200", "撤单操作");
	REVOKE("841500", "撤单操作");

	/** 枚举值 */
	private String value;
	/** 描述 */
	private String desc;
	
	private BizTypeEnum(String value,String desc) {
		this.value = value;
		this.desc = desc;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static BizTypeEnum getEnumByValue(String value) {
		for (BizTypeEnum temp : BizTypeEnum.values()) {
			if (temp.value.equals(value)) {
				return temp;
			}
		}
		return null;
	}
	
	public static BizTypeEnum getEnumByName(String name) {
		for (BizTypeEnum temp : BizTypeEnum.values()) {
			if (temp.toString().equals(name)) {
				return temp;
			}
		}
		return null;
	}

}
