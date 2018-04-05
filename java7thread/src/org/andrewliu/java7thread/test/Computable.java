package org.andrewliu.java7thread.test;

public interface Computable<A,V>{
	V compute(A arg) throws InterruptedException;

}
