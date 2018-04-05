package org.andrewliu.socket.codedecode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * 基本文件的魔术字符串形式编码/解码
 * 
 * @author de
 * 
 */
public class VoteMsgTextCoder implements IVoteMsgCoder {
	/**
	 * Wire Format "Voteproto" <"v"|"i">[<RESPFLAG>]<CANDIDATE> [<VOTECNT>]
	 * Charset is fixed by the wire format
	 */

	// 固定字符串（魔法字符串
	public static final String MAGIC = "Voting";
	public static final String VOTESTR = "v";
	public static final String INQSTR = "i";
	public static final String RESPONSESTR = "R";

	public static final String CHARSETNAME = "US-ASCII";
	public static final String DELIMSTR = " ";
	public static final int MAX_WIRE_LENGTH = 2000;

	@Override
	public byte[] toWire(VoteMsg msg) throws IOException {

		String msgString = MAGIC + DELIMSTR
				+ (msg.isInquiry() ? INQSTR : VOTESTR) + DELIMSTR
				+ (msg.isResponse() ? RESPONSESTR + DELIMSTR : "")
				+ Integer.toString(msg.getCandidateID()) + DELIMSTR
				+ Long.toString(msg.getVoteCount());
		System.out.println(" send content ="+ msgString);
		byte data[] = msgString.getBytes(CHARSETNAME);
		return data;
	}

	@Override
	public VoteMsg fromWire(byte[] message) throws IOException {
		ByteArrayInputStream msgStream = new ByteArrayInputStream(message);
		//通过Scanner扫描分隔符
		Scanner s = new Scanner(new InputStreamReader(msgStream,CHARSETNAME));
		boolean  isInquiry ;
		boolean isResponse;
		int candidateID;
		long voteCount;
		String token;
		try{
			token = s.next();
			if(!token.equals(MAGIC)){
				throw new IOException("Bad magic String: "+token);
			}
			token = s.next();
			if(token.equals(VOTESTR)){
				isInquiry = false;
			}else if(!token.equals(INQSTR)){
				throw new IOException("Bad vote/inq indicator:"+token);
			}else{
				isInquiry = true;
			}
			
			
			token = s.next();
			if(token.equals(RESPONSESTR)){
				isResponse = true;
				token = s.next();
			}else{
				isResponse = false;
			}
			
			candidateID = Integer.parseInt(token);
			if(isResponse){
				token = s.next();
				voteCount = Long.parseLong(token);
			}else{
				voteCount = 0;
			}
		}catch(IOException ioe){
			throw new IOException("Parse error....");
		}
		
		return new VoteMsg(isResponse,isInquiry,candidateID,voteCount);
	}

}
