package org.andrewliu.java7thread.test;

import java.math.BigInteger;

/**
 * ���۸ߵļ��㺯��
 * @author de
 *
 */
public class ExpensiveFunction  implements Computable<String,BigInteger>{

	@Override
	public BigInteger compute(String arg) throws InterruptedException {
		return new BigInteger(arg);
	}

}
