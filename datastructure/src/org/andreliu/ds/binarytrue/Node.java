package org.andreliu.ds.binarytrue;

public class Node<T> {

	//Ê÷µÄ½Úµã  
	      
	    private Node<T> left;  
	    private Node<T> right;  
	    private T value;  
	      
	    public Node() {  
	    }  
	    public Node(Node<T> left,Node<T> right,T value) {  
	        this.left = left;  
	        this.right = right;  
	        this.value = value;  
	    }  
	      
	    public Node(T value) {  
	        this(null,null,value);  
	    }  
	    public Node<T> getLeft() {  
	        return left;  
	    }  
	    public void setLeft(Node<T> left) {  
	        this.left = left;  
	    }  
	    public Node<T> getRight() {  
	        return right;  
	    }  
	    public void setRight(Node<T> right) {  
	        this.right = right;  
	    }  
	    public T getValue() {  
	        return value;  
	    }  
	    public void setValue(T value) {  
	        this.value = value;  
	    }  
	          
}
