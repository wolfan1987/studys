package com.zjht.hchpserver.asynserver;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class AsynSocketServerHandler implements CompletionHandler<Integer, ByteBuffer> {  
	private static final Logger logger = Logger.getLogger(AsynSocketServerHandler.class.getName());
    private static Charset utf8 = Charset.forName("utf-8");  
    private AsynchronousSocketChannel channel;     
    private Lock  lock = new ReentrantLock();
    private  int count;
    private  int  preLength = -1;
    private  int  lostlength = -1;
    private ByteBuffer  preByteBuffer = null;
    private ByteBuffer returnBuffer = ByteBuffer.allocate(128);
  
    public AsynSocketServerHandler(AsynchronousSocketChannel channel) {  
        this.channel = channel;  
    }  
  
    @Override  
    public void completed(Integer result, ByteBuffer buff) {  
        if (result > 0) {  
        	lock.lock();
        	count++;
			System.out.println("server process count="+count);
        	buff.flip();  //将position设为起始位置
        	int  p = buff.position();
        	int  limit = buff.limit();
        	int dataLen = buff.getInt(0);  //真正数据字节大小第一个字节的值
        	if(result != dataLen){
//        		channel.read(buff, buff, this);
//        		return;   
        	}
        	byte[] buffArray = buff.array();//将buffer转为数组
        	byte[] resultArray = new byte[limit]; //存放实际业务数据的数组
        	      
        	if((limit-4)!=dataLen){      
        		if(preLength<0){
        			preLength = limit;
        			//保存上次的数据
        			preByteBuffer = ByteBuffer.wrap(buff.array());
        			lostlength = dataLen - limit;
        		}else if(preLength>0){
        			System.out.println("----要将上次的数据拼溱------");
        		}     
        		//System.out.println("error=="+utf8.decode(buff).toString());
        		System.out.println("result="+result+"buffArray.length="+buffArray.length);
        		System.out.println("所传数据不符合大小限制！dataLen="+dataLen+"----limit="+limit+"position="+ p);
        		 returnBuffer.putInt(0000);
                 returnBuffer.flip();   
                 Future<Integer> w = channel.write(returnBuffer);  
                 try {          
                     w.get();      
                 } catch (InterruptedException e) {  
                     e.printStackTrace();  
                 } catch (ExecutionException e) {  
                     e.printStackTrace();  
                 }        
                 buff.compact();
        	}else{    
        		System.out.println("limit="+limit+"----"+"dataLen="+dataLen);
        		
        		System.arraycopy(buffArray, 4, resultArray, 0, dataLen);//读出实际业务数据
                String msg = utf8.decode(ByteBuffer.wrap(resultArray)).toString();
                buff.compact();//将容量恢复（将limit由原来的内容大小变为实际分配时的大小
                buffArray=null;
                resultArray = null;
                System.out.println("buffer的result = "+msg);  
                //往客户端回写数据，直到写完再进入下一次写
                returnBuffer.putInt(1024);
                returnBuffer.flip();
                Future<Integer> w = channel.write(returnBuffer);  
                try {          
                    w.get();    
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                } catch (ExecutionException e) {  
                    e.printStackTrace();  
                }        
        		
        	}          
        	
            lock.unlock();
            //清空原来的数据，为下一次读准备
            buff.clear();  
            returnBuffer.clear();
            channel.read(buff, buff, this);  
        }else if(result == -1) {  
          try {  
              channel.close();  
          } catch (IOException e) {  
              e.printStackTrace();    
          }  
      	 logger.info("通道"+channel+"无数据可读!");  
      } 
    }  
  
    @Override  
    public void failed(Throwable exc, ByteBuffer buff) {  
    }  
    
//    private  ByteBuffer pLink = ByteBuffer.allocate(2048);
//    private int packetLen = 100;
//    private byte[]  _packet = new byte[1024];
//    public List<byte[]> getPacket(ByteBuffer buffer) throws Exception{ 
//    	pLink.clear();  
//    	try{  
//    		while(buffer.remaining() > 0){   
//    			if(packetLen == 0){  //此时存在两种情况及在数据包包长没有获得的情况下可能已经获得过一次数据包   
//    	if(buffer.remaining() + _packet.length < 3){  
//    		byte[] temp = new byte[buffer.remaining()];  
//    		buffer.get(temp);  
//    		_packet = PacketUtil.joinBytes(_packet , temp);  
//    		break;  //保存包头 
//    	}else{if(_packet.length == 0){  
//    		buffer.get();  
//    		packetLen = PacketUtil.parserBuffer2ToInt(buffer);   
//    	}else if(_packet.length == 1){   
//    			packetLen = PacketUtil.parserBuffer2ToInt(buffer);  
//    	} else if(_packet.length == 2){   
//    		byte[] lenByte = new byte[2];  
//    		lenByte[0] = _packet[1];  
//    		lenByte[1] = buffer.get();   
//    		packetLen = PacketUtil.parserBytes2ToInt(lenByte);  
//    		} else{   
//    			packetLen = PacketUtil.parserBytes2ToInt(_packet , 1);  
//    			}     
//    	}  
//    	}   
//    		if(_packet.length <= 3){   //此时_packet 没有有用数据，所需数据都在缓冲区中   
//    		if(buffer.remaining() < packetLen){  
//    			_packet = new byte[buffer.remaining()];    
//    			buffer.get(_packet);  
//    			}else{   
//    				byte[] p = new byte[packetLen];  
//    				buffer.get(p);  
//    				pLink.add(p); 
//    				packetLen = 0;  
//    				_packet = new byte[0];  
//    				} 
//    		}else {  
//    			if(buffer.remaining() + _packet.length - 3 < packetLen){   //剩余数据包不足一个完整包，保存后等待写一个   
//    			byte[] temp = new byte[buffer.remaining()];   
//    			buffer.get(temp);  
//    			_packet = PacketUtil.joinBytes(_packet , temp);
//    			break;  
//    			}else{            //数据包完整或者多出  
//    				byte[] temp = new byte[packetLen - ( _packet.length - 3) ];   
//    				buffer.get(temp);   
//    				pLink.add(PacketUtil.subPacket(PacketUtil.joinBytes(_packet , temp)));  
//    				_packet = new byte[0]; 
//    				packetLen = 0;  
//    				}  
//    			}   }  
//    		}catch(Exception e){  
//    			System.out.println("..GETPACKET packetLen = " + packetLen + " _packet.length = " + _packet.length);  
//    			throw e; 
//    		}  
//    	return pLink;  
//    	}
//    			}
//    		}
//    	}
//    }
    
}  
