package org.andreliu.ds.binarytrue4my;


/**
 * �������ڵ���Я��������
 * @author de
 *
 */
public class BinaryTreeNodeData  extends AbstractBinaryTreeNode{

	private  int    userId;
	private  String  userName;
	
	/**
	 * ����Data����
	 * @param userId
	 * @param userName
	 */
	public BinaryTreeNodeData(int userId, String userName) {
		super(userId,userName);
		if(userId < 0 || null == userName || userName.length()==0){
			throw new IllegalArgumentException("�������ʱ�����ݲ��Ϸ�!");
		}
		//��userID����dataKey
		this.userId = userId;
		this.userName = userName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
