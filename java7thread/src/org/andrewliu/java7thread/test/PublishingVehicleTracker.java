package org.andrewliu.java7thread.test;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ��ȫ�����ײ�״̬�ĳ���������
 * @author de
 *
 */
public class PublishingVehicleTracker {

	private final Map<String,SafePoint>  locations;
	private final Map<String,SafePoint> unmodifiableMap;
	
	public PublishingVehicleTracker(Map<String,SafePoint> locations){
		//��ʼ��һ��������ȫ��HashMap
		this.locations = new ConcurrentHashMap<String,SafePoint>(locations);
		//��ʼ��һ�������޸ĵ�Map(ֻ����Map������Ӻ͵���Ԫ��)
		this.unmodifiableMap = Collections.unmodifiableMap(this.locations);
	}
	//���ز����޸ĵ�Map��ֻ�ܵ��������
	public Map<String,SafePoint> getLocatioins(){
		return unmodifiableMap;
	}
	//����ID���һ����ȫ�����
	public SafePoint getLocatioin(String id){
		return locations.get(id);
	}
	//�Խӿ���ʽ�޸�ĳ��ȫ���x,yֵ�������ǹ�����ʽ���޸�
	public void setLocation(String id,int x,int y){
		if(!locations.containsKey(id)){
			throw new IllegalArgumentException("invalid vechile name : "+ id);
		}
		locations.get(id).set(x, y);
	}
}
