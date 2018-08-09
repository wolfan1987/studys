package org.andrewliu.thread.jdblib;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * �����
 * @author de
 *�����������еĶ���
 * @param <T>
 */
public class Pool<T> {

	private int size;
	private List<T> items = new ArrayList<T>();
	private volatile boolean[] checkedOut;
	//����һ��������Ϣ��������������Ƿ��ǩ����ǩ��
	private Semaphore available;
	public Pool(Class<T> classObject, int size){
		this.size = size;
		checkedOut = new  boolean[size];
		available = new Semaphore(size,true);
		//��ʼ�����еĶ���
		for(int i = 0; i < size; ++i){
			try {
				items.add(classObject.newInstance());
			} catch (Exception e) {
				throw new RuntimeException(e);
			} 
		}
	}
	
	
	public T checkOut() throws InterruptedException{
		available.acquire(); //����Ƿ��ǩ��,���û�п�ǩ���ģ����������ڴˣ��ȴ�available.release()ǩ������Ļ���
		
		return getItem();
	}
	
	public void checkIn(T x){
		if(releaseItem(x)){ //���ն���ɹ�
			available.release(); //ǩ��һ������(����һ�����)����ʱ���ǩ���������ģ����ᱻrelease����
		}
	}
	
	private  synchronized T getItem(){  //����Ƿ��п��ö����ȡ
		for(int i = 0; i < size; ++i){
			if(!checkedOut[i]){
				checkedOut[i] = true;
				return items.get(i);
			}
		}
		return null;
	}
	
    private synchronized boolean releaseItem(T item){  //���ͷŵĶ���ǩ��״̬��Ϊfalse����ʾ���ڳ���
    	int index = items.indexOf(item);
    	if(index == -1) return false;
    	if(checkedOut[index]){
    		checkedOut[index] = false;
    		return true;
    	}
    	return false;
    }
	
}
