package org.foresee.Algorithm.dynamic;

import java.util.ArrayList;
import java.util.List;

/**
 * 给定N中物品和一个背包。物品i的重量是Wi,其价值位Vi ，背包的容量为C。问应该如何选择装入背包的物品， 使得转入背包的物品的总价值为最大？
 * 在选择物品的时候，对每种物品i只有两种选择，即装入背包或不装入背包。不能讲物品i装入多次，也不能只装入物品的一部分。 因此，该问题被称为0-1背包问题
 */
public class Knapsack01 {
	public static void main(String[] args) {
		List<Goods> goodsList = new ArrayList<>();
		goodsList.add(new Goods(10, 60, "g1"));
		goodsList.add(new Goods(20, 100, "g2"));
		goodsList.add(new Goods(30, 120, "g3"));
		int capacity = 50;
		Goods[] goods = goodsList.toArray(new Goods[goodsList.size()]);
		// List<Goods> selected=maxValueBug(goods, capacity);
		// for(int i=0; i<selected.size(); i++){
		// System.out.println(selected.get(i).name);
		// }
		List<Goods> selected = maxValue(goods, capacity);
		for (int i = 0; i < selected.size(); i++) {
			System.out.println(selected.get(i).name);
		}
		maxValue2(goods, capacity);
		maxValue3(goods, capacity);
	}
	/**
	 * 还是网上的思路，进一步减少空间，因为下一列计算没用到前一列的，所以一行就够了
	 * 构造结果时，会随机用到之前的结果，维护一个Map，键是当前容量，值是物品列表，每一行表示它是跳变的容量
	 * 比如上一行是10，这一行是20，说明重量10~19的物品列表都是10的值存着
	 * 很难构造结果，唉，往后看书了，不写了
	 */
	public static void maxValue3(Goods[] goods, int capacity) {
		int n = goods.length;
		int[] v = new int[capacity + 1]; // 第一行代表历史数据，第二行代表这次计算
		for (int j = 0; j < capacity; j++) {
			v[j] = 0;
		}
		for (int i = 1; i <= n; i++) {
			for (int j = capacity; j >= 1; j--) { // 这里要从后向前，因为后边用到前边的，不能先该前边
				if (j >= goods[i - 1].weight) {
					int addGoodsJ = v[j - goods[i - 1].weight] + goods[i - 1].value;
					if (addGoodsJ > v[j]) {
						v[j] = addGoodsJ;
					}
				}
			}
		}
		System.out.println(v[capacity]);
	}

	// 还是网上的思路，时间复杂度和下边一样，减少空间，因为下一次计算只用到上一行的，所以两行就够了
	public static void maxValue2(Goods[] goods, int capacity) {
		int n = goods.length;
		int[][] v = new int[2][capacity + 1]; // 第一行代表历史数据，第二行代表这次计算
		for (int j = 0; j < capacity; j++) {
			v[0][j] = 0;
		}
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= capacity; j++) {
				if (j < goods[i - 1].weight) {
					v[1][j] = v[0][j];
				} else {
					int addGoodsJ = v[0][j - goods[i - 1].weight] + goods[i - 1].value;
					if (addGoodsJ > v[0][j]) {
						v[1][j] = addGoodsJ;
					} else {
						v[1][j] = v[0][j];
					}
				}
			}
			for (int j = 1; j <= capacity; j++) {// 每一行结束，把新计算的赋给上一行做下次的历史数据
				v[0][j] = v[1][j];
			}
		}
		System.out.println(v[1][capacity]);
	}

	/**
	 * 网上的思路：这是真动态规划的思路，把问题缩小成更小的问题，物品和背包容量都是逐渐增大的。
	 * 设v[i][j]表示有前i个物品且背包容量为j时的最大价值，那么i=0，j=任意时，什么都没有，
	 * 如果i=任意，j=0时，什么都装不下，这是初始条件。当i=1，j=1时，考虑j这个1的容量能不能承受i=1这个物品的重量，
	 * 如果不能，那j=1只能和j=0时一样装那么些东西，如果能装下，两种情况，装它或不装它
	 * 装i这个物品占据背包goods[i].weight的容量，就应该去查询少了这些容量时的最大物品价值，在算上新装的i，得到装的价值
	 * 如果不装，那么就和不存在第i个物品时一样，价值是v[i-1][j]， 比较这两种情况，决定装或者不装。
	 */
	public static List<Goods> maxValue(Goods[] goods, int capacity) {
		int n = goods.length;
		int[][] v = new int[n + 1][capacity + 1];
		for (int i = 0; i < n; i++) {
			v[i][0] = 0;
		}
		for (int j = 0; j < capacity; j++) {
			v[0][j] = 0;
		}
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= capacity; j++) {
				if (j < goods[i - 1].weight) {
					v[i][j] = v[i - 1][j];
				} else {
					v[i][j] = Integer.max(v[i - 1][j], v[i - 1][j - goods[i - 1].weight] + goods[i - 1].value);
				}
			}
		}
		List<Goods> select = new ArrayList<>();
		while (capacity > 0 && n > 0) {
			if (v[n][capacity] > v[n - 1][capacity]) {
				select.add(goods[n - 1]);
				capacity -= goods[n - 1].weight;
				n--;
			} else {
				n--;
			}
		}
		return select;
	}

	// 下面这个程序有bug，只从select中删去一个并查看能不能放得下新的商品，如果新的商品需要去掉两个才能放下，就没考虑
	/**
	 * 输入时商品数组，和背包容量，算法思路： 维护一个按单位重量价值递减排序的选择列表，扫描商品数组，如果下一个商品重量小于空余容量，直接放进背包；
	 * 如果下一个不小于空余容量，那么尝试
	 */
	public static List<Goods> maxValueBug(Goods[] goods, int capacity) {
		List<Goods> select = new ArrayList<>();
		int n = goods.length;
		int empty = capacity;
		for (int i = 0; i < n; i++) {
			if (goods[i].weight <= empty) {
				select.add(goods[i]);
				empty -= goods[i].weight;
			} else {
				for (int j = 0; j < select.size(); j++) {
					int tempCapacity = empty + select.get(j).weight;
					if (goods[i].weight <= tempCapacity) {
						if (goods[i].value > select.get(j).value) {
							empty += select.remove(j).weight;
							select.add(goods[i]);
							empty -= goods[i].weight;
							break;
						}
					}
				}
			}
		}
		return select;
	}

	public static class Goods {
		public int weight;
		public int value;
		public String name;

		public Goods(int weight, int value, String name) {
			super();
			this.weight = weight;
			this.value = value;
			this.name = name;
		}

	}
}
