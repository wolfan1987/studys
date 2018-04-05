package org.andrewliu.java7thread.java7synchhelper;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * �ڼ��ϵ��ͬ��������,Ҳ�ƣ�դ����
 * CyclicBarrier�����ڿ����߳�ͬ���ĸ�����.
 * �������������߶���߳���ĳ�����Ͻ���ͬ����ͬ����ʹ��һ�����������г�ʼ�������������Ҫ��ĳ������ͬ�����߳�����
 * ��һ���̵߳���ָ���ĵ����������await()�����ȴ��������̣߳����̵߳���await()������CyclicBarrier�ཫ
 * ��������̲߳�ʹ֮����ֱ�����������̵߳�������һ���̵߳���CyclicBarrier���await()����ʱ��CyclicBarrier���󽫻�������
 * �ڵȴ����̣߳�Ȼ����Щ�߳̽�����ִ�С��������ڹ��캯���д���һ��Runnable������Ϊͬ����ļ��е㣨դ�������������̵߳��Ｏ�ϵ�
 * �����Runnable������Ϊ�߳�ִ�У��������ʹ��������ڲ��������Ͽ����������α�̼�����
 * ����������getNumberWaiting()������������await()���������̵߳���Ŀ��getParties()���������ض���ͬ������������isBroken()����������CyclicBarrier�Ƿ�����״̬��
 * CyclicBarrier������ܱ����ûس�ʼ״̬�����������ڲ����������óɳ�ʼ��ʱ��ֵ��
 * @author de
 *
 */
public class CyclicBarrierHelper_3_5 {

	public static void main(String[] args) {
		final int ROWS = 10000;  //10000��
		final int NUMBERS = 1000; //ÿ��1000������
		final int SEARCH = 5; //Ҫ���ҵ�����
		final int PARTICIPANTS = 5; //���ٸ���֧
		final int LINEX_PARTICIPANT = 2000;  //
		MatrixMock mock = new MatrixMock(ROWS,NUMBERS,SEARCH);
		Results results = new Results(ROWS);
		Grouper grouper = new Grouper(results);//���ļ��е�
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
		data = new int[size][length];  //��ʼ��һ������i=row,j=�У�number��Ҫ�ҵ����֣�counter��¼����number�ĸ���
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
	
	
	public  int[] getRow(int row){  //ȡ��ĳһ��
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
			int row[] = mock.getRow(i); //�õ���i����������
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
		}catch(InterruptedException e){ //��һ���߳��жϽ��׳�����쳣
			e.printStackTrace();
		}catch(BrokenBarrierException e){//��һ���߳��жϽ�������쳣�������ڵȴ����߳̽��׳�����쳣
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