package org.foresee.Algorithm.sort;

import org.foresee.Algorithm.GenerateNumber;

public class InsertionSort {
	public static void main(String[] args) {
		String filename="E:/JavaSpace/Algorithm/sort.data";
		int[] data=GenerateNumber.getFileData(filename);
		InsertionSort.sort(data);
		for(int i=0; i<data.length; i++){
			System.out.println(data[i]);
		}
	}
	/**
	 * 插入排序（原址排序） ，计算代价最差为n*n
	 */
	public static void sort(int[] A){
		for(int j=1; j<A.length; j++){
			int key=A[j];
			// Insert A[j] into the sorted sequence A[1...j-1]
			int i=j-1;
			while(i>=0 && A[i]>key){
				A[i+1]=A[i];
				i=i-1;
			}
			A[i+1]=key;
		}
	}
}
