package org.andrewliu.java7thread.java7conlist;

import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * 链表阻塞式双端队列
 * 阻塞式列表在插入和删除操作时，如果列表已满或为空，操作不会立即执行，而是将调用 这个操作的线程阻塞队列直到操作可以执行成功。
 * 方法说明：
 * put():将对象插入到列表中，如果列表已满，调用这个方法的线程将被阻塞，直到列表中有了可用空间。
 * take();从列表中取出对象，如果列表为空，调用 这个方法的线程将被阻塞直到列表不为空。
 * takeFirst()和takeLast(): 返回元素并移除，列表为空，阻塞线程直到有元素可用。
 * getFirst()和getLast():  返回元素不会从列表中移除，如果为空，抛出：NoSuchElementException异常
 * peek() peekFirst和peekLast(): 返回 元素不移除元素，如果为空，返回 null
 * poll,pollFirst()和pollLast(): 返回元素并从列表中移除元素，如果为空，返回 null
 * add(),addFirst()和addLast(): 添加元素，如果列表已满，将抛出IllegalStateException异常。
 * @author de
 *
 */
public class LinkedBlockingDequeTest_6_3 {

	public static void main(String[] args) throws InterruptedException {
		LinkedBlockingDeque<String> list = new LinkedBlockingDeque<>(3);
		LinkedBlockingDequeClient client = new LinkedBlockingDequeClient(list);
		Thread thread = new Thread(client);
		thread.start();
		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 3; j++){
				String request = list.take();
				System.out.printf("Main: Request: %s at %s. Size: %d\n", request,new Date(),list.size());
			}
			TimeUnit.MILLISECONDS.sleep(300);
		}
		
		System.out.printf("Main: End of the rpogram.\n");
	}
}

class LinkedBlockingDequeClient implements Runnable{
	private LinkedBlockingDeque<String>  requestList;

	public LinkedBlockingDequeClient(LinkedBlockingDeque<String> requestList) {
		super();
		this.requestList = requestList;
	}

	@Override
	public void run() {
		for ( int i = 0; i < 3; i++){
			for(int j = 0; j < 5; j++){
				StringBuilder request = new StringBuilder();
				request.append(i);
				request.append(":");
				request.append(j);
				requestList.push(request.toString());
				System.out.printf("Client: %s at %s.\n", request,new Date());
			}
			
			try{
				TimeUnit.SECONDS.sleep(2);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
		System.out.printf("Client: Ednd.\n");
	}
	
}