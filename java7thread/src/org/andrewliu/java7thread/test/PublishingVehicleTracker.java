package org.andrewliu.java7thread.test;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 安全发布底层状态的车辆跟踪器
 * @author de
 *
 */
public class PublishingVehicleTracker {

	private final Map<String,SafePoint>  locations;
	private final Map<String,SafePoint> unmodifiableMap;
	
	public PublishingVehicleTracker(Map<String,SafePoint> locations){
		//初始化一个并发安全的HashMap
		this.locations = new ConcurrentHashMap<String,SafePoint>(locations);
		//初始化一个不可修改的Map(只能往Map里面添加和迭代元素)
		this.unmodifiableMap = Collections.unmodifiableMap(this.locations);
	}
	//返回不可修改的Map，只能迭代和添加
	public Map<String,SafePoint> getLocatioins(){
		return unmodifiableMap;
	}
	//根据ID获得一个安全点对象
	public SafePoint getLocatioin(String id){
		return locations.get(id);
	}
	//以接口形式修改某安全点的x,y值，而不是公开形式的修改
	public void setLocation(String id,int x,int y){
		if(!locations.containsKey(id)){
			throw new IllegalArgumentException("invalid vechile name : "+ id);
		}
		locations.get(id).set(x, y);
	}
}
