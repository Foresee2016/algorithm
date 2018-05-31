package org.foresee.Algorithm.sort;

import java.util.Arrays;

import org.foresee.Algorithm.GenerateNumber;

/**
 * 排序这一节的练习题，计算数组中的逆序对，i<j且A[i]>A[j],则(i,j)称为A的一个逆序对 .
 * 书里提示了修改归并排序。所以在归并排序merge的时候，每次有交换就是一个逆序
 */
public class InvertionCount {
	public static void main(String[] args) {
		String filename = "E:/JavaSpace/Algorithm/sort.data";
		int[] data = GenerateNumber.getFileData(filename);
		int[] data2=Arrays.copyOf(data, data.length);
		int[] data3={2,8,1,6,3};
		int[] data4=Arrays.copyOf(data3, data3.length);
		int revertionCount1=InvertionCount.countInversion(data);
		int revertionCount2=InvertionCount.countInvertion2(data2);
		System.out.println(revertionCount1+", "+revertionCount2);
		int revertionCount3=InvertionCount.countInversion(data3);
		int revertionCount4=InvertionCount.countInvertion2(data4);
		System.out.println(revertionCount3+", "+revertionCount4);
	}
	public static int countInvertion2(int[] A) { //使用暴力求解，验证结果是否正确
		int cnt=0;
		for (int i = 0; i < A.length - 1; i++) {
			for (int j = i+1; j < A.length; j++) {
				if(A[i]>A[j]){
					cnt++;
				}
			}
		}
		return cnt;
	}
	public static int countInversion(int[] A) {
		count=0;
		sort(A, 0, A.length-1);
		return count;
	}
	public static int count=0; //线程不安全
	public static void sort(int[] data, int p, int r) {
		if (p < r) {
			int q = Math.floorDiv(p + r, 2);
			sort(data, p, q);
			sort(data, q + 1, r);
			mergeWithoutSentry(data, p, q, r);
		}
	}
	public static void mergeWithoutSentry(int[] data, int p, int q, int r) {
		int n1 = q - p + 1, n2 = r - q;
		int[] L = new int[n1], R = new int[n2];
		for (int i = 0; i < n1; i++) {
			L[i] = data[p + i];
		}
		for (int i = 0; i < n2; i++) {
			R[i] = data[q + i + 1];
		}
		int i = 0, j = 0, k = p;
		try {
			for (; k <= r; k++) {
				if (L[i] <= R[j]) {
					data[k] = L[i];
					i++;
				} else {
					count+=n1-i;
					data[k] = R[j];
					j++;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			if (i != n1) {
				for (; i < n1; i++, k++) {
					data[k]=L[i];
				}
			}else{
				for(; j<n2; j++, k++){
					data[k]=R[j];
				}
			}
		}
	}
}
