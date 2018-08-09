package org.andreliu.ds.binarytrue4my;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * �Զ��������в��� 1������һ�������� 2�����������������ݶ��� 3��ɾ�����������ݶ��� 4����nodeKey�Ӷ������ϲ���ĳ���ݶ���
 * 5����ӡ�����ҵ���ĳ�ڵ���������нڵ� 6��ǰ��������ݹ�/�ǵݹ�[ջ]�� 7��������� ���ݹ�/�ǵݹ�[ջ]�� 8������������ݹ�/�ǵݹ�[ջ]��
 * 9����α������ǵݹ飩
 * 
 * @author de
 * 
 */
public class BinaryTreeOperator<T extends AbstractBinaryTreeNode> {

	private List<T> treeNodeList = new LinkedList<T>();
	private int size;
	private AbstractBinaryTreeNode root;

	/**
	 * ����һ��������
	 * 
	 * @param treeDataList
	 *            �̳�AbstractBinaryTreeNode�Ķ���List
	 */
	public void buildBinaryTree(List<T> treeDataList) {

		if (treeDataList == null || treeDataList.size() <= 0) {
			throw new IllegalArgumentException("�������������ݲ��Ϸ�!");
		}
//		int treeNodeSize = treeDataList.size();
//		// �����нڵ�����Ƶ�treeNodeList��,Ȼ��ֻ��treeNodeList�в���
		treeNodeList.addAll(treeDataList);
		for(T  node :  treeNodeList){
			insert(node);
		}
//
//		// ��ǰlastParentIndex-1�����ڵ㰴�ո��ڵ��뺢�ӽڵ�����ֹ�ϵ����������
//		for (int parentIndex = 0; parentIndex < treeNodeSize / 2 - 1; parentIndex++) {
//			// ����
//			treeNodeList.get(parentIndex).setLeftNode(
//					treeNodeList.get(parentIndex * 2 + 1));
//			// �Һ���
//			treeNodeList.get(parentIndex).setRightNode(
//					treeNodeList.get(parentIndex * 2 + 2));
//
//			size += 2;
//		}
//		// ���һ�����ڵ�:��Ϊ���һ�����ڵ����û���Һ��ӣ����Ե����ó�������
//		int lastParentIndex = treeNodeSize / 2 - 1;
//		// ����
//		treeNodeList.get(lastParentIndex).setLeftNode(
//				treeNodeList.get(lastParentIndex * 2 + 1));
//		size += 1;
//		// �Һ���,�������ĳ���Ϊ�����Ž����Һ���
//		if (treeNodeSize % 2 == 1) {
//			treeNodeList.get(lastParentIndex).setRightNode(
//					treeNodeList.get(lastParentIndex * 2 + 2));
//			size += 1;
//		}
		

	}

	/**
	 * ���������ϲ���һ���ڵ�
	 * 
	 * @param key
	 * @param data
	 */
	public void insert(T newTreeNode) {
		int key = newTreeNode.getKey();

		if (root == null) {
			root = newTreeNode;
			size++;
		} else { // not empty tree
			AbstractBinaryTreeNode current = root;
			AbstractBinaryTreeNode parent;

			while (true) {
				parent = current;
				if (key < current.getKey()) { // �嵽���
					current = current.getLeftNode();
					if (current == null) {
						parent.setLeftNode(newTreeNode);
						size++;
						return;
					}
				} // end if go left
				else { // �嵽�ұ�
					current = current.getRightNode();
					if (current == null) {
						parent.setRightNode(newTreeNode);
						size++;
						return;
					}
				} // end if go right
			} // end while (true)
		}
	} // end insert

