package com.zjht.asyniobiframework.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.pool2.impl.GenericObjectPool;

import com.zjht.asyniobiframework.auth.VerificationData;
import com.zjht.asyniobiframework.config.AsynIoClientConfig;
import com.zjht.asyniobiframework.exception.BIHandlerException;
import com.zjht.asyniobiframework.exception.HandlerRequestException;
import com.zjht.asyniobiframework.exception.MessageTypeRegisterException;
import com.zjht.asyniobiframework.handler.IBIJsonHandler;
import com.zjht.asyniobiframework.handler.IBIMessageHandler;
import com.zjht.asyniobiframework.message.AbstractRequestMessage;
import com.zjht.asyniobiframework.message.AbstractResponseMessage;
import com.zjht.asyniobiframework.message.BaseJsonString;
import com.zjht.asyniobiframework.message.beans.TestMessageResponse;
import com.zjht.asyniobiframework.pools.AbstractAsynIOClientThread;
import com.zjht.asyniobiframework.pools.AsynIOClientJsonThread;
import com.zjht.asyniobiframework.pools.AsynIOClientJsonThreadFactory;
import com.zjht.asyniobiframework.pools.AsynIOClientMessageThread;
import com.zjht.asyniobiframework.pools.AsynIOClientMessageThreadFactory;
import com.zjht.asyniobiframework.pools.AsynIOGroupProvider;

public class AsynBIProcessContext extends Thread implements
		IMessageOperator<AbstractRequestMessage> {
	private GenericObjectPool<AsynIOClientJsonThread> jsonIoThreadPool = null;
	private GenericObjectPool<AsynIOClientMessageThread> messageIoThreadPool = null;
	private DataTypeEnum dataType;
	private AsynIoClientConfig  asynIoConfig = null;

	private AsynBIProcessContext() {
	}

	public static AsynBIProcessContext newAsynBIContext() {
		return new AsynBIProcessContext();
	}
	/**
	 * 待消费的消息队列
	 */
	private Map<Class<?>, ConcurrentLinkedQueue<AbstractRequestMessage>> requestMessageQueue = new ConcurrentHashMap<Class<?>, ConcurrentLinkedQueue<AbstractRequestMessage>>();
	private Map<Class<?>, ConcurrentLinkedQueue<IBIMessageHandler<?>>> contextMessageHandlerQueue = new ConcurrentHashMap<Class<?>, ConcurrentLinkedQueue<IBIMessageHandler<?>>>();
	/**
	 * 待消费的json数据队列
	 */
	private Map<Class<?>, ConcurrentLinkedQueue<String>> jsonDataQueue = new ConcurrentHashMap<Class<?>, ConcurrentLinkedQueue<String>>();
	private Map<Class<?>, ConcurrentLinkedQueue<Class<?>>> contextJsonHandlerQueue = new ConcurrentHashMap<Class<?>, ConcurrentLinkedQueue<Class<?>>>();
	
	private Executor executor = null;
	private Class<?> currentMsgType = null;

	private AsynBIProcessContext register(Class<?> requestMessageType)
			throws MessageTypeRegisterException {

		if (dataType.equals(DataTypeEnum.JSON_DATA)) {
			jsonDataQueue.put(BaseJsonString.class,
					new ConcurrentLinkedQueue<String>());
			contextJsonHandlerQueue.put(BaseJsonString.class,
					new ConcurrentLinkedQueue<Class<?>>());
		} else if (dataType.equals(DataTypeEnum.MESSAGE_DATA)) {
			requestMessageQueue.put(requestMessageType,
					new ConcurrentLinkedQueue<AbstractRequestMessage>());
			contextMessageHandlerQueue.put(requestMessageType,
					new ConcurrentLinkedQueue<IBIMessageHandler<?>>());
			currentMsgType = requestMessageType;
		} else {
			System.out.println("xml 类型的数据目前不支持！");
		}

		return this;
	}

	@Override
	public AbstractResponseMessage acceptRequest(
			AbstractRequestMessage requestMsg) throws HandlerRequestException {
		// 将消息放入队列中
		requestMessageQueue.get(requestMsg.getClass()).add(requestMsg);
		return null;
	}

	@Override
	public AbstractResponseMessage acceptRequest(String json,
			Class<?> msgTypeClass) throws HandlerRequestException {
		jsonDataQueue.get(msgTypeClass).add(json);
		return null;
	}

	@Override
	public AsynBIProcessContext addHandler(Class<?> msgTypeClass,
			IBIMessageHandler<?> handler) throws BIHandlerException {
		if (dataType.equals(DataTypeEnum.MESSAGE_DATA)) {
			contextMessageHandlerQueue.get(msgTypeClass).add(handler);
		} else {
			throw new BIHandlerException("添加的Handler与当前处理数据类型不符！");
		}
		return this;
	}

	@Override
	public AsynBIProcessContext addHandler(Class<?> msgTypeClass,
			Class<?> handlerClass) throws BIHandlerException {
		if (dataType.equals(DataTypeEnum.JSON_DATA)) {
			contextJsonHandlerQueue.get(msgTypeClass).add(handlerClass);
		} else {
			throw new BIHandlerException("添加的Handler与当前处理数据类型不符！");
		}
		return this;
	}

	@Override
	public AsynBIProcessContext registerInit(Class<?> requestMessageType,
			DataTypeEnum dataType) throws MessageTypeRegisterException {
		this.dataType = dataType;
		register(requestMessageType);
		executor =  Executors.newFixedThreadPool(asynIoConfig.getThreadPoolSize());
		if (dataType.equals(DataTypeEnum.JSON_DATA)) {
			AsynIOClientJsonThreadFactory ioThreadFactory = AsynIOClientJsonThreadFactory
					.newFactory().registerProviderJson(
							new AsynIOGroupProvider(asynIoConfig));
			jsonIoThreadPool = new GenericObjectPool<AsynIOClientJsonThread>(
					ioThreadFactory, ioThreadFactory.bindingDefaultConfig());
			ioThreadFactory.init(jsonDataQueue.get(BaseJsonString.class),
					contextJsonHandlerQueue.get(BaseJsonString.class),
					jsonIoThreadPool);
		} else if (dataType.equals(DataTypeEnum.MESSAGE_DATA)) {
			AsynIOClientMessageThreadFactory ioThreadFactory = AsynIOClientMessageThreadFactory
					.newFactory().registerProviderMsg(
							new AsynIOGroupProvider(asynIoConfig));    
			messageIoThreadPool = new GenericObjectPool<AsynIOClientMessageThread>(
					ioThreadFactory, ioThreadFactory.bindingDefaultConfig());
			ioThreadFactory.init(requestMessageQueue.get(currentMsgType),
					contextMessageHandlerQueue.get(currentMsgType),
					messageIoThreadPool);
		} else {
			System.out.println(".............初始化线程池工厂失败!..........");
		}

		return this;
	}

	@Override
	public void run() {
		while (true) {
			try {
				AbstractAsynIOClientThread client =  null;
				if(dataType.equals(DataTypeEnum.JSON_DATA)){
					client = jsonIoThreadPool.borrowObject();
					executor.execute(client);  
				}else if(dataType.equals(DataTypeEnum.JSON_DATA)){
					client = messageIoThreadPool.borrowObject();
					executor.execute(client);  
				}else{
					System.out.println("未知消息类型!");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean authVerification(VerificationData data) {

		return false;
	}

	public static void main(String[] args) {
	}

	@Override
	public AsynBIProcessContext bindingConfig(AsynIoClientConfig config) {
		this.asynIoConfig = config;
		return this;
	}

}
