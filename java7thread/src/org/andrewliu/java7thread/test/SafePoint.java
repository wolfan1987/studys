package org.andrewliu.java7thread.test;

/**
 * �̰߳�ȫ�ҿɱ��Point��
 * @author de
 *
 */
public class SafePoint {

	private int x,y; 
	//˽�в��ɱ�
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
	//ֻ�ṩ��ͬ����������ʽ���ʶ����ֵ
	public synchronized int[] get(){
		return new int[] {x,y};
	}
	//�ṩͬ���ӿ����޸Ķ����ֵ
	public synchronized void set(int x,int y){
		this.x = x;
		this.y = y;
	}
}
