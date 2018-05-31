package org.foresee.Algorithm.sort;

public class CountingSort {
	/**
	 * 计数排序（不是原址排序）：在输入数组A[1..n]都是正整数，可以有重复的数，且最大数值较小（与n同阶）时，可以使用
	 * 假设k是数组最大值，过程中会构造C[]，其大小为k，所以比较占空间。 时间复杂度为O(n)
	 */
	public static void main(String[] args) {
		int[] data = new int[] { 2, 5, 3, 0, 2, 3, 0, 3 };
		int[] res = CountingSort.sort(data, 5);
		for (int i = 0; i < res.length; i++) {
			System.out.println(res[i]);
		}
	}

	/**
	 * k是待排序数组中的最大值
	 * 计数排序思路：构造一个数组C，C的长度是k+1。
	 * 计A[]中元素出现的次数，并相应保存在C中，然后C中向左求积分，这样得到的就是小于A[j]中对于元素出现的次数。
	 * 这样把A[j]放到该次数的位置就行了。注意出现次数不一定是1，所以每次放完减一，保证下个同值的数放到前一个位置。 
	 */
	public static int[] sort(int[] A, int k) {
		int[] C = new int[k + 1];
		for (int i = 0; i < k + 1; i++) {
			C[i] = 0;
		}
		for (int j = 0; j < A.length; j++) {
			C[A[j]] = C[A[j]] + 1;
		} // 现在C[i]中存着等于i的元素出现的次数
		for (int i = 1; i < k + 1; i++) {
			C[i] = C[i] + C[i - 1];
		} // 现在C[i]中存着小于等于i的元素出现的次数
		int[] B = new int[A.length];
		for (int j = A.length - 1; j >= 0; j--) {
			B[C[A[j]] - 1] = A[j];
			C[A[j]] = C[A[j]] - 1;
		}
		return B;
	}
}
