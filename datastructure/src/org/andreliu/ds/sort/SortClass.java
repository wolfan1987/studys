package org.andreliu.ds.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ���������㷨ʵ��
 * ����8���㷨˵����
 * ���ࣺ
 * 1����������ֱ�Ӳ�������ϣ������
 * 2����������ð�����򡢿�������
 * 3��ѡ������ֱ��ѡ�����򡢶�����
 * 4���鲢����
 * 5���������򣨻�������
 * �����ڴ���ࣺ�鲢����
 * �����ڴ����٣�������
 * ƽ���ٶ���죺��������
 * ���ȶ����򣺿�������ϣ�����򡢶�����
 * @author de
 *
 */
public class SortClass {

	private   int[]  array = {49,38,65,97,76,13,27,49,78,34,12,64,5,4,62,99,98,54,56,17,18,23,34,15,35,25,53,51};
	/**
	 * ֱ�Ӳ�������
	 * ����˼�룺 ��Ҫ�����һ�����У�����ǰ��(n-1)�����Ѿ��������˳������Ҫ�ѵ�n�������뵽ǰ����������У�ʹ����n����Ҳ���ź���ģ���˷���ѭ����
	 * ֱ��ȫ���ź���
	 */
	public  void insertSort(){
		int temp = 0;
		
		for(int i = 1; i < array.length; i++){
			int j = i-1;
			temp = array[i];
			for ( ; j >= 0 &&temp <array[j];j--){
				array[j+1] = array[j]; //������temp��ֵ�������һ����λ 
			}
			array[j+1] = temp;
		}
		System.out.println("��ӡֱ�Ӳ���������----------");
		for( int i = 0; i< array.length; i++){
			System.out.println(array[i]);
		}
	}
	
	/**
	 * ϣ������(��С��������)�� 
	 * ����˼�룺�㷨�Ƚ�Ҫ�����һ������ĳ������d(n/2,ΪҪ�������ĸ������ֳ������飬ÿ���м�¼���±����d����ÿ����ȫ��Ԫ�ؽ���ֱ�Ӳ�������
	 * Ȼ������һ����С������(d/2)�������з��飬��ÿ�����ٽ���ֱ�Ӳ������򣬵���������1ʱ������ֱ�Ӳ��������������ɡ�
	 * 
	 */
	public void shellSort(){
		double d1 = array.length;
		int temp = 0;
		while(true){
			d1 = Math.ceil(d1/2);
			int d = (int)d1;
			for(int x = 0; x<d; x++){
				for(int i = x+d; i < array.length; i+=d){
					int j = i-d;
					temp = array[i];
					for(; j >=0&&temp<array[j];j-=d){
						array[j+d] = array[j];
					}
					array[j+d] = temp;
				}
			}
			
			if(d == 1){
				break;
			}
		}
		
		System.out.println(Arrays.toString(array));
	}
	
	
	/**
	 * ��ѡ������
	 * ����˼�룺��Ҫ�����һ�����У�ѡ����С��һ�������һ��λ�õ���������Ȼ����ʣ�µ����������ҵ���С����ڶ���λ�õ���
	 * ���������ѭ���������ڶ����������һ�����Ƚ�Ϊֹ��
	 * 
	 */
	public void selectSort(){
		int position = 0;
		for(int i = 0; i < array.length; i++){
			int j = i+1;
			position = i;
			int temp = array[i];
			for ( ; j < array.length; j++){
				if(array[j]<temp){
					temp = array[j];
					position = j;
				}
			}
			array[position] = array[i];
			array[i] = temp;
		}
	}
	
	/**
	 * ������
	 */
	public void heapSort(){
		System.out.println("������ʼ����������");
		int arrayLength = array.length;
		//ѭ������
		for ( int i = 0; i < arrayLength-1;i++){
			//����
			buildMaxHeap(array,arrayLength-1-i);
			//�����Ѷ������һ��Ԫ��
			swap(array,0,arrayLength-1-i);
			System.out.println(Arrays.toString(array));
		}
	}
	
	/**
	 * ��array���ݴ�0��lastIndex���󶥶�
	 * @param data
	 * @param lastIndex
	 */
	private void buildMaxHeap(int[] data,int lastIndex){
		//��lastIndex���ڵ㣨���һ���ڵ㣩�ĸ��ڵ㿪ʼ
		for ( int i = (lastIndex-1)/2; i >=0;i--){
			//k���������жϵĽڵ�
			int k = i;
			//�����ǰk�ڵ���ӽڵ����
			while(k*2+1<=lastIndex){
				//k�ڵ�����ӽڵ������
				int biggerIndex = 2*k+1;
				//���biggerIndeС��lastindex������biggerIndex+1�����k�ڵ�����ӽڵ����
				if(biggerIndex<lastIndex){
					//������ӽڵ��ֵ�ϴ�
					if(array[biggerIndex] < array[biggerIndex+1]){
						//biggerIndex���Ǽ�¼�ϴ��ӽڵ������
						biggerIndex++;
					}
				}
				//���k�ڵ��ֵС����ϴ���ӽڵ��ֵ
				if(data[k] < data[biggerIndex]){
					//��������
					swap(array,k,biggerIndex);
					//��biggerIndex����k,��ʼwhileѭ������һ��ѭ�������±�֤k�ڵ��ֵ���������ӽڵ��ֵ
					k = biggerIndex;
				}else{
					break;
				}
			}
		}
	}
	
	private void swap(int[] data,int i ,int j){
		int tmp = data[i];
		data[i] = data[j];
		data[j] = tmp;
	}
	
