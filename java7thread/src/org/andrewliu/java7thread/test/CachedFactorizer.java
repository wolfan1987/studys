package org.andrewliu.java7thread.test;

import java.io.IOException;
import java.math.BigInteger;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 在用了Synchronized来同步时，被同步的代码块中没必要用AtomicXXX型的变量，因为synchronized可以保证安全性
 * 用synchronized 锁住代码块时，尽量让代码是短的。
 * @author de
 *
 */
public class CachedFactorizer  implements Servlet{

	private  BigInteger lastNumber;
	private BigInteger[] lastFactors;
	private long hits;
	private  long cacheHits;
	//ThreadLocal类的使用
	private static ThreadLocal<ImageData> imageHolder = new  ThreadLocal<ImageData>(){
		public ImageData initalValue(){
			return   new ImageData();
		}
	};
	
	//从Future中取得返回结果。
	public static ImageData getImageData(){
		return imageHolder.get();
	}
	
	public synchronized long getHits(){
		return hits;
		
	}
	public synchronized double getCacheHitRatio(){
		return (double) cacheHits / (double) hits;
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		BigInteger i =  new BigInteger("3000");
		BigInteger[]  factors = null;
		synchronized (this){
			++hits;
			if ( i.equals(lastNumber)){
				++cacheHits;
				factors = lastFactors.clone();
			}
		}
		
		if ( factors == null){
			//factors = factor(i);
			synchronized ( this){
				lastNumber = i;
				lastFactors = factors.clone();
			}
		}
		//encodeIntoResponse(resp,factors);
		
	}

}
