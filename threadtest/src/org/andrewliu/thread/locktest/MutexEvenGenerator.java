package org.andrewliu.thread.locktest;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * java 并发库中的:java.util.concurrent.locks中的显式互斥机制。
 * Lock对象必须被显式地创建、锁定、释放，它用于解决某些特定问题比synchronized关键更灵活。如：
 * 用于偿试获取锁且最终获取失败，或尝试着获取锁一段时间，然后放弃锁。他还可以完成synchronized
 * 在出错后不能完成的清理工作，经维护系统使其处理良好的状态。
 * ReentrantLock允许尝试着获取但最终未获取锁，这样如果其他人已经获取了这个锁，那你就可以决定决定离开去执行其他一些事情，
 * 而不是等待直至这个锁被释放。
 * 相对于内建的synchronized锁来说，Lock的控制力度更继粒度，这对于实现专有同步结构很有用，如：遍历链接列表中的节点的节点传递的加锁机制（也称
 * 为锁耦合），这种遍历代码必须在释放当前节点的锁之前捕获下一个节点的锁。
 * @author de
 *
 */
public class MutexEvenGenerator  extends IntGenerator{
	private int currentEvenValue = 0;
	//初始化显式锁
	private Lock lock = new ReentrantLock();
	@Override
	public int next() {
		lock.lock();//开始锁定,相当于synchronized关键字的起始{处,从此处开始，到unlock这段代码是临界资源。
		try{
			++currentEvenValue;
			Thread.yield();
			++currentEvenValue;
			return currentEvenValue;
		} finally{
			lock.unlock();//解除锁定  
		}
	}

	public static void main(String[] args) {
		EvenChecker.test(new MutexEvenGenerator());
	}
}
