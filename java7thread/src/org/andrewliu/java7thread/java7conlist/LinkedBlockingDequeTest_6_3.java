package org.andrewliu.java7thread.java7conlist;

import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * ��������ʽ˫�˶���
 * ����ʽ�б��ڲ����ɾ������ʱ������б�������Ϊ�գ�������������ִ�У����ǽ����� ����������߳���������ֱ����������ִ�гɹ���
 * ����˵����
 * put():��������뵽�б��У�����б���������������������߳̽���������ֱ���б������˿��ÿռ䡣
 * take();���б���ȡ����������б�Ϊ�գ����� ����������߳̽�������ֱ���б�Ϊ�ա�
 * takeFirst()��takeLast(): ����Ԫ�ز��Ƴ����б�Ϊ�գ������߳�ֱ����Ԫ�ؿ��á�
 * getFirst()��getLast():  ����Ԫ�ز�����б����Ƴ������Ϊ�գ��׳���NoSuchElementException�쳣
 * peek() peekFirst��peekLast(): ���� Ԫ�ز��Ƴ�Ԫ�أ����Ϊ�գ����� null
 * poll,pollFirst()��pollLast(): ����Ԫ�ز����б����Ƴ�Ԫ�أ����Ϊ�գ����� null
 * add(),addFirst()��addLast(): ���Ԫ�أ�����б����������׳�IllegalStateException�쳣��
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