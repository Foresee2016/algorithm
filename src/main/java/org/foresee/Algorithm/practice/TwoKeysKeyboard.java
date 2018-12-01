package org.foresee.Algorithm.practice;

// LeetCode 605
public class TwoKeysKeyboard {
	public static void main(String[] args) {
		int res=minSteps(9);
		int res2=minSteps2(9);
		System.out.println(res+","+res2);
		
	}

	public static int minSteps(int n) {
		int[] steps=new int[n+1];
		steps[1]=0;
		int m=2;
		while(m<=n){
			int p=1, min=Integer.MAX_VALUE;
			while(p<m){
				if(m%p==0){
					min=Integer.min(m/p+steps[p], min);
				}
				p++;
			}
			steps[m]=min;
			m++;
		}
		return steps[n];
	}
	/**
	 * 官网的解法，也是想到了分解因子，但比我的聪明很多。
	 * 他是一次把一个因子全提出来，比如约2，就一直约2直到不能再约
	 * O(根号(n))时间，很强
	 */
	public static int minSteps2(int n) {
		int ans = 0, d = 2;
        while (n > 1) {
            while (n % d == 0) {
                ans += d;
                n /= d;
            }
            d++;
        }
        return ans;
	}
}
