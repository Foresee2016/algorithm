package org.foresee.Algorithm;

import org.foresee.Algorithm.sort.QuickSort;

/**
 * 计算数组最大最小值，中位数，第i个顺序统计量（第i小的数）等
 */
public class OrderStatistic {
	public static void main(String[] args) {
		int[] data = new int[] { 5, 61, 9, 16, 51, 7, 4, 87, 2, 47, 41, 4 };
		int min = minimum(data);
		System.out.println("min: " + min);
		MinMax res = minMax(data);
		System.out.println("max: " + res.max + ", min: " + res.min);
		int thirdMin = OrderStatistic.randomSelect(data, 3);
		System.out.println("the third min: " + thirdMin);
		// 验证两个有序数组取总体中位数的算法
		int[] dataA = new int[] { 1, 6, 15, 22, 26, 31, 38, 56, 72 };
		int[] dataB = new int[] { 8, 11, 25, 33, 61, 88, 91, 97, 99 };
		int medianAB = medianOfTwo(dataA, dataB);
		int[] dataAB = mergeTwo(dataA, dataB);
		System.out.println("medianAB: " + medianAB+", check: "+dataAB[dataAB.length/2 - 1]);
	}
	/*
	 * 这里注记思考题中的一些提示，算法不写了，记思路，挺启发的。
	 * 1. 寻找n个元素序列中的前i个最大元素，基于比较的算法。
	 * 思路a：对输入数据排序，再找前i个最大数。
	 * 思路b：对输入数据建立一个最大优先级队列，再extract i次
	 * 思路c：利用一个顺序统计量算法找到第i大的元素，用它做主元划分输入数组，再对划出的前i大的数排序。
	 * 其中，前两个思路都是nlog2(n)的时间复杂度，c是2n+ilog2(i)的，一般情况c要好一些。 
	 */
	/**
	 * 为了验证下边的medianOfTwo算法，把A和B两个有序数组合成一个有序数组，然后取中位数，看是否计算一致
	 * 合成时就直接哪边大就放进去就行了，如果一边拿完了，有
	 */
	public static int[] mergeTwo(int[] A, int[] B) {
		int[] res = new int[A.length + B.length];
		int i = 0, j = 0;
		try {
			for (;;) {
				if (A[i] < B[j]) {
					res[i + j] = A[i];
					i++;
				} else {
					res[i + j] = B[j];
					j++;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			if(i!=A.length){
				for(;i<A.length;i++){
					res[i+j]=A[i];
				}
			}else{
				for(; j<B.length; j++){
					res[i+j]=B[j];
				}
			}
		}
		return res;
	}

	/**
	 * 一个中间方法，省的每次指定全部长度。
	 */
	public static int medianOfTwo(int[] A, int[] B) {
		if (A.length != B.length) { // 应该throw Exception，这里为了测试，返回0了
			return 0;
		}
		return medianOfTwo(A, 0, A.length - 1, B, 0, B.length - 1);
	}

	/**
	 * 题目：从两个有序数组A，B中（A和B是等长度的，比如为n），在log2(n)时间复杂度内求出这2n个数的中位数 思路：
	 * 选A的中位数和B的中位数mA和mB，如果mA小于mB，说明最后的中位数应该在A的后一半和B的前一半，
	 * 现在A剩下了后边n/2，B剩下了前边n/2，而要找的是这2n个数里的第n个，A去掉的一半都比它小，
	 * 所以现在要找这n个数里的第n/2个了。一直找，直到某一个里只剩下一个数了，那个数就是2n个数的中位数。 而mA大于mB的情况也一样去另一方向的。
	 * 注：偶数的中位数有两个，这里专指下中位数
	 */
	protected static int medianOfTwo(int[] A, int lA, int rA, int[] B, int lB, int rB) {
		if (rA == lA) {
			return A[lA];
		}
		if (rB == lB) {
			return B[lB];
		}
		int mA = lA + (int) ((rA - lA) / 2), mB = lB + (int) ((rB - lB) / 2);
		if (A[mA] < B[mB]) {
			return medianOfTwo(A, mA, rA, B, lB, mB);
		} else {
			return medianOfTwo(A, lA, mA, B, mB, rB);
		}
	}

	/**
	 * 从数组A中找到第i小的元素，
	 */
	public static int randomSelect(int[] A, int i) {
		return randomSelect(A, 0, A.length - 1, i);
	}

	/**
	 * 从数组A的p到r区间内找到第i小的数，期望运行时间O(n)（建立在数组元素值都不同的基础上）
	 * 如果有相同值会干扰运行时间计算，比如假定所有值都相同，每次分界只能剔除一个数，这样会是O(n*n) 思路：（根快速排序中分两部分的想法一样）
	 * 随机选一个数把A分为两半，左一部分是小于该数的，计算左边部分的长度，如果刚好等于i，则分界处就是第i小的数
	 * 如果大于i，说明第i小的数在左边找，如果小于i，在右边找。 注：还有个最差运行时间是O(n)的选择算法，但实现起来复杂，这里不记了
	 */
	public static int randomSelect(int[] A, int p, int r, int i) {
		if (p == r) {
			return A[p];
		}
		int q = QuickSort.randomPartition(A, p, r);
		int k = q - p + 1;
		if (i == k) {
			return A[q];
		} else {
			if (i < k) {
				return randomSelect(A, p, q - 1, i);
			} else {
				return randomSelect(A, q + 1, r, i - k);
			}
		}
	}

	/**
	 * 找最小值，用n-1次比较
	 */
	public static int minimum(int[] A) {
		int min = A[0];
		for (int i = 1; i < A.length; i++) {
			if (A[i] < min) {
				min = A[i];
			}
		}
		return min;
	}

	/**
	 * 同时 找到最大最小值，用3*n/2次比较就行了，而不是2n
	 * 思路是像擂台赛一样，两两一组，赢的和赢的比，输的和输的比，赢的不和输的比，所以节约了一些。
	 */
	public static MinMax minMax(int[] A) {
		if (A.length < 2) {
			return new MinMax(A[0], A[0]);
		}
		int min, max;
		if (A[0] > A[1]) {
			max = A[0];
			min = A[1];
		} else {
			min = A[0];
			max = A[1];
		}
		int i = 2;
		try {
			for (; i < A.length; i += 2) {
				if (A[i] > A[i + 1]) {
					if (A[i] > max) {
						max = A[i];
					}
					if (A[i + 1] < min) {
						min = A[i + 1];
					}
				} else {
					if (A[i] < min) {
						min = A[i];
					}
					if (A[i + 1] > max) {
						max = A[i + 1];
					}
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// 这是因为数组长度是奇数时，最后一次i+1取不到，这里把最后一个i的大小判断
			if (A[i] > max) {
				max = A[i];
			} else {
				if (A[i] < min) {
					min = A[i];
				}
			}
		}
		return new MinMax(min, max);
	}

	public static class MinMax {
		public int min;
		public int max;

		public MinMax(int min, int max) {
			this.max = max;
			this.min = min;
		}
	}
}
