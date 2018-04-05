package org.andrewliu.java7thread.test;

import java.io.IOException;
import java.math.BigInteger;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 缓存的使用demo
 * @author de
 *
 */
public class Factorizer  implements Servlet{
	
	private final Computable<BigInteger,BigInteger[]> c = new Computable<BigInteger,BigInteger[]>(){

		@Override
		public BigInteger[] compute(BigInteger arg) throws InterruptedException {
			return factor(arg);
		}
		
	};
	//初始化key=BigInteger，value=BigInteger[]的缓存
	private final Computable<BigInteger,BigInteger[]> cache = new Memoizer<BigInteger,BigInteger[]>(c);
	
	public BigInteger[] factor(BigInteger arg){
		return null;
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
	public void init(ServletConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void service(ServletRequest req, ServletResponse resp)
			throws ServletException, IOException {
		try{
			BigInteger i = extractFromRequest(req);
			encodeIntoResponse(resp,cache.compute(i));
		} catch(InterruptedException e){
			e.getCause();
		}
	}

	private  BigInteger  extractFromRequest(ServletRequest req){
		return null;
	}
	
	public void encodeIntoResponse(ServletResponse resp,BigInteger[] args){
		
	}
}
