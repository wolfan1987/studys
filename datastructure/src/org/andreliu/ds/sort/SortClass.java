package org.andreliu.ds.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 各种排序算法实现
 * 排序8大算法说明：
 * 分类：
 * 1、插入排序（直接插入排序、希尔排序）
 * 2、交换排序（冒泡排序、快速排序）
 * 3、选择排序（直接选择排序、堆排序）
 * 4、归并排序
 * 5、分配排序（基数排序）
 * 所需内存最多：归并排序
 * 所需内存最少：堆排序
 * 平均速度最快：快速排序
 * 不稳定排序：快速排序、希尔排序、堆排序
 * @author de
 *
 */
public class SortClass {

	private   int[]  array = {49,38,65,97,76,13,27,49,78,34,12,64,5,4,62,99,98,54,56,17,18,23,34,15,35,25,53,51};
	/**
	 * 直接插入排序
	 * 基本思想： 在要排序的一组数中，假设前面(n-1)个数已经是排序好顺序，现在要把第n个数插入到前面的有序数中，使得这n个数也是排好序的，如此反复循环，
	 * 直到全部排好序。
	 */
	public  void insertSort(){
		int temp = 0;
		
		for(int i = 1; i < array.length; i++){
			int j = i-1;
			temp = array[i];
			for ( ; j >= 0 &&temp <array[j];j--){
				array[j+1] = array[j]; //将大于temp的值整体后移一个单位 
			}
			array[j+1] = temp;
		}
		System.out.println("打印直接插入排序结果----------");
		for( int i = 0; i< array.length; i++){
			System.out.println(array[i]);
		}
	}
	
	/**
	 * 希尔排序(最小增量排序)： 
	 * 基本思想：算法先将要排序的一组数按某个增量d(n/2,为要排序数的个数）分成若干组，每组中记录的下标相差d，对每组中全部元素进行直接插入排序，
	 * 然后再用一个较小的增量(d/2)对它进行分组，在每组中再进行直接插入排序，当增量减到1时，进行直接插入排序后，排序完成。
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
	 * 简单选择排序：
	 * 基本思想：在要排序的一组数中，选出最小的一个数与第一个位置的数交换，然后在剩下的数当中再找到最小的与第二个位置的数
	 * 交换，如此循环到倒数第二个数和最后一个数比较为止。
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
	 * 堆排序
	 */
	public void heapSort(){
		System.out.println("堆排序开始。。。。。");
		int arrayLength = array.length;
		//循环建堆
		for ( int i = 0; i < arrayLength-1;i++){
			//建堆
			buildMaxHeap(array,arrayLength-1-i);
			//交换堆顶和最后一个元素
			swap(array,0,arrayLength-1-i);
			System.out.println(Arrays.toString(array));
		}
	}
	
