package org.foresee.Algorithm.sort;

import org.foresee.Algorithm.GenerateNumber;

public class MergeSort {
	public static void main(String[] args) {
		String filename = "E:/JavaSpace/Algorithm/sort.data";
		int[] data = GenerateNumber.getFileData(filename);
		MergeSort.sort(data);
		for (int i = 0; i < data.length; i++) {
			System.out.println(data[i]);
		}
	}

	/**
	 * 归并排序（原址排序），用递归的方式不断向下分一半，直到只剩下一个元素，然后合并。
	 * 计算时间代价，用递归树考虑，每层合并需要c*n的时间，总共有log2(n)层，共n*log2(n)时间
	 */
	public static void sort(int[] data) {
		sort(data, 0, data.length - 1);
	}

	public static void sort(int[] data, int p, int r) {
		if (p < r) {
			int q = Math.floorDiv(p + r, 2);
			sort(data, p, q);
			sort(data, q + 1, r);
			mergeWithoutSentry(data, p, q, r);
		}
	}

	/**
	 * 合并的过程 ，p到q是左边，q+1到r是右边，构建左右两个数组
	 * 额外的一个空间放置哨兵，它的值比任何可能的值都大，避免每次判断是否数组到底了（null）
	 * 判断左右数组值的大小，从小的那一边拿一个放到合并后的数组中（这里是原数组data）
	 * 注：如果需要计算集合中的逆序对数量，只需在合并时
	 * if(L[i]<=R[j])的else里count++就行了，因为左边大于右边，刚好构成逆序对
	 */
	public static void merge(int[] data, int p, int q, int r) {
		int n1 = q - p + 1, n2 = r - q;
		int[] L = new int[n1 + 1], R = new int[n2 + 1];
		for (int i = 0; i < n1; i++) {
			L[i] = data[p + i];
		}
		for (int i = 0; i < n2; i++) {
			R[i] = data[q + i + 1];
		}
		L[n1] = Integer.MAX_VALUE;
		R[n2] = Integer.MAX_VALUE;
		for (int i = 0, j = 0, k = p; k <= r; k++) {
			if (L[i] <= R[j]) {
				data[k] = L[i];
				i++;
			} else {
				data[k] = R[j];
				j++;
			}
		}
	}
	/**
	 * 不用哨兵变量完成合并过程，Java和C++里都有try catch，也就不用判断null了，C不行。
	 * 当L或R被拿空了时有ArrayIndexOutOfBoundsException，捕获，判断，哪边没拿完，就顺着放进去。
	 */
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
