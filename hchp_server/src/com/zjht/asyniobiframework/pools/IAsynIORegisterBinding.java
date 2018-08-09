package com.zjht.asyniobiframework.pools;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public interface IAsynIORegisterBinding {

	public  AsynIOClientJsonThreadFactory  registerProviderJson(AsynIOGroupProvider  provider);
	public  AsynIOClientMessageThreadFactory registerProviderMsg(AsynIOGroupProvider  provider);
	public  GenericObjectPoolConfig  bindingDefaultConfig();
	
}
