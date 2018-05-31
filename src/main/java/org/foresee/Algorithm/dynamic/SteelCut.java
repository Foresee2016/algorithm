package org.foresee.Algorithm.dynamic;

/**
 * 钢条切割问题，已知各尺寸钢条对应的价格（尺寸均为整数，比如1,2,3,...），给定一根长度为n的钢条，求如何切割，收益最大。
 * 长度为n的钢条有exp(2,n-1)种切割方法，用加号表示切割方案，比如7=2+2+3 收益r(n)=max(p(n), r(1)+r(n-1),
 * r(2)+r(n-2), r(n-1)+r(1)), p(n)对应不切割的收益 简化版r(n)=max(p(i),
 * r(i-1))左边i长度不切割，剩下取切割最大值，这样一次只再进行一个递归
 */
public class SteelCut {
	public static void main(String[] args) {
		int totol = cutRod(price, 10);
		System.out.println("max total: " + totol + ", counter: " + counter);
		counter = 0;
		totol = memoizedCutRod(price, 10);
		System.out.println("memoized max total: " + totol + ", counter: " + counter);
		counter=0;
		totol = bottomUpCutRod(price, 10);
		System.out.println("bottom up max total: " + totol + ", counter: " + counter);
		int n=10;
		CutResult result=exBottomUpCutRod(price, n);
		String cut="";
		while(n>0){
			cut+=result.cut[n]+"+";
			n=n-result.cut[n];
		}
		System.out.println("ex max total: "+result.total+", cut: "+cut);
	}

	public static final int[] price = new int[] { 1, 5, 8, 9, 10, 17, 17, 20, 24, 26 };
	public static int counter = 0;
	public static CutResult exBottomUpCutRod(int[] p, int n) {
		int[] r=new int[n+1];
		CutResult res=new CutResult();
		res.cut=new int[n+1];
		r[0]=0;
		int q=-1;
		for(int i=1; i<=n; i++){
			q=-1;
			for(int j=1; j<=i; j++){
				if(q<(p[j-1]+r[i-j])){
					q=p[j-1]+r[i-j];
					res.cut[i]=j;
				}
			}
			r[i]=q;
		}
		res.total=q;
		return res;
	}
	public static class CutResult {
		public int[] cut;
		public int total;
	}
	/**
	 * 一样是带备忘的，从底下开始算，长度为1,2,3，直到算出长度n的最大收益，
	 * 跟带备忘的递归有相同运行时间，只是迭代比递归好一些 
	 */
	public static int bottomUpCutRod(int[] p, int n) {
		int[] r=new int[n+1];
		r[0]=0;
		for(int i=1; i<=n; i++){
			int q=-1;
			for(int j=1; j<=i; j++){
				q=Integer.max(q, p[j-1]+r[i-j]);
				counter++;
			}
			r[i]=q;
		}
		return r[n];
	}
	/**
	 * 动态规划算法，带备忘的自顶而下的递归，每次计算完该段长度最优价格后记在数组里，之后直接查而不是再计算
	 * 典型的空间换时间，时间复杂度是n*n，是等差数列n+(n-1)+(n-2)+...+1求和
	 */
	public static int memoizedCutRod(int[] p, int n) {
		int[] r = new int[n];
		for (int i = 0; i < n; i++) {
			r[i] = -1;
		}
		return memoizedCutRod(p, n, r);
	}

	public static int memoizedCutRod(int[] p, int n, int[] r) {
		counter++;
		if (n == 0) {
			return 0;
		}
		if (r[n - 1] >= 0) {
			return r[n - 1];
		}		
		int q = -1;
		for (int i = 0; i < n; i++) {
			q = Integer.max(q, p[i] + memoizedCutRod(p, n - i - 1, r));
		}
		r[n - 1] = q;
		return q;
	}

	/**
	 * 朴素递归算法，运行时间2的n次方，性能差，因为重复求解相同子问题。
	 */
	public static int cutRod(int[] p, int n) {
		if (n == 0) {
			return 0;
		}
		int q = -1;
		counter++;
		for (int i = 0; i < n; i++) {
			q = Integer.max(q, p[i] + cutRod(p, n - i - 1));
		}
		return q;
	}
}
