package org.andrewliu.java7thread.java7synch;

/**
 * ͬ���ؼ��ֲ��ԣ�
 * synchronized:ͬ���ؼ������ڿ���ͬ������飨�ٽ�����������̻߳�ͬʱ���ʵ�����
 * ���������ڷ����ϣ�������ϣ�һ�������ϣ������)�ϵ�һ�֡�
 * ����һ���̳߳�����ĳһ�������������synchronized��ʱ�������̲߳��ܷ�����Щ���룬ͬʱ
 * �˶��������synchronized������Ҳ���ܱ������̷߳��ʣ�ֻ�е�ǰ�߳��ͷ�����֮����ܷ��ʡ�
 * ������������⣺
 * 1�������ǰ�̷߳��ʵ��ǷǾ�̬�������ٽ�������ô��ֻ�ԷǾ�̬�����ٽ����Ĵ�������Ȩ�ޣ���̬��������Ȩ�ޣ������߳̿��Է��ʾ�̬����,��ʱҪע��Թ������ݵķ��ʣ�
 *    �п��������߳���ͬʱ����һ�����ݣ��������벻���Ľ����
 * 2����synchronized�����Ĳ��ǵ�ǰ���󣬶������������������߳�ͬʱ������Щ����飬����A�̷߳���D�����B����ʱ��������this(D����)����������D����
 *    �йص�synchronized���Ͳ��ܱ������̷߳��ʣ������E�̷߳���D�����F��������ٽ���ʱ������ٽ�����������G������ôE�߳̿��Է������F��������ٽ�������Ϊ
 *    ��ʱ���ǵ�������ͬһ������
 * ע�⣺
 *    Synchronized��Ҫ��Ӱ�����ܣ�����ʱ�ٽ���Ҫ�����̡ܶ�
 * @author de
 *
 */
public class BankAccountSynch_2_2  {

	public static void main(String[] args) {
		Account account = new Account();
		account.setBalance(1000);
		Company company = new Company(account);
		Thread companyThread = new Thread(company);
		
		Bank bank = new Bank(account);
		Thread bankThread = new Thread(bank);
		
		System.out.printf("Account: Initial Balance: %f\n", account.getBalance());
		companyThread.start();
		bankThread.start();
		
		
		try {
			companyThread.join();
			bankThread.join();
			System.out.printf("Account : Final Balance: %f\n", account.getBalance());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
}


class Account{
	private double balance;

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	
	public synchronized void addAmount(double amount){
		double tmp = balance;
		try{
			Thread.sleep(10);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		tmp+=amount;
		balance = tmp;
	}
	
	public synchronized void subtractAmount(double amount){
		double tmp = balance;
		try{
			Thread.sleep(10);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		tmp-=amount;
		balance=tmp;
	}
}

class Bank implements Runnable{
	private Account account;
	public Bank(Account account){
		this.account = account;
	}
	@Override
	public void run() {
		for( int i=0; i < 100; i++){
			account.subtractAmount(1000);
		}
	}
	
}

class Company implements Runnable{

	private Account account;
	public Company(Account account){
		this.account = account;
	}
	
	@Override
	public void run() {
		for(int i = 0; i < 100; i++){
			account.addAmount(1000);
		}
	}
	
}



