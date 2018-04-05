package org.andrewliu.socket.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;



public class EchoSelectorProtocol implements TCPProtocol {

	
	private int bufSize;

	
	public EchoSelectorProtocol(int bufSize) {
		super();
		this.bufSize = bufSize;
	}

	@Override
	public void handleAccept(SelectionKey key) throws IOException {
		//ͨ��key���Եõ��ŵ�,Ȼ���������
		SocketChannel clntChan = ((ServerSocketChannel)key.channel()).accept();
		//������ϢΪ������ʽ
		clntChan.configureBlocking(false);
		//ע��ͨ�����Խ��еĲ���
		clntChan.register(key.selector(), SelectionKey.OP_READ,ByteBuffer.allocate(bufSize));
	}

	@Override
	public void handleRead(SelectionKey key) throws IOException {
		//�õ���Ϣ
		SocketChannel clntChan = (SocketChannel)key.channel();
		//�õ���������
		ByteBuffer buf = (ByteBuffer)key.attachment();  //�����ŵ��ĸ���
		//��ȡ����
		long bytesRead = clntChan.read(buf);
		if(bytesRead == -1){
			//�ر�ʱ�����Ƴ����ŵ������ļ�
			clntChan.close();
		}else if(bytesRead >0){
			//�����������ݣ�����Ϣ���Ϊ��д����������ɶ�,��Щkeyֻ�����´ε���select()����ʱ�Ż�������
			key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		}
	}

	@Override
	public void handleWrite(SelectionKey key) throws IOException {
		//�õ�Ҫд������
		ByteBuffer buf = (ByteBuffer)key.attachment();
		//׼��д����
		buf.flip();
		//�õ�ͨ��
		SocketChannel clntChan = (SocketChannel)key.channel();
		//��ͨ��д����
		clntChan.write(buf);
		//�����������
		if(!buf.hasRemaining()){
			//����������ֻ�����Ĺ���
			key.interestOps(SelectionKey.OP_READ);
		}
		//ѹ��buf�е�����
		buf.compact();
	}

}
