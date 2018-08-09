package org.andreliu.ds.binarytrue;

import java.util.EmptyStackException;
import java.util.LinkedList;

public class Stack {

	private LinkedList list;
	 
    public Stack() {
        this.list = new LinkedList();
    }
 
    public boolean empty() {
        return list.isEmpty();
    }
 
    public Object peek() {
        if (empty())
            throw new EmptyStackException();
        return list.getLast();
    }
 
    public Object pop() {
        if (empty())
            throw new EmptyStackException();
        return list.removeLast();
    }
 
    public void push(Object o) {
        list.add(o);
    }
 
    public static void main(String[] args) {
        Stack stack = new Stack();
        stack.push(new Integer(1));
        stack.push(new Integer(11));
        stack.push(new Integer(1111));
        stack.push(new Integer(22));
        stack.push(new Integer(222));
        stack.push(new Integer(31));
        stack.push(new Integer(221));
        while (!stack.empty()) {
            System.out.println(stack.pop());
        }
    }
}
