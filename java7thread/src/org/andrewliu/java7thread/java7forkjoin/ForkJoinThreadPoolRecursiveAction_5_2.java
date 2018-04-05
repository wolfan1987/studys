package org.andrewliu.java7thread.java7forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;


/**
 * Fork/Join��ܣ�����ExecutorService�ӿڵ���һ��ʵ�֣����������������͵����⣬��ʱҲ��Ϊ�ֽ�/�ϲ���ܡ�
 * ����Ҫ��������ܹ�ͨ�����μ�����Divide and Conquer Technique)�������ֳ�С��������⡣
 * �磺�ȼ��Ҫ���������Ĵ�С���������һ���趨�Ĵ�С���Ǿͽ������ֳɿ���ͨ�������ִ�е�С�����������Ĵ�С���趨
 * �Ĵ�СҪС���Ϳ���ֱ��������������������⣬Ȼ�󣬸�����Ҫ��������Ľ����
 * ʹ�ã�
 * һ�������Ƿ���Ҫ��֣�Ҫ����������������ԣ���������Ҫ�����Ԫ����Ŀ��ʱ�����ο�������
 * ���Խ�ForkJoinPool�࿴��һ�������Executorִ�������͡������������ֲ�����
 * �ֽ�(Fork)����������Ҫ��һ�������ֳɸ�С�Ķ������ʱ���ڿ����ִ����Щ����
 * �ϲ�(Join)��������һ��������ȴ��䴴���Ķ������������ִ�С�
 * Fork/Join��ܺ�Executor��ܣ���Ҫ���������ڹ�����ȡ�㷨��Join������һ��������ȴ��������������������ɣ�ִ�����
 * ������̳߳�֮Ϊ�������̣߳��������߳�Ѱ��������δ��ִ�е�����Ȼ��ʼִ�С�
 * Fork/Join���ִ�е��������������ƣ�
 * 1������ֻ��ʹ��fork()��join()��������ͬ�����ơ�fork/join�Ĺ������̺߳������߳���ͬ��ִ�еģ���������߳����ߣ��������߳�Ҳ��ִ����������
 * 2��������ִ��I/O�����������ļ����ݵĶ�ȡ��д�롣
 * 3���������׳�������ʱ�쳣�������ڴ����д������Щ�쳣��
 * Fork/Join��ܺ��������ɣ�
 * ForkJoinPool:������ʵ����ExecutorService�ӿں͹�����ȡ�㷨�������������̣߳����ṩ�����״̬��Ϣ���Լ�ִ����Ϣ��
 * ForkJoinTask:�������һ������ForkJoinPool��ִ�е�����Ļ��ࡣ
 * Ҫ��ʵ��Fork/Join������Ҫʵ��һ������������֮һ�����ࣺ
 * RecursiveAction: ��������û�з��ؽ���ĳ�����
 * RecursiveTask:�����������з��ؽ���ĳ�����
 * һ��ʵ���ǣ���ForkJoinPoolִ��ʵ��������ForkJoinTask�����������
 * ����ͬ��ͬ���ķ�ʽ����ִ�����񣬵�һ��������ִ������������������ʱ�������񽫵ȴ���������ɣ�ִ����������̳߳�Ϊ�������̡߳�
 * �������ִ�к�û�κη��ؽ��������RecursiveAction����Ϊʵ������Ļ��ࡣ
 * 
 * ForkJoinPool������ִ������ķ�����
 * 1��execute(Runnabletask):�����ݽ�ȥ����Runnable�����ForkJoin�࣬��ʹ��Runnable����ʱForkJoinPool��Ͳ����ù�����ȡ�㷨��������ʹ��
 *                           ForkJoinTask��ʱ���ù�����ȡ�㷨.
 * 2��invoke(ForkJoin<T> task): ForkJoinPool���execute()�������첽���õģ���ForkJoinPool���invoke()��������ͬ�����õġ��������ֱ��
 * 							 ���ݽ���������ִ�н�����Ż᷵�ء�
 * 3������ForkJoinPool��ʹ��Callable����ʱҲ��ʹ�ù�����ȡ�㷨�����ֻ��ִ������ʹ��Callable.
 * 4��invokeAll(ForkJoinTask<?>...tasks): ����汾�ķ�������һ���ɱ�Ĳ����б����Դ��ݾ����ܶ��ForkJoinTask��������������Ϊ������
 * 5��invokeAll(Collection<T> tasks): ����汾�ķ�������һ���������͵�T�Ķ��󼯺�,��ArrayList,LinkedList,TreeSet���󣬵�T������ForkJoinTask
 *                                    ��������������ࡣ
 * 6������ʹ��ForkJoinPoolִ��forkJoinTask����Ҳ������ִ��Runnable��Callable��������Ҳ����ʹ��ForkJoinTask���addapt()����������
 *    һ��Callable�������һ��Runnable����Ȼ��֮ת��Ϊһ��������ȥִ�С�
 * 
 * @author de
 *
 */
