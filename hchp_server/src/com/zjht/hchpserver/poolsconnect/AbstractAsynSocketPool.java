package com.zjht.hchpserver.poolsconnect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public  abstract class AbstractAsynSocketPool<T> {

	protected volatile int size = 0;
	protected List<T> items = new ArrayList<T>();
	protected volatile boolean[] checkedOut;
	protected Map<String,T>  checkInMap = new ConcurrentHashMap<String,T>();
	protected volatile  int checkoutCount = 0;
	//定义一个计数信息量，来管理对象是否可签出和签入
	protected Semaphore available;
	
	public  boolean  initPool(Class<T> classObject, int size){
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
		return true;
	}
	
	public abstract  boolean  initPool(int size);
	
	
	public T checkOut() throws InterruptedException{
		available.acquire(); //检查是否可签出,如果没有可签出的，将会阻塞在此，等待available.release()签入对象后的唤醒
		System.out.println("checkout a asynsocket!");
		return getItem();
	}
	
	public void checkIn(T x){
		if(releaseItem(x)){ //回收对象成功
			System.out.println("checkin a asynsocket!");
			available.release(); //签入一个对象(返回一个许可)，此时如果签出有阻塞的，将会被release唤醒
		}  
	}
	
	private  synchronized T getItem(){  //检查是否有可用对象获取
		for(int i = 0; i < size; ++i){
			if(!checkedOut[i]){
				checkedOut[i] = true;
				checkoutCount++;
				T  obj  = items.get(i);
				checkInMap.put("key"+i, obj);
				return obj;
			}
		}
		return null;
	}
	
    private synchronized boolean releaseItem(T item){  //将释放的对象签出状态设为false，表示还在池中
    	int index = items.indexOf(item);
    	if(index == -1) return false;
    	if(checkedOut[index]){
    		checkedOut[index] = false;
    		checkoutCount--;
    		return true;
    	}
    	return false;
    }
    
    
}
