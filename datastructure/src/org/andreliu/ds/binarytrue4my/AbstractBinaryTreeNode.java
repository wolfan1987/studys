package org.andreliu.ds.binarytrue4my;

/**
 * 二叉树节点基类
 * @author de
 *
 * @param <T>  实际存储的数据对象
 */
public  abstract class AbstractBinaryTreeNode {

	/**
	 * 在这里nodeKey是T对象某关键属性字符串形式,方便在树中对数据进行查找比较
	 */
	private  String  nodeKey ;
	/**
	 * 数字形式的key,只要用于标识唯一
	 */
	private  int    key;
	
	/**
	 * 左节点集
	 */
	private  AbstractBinaryTreeNode leftNode;
	/**
	 * 右节点集
	 */
	private  AbstractBinaryTreeNode  rightNode;
	
	public AbstractBinaryTreeNode(int key,String nodeKey) {
		super();
		this.key = key;
		this.nodeKey = nodeKey;
	}

	public String getNodeKey() {
		return nodeKey;
	}

	public void setNodeKey(String nodeKey) {
		this.nodeKey = nodeKey;
	}

	public AbstractBinaryTreeNode getLeftNode() {
		return leftNode;
	}
	public void setLeftNode(AbstractBinaryTreeNode leftNode) {
		this.leftNode = leftNode;
	}
	public AbstractBinaryTreeNode getRightNode() {
		return rightNode;
	}
	public void setRightNode(AbstractBinaryTreeNode rightNode) {
		this.rightNode = rightNode;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}
	
	
	
}
