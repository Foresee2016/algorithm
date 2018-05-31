package org.foresee.Algorithm.sort;

import org.foresee.Algorithm.GenerateNumber;

public class QuickSort {
	public static void main(String[] args) {
		String filename = "E:/JavaSpace/Algorithm/sort.data";
		int[] data = GenerateNumber.getFileData(filename);
//		int[] data = new int[]{2,8,7,1,3,5,6,4};
		QuickSort.sort(data);
		for (int i = 0; i < data.length; i++) {
			System.out.println(data[i]);
		}
	}
	public static void sort(int[] A) {
		sort(A, 0, A.length-1);
	}
	public static void sort(int[] A, int p, int r){
		if(p<r){
			int q=partition(A, p, r);
			sort(A, p, q-1);
			sort(A, q+1, r);
		}
	}
	public static int partition(int[] A, int p, int r) {
		int x=A[r];
		int i=p-1;
		for(int j=p; j<r; j++){
			if(A[j]<=x){
				i=i+1;
				int temp=A[i];
				A[i]=A[j];
				A[j]=temp;
			}
		}
		int temp=A[i+1];
		A[i+1]=A[r];
		A[r]=temp;
		return i+1;
	}
	/**
	 * 当数组大致按从大到小排序时，以末尾元素做分界效果较差，不能大致均匀分成两组，
	 * 随机一个p到r之间的数，将A中该位置先与末尾元素交换，再按正常的做。
	 * 随机分界按概率来说，不会总选到最差的分界元素，所以性能接近nlog2(n)
	 */
	public static int randomPartition(int[] A, int p, int r) {
		int rand=(int) (Math.random()*(r-p+1)) + p;
		int temp=A[r];
		A[r]=A[rand];
		A[rand]=temp;
		return partition(A, p, r);
	}
}
