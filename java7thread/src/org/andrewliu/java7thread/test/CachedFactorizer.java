package org.andrewliu.java7thread.test;

import java.io.IOException;
import java.math.BigInteger;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * ������Synchronized��ͬ��ʱ����ͬ���Ĵ������û��Ҫ��AtomicXXX�͵ı�������Ϊsynchronized���Ա�֤��ȫ��
 * ��synchronized ��ס�����ʱ�������ô����Ƕ̵ġ�
 * @author de
 *
 */
public class CachedFactorizer  implements Servlet{

	private  BigInteger lastNumber;
	private BigInteger[] lastFactors;
	private long hits;
	private  long cacheHits;
	//ThreadLocal���ʹ��
	private static ThreadLocal<ImageData> imageHolder = new  ThreadLocal<ImageData>(){
		public ImageData initalValue(){
			return   new ImageData();
		}
	};
	
	//��Future��ȡ�÷��ؽ����
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
