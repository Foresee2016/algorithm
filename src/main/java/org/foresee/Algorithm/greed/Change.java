package org.foresee.Algorithm.greed;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 找零问题，有25美分，10美分，5美分和1美分，用最少的硬币找n美分零钱
 *
 */
public class Change {
	public static final int[] COINS = { 25, 10, 5, 1 };

	public static void main(String[] args) {
		List<Integer> coin = change(79);
		for (int i = 0; i < coin.size(); i++) {
			System.out.print(coin.get(i) + ",");
		}
		System.out.println();
//		final int[] coins2={1,5,6,7,8};
		final int[] coins2={1,8,7,6,5};
		int value=11;
		List<Integer> select=change(coins2, value);
		System.out.println("change for value: "+value);
		for (Integer integer : select) {
			System.out.print(integer+",");
		}
		System.out.println();
	}

	/**
	 * 贪心算法，每次找零最大的一个，减去这个继续找 这种贪心算法不能适应所有面值方案，比如硬币面值5,4,3,1，找零7，应该4+3，但会输出5+1+1
	 * 适合于面额等于（或者间隔大于）c的幂时，其中c>0，这时不会出现上边这样后边密集前边稀疏的情况。
	 */
	public static List<Integer> change(int n) {
		List<Integer> coin = new ArrayList<>();
		for (int i = 0; i < COINS.length; i++) {
			while (n > COINS[i]) {
				coin.add(COINS[i]);
				n -= COINS[i];
			}
		}
		return coin;
	}

	/**
	 * 动态规划方法，给定任意k个面额的硬币，找零n. coin是硬币面额数组，第一个面额必须是1，否则不能构成任意值。
	 * 这次是自己想出来的，Nice
	 * 思路：先找最优子结构，假定c[i][j]表示使用前i个面值硬币找零值为j时用的最少硬币数目，初始值，就是只有面值为1的，
	 * 那么不论j是几，都必须用j个硬币才可以，所以c[0]这一行初始为j了。
	 * 在有了更大面值硬币时，求c[i][j]有两种情况，一种是使用了coin[i]这个硬币，剩下j-coin[i]的价值需要找零，
	 * 到这一行前边去找对应j-coin[i]价值找零最少硬币数目，加上这次使用的coin[i]这一个，是使用的情况，
	 * 还有不使用coin[i]这个硬币，那么就等于上一行，因为上一行就是没有coin[i]时的最少数目。
	 * 所以递归式c[i][j]=MIN(c[i-1][j], c[i][j-coin[i]])
	 * 逐行往上加硬币面值行就行了。（没有先写出递归方法） 
	 */
	public static List<Integer> change(int[] coin, int value) {
		if (coin.length == 0 || coin[0] != 1) {
			return null;
		}
		int k = coin.length;
		int[][] c = new int[k][value+1];
		for(int i=0; i<k; i++){
			c[i][0]=0;
		}
		for (int j = 0; j <= value; j++) {
			c[0][j] = j; 
		}		
		for (int i = 1; i < k; i++) {
			for (int j = 1; j <= value; j++) {
				if (j < coin[i]) {
					c[i][j]=c[i-1][j];
				}else{
					int changeWithJ=1+c[i][j-coin[i]];
					c[i][j]=Integer.min(c[i-1][j], changeWithJ);
				}
			}
		}
		List<Integer> select=new Vector<>();
		k--;
		while(k>0 && value>0){
			if(c[k][value]<c[k-1][value]){
				select.add(coin[k]);
				value-=coin[k];
			}else {
				k--;
			}
		}
		while(value>0){
			select.add(coin[0]);
			value--;
		}
		return select;
	}
}
