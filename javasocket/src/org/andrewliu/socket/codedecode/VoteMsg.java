package org.andrewliu.socket.codedecode;

/**
 * 投票信息实体
 * 根据此实体来实现协议
 * @author de
 *
 */
public class VoteMsg {

	/**
	 * true = 查询
	 * flase = 投票
	 */
	private boolean isInquiry;
	/**
	 * true = 是服务器响应
	 * false = 请求
	 */
	private boolean isResponse;
	/**
	 * 候选人ID  (0-1000)
	 */
	private int candidateID;
	/**
	 * 候选人总选票数  (非零值，response时)  不能为负数）
	 */
	private long voteCount;
	
	public static final int MAX_CANDIDATE_ID = 1000;

	public VoteMsg(boolean isInquiry, boolean isResponse, int candidateID,
			long voteCount) {
		if(voteCount !=0 && !isResponse){
			throw new IllegalArgumentException("Request vote count must be zero");
		}
		System.out.println("candidateId = "+candidateID);
		if(candidateID < 0 || candidateID > MAX_CANDIDATE_ID){
			throw new IllegalArgumentException("Bad Candidate ID: "	+ candidateID);
		}
		
		if(voteCount<0){
			throw new IllegalArgumentException("Total must be >= zero");
		}
		this.isInquiry = isInquiry;
		this.isResponse = isResponse;
		this.candidateID = candidateID;
		this.voteCount = voteCount;
	}

	public boolean isInquiry() {
		return isInquiry;
	}

	public void setInquiry(boolean isInquiry) {
		this.isInquiry = isInquiry;
	}

	public boolean isResponse() {
		return isResponse;
	}

	public void setResponse(boolean isResponse) {
		this.isResponse = isResponse;
	}

	public int getCandidateID() {
		return candidateID;
	}

	public void setCandidateID(int candidateID) {
		if(candidateID < 0 || candidateID > MAX_CANDIDATE_ID){
			throw new IllegalArgumentException("Bad Candidate ID: "	+ candidateID);
		}
		this.candidateID = candidateID;
	}

	public long getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(long count) {
		if(voteCount !=0 && !isResponse || count < 0){
			throw new IllegalArgumentException("Request vote count must be zero");
		}
		this.voteCount = count;
	}
	
	
	@Override
	public  String toString(){
		String res = (isInquiry ? "inquiry" : "vote")+ "for candidate "+ candidateID;
		if(isResponse){
			res = "response to "+ res+ " who now has "+ voteCount + "vote(s)";
		}
		return res;
	}
	
	
}
