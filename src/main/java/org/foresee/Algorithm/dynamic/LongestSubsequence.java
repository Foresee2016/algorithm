package org.foresee.Algorithm.dynamic;

import java.util.ArrayList;
import java.util.List;

/**
 * 定义DNA相似度： 定义1.如果一个串转换到另一个串所需的操作很少，那么两个DNA串是相似的。
 * 定义2.寻找第三个串S3，它的所有碱基都出现在S1和S2中，且在三个串中出现的顺序都相同，但在S1和S2中不要求连续出现，
 * 能找到的S3越长，就可以认为S1和S2的相似度越高。
 * 
 * 定义2可以归结为最长公共子序列问题，将给定序列中零个或多个元素去掉之后得到的结果。 形式化定义：给定一个序列X={x1,x2,...xm},
 * 另一个序列Z={z1,z2,...zk}满足存在一个严格递增的X的下标序列 {i1,i2,...ik}对所有j=1,2,...k满足xij=zj。
 * 例如Z={B,C,D,B}是X={A,B,C,B,D,A,B}的子序列，对应的下标序列是{2,3,5,7}
 * 如果Z既是X的子序列又是Y的子序列，则Z是X和Y的公共子序列 最长公共子序列问题(Longest common subsequence problem,
 * LCS问题)
 */
