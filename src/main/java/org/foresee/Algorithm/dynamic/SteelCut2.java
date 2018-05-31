package org.foresee.Algorithm.dynamic;

/**
 * 对SteelCut的扩充，切割过程需要一定的成本，比如消耗1，确定切割方案。
 * 对于下边的price，无消耗时2+2+6最大，但消耗2，现在10为最大了。
 */
public class SteelCut2 {
	public static void main(String[] args) {
		int n=10;
		CutResult result=cutRod(price, n);
		String cut="";
		while(n>0){
			cut+=result.cut[n]+"+";
			n=n-result.cut[n];
		}
		System.out.println("ex max total: "+result.total+", cut: "+cut);
	}
	//价格调整为原来的三倍
	public static final int[] price = new int[] { 3,15,24,27,30,51,51,60,72,80 };
	public static final int CUT_CONSUME=1;
	public static CutResult cutRod(int[] price, int n) {
		int[] r=new int[n+1];
		CutResult res=new CutResult();
		res.cut=new int[n+1];
		r[0]=0;
		int q=-1;
		for(int i=1; i<=n; i++){
			q=-1;
			for(int j=1; j<=i; j++){
				int temp=price[j-1]+r[i-j];
				if(j!=1 && j!=i){
					temp=temp-CUT_CONSUME;
				}
				if(q<temp){
					q=temp;
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
}
