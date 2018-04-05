package org.andrewliu.java7thread.java7conlist;

import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 有趣的数据结构:ConcurrentNavigableMap接口及其实现类。实现这个接口的类以如下两部分存放元素：
 * 一个键值（Key)，它是元素的标识并且是唯一的
 * 元素其它部分数据。
 * 以上两个组成部分都必须在不同的类中实现。
 * ConcurrentSkipListMap接口与ConcurrentNavigableMap接口有相同行为的一个非阻塞式列表，它使用了一个Skip List来存放数据，Skip List
 * 是基于并发列表的数据结构，二叉树相近，它比有序列表在添加、搜索、或删除元素时耗费更少的访问时间。
 * 当插入元素到映射中时，ConcurrentSkipListMap接口类使用键值来排序所有元素。除了能返回一个具体元素外，这个类也提供获取子映射的方法。
 * ConcurrentSkipListMap类的其他方法：
 * headMap(K toKey): K 是在ConcurrentSkipListMap对象的泛型参数里用到的键，这个方法返回映射中所有键值小于参数值toKey的子映射。小于key.
 * tailMap(K fromKey): K 是在ConcurrentSkipListMap对象的泛型参数里用到的键，这个方法返回映射中所有键值大于参数值fromKey的子映射。大于key.
 * putIfAbsent(K key,V value): 如果映射中不存在键key,那么就将key和value保存到映射中。
 * pollLastEntry(): 返回并移除映射中的最后一个Map.Entry对象.
 * replace(K key,V value): 如果映射中已经存在键key,则用参数中的value替换现有值。
 * @author de
 *
 */
public class ConcurrentSkipListMapTest_6_6 {

	public static void main(String[] args) {
		ConcurrentSkipListMap<String,Contact> map = new ConcurrentSkipListMap<>();
		Thread threads[] = new Thread[25];
		int counter = 0;
		for ( char i = 'A' ; i < 'Z'; i++){
			ContactTask  task = new ContactTask(map, String.valueOf(i));
			threads[counter] = new Thread(task);
			threads[counter].start();
			counter++;
		}
		
		for ( int i = 0; i < 25; i++){
			try{
				threads[i].join();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
		System.out.printf("Main: Size of the map: %d\n",map.size());
		Map.Entry<String,Contact> element;
		Contact contact;
		element = map.firstEntry();//得到Map里的第一个实体《键值对》
		contact = element.getValue();
		System.out.printf("Main: First Entry: %s: %s\n", contact.getName(),contact.getPhone());
		
		
		element = map.lastEntry();//得到Map里的最后一个实体《键值对》
		contact = element.getValue();
		System.out.printf("Main: Last Entry: %s: %s\n", contact.getName(),contact.getPhone());
		
		System.out.printf("Main: Submap from A1996 to B1002: \n");
		ConcurrentNavigableMap<String,Contact>	 submap = map.subMap("A1996", "B1002");   //以key为范围，获取一个子map
		do{
			
			element = submap.pollFirstEntry();//得到子Map中的第一个元素，并移除
			if(element!=null){
				contact = element.getValue();
				System.out.printf("%s: %s\n", contact.getName(),contact.getPhone());
			}
		}while(element != null);
		
	}
}


class Contact{
	private String name;
	private String phone;
	
	public Contact(String name, String phone) {
		super();
		this.name = name;
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}
}

class ContactTask implements Runnable{
	//键+对象Map
	private  ConcurrentSkipListMap<String,Contact> map;
	private String id;
	public ContactTask(ConcurrentSkipListMap<String, Contact> map, String id) {
		super();
		this.map = map;
		this.id = id;
	}
	@Override
	public void run() {
		for ( int i = 0; i < 1000; i++){
			Contact contact = new Contact(id,String.valueOf(i+1000));
			map.put(id+contact.getPhone(), contact);
		}
	}
	
	
	
	
}