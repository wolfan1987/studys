package org.andrewliu.java7thread.java7conlist;

import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * ��Ȥ�����ݽṹ:ConcurrentNavigableMap�ӿڼ���ʵ���ࡣʵ������ӿڵ��������������ִ��Ԫ�أ�
 * һ����ֵ��Key)������Ԫ�صı�ʶ������Ψһ��
 * Ԫ�������������ݡ�
 * ����������ɲ��ֶ������ڲ�ͬ������ʵ�֡�
 * ConcurrentSkipListMap�ӿ���ConcurrentNavigableMap�ӿ�����ͬ��Ϊ��һ��������ʽ�б���ʹ����һ��Skip List��������ݣ�Skip List
 * �ǻ��ڲ����б�����ݽṹ����������������������б�����ӡ���������ɾ��Ԫ��ʱ�ķѸ��ٵķ���ʱ�䡣
 * ������Ԫ�ص�ӳ����ʱ��ConcurrentSkipListMap�ӿ���ʹ�ü�ֵ����������Ԫ�ء������ܷ���һ������Ԫ���⣬�����Ҳ�ṩ��ȡ��ӳ��ķ�����
 * ConcurrentSkipListMap�������������
 * headMap(K toKey): K ����ConcurrentSkipListMap����ķ��Ͳ������õ��ļ��������������ӳ�������м�ֵС�ڲ���ֵtoKey����ӳ�䡣С��key.
 * tailMap(K fromKey): K ����ConcurrentSkipListMap����ķ��Ͳ������õ��ļ��������������ӳ�������м�ֵ���ڲ���ֵfromKey����ӳ�䡣����key.
 * putIfAbsent(K key,V value): ���ӳ���в����ڼ�key,��ô�ͽ�key��value���浽ӳ���С�
 * pollLastEntry(): ���ز��Ƴ�ӳ���е����һ��Map.Entry����.
 * replace(K key,V value): ���ӳ�����Ѿ����ڼ�key,���ò����е�value�滻����ֵ��
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
		element = map.firstEntry();//�õ�Map��ĵ�һ��ʵ�塶��ֵ�ԡ�
		contact = element.getValue();
		System.out.printf("Main: First Entry: %s: %s\n", contact.getName(),contact.getPhone());
		
		
		element = map.lastEntry();//�õ�Map������һ��ʵ�塶��ֵ�ԡ�
		contact = element.getValue();
		System.out.printf("Main: Last Entry: %s: %s\n", contact.getName(),contact.getPhone());
		
		System.out.printf("Main: Submap from A1996 to B1002: \n");
		ConcurrentNavigableMap<String,Contact>	 submap = map.subMap("A1996", "B1002");   //��keyΪ��Χ����ȡһ����map
		do{
			
			element = submap.pollFirstEntry();//�õ���Map�еĵ�һ��Ԫ�أ����Ƴ�
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
	//��+����Map
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