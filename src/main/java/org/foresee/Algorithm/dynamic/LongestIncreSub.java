package org.foresee.Algorithm.dynamic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 给定长度为n的序列，找其中最长单调递增子序列。
 * 除了穷举法，我想不出什么方法，只能百度了...还是不入门。
 * 有两个思路没看懂，没记。
 */
public class LongestIncreSub {
	public static void main(String[] args) {
//		int[] A = new int[] { 1, 5, 3, 4, 6, 9, 15, 10, 11, 20 };
		int[] A = new int[] { 3, 9, 1, 7, 6, 2, 5, 10, 11, 8 };
		Integer[] res1=bruteForce(A);
		System.out.println(res1.length);
		for(int i=0; i<res1.length; i++){
			System.out.print(res1[i]+",");
		}
		System.out.println();
		
//		int[] res2=longestIncrese(A);
//		System.out.println(res2.length);
//		for(int i=0; i<res2.length; i++){
//			System.out.print(res2[i]+",");
//		}
		System.out.println();
		int[] res3=longestIncrese2(A);
		System.out.println("max: "+res3.length);
		for(int i=0; i<res3.length; i++){
			System.out.print(res3[i]+",");
		}
	}
	/**
	 * 思路3：能达到nlog2(n)的时间复杂度，计算出最长递增子数组的长度，但求出完整子数组有些麻烦，没想通。
	 */
	/**
	 * 思路2：动态规划的思想，从左到右构建最大递增子数组，假如已知了前边i-1个元素对应的最长递增子数组，
	 * 现在要求后边的一个，第i个 ，如果A[i]比前边的某一个元素A[j]大，那么到第i个元素就至少是A[j]位置最大递增子数组长度+1，
	 * 遍历前边每一个j，取能达到的最大长度。
	 * 时间复杂度n*n，但后边项是n的，比思路1的nlog2(n)好一些
	 */
	public static int[] longestIncrese2(int[] A) {
		int n=A.length;
		int[] longest=new int[n];
		for(int i=0; i<n; i++){
			longest[i]=1;
		}
		for(int i=1; i<n; i++){
			for(int j=0; j<i; j++){
				if(A[j]<A[i] && longest[i]<longest[j]+1){
					longest[i]=longest[j]+1;
				}
			}
		}
		// 找出最大值max，方便建立长度为max的数组，耗费时间为n
		int max=0;
		for(int i=0; i<n; i++){
			if(longest[i]>max){
				max=longest[i];
			}
		}
		/**
		 * 构建结果数组，思路：从右边开始找，等于max的值，这个值就是达到最大递增子数组的最末尾一个
		 * 然后max--，再去前面找，达到最大递增子数组max。
		 * 比如子数组最长是8，那么让子数组达到第8个位置的，就是该子数组最末尾一个，之后该在前面找7，
		 * 前边让子数组达到第7个位置的那个元素在哪里。
		 * 假如有多个让子数组达到7的，也应该取后一个，因为它在后面却没有让子数组+1，说明它比前边的末尾更小（或相等），
		 * 更容易让后边找到递增数组。比如有 .... 9,7，....中间这两个元素，都让子数组达到5了，而7这个元素给后边
		 * 递增原素提高的门槛更低，所以就选它。即选后面的。
		 * 注：构建结果数组应该是可以在得到赋值longest里做（我没试，感觉可以），但不知道最后要多长的数组，
		 * 只能构造长度为n的数组，浪费了一些空间，后边这样多扫描了一次原数组，多耗费时间n，但节约空间。
		 */		 
		int [] res=new int[max];
		for(int i=n-1; i>=0 && max>0; i--){
			if(longest[i]==max){
				res[max-1]=A[i];
				max--;
			}
		}
		return res;
	}
	/**
	 * 第一种思路很巧妙，先对A排序得到B，然后求A和B的最大公共子数组，有重复元素时不能用
	 * 时间复杂度n*n 
	 */
	public static int[] longestIncrese(int[] A) {
		int[] B=A.clone();
		Arrays.sort(B);
		return lcsLength(A, B);
	}
	public static int[] lcsLength(int[] x, int[] y) {
		int m=x.length, n=y.length;
		int[][] c=new int[m+1][n+1];
		// 最左一列和最上一行赋0
		for(int i=1; i<=m; i++){
			c[i][0]=0;
		}
		for(int j=0; j<=n; j++){
			c[0][j]=0;
		}
		for(int i=1; i<=m; i++){
			for(int j=1; j<=n; j++){
				if(x[i-1]==y[j-1]){
					c[i][j]=c[i-1][j-1]+1;
				}else if (c[i-1][j]>=c[i][j-1]) {
					c[i][j]=c[i-1][j];
				}else {
					c[i][j]=c[i][j-1];
				}
			}
		}
		List<Integer> integers=new ArrayList<>();
		integers=makeSubseq(x, y, c, m, n, integers);
		int[] res=new int[integers.size()];
		for(int i=0; i<integers.size(); i++){
			res[i]=integers.get(i);
		}
		return res;
	}
	public static List<Integer> makeSubseq(int[] x, int[] y, int[][] c, int i, int j, List<Integer> integers) {
		if(i==0 || j==0){
			return integers;
		}
		if(c[i][j]> c[i-1][j] && c[i][j]>c[i][j-1] && c[i][j]>c[i-1][j-1]){
			// 当前i,j比左右斜上都大，说明这个位置有跳变，那么这个i，j是子数组元素
			integers=makeSubseq(x, y, c, i-1, j-1, integers);
			integers.add(x[i-1]);
			return integers;
		}else if (c[i][j]==c[i-1][j]) {
			return makeSubseq(x, y, c, i-1, j, integers);
		}else{
			return makeSubseq(x, y, c, i, j-1, integers);
		}
	}
	/**
	 * 暴力求解，穷举所有可能的组合。效率差，渐进时间是n的指数，但程序很好理解也好写。 
	 */
	public static Integer[] bruteForce(int[] A) {
		int n = 1 << A.length; // 计算2的x.length次方
		int m = 1;
		Integer[] res = new Integer[] { A[0] };
		while (m < n) {
			m++;
			List<Integer> integers = new ArrayList<>();
			for (int i = 0; i < A.length; i++) {
				if ((m & (1 << i)) != 0) { // 如果m对应的第i位是1，那么此次组合应该加上当前元素
					integers.add(A[i]);
				}
			}
			Integer[] z = integers.toArray(new Integer[integers.size()]);
			if (isIncrese(z)) {
				if (z.length > res.length) {
					res = z;
				}
			}
		}
		return res;
	}

	/**
	 * 判断A是不是单调递增的
	 */
	public static boolean isIncrese(Integer[] A) {
		for (int i = 0; i < A.length - 1; i++) {
			if (A[i + 1] < A[i]) {
				return false;
			}
		}
		return true;
	}
}
