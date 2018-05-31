package org.foresee.Algorithm.sort;

import org.foresee.Algorithm.GenerateNumber;

public class BubbleSort {
public static void main(String[] args) {
	String filename="E:/JavaSpace/Algorithm/sort.data";
	int[] data2=GenerateNumber.getFileData(filename);
	BubbleSort.bubbleSort(data2);
	for(int i=0; i<data2.length; i++){
		System.out.println(data2[i]);
	}
}
	/**
	 * 冒泡排序（原址排序） 
	 */
	public static void bubbleSort(int[] A) {
		int temp;
		for(int i=0; i<A.length-1; i++){
			for(int j=A.length-1; j>i; j--){
				if(A[j]<A[j-1]){
					temp=A[j];
					A[j]=A[j-1];
					A[j-1]=temp;
				}
			}
		}
	}

}
