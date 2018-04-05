package org.andrewliu.java7thread.java7forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;


/**
 * Fork/Join框架：它是ExecutorService接口的另一种实现，用来解决特殊的类型的问题，有时也称为分解/合并框架。
 * 它主要用来解决能够通过分治技术（Divide and Conquer Technique)将问题拆分成小任务的问题。
 * 如：先检查要解决的问题的大小，如果大于一个设定的大小，那就将问题拆分成可以通过框架来执行的小任务。如果问题的大小比设定
 * 的大小要小，就可以直接在任务里解决问这个问题，然后，根据需要返回任务的结果。
 * 使用：
 * 一个任务是否需要拆分，要依赖于任务本身的特性，可以任务要处理的元素数目或时间来参考决定。
 * 可以将ForkJoinPool类看作一个特殊的Executor执行器类型。它有以下两种操作：
 * 分解(Fork)操作：当需要将一个任务拆分成更小的多个任务时，在框架中执行这些任务。
 * 合并(Join)操作：当一个主任务等待其创建的多个子任务的完成执行。
 * Fork/Join框架和Executor框架，主要的区别在于工作窃取算法。Join操作让一个主任务等待它所创建的子任务的完成，执行这个
 * 任务的线程称之为工作者线程，工作者线程寻找其他仍未被执行的任务，然后开始执行。
 * Fork/Join框架执行的任务有以下限制：
 * 1、任务只能使用fork()和join()操作当作同步机制。fork/join的工作者线程和任务线程是同步执行的，如果任务线程休眠，工作者线程也不执行其他任务。
 * 2、任务不能执行I/O操作，比如文件数据的读取与写入。
 * 3、任务不能抛出非运行时异常，必须在代码中处理掉这些异常。
 * Fork/Join框架核心类的组成：
 * ForkJoinPool:它个类实现了ExecutorService接口和工作窃取算法，它管理工作者线程，并提供任务的状态信息，以及执行信息。
 * ForkJoinTask:这个类是一个将在ForkJoinPool中执行的任务的基类。
 * 要想实现Fork/Join任务，需要实现一个以下两个类之一的子类：
 * RecursiveAction: 用于任务没有返回结果的场景。
 * RecursiveTask:彡用于任务有返回结果的场景。
 * 一般实现是：用ForkJoinPool执行实现了任务ForkJoinTask的子类的任务。
 * 任务同以同步的方式方法执行任务，当一个主任务执行两个或更多的子任务时，主任务将等待子任务完成，执行主任务的线程称为工作者线程。
 * 如果任务执行后没任何返回结果，则用RecursiveAction类作为实现任务的基类。
 * 
 * ForkJoinPool类其他执行任务的方法：
 * 1、execute(Runnabletask):即传递进去的是Runnable任务给ForkJoin类，当使用Runnable对象时ForkJoinPool类就不采用工作窃取算法，他仅在使用
 *                           ForkJoinTask类时才用工作窃取算法.
 * 2、invoke(ForkJoin<T> task): ForkJoinPool类的execute()方法是异步调用的，而ForkJoinPool类的invoke()方法则是同步调用的。这个方法直到
 * 							 传递进来的任务执行结束后才会返回。
 * 3、当在ForkJoinPool里使用Callable对象时也不使用工作窃取算法，最好只在执行器里使用Callable.
 * 4、invokeAll(ForkJoinTask<?>...tasks): 这个版本的方法接收一个可变的参数列表，可以传递尽可能多的ForkJoinTask对象给这个方法作为参数。
 * 5、invokeAll(Collection<T> tasks): 这个版本的方法接受一个泛型类型的T的对象集合,如ArrayList,LinkedList,TreeSet对象，但T必须是ForkJoinTask
 *                                    类或者其它的子类。
 * 6、可以使用ForkJoinPool执行forkJoinTask，但也能用来执行Runnable和Callable对象，我们也可以使用ForkJoinTask类的addapt()方法来接收
 *    一个Callable对象或者一个Runnable对象，然后将之转化为一个对象，再去执行。
 * 
 * @author de
 *
 */
public class ForkJoinThreadPoolRecursiveAction_5_2 {

	public static void main(String[] args) {
		ForkJoinProductListGenerator  generator = new ForkJoinProductListGenerator();
		List<ForkJoinProduct>  products = generator.generate(10000);//生成10000个产品
		//新建一个任务更新更新产品列表中的所有产品
		ForkJoinActionTask  task = new ForkJoinActionTask(products,0,products.size(),0.20);
		ForkJoinPool pool = new ForkJoinPool();  //Fork/Join线程池
		pool.execute(task);//执行任务
		do{
			System.out.printf("Main: Thread count: %d\n", pool.getActiveThreadCount());  //一个多少个活动任务
			System.out.printf("Main: Thread Steal: %d\n", pool.getStealCount()); //一个多个个被窃取的任务
			System.out.printf("Main: Parallelism: %d\n", pool.getParallelism()); //一个正在并行执行多少个任务
			try{
				TimeUnit.MILLISECONDS.sleep(5);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}while(!task.isDone());
		pool.shutdown();
		
		if(task.isCompletedNormally()){  //检查任务是否已经完成并且没有错误
			System.out.printf("Main: The process has completed normally.\n");
		}
		
		//打印所有产品价格
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
 * 继承RecursiveAction时，一定要生成序列化版本号,并实现compute方法。
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
		  if(last-first<10){ //当产品数小于10个时，直接一个任务完成更新价格
			  updatePrices();
		  }else{  //当产品数大于等于10时，将产品数按中间分隔然后交给两个任务去执行.
			  int middle = (last+first)/2;
			  System.out.printf("Task : Pending tasks: %s\n" , getQueuedTaskCount());//得到队列中总任务数
			  ForkJoinActionTask  task1 = new ForkJoinActionTask(products,first,middle-1,increment);
			  ForkJoinActionTask  task2 = new ForkJoinActionTask(products,middle+1,last,increment);
			  //同时执行两个任务:
			  invokeAll(task1,task2);
		  }
	}
	
	/**
	 * 直接更新价格的方法
	 */
	private  void updatePrices(){
		for ( int i = first; i < last; i++){
			ForkJoinProduct product = products.get(i);
			product.setPrice(product.getPrice()*(1+increment));//提价20%
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
