package org.andrewliu.socket.codedecode;

import java.io.IOException;

/**
 * ��ͶƱ��Ϣ���б���ͽ��룺
 * @author de
 *
 */
public interface IVoteMsgCoder {

	/**
	 * �����ض���Э�飬��ͶƱ��Ϣת����һ���ֽ����У�����ַ���ʽ)
	 * @param msg
	 * @return
	 * @throws IOException
	 */
	byte[] toWire( VoteMsg msg) throws IOException;
	/**
	 * ������ͬ��Э�飬�Ը������ֽ����н��н�������������Ϣ�����ݹ���һ��ͶƱ��Ϣ����
	 * @param input
	 * @return
	 * @throws IOException
	 */
	VoteMsg fromWire(byte[] input) throws IOException;
}
