package org.andrewliu.java7thread.java7conlist;

import java.util.concurrent.atomic.AtomicLong;

/**
 * ԭ�ӱ������ԣ�AtomicXXX.: ���ṩ�˵��������ϵ�ԭ�ӵ�������ԭ�ӱ����ǲ��ᱻ�������Ż��ı�������һ���߳��ڶ�ԭ�ӱ�������ʱ��
 * ��������߳�Ҳ��ͼ��ͬһԭ�ӱ���ִ�в�����ԭ�ӱ�����ʵ�����ṩ��һ�׻�����ȷ��������һ������ɡ����ֲ�����ΪCASԭ�Ӳ�����
 * ԭ�ӱ�����ʹ����������ͬ����������ֵ�Ĳ������ʣ����в������ǻ���CASԭ�Ӳ����ġ�����֤�˶��߳���ͬһʱ�����һ��ԭ�ӱ���
 * ������������ݲ�һ�����Ĵ��󣬲���������������ʹ��ͬ�����Ʊ�������ͨ������
 * AtomicBoolean,AtomicInteger,AtomicReference
 * 
 * @author de
 *
 */
public class AtomicVarTest_6_8 {

	public static void main(String[] args) {
		AtomicAccount account = new AtomicAccount();
		account.setBalance(1000);//�����ʻ����Ϊ1000;
		
		AtomicCompany company = new AtomicCompany(account);
		Thread companyThread = new Thread(company);
		
		AtomicBank bank = new AtomicBank(account);
		Thread bankThread = new Thread(bank);
		
		System.out.printf("Account: Initial Balance: %d\n", account.getBalance());  //��ʾ�ܽ��
		companyThread.start();  //�����̲߳������
		bankThread.start();
		
		try{
			companyThread.join();  //���̲߳�����
			bankThread.join();
			System.out.printf("Account : Final Balance: %d\n", account.getBalance());  //��ʾ������.
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
}

class AtomicAccount{
	private AtomicLong balance;
	public  AtomicAccount(){
		balance  = new AtomicLong();
	}
	
	public long getBalance(){
		return balance.get();//ʹ��ԭ�ӱ����ķ�������ֵ
	}
	
	public void setBalance(long balance){
		this.balance.set(balance);  //ʹ��ԭ�ӱ�����set��������ֵ
	}
	
	public void addAmount(long amount){
		this.balance.getAndAdd(amount); //��ԭ�ӱ����ķ������ӽ��
	}
	
	public void subtractAmount(long amount){
		this.balance.getAndAdd(-amount);//��ԭ�ӱ����ķ��������
	}
	
}

class AtomicCompany implements Runnable{
	private AtomicAccount  account;

	public AtomicCompany(AtomicAccount account) {
		super();
		this.account = account;
	}

	@Override
	public void run() {
		for ( int i = 0; i < 10; i++){
			account.addAmount(1000);  //��Ǯ
		}
	}
}

class AtomicBank implements Runnable{
	private AtomicAccount account;

	public AtomicBank(AtomicAccount account) {
		super();
		this.account = account;
	}

	@Override
	public void run() {
		for ( int i = 0; i < 10; i++){
			account.subtractAmount(1000);  //ȡǮ
		}
	}
	
	
}
