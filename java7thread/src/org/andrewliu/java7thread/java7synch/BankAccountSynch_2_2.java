package org.andrewliu.java7thread.java7synch;

/**
 * 同步关键字测试：
 * synchronized:同步关键字用于控制同步代码块（临界区），多个线程会同时访问的区域。
 * 它可以用在方法上，代码块上，一个对象上（代码块)上的一种。
 * 当有一个线程持有了某一对象的锁来访问synchronized区时，其它线程不能访问这些代码，同时
 * 此对象的其它synchronized代码区也不能被其它线程访问，只有当前线程释放了锁之后才能访问。
 * 有两种情况除外：
 * 1、如果当前线程访问的是非静态方法的临界区，那么它只对非静态方法临界区的代码有锁权限，静态方法无锁权限，其它线程可以访问静态方法,此时要注意对公共数据的访问，
 *    有可能两个线程在同时操作一个数据，会引起想不到的结果。
 * 2、当synchronized锁定的不是当前对象，而是其它对象，允许多个线程同时访问这些代码块，即：A线程访问D对象的B方法时锁定的是this(D对象)，则所有与D对象
 *    有关的synchronized区就不能被其它线程访问，而如果E线程访问D对象的F方法里的临界区时，如果临界区锁定的是G对象，那么E线程可以访问这个F方法里的临界区，因为
 *    此时他们的锁不是同一个对象。
 * 注意：
 *    Synchronized区要会影响性能，定义时临界区要尽可能短。
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



