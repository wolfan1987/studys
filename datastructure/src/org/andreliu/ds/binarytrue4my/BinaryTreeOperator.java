package org.andreliu.ds.binarytrue4my;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 对二叉树进行操作 1、构造一个二叉树 2、往二叉树插入数据对象 3、删除二叉树数据对象 4、按nodeKey从二叉树上查找某数据对象
 * 5、打印出查找到的某节点对象下所有节点 6、前序遍历（递归/非递归[栈]） 7、中序遍历 （递归/非递归[栈]） 8、后序遍历（递归/非递归[栈]）
 * 9、层次遍历（非递归）
 * 
 * @author de
 * 
 */
public class BinaryTreeOperator<T extends AbstractBinaryTreeNode> {

	private List<T> treeNodeList = new LinkedList<T>();
	private int size;
	private AbstractBinaryTreeNode root;

	/**
	 * 构建一个二叉树
	 * 
	 * @param treeDataList
	 *            继承AbstractBinaryTreeNode的对象List
	 */
	public void buildBinaryTree(List<T> treeDataList) {

		if (treeDataList == null || treeDataList.size() <= 0) {
			throw new IllegalArgumentException("构建二叉树数据不合法!");
		}
//		int treeNodeSize = treeDataList.size();
//		// 将所有节点对象复制到treeNodeList中,然后只在treeNodeList中操作
		treeNodeList.addAll(treeDataList);
		for(T  node :  treeNodeList){
			insert(node);
		}
//
//		// 对前lastParentIndex-1个父节点按照父节点与孩子节点的数字关系建立二叉树
//		for (int parentIndex = 0; parentIndex < treeNodeSize / 2 - 1; parentIndex++) {
//			// 左孩子
//			treeNodeList.get(parentIndex).setLeftNode(
//					treeNodeList.get(parentIndex * 2 + 1));
//			// 右孩子
//			treeNodeList.get(parentIndex).setRightNode(
//					treeNodeList.get(parentIndex * 2 + 2));
//
//			size += 2;
//		}
//		// 最后一个父节点:因为最后一个父节点可能没有右孩子，所以单独拿出来处理
//		int lastParentIndex = treeNodeSize / 2 - 1;
//		// 左孩子
//		treeNodeList.get(lastParentIndex).setLeftNode(
//				treeNodeList.get(lastParentIndex * 2 + 1));
//		size += 1;
//		// 右孩子,如果数组的长度为奇数才建立右孩子
//		if (treeNodeSize % 2 == 1) {
//			treeNodeList.get(lastParentIndex).setRightNode(
//					treeNodeList.get(lastParentIndex * 2 + 2));
//			size += 1;
//		}
		

	}

	/**
	 * 往二叉树上插入一个节点
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
				if (key < current.getKey()) { // 插到左边
					current = current.getLeftNode();
					if (current == null) {
						parent.setLeftNode(newTreeNode);
						size++;
						return;
					}
				} // end if go left
				else { // 插到右边
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
	 * 删除某个节点
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
		// 删除的结点是叶子结点
		if (current.getLeftNode() == null && current.getRightNode() == null) {
			if (current == root)
				root = null;
			if (isLeftChild)
				parent.setLeftNode(null);
			else
				parent.setRightNode(null);
		} else if (current.getRightNode() == null) {// 删除的结点只有左孩子结点
			if (current == root) {
				root = current.getLeftNode();
			}
			if (isLeftChild) {
				parent.setLeftNode(current.getLeftNode());
			} else {
				parent.setRightNode(current.getRightNode());
			}
		} else if (current.getLeftNode() == null) {// 删除的结点只有右孩子
			if (current == root)
				root = current.getRightNode();
			if (isLeftChild) {
				parent.setLeftNode(current.getRightNode());
			} else {
				parent.setRightNode(current.getRightNode());
			}
		} else {// 删除的结点有左右孩子
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

	// 从右子树中查找替代被删除结点位置的结点
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
	 * 按key查找节点
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
	 *  中序遍历(递归)
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
		 *  中序遍历(非递归)
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
		 *  先序遍历(递归)
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
		 * 先序遍历（非递归）
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
		 * 后序遍历(递归)
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
		 * 后续遍历(非递归)
		 */
		public void nrPostOrderTraverse() {

			Stack<AbstractBinaryTreeNode> stack = new Stack<AbstractBinaryTreeNode>();
			AbstractBinaryTreeNode node = root;
			AbstractBinaryTreeNode preNode = null;// 表示最近一次访问的节点

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
		 *  按层次遍历
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
			System.out.println("中序--递归--------------");
			treeopt.inOrderTraverse();
			System.out.println("前序--递归--------------");
			treeopt.preOrderTraverse();
			System.out.println("后序--递归------------");
			treeopt.postOrderTraverse();
			System.out.println("findkey----------");
			treeopt.find(10);
			System.out.println("delete.........nodedata10");
			treeopt.delete(nodeData13);
			System.out.println("前序--非递归--------------");
			treeopt.nrPreOrderTraverse();
			System.out.println("后序--非递归------------");
			treeopt.nrPostOrderTraverse();
			System.out.println("中序--非递归------------");
			treeopt.nrInOrderTraverse();
			System.out.println("层次--非递归------------");
			treeopt.levelTraverse();
			
			
		}
}
