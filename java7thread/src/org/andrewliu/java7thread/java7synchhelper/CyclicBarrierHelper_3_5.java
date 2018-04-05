package org.andrewliu.java7thread.java7synchhelper;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 在集合点的同步辅助类,也称：栅栏。
 * CyclicBarrier：用于控制线程同步的辅助类.
 * 它允许两个或者多个线程在某个点上进行同步，同样是使用一个整型数进行初始化，这个数是需要在某个点上同步的线程数，
 * 当一个线程到达指定的点后，它将调用await()方法等待其他的线程，当线程调用await()方法后，CyclicBarrier类将
 * 阻塞这个线程并使之休眠直到所有其他线程到达。当最后一个线程调用CyclicBarrier类的await()方法时，CyclicBarrier对象将唤醒所有
 * 在等待的线程，然后这些线程将继续执行。它可以在构造函数中传入一个Runnable对象，作为同步后的集中点（栅栏），当所有线程到达集合点
 * 后，这个Runnable对象作为线程执行，这个特性使得这个类在并行任务上可以媲美分治编程技术。
 * 其它方法：getNumberWaiting()方法：返回在await()上阻塞的线程的数目，getParties()方法：返回对象同步的任务数。isBroken()方法：返回CyclicBarrier是否处于损坏状态。
 * CyclicBarrier对象可能被重置回初始状态，并把它的内部计数器重置成初始化时的值。
 * @author de
 *
 */
public class CyclicBarrierHelper_3_5 {

	public static void main(String[] args) {
		final int ROWS = 10000;  //10000行
		final int NUMBERS = 1000; //每行1000个数字
		final int SEARCH = 5; //要查找的数字
		final int PARTICIPANTS = 5; //多少个分支
		final int LINEX_PARTICIPANT = 2000;  //
		MatrixMock mock = new MatrixMock(ROWS,NUMBERS,SEARCH);
		Results results = new Results(ROWS);
		Grouper grouper = new Grouper(results);//最后的集中地
		CyclicBarrier barrier = new CyclicBarrier(PARTICIPANTS,grouper);
		
		Searcher searchers[] = new Searcher[PARTICIPANTS];
		for ( int i = 0; i < PARTICIPANTS; i++){
			searchers[i] = new Searcher(i*LINEX_PARTICIPANT,(i*LINEX_PARTICIPANT)+LINEX_PARTICIPANT,mock,results,5,barrier);
			Thread thread = new Thread(searchers[i]);
			thread.start();
		}
		
		System.out.printf("Main: The main thread has finished.\n");
	}
}


class MatrixMock{
	private int data[][];
	public MatrixMock(int size,int length,int number){
		int counter = 0;
		data = new int[size][length];  //初始化一个矩阵（i=row,j=列，number是要找的数字，counter记录产生number的个数
		Random random = new Random();
		for(int i = 0; i < size; i++){
			for( int j = 0; j < length; j++){
				data[i][j] = random.nextInt(10);
				if(data[i][j] == number){
					counter++;
				}
			}
		}
		
		System.out.printf("Mock: There are %d ocurrences of number in generated data.\n", counter,number);
	}
	
	
	public  int[] getRow(int row){  //取得某一行
		if((row>=0)&&(row<data.length)){
			return data[row];
		}
		return null;
	}
	
}

class Results{
	private int data[];
	public Results(int size){
		data =  new int[size];
	}
	
	public void setData(int position,int value){
		data[position] = value;
	}
	
	public int[] getData(){
		return data;
	}
}

class Searcher implements Runnable{
	private int firstRow;
	private int lastRow;
	private MatrixMock mock;
	private Results results;
	private int number;
	private final CyclicBarrier barrier;
	
	public Searcher(int firstRow, int lastRow, MatrixMock mock,
			Results results, int number, CyclicBarrier barrier) {
		super();
		this.firstRow = firstRow;
		this.lastRow = lastRow;
		this.mock = mock;
		this.results = results;
		this.number = number;
		this.barrier = barrier;
	}

	@Override
	public void run() {
		int counter;
		System.out.printf("%s: Processing lines from %d to %d.\n", Thread.currentThread().getName(),firstRow,lastRow);
		for(int i = firstRow; i < lastRow; i++){
			int row[] = mock.getRow(i); //得到第i行所有数据
			counter = 0;
			for ( int j = 0; j < row.length;j++){
				if(row[j] == number){
					counter++;
				}
			}
			results.setData(i, counter);
		}
		
		System.out.printf("%s: Lines processed.\n", Thread.currentThread().getName());
		try{
			barrier.await();
		}catch(InterruptedException e){ //有一个线程中断将抛出这个异常
			e.printStackTrace();
		}catch(BrokenBarrierException e){//有一个线程中断将上面的异常，其它在等待的线程将抛出这个异常
			e.printStackTrace();
		}
	}
	
}

class Grouper implements Runnable{
	private Results results;
	public Grouper(Results results){
		this.results = results;
	}
	@Override
	public void run() {
		int finalResult = 0;
		System.out.printf("Grouper: Processing results...\n");
		int data[] = results.getData();
		for(int number : data){
			finalResult+=number;
		}
		System.out.printf("Grouper: Total result: %d.\n", finalResult);
		
	}
	
}