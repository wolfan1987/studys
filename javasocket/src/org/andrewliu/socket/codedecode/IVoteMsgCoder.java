package org.andrewliu.socket.codedecode;

import java.io.IOException;

/**
 * 对投票信息进行编码和解码：
 * @author de
 *
 */
public interface IVoteMsgCoder {

	/**
	 * 根据特定的协议，将投票信息转换成一个字节序列（变成字符形式)
	 * @param msg
	 * @return
	 * @throws IOException
	 */
	byte[] toWire( VoteMsg msg) throws IOException;
	/**
	 * 根据相同的协议，对给定的字节序列进行解析，并根据信息的内容构造一个投票信息对象
	 * @param input
	 * @return
	 * @throws IOException
	 */
	VoteMsg fromWire(byte[] input) throws IOException;
}
