package org.andrewliu.java7thread.test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * ��������������һ���ȡ������
 * @author de
 *
 */
public class PrimeGenerator  implements Runnable{
	
	private final List<BigInteger> primes = new ArrayList<BigInteger>();
	//����ȡ����־Ϊԭ����
	private volatile boolean cancelled;
	@Override
	public void run() {
		BigInteger p = BigInteger.ONE;
		while(!cancelled){
			p = p.nextProbablePrime();
			synchronized(this){
				primes.add(p);
			}
		}
	}
	
	public void cancel(){
		cancelled = true;
	}
	public synchronized List<BigInteger> get(){
		return new ArrayList<BigInteger>(primes);
	}
	
	public static void main(String[] args) throws InterruptedException {
		PrimeGenerator generator = new PrimeGenerator();
		new Thread(generator).start();
		try{
			TimeUnit.SECONDS.sleep(1);
		}finally{
			generator.cancel();
		}
		List<BigInteger>  result =  generator.get();
		
		System.out.println("primes.result.size = "+result.size());
	}
	
}
