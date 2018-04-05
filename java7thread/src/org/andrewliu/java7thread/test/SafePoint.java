package org.andrewliu.java7thread.test;

/**
 * 线程安全且可变的Point类
 * @author de
 *
 */
public class SafePoint {

	private int x,y; 
	//私有不可变
	private SafePoint(int[] a){
		this(a[0],a[1]);
	}
	//
	public SafePoint(SafePoint p){
		this(p.get());
	}
	
	public SafePoint(int x,int y){
		this.x = x;
		this.y = y;
	}
	//只提供以同步加数组形式访问对象的值
	public synchronized int[] get(){
		return new int[] {x,y};
	}
	//提供同步接口来修改对象的值
	public synchronized void set(int x,int y){
		this.x = x;
		this.y = y;
	}
}
