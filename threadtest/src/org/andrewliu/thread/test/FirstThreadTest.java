package org.andrewliu.thread.test;

import java.util.concurrent.TimeUnit;

/**
 * 用Runnable 接口定义一个任务
 * @author de
 *
 */
public class FirstThreadTest  implements Runnable{

	protected int countDown = 10;
	private  static int taskCount = 0;
	private final  int  id = taskCount++;
	public FirstThreadTest(){
	}
	
	public  FirstThreadTest(int countDown){
		this.countDown = countDown;
	}
	public  String status(){
		return "#"+id+"("+(countDown > 0 ? countDown : "FirstThreadTest")+"),";
	}
	@Override
	public void run() {
		
		while(countDown-- > 0){
			System.out.println(status());
			//我执行了一会儿任务，线程调度器，你看你要对我进行调度不，现在我可以让出时间让别人执行一会儿,调度器不叫我我就不醒
			//如果线程的优先级相同，同样会将让另一个线程优先
			//Thread.yield();
			try {
				//我只睡1000毫秒,这是jdb1.5之前的用法
				//Thread.sleep(1000);
				//jdb1.5/6中表示线程休眠的方式
				TimeUnit.MILLISECONDS.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public static  void main(String[] args){
		//直接调用run方法，执行任务
		FirstThreadTest  test = new FirstThreadTest();
		test.run();
		//用Thread类包装任务(需要一个实现了Runnable接口的对象)，再用Thread启动任务
		Thread  thread = new Thread(new FirstThreadTest());
		System.out.println("用Thread.start 调用run 开始。。。。。。。。。");
		thread.start();//start 方法会调用run方法
		System.out.println(" Waiting  for LiftOff!");
		
		//多个线程之间交替执行查看,因为任务中有Thread.yield()，所以在调用时，各线程之间会让步，从而打印结果会出现穿插情况
		for ( int i = 0 ; i < 5; i++){
			System.out.println("在for循环中调用Thread.start 调用run 开始。。。。。。。。。i===="+i);
			new Thread(new FirstThreadTest()).start();
		}
		
		
	}
	
	
}
