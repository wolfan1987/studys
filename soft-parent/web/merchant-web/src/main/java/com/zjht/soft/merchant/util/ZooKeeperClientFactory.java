package com.zjht.soft.merchant.util;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class ZooKeeperClientFactory implements FactoryBean<CuratorFramework>, InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(ZooKeeperClientFactory.class);
    
	private Builder builder = CuratorFrameworkFactory.builder();
	private String zooKeeperURL;

	public void afterPropertiesSet() throws Exception {
		Assert.hasText(zooKeeperURL);
		log.info("CuratorFramework Client Property initialized.");
	}

	public CuratorFramework getObject() throws Exception {
		CuratorFramework client = builder.build();
		log.info("CuratorFramework Client initialized.");
		client.start();
		log.info("CuratorFramework Client started.");
		return client;
	}

	public Class<CuratorFramework> getObjectType() {
		return CuratorFramework.class;
	}

	public boolean isSingleton() {
		return true;
	}

//========================================================	
	// 172.26.7.113:21811,172.26.7.113:21822,172.26.7.113:21833
	public void setZooKeeperURL(String zooKeeperURL) {
		this.zooKeeperURL = zooKeeperURL;
		builder.connectString(zooKeeperURL);
	}
	// 120 * 1000
	public void setSessionTimeoutMs(int sessionTimeoutMs) {
		builder.sessionTimeoutMs(sessionTimeoutMs);
	}
	// 60 * 1000
	public void setConnectionTimeoutMs(int connectionTimeoutMs) {
		builder.connectionTimeoutMs(connectionTimeoutMs);
	}
	// new ExponentialBackoffRetry(1000, 3)
	public void setRetryPolicy(RetryPolicy retryPolicy) {
		builder.retryPolicy(retryPolicy);
	}

}
