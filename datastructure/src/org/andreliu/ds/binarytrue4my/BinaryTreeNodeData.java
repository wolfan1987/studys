package org.andreliu.ds.binarytrue4my;


/**
 * 二叉树节点中携带的数据
 * @author de
 *
 */
public class BinaryTreeNodeData  extends AbstractBinaryTreeNode{

	private  int    userId;
	private  String  userName;
	
	/**
	 * 构造Data对象
	 * @param userId
	 * @param userName
	 */
	public BinaryTreeNodeData(int userId, String userName) {
		super(userId,userName);
		if(userId < 0 || null == userName || userName.length()==0){
			throw new IllegalArgumentException("构造对象时，数据不合法!");
		}
		//将userID传给dataKey
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