	/**
	 * ɾ��ĳ���ڵ�
	 * 
	 * @param key
	 * @return
	 */
	public boolean delete(AbstractBinaryTreeNode delTreeNode) {
		int key = delTreeNode.getKey();
		if (root == null) // empty tree
			return false;
		AbstractBinaryTreeNode current = root;
		AbstractBinaryTreeNode parent = root;
		boolean isLeftChild = false;
		while (current.getKey() != key) {
			parent = current;
			if (key < current.getKey()) {
				isLeftChild = true;
				current = current.getLeftNode();
			} else {
				isLeftChild = false;
				current = current.getRightNode();
			}
			if (current == null) { // not found
				return false;
			}
		}
		// found it
		// ɾ���Ľ����Ҷ�ӽ��
		if (current.getLeftNode() == null && current.getRightNode() == null) {
			if (current == root)
				root = null;
			if (isLeftChild)
				parent.setLeftNode(null);
			else
				parent.setRightNode(null);
		} else if (current.getRightNode() == null) {// ɾ���Ľ��ֻ�����ӽ��
			if (current == root) {
				root = current.getLeftNode();
			}
			if (isLeftChild) {
				parent.setLeftNode(current.getLeftNode());
			} else {
				parent.setRightNode(current.getRightNode());
			}
		} else if (current.getLeftNode() == null) {// ɾ���Ľ��ֻ���Һ���
			if (current == root)
				root = current.getRightNode();
			if (isLeftChild) {
				parent.setLeftNode(current.getRightNode());
			} else {
				parent.setRightNode(current.getRightNode());
			}
		} else {// ɾ���Ľ�������Һ���
			AbstractBinaryTreeNode successor = getSuccessor(current);
			if (current == root) {
				root = successor;
			}
			if (isLeftChild) {
				parent.setLeftNode(successor);
			} else {
				parent.setRightNode(successor);
			}
			successor.setLeftNode(current.getLeftNode());
		}

		return true;
	}

	// ���������в��������ɾ�����λ�õĽ��
	private AbstractBinaryTreeNode getSuccessor(AbstractBinaryTreeNode delNode) {
		AbstractBinaryTreeNode successor = delNode;
		AbstractBinaryTreeNode parentSuccessor = delNode;
		AbstractBinaryTreeNode current = delNode.getRightNode();
		while (current != null) {
			parentSuccessor = successor;
			successor = current;
			current = current.getLeftNode();
		}

		if (successor != delNode.getRightNode()) {
			parentSuccessor.setLeftNode(successor.getRightNode());
			successor.setRightNode(delNode.getRightNode());
		}

		return successor;
	}

	/**
	 * ��key���ҽڵ�
	 * 
	 * @param findTreeNode
	 * @return
	 */
	public AbstractBinaryTreeNode find(int key) {
		AbstractBinaryTreeNode current = root;
		if (root == null) // empty tree
			return null;
		else {
			while (current.getKey() != key) {
				if (key < current.getKey()) { // go left
					current = current.getLeftNode();
				} else {
					current = current.getRightNode(); // go right
				}

				if (current == null) {
					return null;
				}
			}
		}
		if(current!=null){
			System.out.println("find_tree_node.key = "+ current.getKey() + "--nodeKey = "+current.getNodeKey());
		}
		return current;
	}
	
	/**
	 *  �������(�ݹ�)
	 */
		public void inOrderTraverse() {
			inOrderTraverse(root);
		}

		public void inOrderTraverse(AbstractBinaryTreeNode node) {
			if (node != null) {
				inOrderTraverse(node.getLeftNode());
				System.out.println(node.getKey()+"---"+node.getNodeKey());
				inOrderTraverse(node.getRightNode());
			}
		}

		/**
		 *  �������(�ǵݹ�)
		 */
		public void nrInOrderTraverse() {

			Stack<AbstractBinaryTreeNode> stack = new Stack<AbstractBinaryTreeNode>();
			AbstractBinaryTreeNode node = root;
			while (node != null || !stack.isEmpty()) {
				while (node != null) {
					stack.push(node);
					node = node.getLeftNode();
				}
				node = stack.pop();
				System.out.println(node.getKey()+"---"+node.getNodeKey());
				node = node.getRightNode();

			}

		}

		/**
		 *  �������(�ݹ�)
		 */
		public void preOrderTraverse() {
			preOrderTraverse(root);
		}

		public void preOrderTraverse(AbstractBinaryTreeNode node) {
			if (node != null) {
				System.out.println(node.getKey()+"---"+node.getNodeKey());
				preOrderTraverse(node.getLeftNode());
				preOrderTraverse(node.getRightNode());
			}
		}

		/**
		 * ����������ǵݹ飩
		 */
		public void nrPreOrderTraverse() {

			Stack<AbstractBinaryTreeNode> stack = new Stack<AbstractBinaryTreeNode>();
			AbstractBinaryTreeNode node = root;

			while (node != null || !stack.isEmpty()) {

				while (node != null) {
					System.out.println(node.getKey()+"---"+node.getNodeKey());
					stack.push(node);
					node = node.getLeftNode();
				}
				node = stack.pop();
				node = node.getRightNode();
			}

		}

		/**
		 * �������(�ݹ�)
		 */
		public void postOrderTraverse() {
			postOrderTraverse(root);
		}

		public void postOrderTraverse(AbstractBinaryTreeNode node) {
			if (node != null) {
				postOrderTraverse(node.getLeftNode());
				postOrderTraverse(node.getRightNode());
				System.out.println(node.getKey()+"---"+node.getNodeKey());
			}
		}