public class ForkJoinThreadPoolRecursiveAction_5_2 {

	public static void main(String[] args) {
		ForkJoinProductListGenerator  generator = new ForkJoinProductListGenerator();
		List<ForkJoinProduct>  products = generator.generate(10000);//����10000����Ʒ
		//�½�һ��������¸��²�Ʒ�б��е����в�Ʒ
		ForkJoinActionTask  task = new ForkJoinActionTask(products,0,products.size(),0.20);
		ForkJoinPool pool = new ForkJoinPool();  //Fork/Join�̳߳�
		pool.execute(task);//ִ������
		do{
			System.out.printf("Main: Thread count: %d\n", pool.getActiveThreadCount());  //һ�����ٸ������
			System.out.printf("Main: Thread Steal: %d\n", pool.getStealCount()); //һ�����������ȡ������
			System.out.printf("Main: Parallelism: %d\n", pool.getParallelism()); //һ�����ڲ���ִ�ж��ٸ�����
			try{
				TimeUnit.MILLISECONDS.sleep(5);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}while(!task.isDone());
		pool.shutdown();
		
		if(task.isCompletedNormally()){  //��������Ƿ��Ѿ���ɲ���û�д���
			System.out.printf("Main: The process has completed normally.\n");
		}
		
		//��ӡ���в�Ʒ�۸�
		for(int i = 0; i < products.size(); i++){
			ForkJoinProduct  product = products.get(i);
			if(product.getPrice() != 12){
				System.out.printf("Product %s: %f\n", product.getName(),product.getPrice());
			}
		}
		
		System.out.println("Main : end of the program.\n");
		
	}
}

/**
 * �̳�RecursiveActionʱ��һ��Ҫ�������л��汾��,��ʵ��compute������
 * @author de
 *
 */
class ForkJoinActionTask extends RecursiveAction{

	private static final long serialVersionUID = 7536410814410312870L;
	private List<ForkJoinProduct>  products;
	private int first;
	private int last;
	private double increment;
	
	public ForkJoinActionTask(List<ForkJoinProduct> products, int first,
			int last, double increment) {
		super();
		this.products = products;
		this.first = first;
		this.last = last;
		this.increment = increment;
	}


	@Override
	protected void compute() {
		  if(last-first<10){ //����Ʒ��С��10��ʱ��ֱ��һ��������ɸ��¼۸�
			  updatePrices();
		  }else{  //����Ʒ�����ڵ���10ʱ������Ʒ�����м�ָ�Ȼ�󽻸���������ȥִ��.
			  int middle = (last+first)/2;
			  System.out.printf("Task : Pending tasks: %s\n" , getQueuedTaskCount());//�õ���������������
			  ForkJoinActionTask  task1 = new ForkJoinActionTask(products,first,middle-1,increment);
			  ForkJoinActionTask  task2 = new ForkJoinActionTask(products,middle+1,last,increment);
			  //ͬʱִ����������:
			  invokeAll(task1,task2);
		  }
	}
	
	/**
	 * ֱ�Ӹ��¼۸�ķ���
	 */
	private  void updatePrices(){
		for ( int i = first; i < last; i++){
			ForkJoinProduct product = products.get(i);
			product.setPrice(product.getPrice()*(1+increment));//���20%
		}
	}
}

class ForkJoinProductListGenerator{
	public List<ForkJoinProduct>  generate (int size){
		List<ForkJoinProduct> ret = new ArrayList<ForkJoinProduct>();
		
		for ( int i = 0; i < size; i++){
			ForkJoinProduct product = new ForkJoinProduct();
			product.setName("Product--- "+i);
			product.setPrice(10);
			ret.add(product);
		}
		return ret;
	}
	
}

class ForkJoinProduct{
	private String name;
	private double price;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
}
