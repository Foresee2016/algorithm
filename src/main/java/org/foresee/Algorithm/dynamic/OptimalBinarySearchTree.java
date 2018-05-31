package org.foresee.Algorithm.dynamic;

/**
 * 最优二叉搜索树问题，定义： 给定n个不同关键字的已排序（例如从小到大排序）序列K={k1,k2,..kn}，希望用这些关键字构造一棵二叉搜索树，
 * 对每个关键字ki，都有一个概率pi表示其搜索频率。
 * 有些要搜索的值可能不在K中，因此有n+1个伪关键字d0,d1,...dn表示不在K中的，其中di表示所有在ki到ki+1之间的值。
 * d0表示小于k1的值，dn表示大于Kn的值。每个伪关键字di也有对应qi表示搜索频率。 在树中di是叶节点（最末梢的节点）。
 * pi和qi满足：p1+p2+...+pn + q0+q1+q2+...+qn = 1
 * 构建二叉搜索树之后，每个节点x的搜索代价是1+depth(x)，乘对应的概率，得到整棵树的搜索期望。 伪关键字算在内。E =
 * SUM((depth(ki)+1)*pi) + SUM((depth(di)+1)*qi)
 * 程序中pi和qi取100为满值，这样用整数数组表示，调试方便一些
 * 
 * 思路：求解包含关键字ki,...kj的最优二叉搜索树时（其中i>=1,j<=n, j>=i-1，当j=i-1时子树中只包含伪关键字di-1
 * 期望搜索代价e[i,j]表示在ki,...kj的最优二叉搜索树中进行一次搜索的期望代价，所以最终希望计算e[1,n]
 * 对于j=i-1的情况，只包含di-1，所以e[i,i-1]=qi-1
 * 对于j>=i的情况，需要从ki,...,kj中选一个根节点kr，然后构造一棵包含关键字ki...kr-1的最优二叉搜索树作为左子树，
 * 以及一棵包含关键字kr+1,...,kj的最优二叉搜索树作为右子树，当它们作为子树时，每个节点深度都增加了1，而期望是和，
 * 所以增加的期望是子树节点的概率之和，w(i,j)=SUM(pi,...pj)+SUM(qi-1,...qj)
 * 因此，若kr为包含关键字ki,...kj的最优二叉搜索树的根节点，有：
 * e[i,j]=pi +e[i,r-1]+w(i,r-1) +e[r+1,j]+w(r+1,j)，因为w(i,j)=w(i,r-1)+pr+w(r+1,j),
 * 合并得：e[i,j]=e[i,r-1]+e[r+1,j]+w(i,j)
 * 所以能得到求最低期望的递归式e[i,j]=min_i<=r<=j(e[i,r-1]+e[r+1,j]+w(i,j))，特殊：j=i-1时得qi-1
 * 采用由下到上计算，用e[][]保存期望，用w[][]保存概率和
 */
public class OptimalBinarySearchTree {
	public static void main(String[] args) {
		int[] p = new int[] { 15, 10, 5, 10, 20 };
		int[] q = new int[] { 5, 10, 5, 5, 5, 10 };
		BstResult result = optBst(p, q);
		printRoot(result.root);
		System.out.println();
		BstResult result2 = optBst_BK(p, q, p.length);
		printRoot_BK(result2.root);
	}

