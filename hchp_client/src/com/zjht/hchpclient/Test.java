package com.zjht.hchpclient;

import java.math.BigDecimal;

public class Test {

	public static void main(String[] args) {
		String appendInfo;
		
		String trDate;
		String trTime;
		String workShift;
		String merchNo;
		String termNo;
		String trSeq;
		String amount; 
		String shopId;
		String sysNo;
		String cardNo;
		String statationName;
		String tradeType;
		BigDecimal amountBD;
		String item = "8984401554100201220308320150824071900011819000530875478PER6225551030109050   00000001863200101010038000005970000";
	    appendInfo = item.substring(89,item.length()-4);
		
		if (appendInfo.length()<27)
		{
			return;
		}
		
		trDate = item.substring(23,31);
		trTime = item.substring(31,37);
		
		if (appendInfo.length()==37)
		{
			workShift = appendInfo.substring(27,37);
		}
		else//没有班次信息
		{
			workShift = "";
		}
		
		merchNo = item.substring(0,15);
		termNo = item.substring(15,23);
		trSeq = item.substring(37,43);
		sysNo = item.substring(43,55);
		tradeType = item.substring(55, 57);
		cardNo = item.substring(58,77);
		//zjProNo = appendInfo.substring(3,11);
		
		//qytBD = new BigDecimal(appendInfo.substring(19,27)).divide(new BigDecimal(100));
		//qyt = qytBD.toString();
		
		//priceBD = new BigDecimal(appendInfo.substring(11,19)).divide(new BigDecimal(100));
		//price = priceBD.toString();
		
		amountBD = new BigDecimal(item.substring(77,89)).divide(new BigDecimal(100));
		amount = amountBD.toEngineeringString();//需要处理2位小数
		
		System.out.printf("merchNo=%s", merchNo);
		System.out.println();
		System.out.printf("trDate=%s", trDate);
		System.out.println();
		System.out.printf("trTime=%s", trTime);
		System.out.println();
		System.out.printf("workShift=%s", workShift);
		System.out.println();
		System.out.printf("termNo=%s", termNo);
		System.out.println();
		System.out.printf("trSeq=%s", trSeq);
		System.out.println();
		System.out.printf("amount=%s", amount);
		System.out.println();
		//System.out.printf("shopId=%s", shopId);
		System.out.printf("sysNo=%s", sysNo);
		System.out.println();
		System.out.printf("cardNo=%s", cardNo);
		System.out.println();
		System.out.printf("tradeType=%s",tradeType);
		//System.out.printf("statationName=%s", statationName);
	}
}