public class LongestSubsequence {
	public static void main(String[] args) {
		String[] X = new String[] { "A", "B", "C", "B", "D", "A", "B" };
		String[] Y = new String[] { "B", "D", "C", "A", "B", "A" };
		String[] result = bruteForce(X, Y);
		System.out.println(result.length);
		for (int i = 0; i < result.length; i++) {
			System.out.print(result[i] + ",");
		}
		System.out.println();
//		String[] X2 = new String[] { "A", "B", "C" }; // 空值测试，应该没有公共子序列
//		String[] Y2 = new String[] { "D", "E", "F" };
//		String[] result2 = bruteForce(X2, Y2);
//		System.out.println(result2.length);
//		for (int i = 0; i < result2.length; i++) {
//			System.out.print(result2[i] + ",");
//		}
//		System.out.println();
		LcsResult result3=lcsLength(X, Y);
		System.out.println(result3.c[X.length][Y.length]);
		String result3Seq=makeSubseq(X, Y, result3.b);
		System.out.println(result3Seq);
		String result3Seq2=makeSubseq2(X, Y, result3.c);
		System.out.println(result3Seq2);
	}
	public static final int OBLIQUE=0;
	public static final int UP=1;
	public static final int LEFT=2;
	/**
	 * 递归求解思路：X，Y两个序列X={x1,x2,...xm},Y={y1,y2,...yn}，如果xm=yn，那么应该
	 * 求X(m-1)和Y(n-1)的最长子序列，并把zk=xm放到结果的末尾
	 * 如果xm!=yn，那么应该求X和Y(n-1)，X(m-1)和Y这两种情况的最长子序列，取长的那个。
	 * 最优子序列c[i][j]这样有三种情况：（i表示x的位置，j表示y中位置）
	 * 1.如果i=0或者j=0，那么c[i][j]=0
	 * 2.如果xi=yj，那么该元素是最终子序列元素，i，j都向前推进一个
	 * 3.如果xi！=yj，那么继承之前大的那个，即c[i][j-1]和c[i-1][j]中大的一个。
	 * 程序实现是自底向上的，而不是带备忘的自顶向下
	 * 时间m*n
	 */
	public static LcsResult lcsLength(String[] x, String[] y) {
		int m=x.length, n=y.length;
		int[][] b=new int[m+1][n+1], c=new int[m+1][n+1];
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
					b[i][j]=OBLIQUE;
				}else if (c[i-1][j]>=c[i][j-1]) {
					c[i][j]=c[i-1][j];
					b[i][j]=UP;
				}else {
					c[i][j]=c[i][j-1];
					b[i][j]=LEFT;
				}
			}
		}
		return new LcsResult(b, c);
	}
	/**
	 * 不使用b完全可以，这样节约一半的空间，完全使用c来构造最后的子序列字符串 
	 */
	public static String makeSubseq2(String[] x, String[] y, int[][] c) {
		String str="";
		return makeSubseq2(x, y, c, x.length, y.length, str);
	}
	public static String makeSubseq2(String[] x, String[] y, int[][] c, int i, int j, String str) {
		if(i==0 || j==0){
			return str;
		}
		if(c[i][j]> c[i-1][j] && c[i][j]>c[i][j-1] && c[i][j]>c[i-1][j-1]){
			// 当前i,j比左右斜上都大，说明这个位置有跳变，那么这个i，j是子数组元素
			str=makeSubseq2(x, y, c, i-1, j-1, str);
			str+=x[i-1];
			return str;
		}else if (c[i][j]==c[i-1][j]) {
			return makeSubseq2(x, y, c, i-1, j, str);
		}else{
			return makeSubseq2(x, y, c, i, j-1, str);
		}
	}
	/**
	 * 使用算法结果b中箭头方向求子序列的字符串 
	 */
	public static String makeSubseq(String[] x, String[] y, int[][] b) {
		String str="";
		return makeSubseq(x, y, b, x.length, y.length, str);
	}
	public static String makeSubseq(String[] x, String[] y, int[][] b, int i, int j, String str) {
		if(i==0 || j==0){
			return str;
		}
		if(b[i][j]==OBLIQUE){
			str=makeSubseq(x, y, b, i-1, j-1, str);
			str+=x[i-1];
			return str;
		}else if(b[i][j]==UP){
			return makeSubseq(x, y, b, i-1, j, str);
		}else{
			return makeSubseq(x, y, b, i, j-1, str);
		}
	}
	public static class LcsResult {
		int[][] b;
		int[][] c;
		public LcsResult(int[][] b, int[][] c) {
			super();
			this.b = b;
			this.c = c;
		}		
	}

	/**
	 * 暴力求解法，穷举X的所有子序列，验证是否是Y的子序列，找出最长的一个（因为可能不止一个）。
	 * 时间复杂度是X.length的指数阶，对较长序列不适用。
	 * 思路：X的所有子序列是全组合，考虑有X.length长度的二进制序列，每次加一个，对应位是1就可以加上该元素。
	 * 例如四个元素，0001表示只有第四个元素，是一种情况；加1,0010是第二种情况，只有第三个元素，
	 * 再加1,0011，有第三四个元素，是第三种情况，以此类推，直到1111有全部四个元素。超过时停止
	 */
	public static String[] bruteForce(String[] X, String[] Y) {
		int n = 1 << X.length; // 计算2的x.length次方
		int m = 1;
		String[] res = new String[0];
		while (m < n) {
			m++;
			List<String> strings = new ArrayList<>();
			for (int i = 0; i < X.length; i++) {
				if ((m & (1 << i)) != 0) { // 如果m对应的第i位是1，那么此次组合应该加上当前元素
					strings.add(X[i]);
				}
			}
			String[] z = strings.toArray(new String[strings.size()]);
			if (isSubseq(z, Y)) {
				if (z.length > res.length) {
					res = z;
				}
			}
		}
		return res;
	}

	/**
	 * 验证Z是否是Y的子序列
	 */
	public static boolean isSubseq(String[] z, String[] y) {
		if (z.length > y.length) {
			return false;
		}
		int j = -1;
		for (int i = 0; i < z.length; i++) {
			j = strPos(z[i], y, j + 1);
			if (j == -1) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 求str在数组y的位置（从start开始），返回-1如果没有找到
	 */
	public static int strPos(String str, String[] y, int start) {
		for (int i = start; i < y.length; i++) {
			if (str == y[i]) {
				return i;
			}
		}
		return -1;
	}
}
