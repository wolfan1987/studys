package org.andreliu.ds.binarytrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;

public class BinaryTree2<T> {

	private Node<T> root;

	public BinaryTree2() {
	}

	public BinaryTree2(Node<T> root) {
		this.root = root;
	}

	// ����������
	public void buildTree() {

		Scanner scn = null;
		try {
			scn = new Scanner(new File("F:\\workspace10\\datastructure\\bin\\input.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		root = createTree(root, scn);
	}

	// �����������������
	private Node<T> createTree(Node<T> node, Scanner scn) {

		String temp = scn.next();

		if (temp.trim().equals("#")) {
			return null;
		} else {
			node = new Node<T>((T) temp);
			node.setLeft(createTree(node.getLeft(), scn));
			node.setRight(createTree(node.getRight(), scn));
			return node;
		}

	}

	// �������(�ݹ�)
	public void inOrderTraverse() {
		inOrderTraverse(root);
	}

	public void inOrderTraverse(Node<T> node) {
		if (node != null) {
			inOrderTraverse(node.getLeft());
			System.out.println(node.getValue());
			inOrderTraverse(node.getRight());
		}
	}

	// �������(�ǵݹ�)
	public void nrInOrderTraverse() {

		Stack<Node<T>> stack = new Stack<Node<T>>();
		Node<T> node = root;
		while (node != null || !stack.isEmpty()) {
			while (node != null) {
				stack.push(node);
				node = node.getLeft();
			}
			node = stack.pop();
			System.out.println(node.getValue());
			node = node.getRight();

		}

	}

	// �������(�ݹ�)
	public void preOrderTraverse() {
		preOrderTraverse(root);
	}

	public void preOrderTraverse(Node<T> node) {
		if (node != null) {
			System.out.println(node.getValue());
			preOrderTraverse(node.getLeft());
			preOrderTraverse(node.getRight());
		}
	}

	// ����������ǵݹ飩
	public void nrPreOrderTraverse() {

		Stack<Node<T>> stack = new Stack<Node<T>>();
		Node<T> node = root;

		while (node != null || !stack.isEmpty()) {

			while (node != null) {
				System.out.println(node.getValue());
				stack.push(node);
				node = node.getLeft();
			}
			node = stack.pop();
			node = node.getRight();
		}

	}

	// �������(�ݹ�)
	public void postOrderTraverse() {
		postOrderTraverse(root);
	}

	public void postOrderTraverse(Node<T> node) {
		if (node != null) {
			postOrderTraverse(node.getLeft());
			postOrderTraverse(node.getRight());
			System.out.println(node.getValue());
		}
	}

	// ��������(�ǵݹ�)
	public void nrPostOrderTraverse() {

		Stack<Node<T>> stack = new Stack<Node<T>>();
		Node<T> node = root;
		Node<T> preNode = null;// ��ʾ���һ�η��ʵĽڵ�

		while (node != null || !stack.isEmpty()) {

			while (node != null) {
				stack.push(node);
				node = node.getLeft();
			}

			node = stack.peek();

			if (node.getRight() == null || node.getRight() == preNode) {
				System.out.println(node.getValue());
				node = stack.pop();
				preNode = node;
				node = null;
			} else {
				node = node.getRight();
			}

		}

	}

	// ����α���
	public void levelTraverse() {
		levelTraverse(root);
	}

	public void levelTraverse(Node<T> node) {

		Queue<Node<T>> queue = new LinkedBlockingQueue<Node<T>>();
		queue.add(node);
		while (!queue.isEmpty()) {

			Node<T> temp = queue.poll();
			if (temp != null) {
				System.out.println(temp.getValue());
				queue.add(temp.getLeft());
				queue.add(temp.getRight());
			}

		}

	}
	
	
	
	
	/** 
     * @param args 
     */  
    public static void main(String[] args) {  
    	BinaryTree2<Integer> tree = new BinaryTree2<Integer>();  
        tree.buildTree();  
        System.out.println("�������");  
        tree.inOrderTraverse();  
        tree.nrInOrderTraverse();  
        System.out.println("��������");  
        //tree.nrPostOrderTraverse();  
        tree.postOrderTraverse();  
        tree.nrPostOrderTraverse();  
        System.out.println("�������");  
        tree.preOrderTraverse();  
        tree.nrPreOrderTraverse();  
	
	
   }
}
