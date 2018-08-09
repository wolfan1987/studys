package org.andrewliu.thread.locktest;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * java �������е�:java.util.concurrent.locks�е���ʽ������ơ�
 * Lock������뱻��ʽ�ش������������ͷţ������ڽ��ĳЩ�ض������synchronized�ؼ������磺
 * ���ڳ��Ի�ȡ�������ջ�ȡʧ�ܣ������Ż�ȡ��һ��ʱ�䣬Ȼ��������������������synchronized
 * �ڳ��������ɵ�����������ά��ϵͳʹ�䴦�����õ�״̬��
 * ReentrantLock�������Ż�ȡ������δ��ȡ������������������Ѿ���ȡ�������������Ϳ��Ծ��������뿪ȥִ������һЩ���飬
 * �����ǵȴ�ֱ����������ͷš�
 * ������ڽ���synchronized����˵��Lock�Ŀ������ȸ������ȣ������ʵ��ר��ͬ���ṹ�����ã��磺���������б��еĽڵ�Ľڵ㴫�ݵļ������ƣ�Ҳ��
 * Ϊ����ϣ������ֱ�������������ͷŵ�ǰ�ڵ����֮ǰ������һ���ڵ������
 * @author de
 *
 */
public class MutexEvenGenerator  extends IntGenerator{
	private int currentEvenValue = 0;
	//��ʼ����ʽ��
	private Lock lock = new ReentrantLock();
	@Override
	public int next() {
		lock.lock();//��ʼ����,�൱��synchronized�ؼ��ֵ���ʼ{��,�Ӵ˴���ʼ����unlock��δ������ٽ���Դ��
		try{
			++currentEvenValue;
			Thread.yield();
			++currentEvenValue;
			return currentEvenValue;
		} finally{
			lock.unlock();//�������  
		}
	}

	public static void main(String[] args) {
		EvenChecker.test(new MutexEvenGenerator());
	}
}
