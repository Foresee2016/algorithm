package org.foresee.Algorithm.practice;

//403. Frog Jump
public class FrogJump {
	public static void main(String[] args) {
		int[] stones = { 0, 1, 3, 5, 6, 8, 12, 17 };
		boolean res = canCross(stones);
		System.out.println(res);
		int[] stones2 = { 0, 1, 2, 3, 4, 8, 9, 11 };
		boolean res2 = canCross(stones2);
		System.out.println(res2);
	}

	//递归算法超时 Time Limit Exceeded
	public static boolean canCross(int[] stones) {
		if (stones.length < 2 || stones[1] != 1) {
			return false;
		}
		int jump = 1, loc = 1;
		return canCrossRecursion(stones, loc, jump);
	}

	public static boolean canCrossRecursion(int[] stones, int loc, int jump) {
		if (loc == stones.length - 1) {
			return true;
		}
		for (int i = loc + 1; i < stones.length; i++) {
			int dist = stones[i] - stones[loc];
			if (dist <= jump + 1) {
				if (dist >= jump - 1) {
					if (canCrossRecursion(stones, i, dist)) {
						return true;
					}
				}
				continue;
			}
			return false;
		}
		return false;
	}
}
