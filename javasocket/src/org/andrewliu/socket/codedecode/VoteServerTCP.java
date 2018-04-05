package org.andrewliu.socket.codedecode;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ͶƱ�������ˣ�
 * @author de
 *
 */
public class VoteServerTCP {

	public static void main(String[] args) throws IOException {
		int port = 5202;
		ServerSocket servSock = new ServerSocket(port);
		//���ڽ���
		IVoteMsgCoder coder = new VoteMsgBinCoder();
		//����ͶƱ
		VoteService service = new VoteService();
		
		//��ѭ���в��ϼ����ͻ���ͶƱ����
		while(true){
			Socket clntSock = servSock.accept();
			System.out.println(" Handling client at "+ clntSock.getRemoteSocketAddress());
			//֡��Ϣ������
			Framer framer = new LengthFramer(clntSock.getInputStream());
			try{
				byte[] req ; 
				//ѭ�����ܴ���ӿͻ��˴�����VoteMsg
				while((req = framer.nextMsg()) !=null){
					System.out.println("Received message("+req.length+" bytes )");
					//���õ��ֽ���Ϣת��Ϊ���󣬲��ŵ�service��ȥ����Ʊ�����ҷ��ص�ǰ�˵�ͶƱ��Ϣ���Է��ظ��ͻ���
					VoteMsg responseMsg = service.handleRequest(coder.fromWire(req));
					//��ͶƱ��Ϣת�����룬����װ����ʽ����֡��Ȼ�󷵻ظ��ͻ���
					framer.frameMsg(coder.toWire(responseMsg), clntSock.getOutputStream());
				}
			}catch(IOException ioe){
				System.err.println(" Error handling client: "+ ioe.getMessage());
				ioe.printStackTrace();
			}finally{
				System.out.println("Closing connection!");
				clntSock.close();
			}
		}
		
	}
}
