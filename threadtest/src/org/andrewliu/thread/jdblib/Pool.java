package org.andrewliu.thread.jdblib;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * 对象池
 * @author de
 *任务想放入池中的对象
 * @param <T>
 */
public class Pool<T> {

	private int size;
	private List<T> items = new ArrayList<T>();
	private volatile boolean[] checkedOut;
	//定义一个计数信息量，来管理对象是否可签出和签入
	private Semaphore available;
	public Pool(Class<T> classObject, int size){
		this.size = size;
		checkedOut = new  boolean[size];
		available = new Semaphore(size,true);
		//初始化池中的对象
		for(int i = 0; i < size; ++i){
			try {
				items.add(classObject.newInstance());
			} catch (Exception e) {
				throw new RuntimeException(e);
			} 
		}
	}
	
	
	public T checkOut() throws InterruptedException{
		available.acquire(); //检查是否可签出,如果没有可签出的，将会阻塞在此，等待available.release()签入对象后的唤醒
		
		return getItem();
	}
	
	public void checkIn(T x){
		if(releaseItem(x)){ //回收对象成功
			available.release(); //签入一个对象(返回一个许可)，此时如果签出有阻塞的，将会被release唤醒
		}
	}
	
	private  synchronized T getItem(){  //检查是否有可用对象获取
		for(int i = 0; i < size; ++i){
			if(!checkedOut[i]){
				checkedOut[i] = true;
				return items.get(i);
			}
		}
		return null;
	}
	
    private synchronized boolean releaseItem(T item){  //将释放的对象签出状态设为false，表示还在池中
    	int index = items.indexOf(item);
    	if(index == -1) return false;
    	if(checkedOut[index]){
    		checkedOut[index] = false;
    		return true;
    	}
    	return false;
    }
	
}