	/**
	 * 照着下边的改的，只是把e，w，root改小一行/列，减少空间浪费。 
	 * 循环中的变量都没改，只是涉及到e，w的地方行-1，涉及root赋值的地方行列改小了1，这样节约一行一列。
	 */
	public static BstResult optBst(int[] p, int[] q) {
		int n = p.length;
		int[][] e = new int[n + 1][n + 1];
		int[][] w = new int[n + 1][n + 1];
		int[][] root = new int[n][n];
		for (int i = 1; i < n + 1; i++) {
			e[i - 1][i - 1] = q[i - 1];
			w[i - 1][i - 1] = q[i - 1];
		}
		for (int l = 1; l <= n; l++) {
			for (int i = 1; i <= n - l + 1; i++) {
				int j = i + l - 1;
				e[i - 1][j] = Integer.MAX_VALUE;
				w[i - 1][j] = w[i - 1][j - 1] + p[j - 1] + q[j];
				for (int r = i; r <= j; r++) {
					int t = e[i - 1][r - 1] + e[r][j] + w[i - 1][j];
					if (t < e[i - 1][j]) {
						e[i - 1][j] = t;
						root[i - 1][j - 1] = r;
					}
				}
			}
		}
		return new BstResult(e, root);
	}
	/**
	 * 按照节约一行一列后的root[][]格式，打印搜索树结果。可以按这个顺序真的构造一个二叉搜索树 
	 */
	public static void printRoot(int[][] root) {
		printRoot(root, 0, root.length - 1, true);
	}

	public static void printRoot(int[][] root, int i, int j, boolean isLeft) {
		if (i > j) { // 超过边界了
			if (isLeft) {
				System.out.println("leaf: d" + j);
			} else {
				System.out.println("leaf: d" + j);
			}
		} else {
			int r = root[i][j];
			System.out.println("root: k" + (r-1));
			System.out.println("left: ");
			printRoot(root, i, r - 1 - 1, true);
			System.out.println(" --up");
			System.out.println("right: ");
			printRoot(root, r + 1 - 1, j, false);
			System.out.println(" --up");
		}
	}

	/**
	 * 对应算法导论上的程序，e的行从1取到n+1，列从0取到n，这样完全对应公式，容易看懂。 w和e一样角标，root也相应改变（1到n为有效结果）
	 */
	public static BstResult optBst_BK(int[] p, int[] q, int n) {
		int[][] e = new int[n + 2][n + 1];
		int[][] w = new int[n + 2][n + 1];
		int[][] root = new int[n + 1][n + 1];
		for (int i = 1; i < n + 1; i++) {
			e[i][i - 1] = q[i - 1];
			w[i][i - 1] = q[i - 1];
		}
		for (int l = 1; l <= n; l++) {
			for (int i = 1; i <= n - l + 1; i++) {
				int j = i + l - 1;
				e[i][j] = Integer.MAX_VALUE;
				w[i][j] = w[i][j - 1] + p[j - 1] + q[j]; // 注：书里p从1开始取，但这里数组得从0
				for (int r = i; r <= j; r++) {
					int t = e[i][r - 1] + e[r + 1][j] + w[i][j];
					if (t < e[i][j]) {
						e[i][j] = t;
						root[i][j] = r;
					}
				}
			}
		}
		return new BstResult(e, root);
	}

	/**
	 * 按算法导论原始程序，返回root是int[n+1][n+1]的情况下，打印输出
	 * 每次这条路径结束会打印--up表示向上跳一级，在该级节点继续挂其它节点
	 */
	public static void printRoot_BK(int[][] root) {
		printRoot_BK(root, 1, root[0].length - 1, true);
	}

	public static void printRoot_BK(int[][] root, int i, int j, boolean isLeft) {
		if (i > j) { // 超过边界了
			if (isLeft) {	//正在向左走，所以打印左叶子
				System.out.println("leaf: d" + j);
			} else {
				System.out.println("leaf: d" + j);
			}
		} else {
			int r = root[i][j];
			System.out.println("root: k" + r);
			System.out.println("left: ");
			printRoot_BK(root, i, r - 1, true);
			System.out.println(" --up");
			System.out.println("right: ");
			printRoot_BK(root, r + 1, j, false);
			System.out.println(" --up");
		}
	}

	public static class BstResult {
		int[][] e;
		int[][] root;

		public BstResult(int[][] e, int[][] root) {
			super();
			this.e = e;
			this.root = root;
		}
	}
}
