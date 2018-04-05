package org.andrewliu.socket.codedecode;

import java.io.IOException;
import java.io.OutputStream;

/**
 * ��֡���������ӿ�
 * @author de
 *
 */
public interface Framer {

	/**
	 * ��ӳ�֡��Ϣ����ָ����Ϣ�����ָ������
	 * @param message
	 * @param out
	 * @throws IOException
	 */
	void frameMsg(byte[] message,OutputStream out) throws IOException;
	/**
	 * ɨ��ָ�����������г�ȡ����һ����Ϣ��
	 * @return
	 * @throws IOException
	 */
	byte[] nextMsg() throws IOException;
	
}
