package org.andrewliu.jvm.test;

/**
 * ���Ե���ʧ��
 * vmArgs: -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8 -XX:-HandlePromotionFailure=ture
 * 
 * @author zjhtadmin
 *
 */
public class HandlePromotionTest {
	private  static final int _1MB = 1024 * 1024;
	
	public static void testHandlePromotion(){
		byte[] allocation1,allocation2,allocation3,allocation4,allocation5,
		allocation6,allocation7;
		allocation1 = new byte[2*_1MB];
		allocation2 = new byte[2*_1MB];
		allocation3 = new byte[2*_1MB];
		allocation1 = null;
		allocation4 = new byte[2*_1MB];
		allocation5 = new byte[2*_1MB];
		allocation6 = new byte[2*_1MB];
		allocation4 = null;
		allocation5 = null;
		allocation6 = null;
		allocation7 = new byte[2*_1MB];
	}
	
	public static void main(String[] args) {
		//testHandlePromotion();
	}

}
