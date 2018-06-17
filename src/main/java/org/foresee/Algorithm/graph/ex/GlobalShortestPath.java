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
}
