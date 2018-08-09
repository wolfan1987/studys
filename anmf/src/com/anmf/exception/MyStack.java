package com.anmf.exception;

public class MyStack {

		private Object[] stack;
		
		private int size;
		
		public MyStack(){
			this(10);
		}
		
		public MyStack(int len){
			stack = new Object[len];
		}
		
		public int size(){
			return size;
		}
		
		public int capacity(){
			return stack.length;
		}
		
		//ʵ�ֶ�̬����;
		public void ensureCapacity(){
			if(size() == capacity()){
				Object[] newStack = new Object[size() * 3 / 2 + 1];
				System.arraycopy(stack, 0, newStack, 0, size());
				stack = newStack;
			}
		}
		public void add(Object o){
			size++;
			ensureCapacity();
			stack[size - 1] = o;
		}
		public boolean isEmpty(){
			return size == 0;
		}
		
		//��ջ
		public Object get(){
			if(isEmpty()){
				throw new ArrayIndexOutOfBoundsException("����Ϊ��");
			}
			
			Object o = stack[--size];
			stack[size] = null;
			return o;
		}
		//ɾ��һ��
		public  void delete(){
			System.out.println(get()+"��ɾ��");
			
		}
		
		public static void main(String[] args) {
			MyStack stack = new MyStack(3);
			String[] data = new String[] { "aa", "bbb", "cc" };
			for (int i = 0; i < data.length; i++) {
					stack.add(data[i]);
					System.out.println(data[i] + "");
			}
			System.out.println("***********");
			while (!stack.isEmpty()) {
				System.out.println(stack.get() + "");
				stack.delete();
			}
			//} 
	    }
}
