package org.andrewliu.java7thread.java7conlist;

import java.util.concurrent.ConcurrentLinkedDeque;


/** ��������ʽ˫�˶���.
 * ������ʽ�����б������б���ConcurrentLinkedDeque: ���ִ�еĲ������ܹ��������У��������׳��쳣�򷵻�null.
 * ��������Ԫ�ط�����
 * getFirst()��getLast()�������б��е�һ�������һ��Ԫ�أ����ص�Ԫ�ز�����б����Ƴ�������б�Ϊ�գ������������׳�NoSuchElementException�쳣.
 * peek(),peekFirst()��peeklast()���ֱ𷵻��б��е�һ�������һ��Ԫ�أ����ص�Ԫ�ز�����б����Ƴ�������б�Ϊ�գ���Щ��������null.
 * remove() removeFirst()��removeLast():�ֱ𷵻��б��е�һ�������һ��Ԫ�أ����ص�Ԫ�ؽ�����б����Ƴ�������б�Ϊ�գ���Щ�����׳�NoSuchElementExcepetion;
 * @author de
 *
 */
public class ConcurrentLinkedDequeTest_6_2 {

	public static void main(String[] args) {
		ConcurrentLinkedDeque<String>  list = new ConcurrentLinkedDeque<>();  //��size()�����õ��Ĵ�С���ܲ���ȷ����Ҫͨ��������ͳ�ƣ������л����Ԫ�أ�
		Thread threads[] = new Thread[100];
		 for ( int i = 0; i < threads.length; i++){
			 AddTask  task= new AddTask(list);
			 threads[i] = new Thread(task);
			 threads[i].start();
		 }
		 
		 System.out.printf("Main: %d AddTask threads have been launched\n", threads.length);
		 
		 //��join()������AddTask�����߳������
		 for(int i = 0; i < threads.length; i++){
			 try{
				 threads[i].join();
			 }catch(InterruptedException e){
				 e.printStackTrace();
			 }
		 }
		 
		 System.out.printf("Main: Size of the List: %d\n", list.size());
		 
		 //ִ��PollTask,�Ƴ�Ԫ��
		 for ( int i = 0; i < threads.length; i++){
			 PollTask task = new PollTask(list);
			 threads[i] = new Thread(task);
			 threads[i].start();
		 }
		 System.out.printf("Main: %d PollTask threads have been launched\n",threads.length);
	
		 for ( int i = 0; i < threads.length; i++){
			 try{
				 threads[i].join();
			 }catch(InterruptedException e){
				 e.printStackTrace();
			 }
		 }
		 
		 System.out.printf("Main: Size of the list: %d\n", list.size());
	}
}


class AddTask implements Runnable{
	private ConcurrentLinkedDeque<String> list;
	public AddTask(ConcurrentLinkedDeque<String> list){
		this.list = list;
	}
	@Override
	public void run() {
		String name = Thread.currentThread().getName();
		for(int i = 0; i < 10000; i++){
			list.add(name+": Element  "+i);
		}
	}
	
}
class PollTask implements Runnable{
	private ConcurrentLinkedDeque<String> list;
	public PollTask(ConcurrentLinkedDeque<String> list){
		this.list = list;
	}
	
	
	@Override
	public void run() {
		for ( int i = 0; i < 5000; i++){
			list.pollFirst(); //���ز��Ƴ���һ��Ԫ�أ����򷵻�null
			list.pollLast(); //���� ���Ƴ����һ��Ԫ�أ��޸շ���null
		}
	}
	
}