	/**
	 * ð������
	 * ��Ҫ�����һ�����У��Ե�ǰ��δ�ź���ķ�Χ�ڵ�ȫ�������������¶����ڵ����������ν��бȽϺ͵������ýϴ�������³�����С������ð������
	 * ÿ�������ڵ����ȽϺ������ǵ�����������Ҫ���෴���ͽ����ǻ�����
	 */
	public void  bubbleSort(){
		System.out.println("ð������ʼ��---"+ Arrays.toString(array));
		int temp = 0;
		for(int i = 0; i < array.length-1;i++){
			for(int j = 0; j < array.length-1-i;j++){
				if(array[j] > array[j+1]){
					temp = array[j];
					array[j] = array[j+1];
					array[j+1] = temp;
				}
			}
		}
		System.out.println("ð����������"+Arrays.toString(array));
	}
	
	
	/**
	 * ��������
	 * ����˼�룺 ѡ��һ����׼Ԫ�أ�ͨ��ѡ���һ��Ԫ�ػ������һ��Ԫ�أ�ͨ��һ��ɨ�裬�����������зֳ������֣�һ���ֱȻ�׼Ԫ��С��һ���ִ��ڵ���
	 * ��׼Ԫ�أ���ʱ��׼Ԫ�������ź�������ȷλ�ã�Ȼ������ͬ���ķ����ݹ�����򻮷ֵ������֡�
	 * ѡһ�������������Ϊ���룬������ֱ�����������Եݹ���ʽ����������
	 */
	public  void quickSort(){
		if(array.length>0){
			_quickSort(array,0,array.length-1);
		}
	}
	
	
	private int getMiddle(int[] list,int low,int high){
		int tmp = list[low]; //������ĵ�һ����Ϊ����
		while(low< high){
			while(low < high && list[high] >= tmp){
				high--;
			}
			list[low] = list[high];//������С�ļ�¼�Ƶ��Ͷ�
			while(low < high && list[low] <= tmp){
				low++;
			}
			list[high] = list[low];//�������ļ�¼�Ƶ��߶�
		}
		
		list[low] = tmp ; //�����¼��β
		return low;  //���������λ��
		
	}
	
	private void _quickSort(int[] list,int low,int high){
		if(low < high){
			//��list����һ��Ϊ��
			int middle = getMiddle(list,low,high);
			_quickSort(list,low,middle-1);//�Ե��ֱ���еݹ�����
			_quickSort(list,middle+1,high);//�Ը��ֱ��������
			
		}
	}
	
	/**
	 * �鲢����
	 * ����˼�룺
	 * �鲢��Merge)�����ǽ�����(����������)�����ȫ����һ���µ���������Ѵ��������з�Ϊ���ɸ������У�ÿ��������������ģ�Ȼ���ٰ�
	 * ���������кϲ�Ϊ�����������С�
	 */
	public void mergingSort(){
	  	sort(array,0,array.length-1);
	}
	private void sort(int[] array,int left ,int right){
		if(left < right){
			//�ҳ��м�����
			int center = (left+right)/2;
			//�����������еݹ�
			sort(array,left,center);
			//���ұ�������еݹ�
			sort(array,center+1,right);
			//�ϲ�
			merger(array,left,center,right);
		}
	}
	private void merger(int[] array,int left,int center,int right){
		int[] tmpArray = new int[array.length];
		int mid = center+1;
		//third��¼�м����������
		int third = left;
		int tmp = left;
		while(left <= center && mid<=right){
			//������������ȡ����С�ķ����м�����
			if(array[left]<=array[mid]){
				tmpArray[third++] = array[left++];
			}else{
				tmpArray[third++] = array[mid++];
			}
		}
		//ʣ�ಿ�����η����м�����
		while(mid <=right){
			tmpArray[third++] = array[mid++];
		}
		while(left <= center){
			tmpArray[third++] = array[left++];
		}
		//���м������е����ݸ��ƻ�ԭ����
		while(tmp <= right){
			array[tmp] = tmpArray[tmp++];
		}
		System.out.println(Arrays.toString(array));
	}
	
	/**
	 * ��������:
	 * ����˼�룺
	 * �����д��Ƚ���ֵ(������)ͳһΪͬ������λ���ȣ���λ�϶̵���ǰ�油�㡣Ȼ�󣬴����λ��ʼ�����ν���һ���������������λ����һֱ�����λ�������
	 * �Ժ����оͱ��һ����������
	 */
	public void radixSort(){
		_radixSort(array);
		System.out.println(Arrays.toString(array));
	}
	
	private  void _radixSort(int[] array){
		//����ȷ�����������
		int max = array[0];
		for ( int i = 1; i < array.length;i++){
			if(array[i] > max){
				max = array[i];
			}
		}
		
		int time = 0;
		//�ж�λ��
		while(max > 0 ){
			 max/=10;
			 time++;
		}
		
		//����10������
		List<ArrayList> queue = new ArrayList<ArrayList>();
		for ( int i = 0; i < 10; i++){
			ArrayList<Integer>  queue1 = new ArrayList<Integer>();
			queue.add(queue1);
		}
		
		//����time�η�����ռ�
		for ( int i = 0; i < time; i++){
			//��������Ԫ��
			for(int j = 0; j < array.length;i++){
				//�õ����ֵĵ�time+1λ��
				int x = array[j] %(int)Math.pow(10,i+1)/(int)Math.pow(10, i);
				ArrayList<Integer> queue2 = queue.get(x);
				queue2.add(array[j]);
				queue.set(x, queue2);
			}
			//Ԫ�ؼ�����
			int count = 0;
			//�ռ�����
			for(int k = 0; k < 10; k++){
				while(queue.get(k).size()>0){
					ArrayList<Integer> queue3 = queue.get(k);
					array[count] = queue3.get(0);
					queue3.remove(0);
					count++;
				}
			}
		}
	}
	
	
	public static void main(String[] args) {
		SortClass  sc = new SortClass();
		sc.insertSort();
	}
}
