package org.andrewliu.socket.codedecode;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 成帧技术基本接口
 * @author de
 *
 */
public interface Framer {

	/**
	 * 添加成帧信息并将指定消息输出到指定流。
	 * @param message
	 * @param out
	 * @throws IOException
	 */
	void frameMsg(byte[] message,OutputStream out) throws IOException;
	/**
	 * 扫描指定的流，从中抽取出下一条消息。
	 * @return
	 * @throws IOException
	 */
	byte[] nextMsg() throws IOException;
	
}
