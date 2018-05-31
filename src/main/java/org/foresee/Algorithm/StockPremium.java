package org.foresee.Algorithm;

/**
 * 算法导论上的一个介绍分治策略的例子，给出几天的股票价格，寻找最大差价，即哪天买入哪天卖出，收益最大
 */
public class StockPremium {
	public static void main(String[] args) {
		int[] data = new int[] { 100, 113, 110, 85, 105, 102, 86, 63, 81, 101, 94, 106, 101, 79, 94, 90, 97 };
		// int[] data = new int[] { 200, 190, 175, 165, 153, 142, 133, 113, 111,
		// 100, 94, 86, 71, 69, 54, 32, 20 };
		StockResult result = StockPremium.bruteForce(data);
		System.out.println("buy: " + result.buy + ", sell: " + result.sell + ", premium: " + result.premium);
		StockResult result2 = StockPremium.divideConquer(data);
		System.out.println("buy: " + result2.buy + ", sell: " + result2.sell + ", premium: " + result2.premium);
		StockResult result3 = StockPremium.quickFindMaxPremium(data);
		System.out.println("buy: " + result3.buy + ", sell: " + result3.sell + ", premium: " + result3.premium);
	}

	/**
	 * 最佳解法
	 * 思路：从左到右，记录到目前为止，计算出的最大子数组，还有以当前计算位置元素为右边界的最大数组（称为边界最大子数组）。
	 * 每当计算位置向右移动一个，边界最大子数组可能有两种情况，一是当前位置的元素值比前一个边界最大子数组还大，
	 * 那新的边界最大子数组就是当前位置元素，否则，应该加上当前位置的元素值。
	 * 最大子数组也只有两种情况，一是比当前边界最大子数组小，那就应该等于当前边界最大子数组，否则保持原样。
	 * 想出这算法的人真是聪明，计算代价是线性时间，求差值n次，计算也为n次，总共2n
	 */
	public static StockResult quickFindMaxPremium(int[] data) {
		int[] A = new int[data.length - 1]; // 生成每天价格的差值数组
		for (int i = 0; i < A.length; i++) {
			A[i] = data[i + 1] - data[i];
		}
		int max = A[0], boundary = A[0], left = 0, right = 0, bleft = 0;
		for (int i = 1; i < A.length; i++) {
			if ((A[i] + boundary) >= A[i]) {
				boundary += A[i];
			} else {
				boundary = A[i];
				bleft = i;
			}
			if (max < boundary) {
				max = boundary;
				left = bleft;
				right = i;
			}
		}
		if(max<0){	// 最大收益小于0，无收益。
			return new StockResult(0, 0, 0);
		}
		return new StockResult(left, right + 1, max);
	}

	/**
	 * 综合两种方法，按计算时间分界，决定用哪一种。 bruteForce() 计算时间代价是 n*(n-1)/2，即从n中选2个
	 * divideConquer() 分治计算时间代价是 nlog2(n)，算是计算每天差价的n-1 按 nlog2(n)+n-1 =
	 * n*(n-1)/2， n约等于8，所以8之前用bruteForce，之后用分治
	 */
	public static StockResult findMaxPremium(int[] data) {
		if (data.length <= 8) {
			return bruteForce(data);
		} else {
			return divideConquer(data);
		}
	}

	/**
	 * 暴力求解法，遍历所有可能的买入和卖出日期，找到最大收益的组合。 如果价格一直在跌，没有大于0的收益，返回都是0
	 */
	public static StockResult bruteForce(int[] A) {
		int buy = 0, sell = 0, premium = 0;
		for (int i = 0; i < A.length - 1; i++) {
			for (int j = i + 1; j < A.length; j++) {
				if ((A[j] - A[i]) > premium) {
					buy = i;
					sell = j;
					premium = A[j] - A[i];
				}
			}
		}
		return new StockResult(buy, sell, premium);
	}

	/**
	 * 问题变换，考虑每天的价格变化，计入数组A，转化为寻找A的和最大的非空连续子数组。
	 * 这种连续子数组称为最大子数组。使用分治策略寻找一个最大子数组（因为可能不唯一） 从尽量中间位置把数组分为左右两半，最大子数组可能的三种情况：
	 * 1.完全位于子数组A[low..mid]中 2.完全位于子数组A[mid+1..high]中
	 * 3.跨越中点，因此low<=i<=mid<j<=high 1.2.前两个问题直接相当于递归原问题，
	 * 3.后一个问题，因为必须跨中点只需找出型如A[i..mid]和A[mid+1..j]的最大子数组然后合并，线性时间完成
	 */
	public static StockResult divideConquer(int[] data) {
		int[] A = new int[data.length - 1]; // 生成每天价格的差值数组
		for (int i = 0; i < A.length; i++) {
			A[i] = data[i + 1] - data[i];
		}
		StockResult result = findMaxSubarray(A, 0, A.length - 1); // high要取A.length-1因为从零开始计
		if (result.premium < 0) {
			// 如果收益是负的，返回0,0,0，这能在价格一直跌的时候回0而不是最小损失的那一天
			return new StockResult(0, 0, 0);
		}
		return result;
	}

	protected static StockResult findMaxSubarray(int[] A, int low, int high) {
		if (low == high) {
			return new StockResult(low, high + 1, A[low]);
			// 加1因为A数组是后一天价格和前一天价格差值，表达为哪天买和哪天卖
		}
		int mid = Math.floorDiv(low + high, 2);
		StockResult left = findMaxSubarray(A, low, mid);
		StockResult right = findMaxSubarray(A, mid + 1, high);
		StockResult cross = findMaxCrossingSubarray(A, low, mid, high);
		return StockResult.maxStockResult(left, right, cross);
	}

	/**
	 * 找跨越中点的最大子数组，先从中点向左找到最大收益，再向右找到最大收益，加和就行了。 花费n的一次方渐进时间，n为数组A的长度
	 */
	protected static StockResult findMaxCrossingSubarray(int[] A, int low, int mid, int high) {
		int leftSum = Integer.MIN_VALUE, sum = 0, maxLeft = mid;
		for (int i = mid; i >= low; i--) {
			sum = sum + A[i];
			if (sum > leftSum) {
				leftSum = sum;
				maxLeft = i;
			}
		}
		int rightSum = Integer.MIN_VALUE;
		sum = 0;
		int maxRight = mid + 1;
		for (int j = mid + 1; j <= high; j++) {
			sum = sum + A[j];
			if (sum > rightSum) {
				rightSum = sum;
				maxRight = j;
			}
		}
		return new StockResult(maxLeft, maxRight + 1, leftSum + rightSum);
	}

	public static class StockResult {
		public int buy;
		public int sell;
		public int premium;

		public StockResult(int buy, int sell, int premium) {
			this.buy = buy;
			this.sell = sell;
			this.premium = premium;
		}

		public static StockResult maxStockResult(StockResult... results) {
			int max = 0;
			for (int i = 1; i < results.length; i++) {
				if (results[i].premium > results[max].premium) {
					max = i;
				}
			}
			return results[max];
		}
	}
}