		/**
		 * ��������(�ǵݹ�)
		 */
		public void nrPostOrderTraverse() {

			Stack<AbstractBinaryTreeNode> stack = new Stack<AbstractBinaryTreeNode>();
			AbstractBinaryTreeNode node = root;
			AbstractBinaryTreeNode preNode = null;// ��ʾ���һ�η��ʵĽڵ�

			while (node != null || !stack.isEmpty()) {

				while (node != null) {
					stack.push(node);
					node = node.getLeftNode();
				}

				node = stack.peek();

				if (node.getRightNode() == null || node.getRightNode() == preNode) {
					System.out.println(node.getKey()+"---"+node.getNodeKey());
					node = stack.pop();
					preNode = node;
					node = null;
				} else {
					node = node.getRightNode();
				}

			}

		}

		/**
		 *  ����α���
		 */
		public void levelTraverse() {
			levelTraverse(root);
		}

		public void levelTraverse(AbstractBinaryTreeNode node) {

			Queue<AbstractBinaryTreeNode> queue = new LinkedBlockingQueue<AbstractBinaryTreeNode>();
			queue.add(node);
			while (!queue.isEmpty()) {

				AbstractBinaryTreeNode temp = queue.poll();
				if (temp != null) {
					System.out.println(temp.getKey()+"---"+temp.getNodeKey());
					if(temp.getLeftNode()!=null){
						queue.add(temp.getLeftNode());
					}
					if(temp.getRightNode() != null){
						queue.add(temp.getRightNode());
					}
				}

			}

		}
		
		
		
		public static void main(String[] args) {
			BinaryTreeNodeData  nodeData1 = new BinaryTreeNodeData(18, "A1");
			BinaryTreeNodeData  nodeData2 = new BinaryTreeNodeData(16, "B2");
			BinaryTreeNodeData  nodeData3 = new BinaryTreeNodeData(19, "C3");
			BinaryTreeNodeData  nodeData4 = new BinaryTreeNodeData(20, "D4");
			BinaryTreeNodeData  nodeData5 = new BinaryTreeNodeData(21, "E5");
			BinaryTreeNodeData  nodeData6 = new BinaryTreeNodeData(22, "F6");
			BinaryTreeNodeData  nodeData7 = new BinaryTreeNodeData(23, "G7");
			BinaryTreeNodeData  nodeData8 = new BinaryTreeNodeData(24, "H8");
			BinaryTreeNodeData  nodeData9 = new BinaryTreeNodeData(25, "I9");
			BinaryTreeNodeData  nodeData10 = new BinaryTreeNodeData(10, "J10");
			BinaryTreeNodeData  nodeData11 = new BinaryTreeNodeData(11, "K11");
			BinaryTreeNodeData  nodeData12 = new BinaryTreeNodeData(12, "L12");
			BinaryTreeNodeData  nodeData13 = new BinaryTreeNodeData(13, "M13");
			BinaryTreeNodeData  nodeData14 = new BinaryTreeNodeData(14, "N14");
			List<BinaryTreeNodeData>  list = new LinkedList<BinaryTreeNodeData>();
			list.add(nodeData1);
			list.add(nodeData2);
			list.add(nodeData3);
			list.add(nodeData9);
			list.add(nodeData10);
			list.add(nodeData11);
			list.add(nodeData4);
			list.add(nodeData5);
			list.add(nodeData6);
			list.add(nodeData7);
			list.add(nodeData8);
			list.add(nodeData12);
			list.add(nodeData13);
			list.add(nodeData14);
			BinaryTreeOperator<BinaryTreeNodeData>  treeopt = new BinaryTreeOperator<BinaryTreeNodeData>();
			treeopt.buildBinaryTree(list);
			System.out.println("����--�ݹ�--------------");
			treeopt.inOrderTraverse();
			System.out.println("ǰ��--�ݹ�--------------");
			treeopt.preOrderTraverse();
			System.out.println("����--�ݹ�------------");
			treeopt.postOrderTraverse();
			System.out.println("findkey----------");
			treeopt.find(10);
			System.out.println("delete.........nodedata10");
			treeopt.delete(nodeData13);
			System.out.println("ǰ��--�ǵݹ�--------------");
			treeopt.nrPreOrderTraverse();
			System.out.println("����--�ǵݹ�------------");
			treeopt.nrPostOrderTraverse();
			System.out.println("����--�ǵݹ�------------");
			treeopt.nrInOrderTraverse();
			System.out.println("���--�ǵݹ�------------");
			treeopt.levelTraverse();
			
			
		}
}