	/**
	 * 对array数据从0到lastIndex建大顶堆
	 * @param data
	 * @param lastIndex
	 */
	private void buildMaxHeap(int[] data,int lastIndex){
		//从lastIndex处节点（最后一个节点）的父节点开始
		for ( int i = (lastIndex-1)/2; i >=0;i--){
			//k保存正在判断的节点
			int k = i;
			//如果当前k节点的子节点存在
			while(k*2+1<=lastIndex){
				//k节点的左子节点的索引
				int biggerIndex = 2*k+1;
				//如果biggerInde小于lastindex，即：biggerIndex+1代表的k节点的右子节点存在
				if(biggerIndex<lastIndex){
					//如果右子节点的值较大
					if(array[biggerIndex] < array[biggerIndex+1]){
						//biggerIndex总是记录较大子节点的索引
						biggerIndex++;
					}
				}
				//如果k节点的值小于其较大的子节点的值
				if(data[k] < data[biggerIndex]){
					//交换他们
					swap(array,k,biggerIndex);
					//将biggerIndex赋予k,开始while循环的下一次循环，重新保证k节点的值大于左右子节点的值
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
	 * 冒泡排序
	 * 在要排序的一组数中，对当前还未排好序的范围内的全部数，自上面下对相邻的两个数依次进行比较和调整，让较大的数往下沉，较小的往上冒。即：
	 * 每当两相邻的数比较后发现它们的排序与排序要求相反，就将它们互换。
	 */
	public void  bubbleSort(){
		System.out.println("冒泡排序开始：---"+ Arrays.toString(array));
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
		System.out.println("冒泡排序结果："+Arrays.toString(array));
	}
	
	
	/**
	 * 快速排序：
	 * 基本思想： 选择一个基准元素，通常选择第一个元素或者最后一个元素，通过一趟扫描，将待排序序列分成两部分，一部分比基准元素小，一部分大于等于
	 * 基准元素，此时基准元素在其排好序后的正确位置，然后再用同样的方法递归地排序划分的两部分。
	 * 选一基数，将数组分为两半，对两半分别进行排序，再以递归形式对两半排序
	 */
	public  void quickSort(){
		if(array.length>0){
			_quickSort(array,0,array.length-1);
		}
	}
	
	
	private int getMiddle(int[] list,int low,int high){
		int tmp = list[low]; //将数组的第一个作为中轴
		while(low< high){
			while(low < high && list[high] >= tmp){
				high--;
			}
			list[low] = list[high];//比中轴小的记录移到低端
			while(low < high && list[low] <= tmp){
				low++;
			}
			list[high] = list[low];//比中轴大的记录称到高端
		}
		
		list[low] = tmp ; //中轴记录到尾
		return low;  //返回中轴的位置
		
	}
	
	private void _quickSort(int[] list,int low,int high){
		if(low < high){
			//将list数组一分为二
			int middle = getMiddle(list,low,high);
			_quickSort(list,low,middle-1);//对低字表进行递归排序
			_quickSort(list,middle+1,high);//对高字表进行排序
			
		}
	}
	
	/**
	 * 归并排序：
	 * 基本思想：
	 * 归并（Merge)排序法是将两个(或两个以上)有序表全并成一个新的有序表，即把待排序序列分为若干个子序列，每个子序列是有序的，然后再把
	 * 有序子序列合并为整体有序序列。
	 */
	public void mergingSort(){
	  	sort(array,0,array.length-1);
	}
	private void sort(int[] array,int left ,int right){
		if(left < right){
			//找出中间索引
			int center = (left+right)/2;
			//对左边数组进行递归
			sort(array,left,center);
			//对右边数组进行递归
			sort(array,center+1,right);
			//合并
			merger(array,left,center,right);
		}
	}
	private void merger(int[] array,int left,int center,int right){
		int[] tmpArray = new int[array.length];
		int mid = center+1;
		//third记录中间数组的索引
		int third = left;
		int tmp = left;
		while(left <= center && mid<=right){
			//从两个数组中取出最小的放入中间数组
			if(array[left]<=array[mid]){
				tmpArray[third++] = array[left++];
			}else{
				tmpArray[third++] = array[mid++];
			}
		}
		//剩余部分依次放入中间数组
		while(mid <=right){
			tmpArray[third++] = array[mid++];
		}
		while(left <= center){
			tmpArray[third++] = array[left++];
		}
		//将中间数组中的内容复制回原数组
		while(tmp <= right){
			array[tmp] = tmpArray[tmp++];
		}
		System.out.println(Arrays.toString(array));
	}
	
	/**
	 * 基数排序:
	 * 基本思想：
	 * 将所有待比较数值(正整数)统一为同样的数位长度，数位较短的数前面补零。然后，从最低位开始，依次进行一次排序。这样从最低位排序一直到最高位排序完成
	 * 以后，数列就变成一个有序序列
	 */
	public void radixSort(){
		_radixSort(array);
		System.out.println(Arrays.toString(array));
	}
	
	private  void _radixSort(int[] array){
		//首先确定排序的趟数
		int max = array[0];
		for ( int i = 1; i < array.length;i++){
			if(array[i] > max){
				max = array[i];
			}
		}
		
		int time = 0;
		//判断位数
		while(max > 0 ){
			 max/=10;
			 time++;
		}
		
		//建立10个队列
		List<ArrayList> queue = new ArrayList<ArrayList>();
		for ( int i = 0; i < 10; i++){
			ArrayList<Integer>  queue1 = new ArrayList<Integer>();
			queue.add(queue1);
		}
		
		//运行time次分配和收集
		for ( int i = 0; i < time; i++){
			//分配数组元素
			for(int j = 0; j < array.length;i++){
				//得到数字的第time+1位数
				int x = array[j] %(int)Math.pow(10,i+1)/(int)Math.pow(10, i);
				ArrayList<Integer> queue2 = queue.get(x);
				queue2.add(array[j]);
				queue.set(x, queue2);
			}
			//元素计数器
			int count = 0;
			//收集队列
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
