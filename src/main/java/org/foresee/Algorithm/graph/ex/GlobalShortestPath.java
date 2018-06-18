package org.foresee.Algorithm.graph.ex;

/**
 * 所有结点之间的最短路径问题，如计算所有城市之间交通道路距离时。
 */
public class GlobalShortestPath {
	/**
	 * 通过对最短路径一条边一条边的扩展，来计算最短路径权重。初始时即为权重矩阵W，每次迭代。 直到计算出L(n-1)，它是最短路径矩阵。
	 */
	public double[][] slowAllPairsShortestPaths(MatrixGraph graph) {
		int n = graph.weights.length;
		double[][] L1 = graph.weights; // 多写了一行，为了对应书上的伪代码，方便理解
		double[][] Lm = L1;
		for (int m = 1; m < n - 1; m++) {
			Lm = extendShortestPaths(Lm, graph.weights);
		}
		return Lm;
	}

	/**
	 * 使用重复平方技术，因为目标不是计算每一个Lm，而是Ln-1。在矩阵乘法中，W的四次方可以从W的平方求得，
	 * extendShortestPaths()也符合这个性质，所以仅用ceil(log(n - 1))个矩阵乘积就够了。
	 * 因为当已经得到最短路径矩阵Ln-1的情况下，Ln及其之后的矩阵不再变化，继续迭代多出的次数不会有影响。
	 * 所以程序里m每次翻倍，大于n-1时停止，而不需要考虑是否等于n-1，停止时m介于n-1和2*(n-1)之间，包含边界。
	 * 算法时间复杂度θ(n*n*n*log(n))
	 */
	public double[][] fasterAllPairsShortestPaths(MatrixGraph graph) {
		int n = graph.weights.length;
		double[][] L1 = graph.weights;
		double[][] L2m = L1;
		int m = 1;
		while (m < n - 1) {
			L2m = extendShortestPaths(L2m, L2m);
			m = 2 * m;
		}
		return L2m;
	}

	/**
	 * 在给定W和L(m-1)情况下，计算L(m)，将最近计算出的最短路径扩展了一条边。 三层for，算法运行时间为O(n*n*n)
	 */
	public double[][] extendShortestPaths(double[][] L, double[][] W) {
		int n = L.length;
		double[][] L2 = new double[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				L2[i][j] = MatrixGraph.INFINITE;
				for (int k = 0; k < n; k++) {
					L2[i][j] = Double.min(L2[i][j], L[i][k] + W[k][j]);
				}
			}
		}
		return L2;
	}

	/**
	 * 计算两方阵A，B的乘积C，可以对比看到与上面计算最短路径的方式很像，只是min()替换为加
	 */
	public double[][] squareMatrixMultiple(double[][] A, double[][] B) {
		int n = A.length;
		double[][] C = new double[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				C[i][j] = 0;
				for (int k = 0; k < n; k++) {
					C[i][j] = C[i][j] + A[i][k] * B[k][j];
				}
			}
		}
		return C;
	}

	/**
	 * 练习25.1-6，从已经计算出的最短路径权重矩阵L计算出前驱矩阵Pi，在O(n*n*n)时间内。
	 * 思路是判断某个位置的值是否是另一位置和权重矩阵的和，是的话，说明是计算来的，该位置的父节点是另一位置
	 */
	public void generatePi(double[][] L, double[][] W, int[][] Pi) {
		int n = L.length;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				Pi[i][j] = MatrixGraph.NIL; // 初始化值
				if (i == j) { // 自己跟自己无所谓路径一说
					continue;
				}
				// 遍历该行所有，看看是从哪里过来的，直接过来的情况也包含在内了，
				// 比如0到4权重是-4且它是最短路径，那么i=0，j=4，k=0时更新的4的父结点是0
				for (int k = 0; k < n; k++) {
					if (L[i][j] == L[i][k] + W[k][j] && k != j) { // W对角线上都是0，成立但无意义
						Pi[i][j] = k;
					}
				}
			}
		}
	}

	// @formatter:off
	/**
	 * Floyd-Warshall算法，自底向上递增次序计算最短路径权重Dn的值。
	 * 算法时间复杂度O(n*n*n)
	 * 不太好理解，书里给出伪代码时没结合上边的说明。
	 * 注意k是可变的，解释里k是增长的，也就是伪代码中，k先从1开始，那么子集里只有结点1，
	 * 所以D1中的最短路径，要么是原本就连到的，要么是通过结点1连到的。当k为2时，子集里有结点1和2，D2中的最短路径，
	 * 是原本连到的，或是通过结点1或2或者都经过。这样一直到k为n，解释里第一个点说的是这个意思。
	 * 第二个点说的意思是，在k增加时，最短路径可能从原来的最短路径的一个分叉中连到结点k，但仍包含在结点1、2、...k中，
	 */
	public double[][] FloydWarshall(double[][] W) {
	// @formatter:on
		int n = W.length;
		double[][] Dk_1 = W;
		for (int k = 0; k < n; k++) {
			double[][] Dk = new double[n][n];
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					Dk[i][j] = Double.min(Dk_1[i][j], Dk_1[i][k] + Dk_1[k][j]);
				}
			}
			Dk_1 = Dk;
		}
		return Dk_1;
	}

	/**
	 * 在计算最短路径权重矩阵的同时，计算前驱矩阵Pi。
	 */
	public double[][] FloydWarshallWithPi(double[][] W, int[][] Pi) {
		// 初始化Pi，在k=0时，没有中间结点，W上有路径，则有父结点，W为无穷的位置，没有父结点
		int n = W.length;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i == j || W[i][j] == MatrixGraph.INFINITE) {
					Pi[i][j] = MatrixGraph.NIL;
				} else {
					Pi[i][j] = i;
				}
			}
		}
		double[][] Dk_1 = W;
		int[][] Pik_1 = Pi;
		for (int k = 0; k < n; k++) {
			double[][] Dk = new double[n][n];
			int[][] Pik = new int[n][n];
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if(i==j){
						Dk[i][j]=0;
						Pik[i][j]=MatrixGraph.NIL;
						continue;
					}
					Dk[i][j] = Double.min(Dk_1[i][j], Dk_1[i][k] + Dk_1[k][j]);
					Pik[i][j] = MatrixGraph.NIL;
					// 如果子集中新增了k但最短路径没有因此改变，那么父结点仍保持原来的
					if (Dk_1[i][j] <= plus(Dk_1[i][k], Dk_1[k][j])) {
						Pik[i][j] = Pik_1[i][j];
					} else {
						// 如果新的路径比没有k时的路径短，会更新最短路径权重，也要更新父结点
						// 新父结点的位置在新增的第k行，要到的结点时j所以在第j列。
						Pik[i][j] = Pik_1[k][j];
					}
				}
			}
			Dk_1 = Dk;
			Pik_1 = Pik;
		}
		for (int i = 0; i < n; i++) { // 按引用传递的，这里把值赋回去
			for (int j = 0; j < n; j++) {
				Pi[i][j] = Pik_1[i][j];
			}
		}
		return Dk_1;
	}
	/**
	 * 添加无穷值的计算法则，防止有负权重时无穷加负值小于无穷的情况
	 */
	private double plus(double Dik, double Dkj) {
		if(Dik == MatrixGraph.INFINITE || Dkj == MatrixGraph.INFINITE){
			return MatrixGraph.INFINITE;
		}
		return Dik+Dkj;
	}
}
