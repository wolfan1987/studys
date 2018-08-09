//package org.andreliu.ds.binarytrue;
//
//public class BTTree {
//	NodeIn root;
//	int size;
//
//	public NodeIn find(int key) {
//		NodeIn current = root;
//
//		if (root == null) // empty tree
//			return null;
//		else {
//			while (current.getKey() != key) {
//				if (key < current.getKey()) { // go left
//					current = current.getLeftChild();
//				} else {
//					current = current.getRightChild(); // go right
//				}
//
//				if (current == null) {
//					return null;
//				}
//			}
//		}
//
//		return current;
//	}
//
//	/**
//	 * @param key
//	 * @param data
//	 */
//	public void insert(int key, int data) {
//		Node newNode = new Node(key, data);
//
//		if (root == null) {
//			root = newNode;
//			size++;
//		} else { // not empty tree
//			Node current = root;
//			Node parent;
//
//			while (true) {
//				parent = current;
//				if (key < current.getKey()) {
//					current = current.getLeftChild();
//					if (current == null) {
//						parent.setLeftChild(newNode);
//						size++;
//						return;
//					}
//				} // end if go left
//				else {
//					current = current.getRightChild();
//					if (current == null) {
//						parent.setRightChild(newNode);
//						size++;
//						return;
//					}
//				} // end if go right
//			} // end while (true)
//		}
//	} // end insert
//
//	/**
//	 * 
//	 * @param Node
//	 *            localRoot
//	 */
//	private void inOrder(Node localRoot) {
//		if (localRoot != null) {
//			inOrder(localRoot.getLeftChild());
//			localRoot.display();
//			inOrder(localRoot.getRightChild());
//		} else
//			return;
//	}
//
//	/**
//	 * 中序遍历
//	 */
//	public void inOrder() {
//		inOrder(root);
//	}
//
//	public int getSize() {
//		return size;
//	}
//
//	// 删除某个结点
//	public boolean delete(int key) {
//		if (root == null) // empty tree
//			return false;
//		Node current = root;
//		Node parent = root;
//		boolean isLeftChild = false;
//		while (current.getKey() != key) {
//			parent = current;
//			if (key < current.getKey()) {
//				isLeftChild = true;
//				current = current.getLeftChild();
//			} else {
//				isLeftChild = false;
//				current = current.getRightChild();
//			}
//			if (current == null) { // not found
//				return false;
//			}
//		}
//		// found it
//		// 删除的结点是叶子结点
//		if (current.getLeftChild() == null && current.getRightChild() == null) {
//			if (current == root)
//				root = null;
//			if (isLeftChild)
//				parent.setLeftChild(null);
//			else
//				parent.setRightChild(null);
//		} else if (current.getRightChild() == null) {// 删除的结点只有左孩子结点
//			if (current == root) {
//				root = current.getLeftChild();
//			}
//			if (isLeftChild) {
//				parent.setLeftChild(current.getLeftChild());
//			} else {
//				parent.setRightChild(current.getRightChild());
//			}
//		} else if (current.getLeftChild() == null) {// 删除的结点只有右孩子
//			if (current == root)
//				root = current.getRightChild();
//			if (isLeftChild) {
//				parent.setLeftChild(current.getRightChild());
//			} else {
//				parent.setRightChild(current.getRightChild());
//			}
//		} else {// 删除的结点有左右孩子
//	           NodeIn successor = getSuccessor(current);
//			if (current == root) {
//				root = successor;
//			}
//			if (isLeftChild) {
//				parent.setLeftChild(successor);
//			} else {
//				parent.setRightChild(successor);
//			}
//			successor.setLeftChild(current.getLeftChild());
//		}
//
//		return true;
//	}
//
//	// 从右子树中查找替代被删除结点位置的结点
//	private Node getSuccessor(NodeIn delNode) {
//		NodeIn successor = delNode;
//		NodeIn parentSuccessor = delNode;
//		NodeIn current = delNode.getRightChild();
//		while (current != null) {
//			parentSuccessor = successor;
//			successor = current;
//			current = current.getLeftChild();
//		}
//
//		if (successor != delNode.getRightChild()) {
//			parentSuccessor.setLeftChild(successor.getRightChild());
//			successor.setRightChild(delNode.getRightChild());
//		}
//
//		return successor;
//	}
//
//}
//
//class Employee {
//	int id;
//	String name;
//	char gender;
//	// getter,setter
//}
//
//// 结点类
//class NodeIn {
//	Employee emp;
//	NodeIn leftChild;
//	NodeIn rightChild;
//	
//
//	public void display() {
//		// 打印出自己与孩子结点的姓名或者其它
//		System.out.println(emp.name);
//		if (leftChild != null) {
//			System.out.println(leftChild.emp.name);
//		}
//		if (leftChild != null) {
//			System.out.println(rightChild.emp.name);
//		}
//	}
//
//	public NodeIn getLeftChild() {
//		return leftChild;
//	}
//
//	public void setLeftChild(NodeIn leftChild) {
//		this.leftChild = leftChild;
//	}
//
//	public NodeIn getRightChild() {
//		return rightChild;
//	}
//
//	public void setRightChild(NodeIn rightChild) {
//		this.rightChild = rightChild;
//	}
//	
//	
//}