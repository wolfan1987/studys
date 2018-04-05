package org.andrewliu.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;


/**
 * Selector:
 * Selector������ڱ���ʹ�÷�����ʽ�ͻ����к��˷���Դ�ġ�æ�ȡ�������NIO��ѡ������Ҫ�����ڶ��ŵ���ѡ��һ���ͻ�����Ҫ�Ľ���I/O������
 * һ��selectorʵ������ͬʱ���һ���ŵ���I/O״̬��ѡ��������һ����·����ѡ��������Ϊһ��ѡ�����ܹ��������ŵ��ϵ�I/O������
 * 
 * ����һ��Selectorʵ��Ҳ��ʹ�þ�̬��������open()��������ע�ᵽ��Ҫ��ص��ŵ��ϣ�ͨ��channel�ķ���ʵ�֣�������ʹ��selector�ķ�����
 * ��󣬵���ѡ������select()�������÷����������ȴ���ֱ����һ���������ŵ�׼������I/O������ȴ���ʱ��select()���������ؿɽ���I/O�������ŵ�������
 * ���ڣ���һ���������߳��У�ͨ������select()�������ܼ�����ŵ��Ƿ�׼���ý���I/O�������������һ��ʱ�����Ȼû���ŵ�׼���ã�select()�����ͷ���0,
 * ������������ִ����������
 * @author de
 *
 */
public class TCPServerSelector {

	private static final int BUFSIZE = 256;
	private static final int TIMEOUT = 3000;
	public static void main(String[] args) throws IOException {
		int[]  ports = {5201,5202,5203,5204,5205,5206,5207};
		Selector selector = Selector.open();//����һ��ͨ��ѡ����
		for(int  port : ports){  //ÿ���˿ڴ���һ��ͨ��
			ServerSocketChannel listnChannel = ServerSocketChannel.open();
			listnChannel.socket().bind(new InetSocketAddress(port));  //���ڴ˶˿ڰ�
 			listnChannel.configureBlocking(false);  //����ͨ��Ϊ������ʽ���������ʱ�Ͳ�ռ����Դ��
			listnChannel.register(selector, SelectionKey.OP_ACCEPT); //��ͨ��ע��һ��ѡ��������ʱѡ����ָʾͨ�����Խ��н������Ӳ���
		}
		
		TCPProtocol protocol = new EchoSelectorProtocol(BUFSIZE);
		
		while(true){
			//���û�еõ�׼���õ�I/O�������ŵ��ͼ�����
			if(selector.select(TIMEOUT) == 0){ 
				System.out.println("....");
				continue;
			}
			//���ȴ�����������һ��I/O�������ŵ�ʱ
			Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();  //�ü����а�����ÿ��׼����ĳһI/O�������ŵ���SelectionKey
			while(keyIter.hasNext()){
				SelectionKey key = keyIter.next();
				if(key.isAcceptable()){  //���Ƿ����Ӿ���
					protocol.handleAccept(key);
				}
				if(key.isReadable()){  //���Ƿ��ȡ����
					protocol.handleRead(key);
				}
				if(key.isValid() && key.isWritable()){  //����Ƿ�ɶ�����
					protocol.handleWrite(key);
				}
				keyIter.remove();  //�Ƴ�������ļ�
			}
		}
	}
}
