package org.andrewliu.java7thread.java7conlist;

import java.util.concurrent.ConcurrentLinkedDeque;


/** 并发链表式双端队列.
 * 非阻塞式并发列表（并发列表）：ConcurrentLinkedDeque: 如果执行的操作不能够立即运行，方法会抛出异常或返回null.
 * 其它操作元素方法：
 * getFirst()和getLast()：返回列表中第一个和最后一个元素，返回的元素不会从列表中移除，如果列表为空，这两个方法抛出NoSuchElementException异常.
 * peek(),peekFirst()和peeklast()：分别返回列表中第一个和最后一个元素，返回的元素不会从列表中移除。如果列表为空，这些方法返回null.
 * remove() removeFirst()和removeLast():分别返回列表中第一个和最后一个元素，返回的元素将会从列表中移除，如果列表为空，这些方法抛出NoSuchElementExcepetion;
 * @author de
 *
 */
public class ConcurrentLinkedDequeTest_6_2 {

	public static void main(String[] args) {
		ConcurrentLinkedDeque<String>  list = new ConcurrentLinkedDeque<>();  //其size()方法得到的大小可能不正确，需要通过遍历来统计（并发中会操作元素）
		Thread threads[] = new Thread[100];
		 for ( int i = 0; i < threads.length; i++){
			 AddTask  task= new AddTask(list);
			 threads[i] = new Thread(task);
			 threads[i].start();
		 }
		 
		 System.out.printf("Main: %d AddTask threads have been launched\n", threads.length);
		 
		 //用join()方法让AddTask任务线程先完成
		 for(int i = 0; i < threads.length; i++){
			 try{
				 threads[i].join();
			 }catch(InterruptedException e){
				 e.printStackTrace();
			 }
		 }
		 
		 System.out.printf("Main: Size of the List: %d\n", list.size());
		 
		 //执行PollTask,移除元素
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
			list.pollFirst(); //返回并移除第一个元素，无则返回null
			list.pollLast(); //返回 并移除最后一个元素，无刚返回null
		}
	}
	
}