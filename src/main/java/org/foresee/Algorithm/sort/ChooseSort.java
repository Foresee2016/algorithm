package org.foresee.Algorithm.sort;

import org.foresee.Algorithm.GenerateNumber;

/**
 * 选择排序，思路是从数组中选出最大的，和A[0]交换，然后在后面选出次大的和A[1]交换，直到最后。
 * 最好和最坏情况都为n*n
 */
public class ChooseSort {
	public static void main(String[] args) {
		String filename="E:/JavaSpace/Algorithm/sort.data";
		int[] data=GenerateNumber.getFileData(filename);
		ChooseSort.sort(data);
		for(int i=0; i<data.length; i++){
			System.out.println(data[i]);
		}
	}
	public static void sort(int[] A) {
		for (int i = 0; i < A.length - 1; i++) {
			int maxIdx=i;
			for (int j = i+1; j < A.length; j++) {
				if(A[j]>A[maxIdx]){
					maxIdx=j;
				}
			}
			int temp=A[i];
			A[i]=A[maxIdx];
			A[maxIdx]=temp;
		}
	}
}
