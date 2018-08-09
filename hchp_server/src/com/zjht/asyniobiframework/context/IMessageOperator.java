package com.zjht.asyniobiframework.context;

import com.zjht.asyniobiframework.auth.VerificationData;
import com.zjht.asyniobiframework.config.AsynIoClientConfig;
import com.zjht.asyniobiframework.exception.BIHandlerException;
import com.zjht.asyniobiframework.exception.HandlerRequestException;
import com.zjht.asyniobiframework.exception.MessageTypeRegisterException;
import com.zjht.asyniobiframework.handler.IBIJsonHandler;
import com.zjht.asyniobiframework.handler.IBIMessageHandler;
import com.zjht.asyniobiframework.message.AbstractRequestMessage;
import com.zjht.asyniobiframework.message.AbstractResponseMessage;

/**
 * 用于在业务边界上下文中注册相关Handler
 * @author de
 *
 */
public interface IMessageOperator<T> {

	public  AbstractResponseMessage   acceptRequest(AbstractRequestMessage  requestMsg) throws HandlerRequestException;
	
	public  AbstractResponseMessage   acceptRequest(String json,Class<?> msgTypeClass) throws HandlerRequestException;
	
	/**
	 * 进行安全验证(如用户名，session是否正常，密码，ssl，证书验证、httpclient请求时，获取相关验证参数后将调用些方法
	 * @param data
	 * @return
	 */
	public  boolean   authVerification(VerificationData  data);
	
	public  AsynBIProcessContext   addHandler(Class<?> msgTypeClass,IBIMessageHandler<?>  handler)  throws BIHandlerException;
	public  AsynBIProcessContext   addHandler(Class<?> msgTypeClass,Class<?> handlerClass)  throws BIHandlerException;
	
	public AsynBIProcessContext  registerInit(Class<?>  msgTypeClass,DataTypeEnum dataType) throws MessageTypeRegisterException;
	
	public AsynBIProcessContext  bindingConfig(AsynIoClientConfig  config);
	

}
