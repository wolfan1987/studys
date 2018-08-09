package org.andreliu.ds.binarytrue4my;

/**
 * �������ڵ����
 * @author de
 *
 * @param <T>  ʵ�ʴ洢�����ݶ���
 */
public  abstract class AbstractBinaryTreeNode {

	/**
	 * ������nodeKey��T����ĳ�ؼ������ַ�����ʽ,���������ж����ݽ��в��ұȽ�
	 */
	private  String  nodeKey ;
	/**
	 * ������ʽ��key,ֻҪ���ڱ�ʶΨһ
	 */
	private  int    key;
	
	/**
	 * ��ڵ㼯
	 */
	private  AbstractBinaryTreeNode leftNode;
	/**
	 * �ҽڵ㼯
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
