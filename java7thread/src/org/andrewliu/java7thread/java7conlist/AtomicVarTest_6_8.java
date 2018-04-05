package org.andrewliu.java7thread.java7conlist;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 原子变量测试：AtomicXXX.: 其提供了单个变量上的原子弹操作。原子变量是不会被编译器优化的变量。当一个线程在对原子变量操作时，
 * 如果其他线程也试图对同一原子变量执行操作，原子变量的实现类提供了一套机制来确定操作在一步内完成。这种操作称为CAS原子操作。
 * 原子变量不使用锁或其他同步机制来其值的并发访问，所有操作都是基于CAS原子操作的。它保证了多线程在同一时间操作一个原子变量
 * 而不会产生数据不一到处的错误，并且它的性能优于使用同步机制保护的普通变量。
 * AtomicBoolean,AtomicInteger,AtomicReference
 * 
 * @author de
 *
 */
public class AtomicVarTest_6_8 {

	public static void main(String[] args) {
		AtomicAccount account = new AtomicAccount();
		account.setBalance(1000);//设置帐户金额为1000;
		
		AtomicCompany company = new AtomicCompany(account);
		Thread companyThread = new Thread(company);
		
		AtomicBank bank = new AtomicBank(account);
		Thread bankThread = new Thread(bank);
		
		System.out.printf("Account: Initial Balance: %d\n", account.getBalance());  //显示总金额
		companyThread.start();  //启动线程操作金额
		bankThread.start();
		
		try{
			companyThread.join();  //让线程操作完
			bankThread.join();
			System.out.printf("Account : Final Balance: %d\n", account.getBalance());  //显示最后余额.
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
		return balance.get();//使用原子变量的方法返回值
	}
	
	public void setBalance(long balance){
		this.balance.set(balance);  //使用原子变量的set方法设置值
	}
	
	public void addAmount(long amount){
		this.balance.getAndAdd(amount); //用原子变量的方法增加金额
	}
	
	public void subtractAmount(long amount){
		this.balance.getAndAdd(-amount);//用原子变量的方法减金额
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
			account.addAmount(1000);  //存钱
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
			account.subtractAmount(1000);  //取钱
		}
	}
	
	
}
