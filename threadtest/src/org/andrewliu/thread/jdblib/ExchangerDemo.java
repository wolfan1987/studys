package org.andrewliu.thread.jdblib;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Exchanger;


public class ExchangerDemo {

}


class ExchangerProducer<T> implements Runnable{
	private Random rand = new Random();
	private Exchanger<List<T>> exchanger;
	private List<T>	 holder;
	private Class  entityClass ;
	ExchangerProducer(Exchanger<List<T>> exchg,List<T> holder){
		exchanger = exchg;
		this.holder = holder;
		 entityClass = (Class)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
		 System.out.println(entityClass);
		
	}
	@Override
	public void run() {
//		try{
//			while(!Thread.interrupted()){
//				for(int i = 0; i < 5;i++){
//				//	holder.add(entityClass.newInstance());
//				}
//			}
//		}catch(InterruptedException e){
//			
//		}
	}
	
	
}