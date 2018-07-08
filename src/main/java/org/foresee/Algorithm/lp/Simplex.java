package org.foresee.Algorithm.lp;

/**
 * 是经典方法，实际中通常相当快速。 与高斯消元法类似，在一定次数迭代后，重写了系统。可看成是关于不等式的高斯消元法。每轮迭代都关联一个“基本解”，很容易从
 * 松弛型里得到此“基本解”，非基本变量设为0即可，并从等式约束中计算基本变量的值。每轮迭代把松弛型转换为等价松弛型。
 * 关联的基本可行解的目标值不会小于上一轮迭代，通常会更大一些。为了增大目标值，选择一个非基本变量，是的如果从0开始增加变量值，
 * 目标值也会增加。变量值受限于其它约束条件，增加它直到某个基本变量变为0，然后重写松弛型，交换此基本变量和选定的非基本变量。
 * 尽管选定了一个特殊的变量来引导算法，但此算法并不显示地保持该解。它简单地重写了线性规划，直到一个最优解变得明显。
 */
public class Simplex {
	private static final int INFINITE = 2000;

	/**
	 * 单纯型法求解线性规划，每次找到一个可以通过增大自变量而让目标函数更大的自变量位置，让该自变量沿着限制条件增加到最大，
	 * 然后转动该变量，替出它，让它作为限制条件，并替入左侧限制条件里对应的。
	 * 
	 * @param A
	 *            系数矩阵
	 * @param b
	 *            常数列
	 * @param c
	 *            目标函数向量
	 * @return n维向量最优解
	 * @throws NoSolutionException 无法初始解
	 * @throws UpboundException 最优值无限大的
	 */
	public double[] simplex(double[][] A, double[] b, double[] c) throws NoSolutionException, UpboundException {
		Group group = initSimplex(A, b, c);
		double[] delta = new double[group.A.length];
		int e = 0;
		while ((e = hasIndex(group)) != -1) {
			for (int m = 0; m < group.B.length; m++) {
				int i = group.B[m];
				if (group.A[i][e] > 0) {
					delta[i] = group.b[i] / group.A[i][e];
				} else {
					delta[i] = INFINITE;
				}
			}
			int l = group.B[0];
			for (int m = 1; m < group.B.length; m++) {
				int cur = group.B[m];
				if (delta[cur] < delta[l]) {
					l = cur;
				}
			}
			if (delta[l] == INFINITE) {
				throw new UpboundException("Unbounded，无限大");
			} else {
				group = pivot(group, l, e);
			}
		}
		double[] x = new double[A[0].length];
		for (int i = 0; i < A[0].length; i++) {
			boolean isIn = false;
			for (int j = 0; j < group.B.length; j++) {
				if (i == group.B[j]) {
					isIn = true;
					break;
				}
			}
			if (isIn) {
				x[i] = group.b[i];
			} else {
				x[i] = 0;
			}
		}
		return x;
	}

	/**
	 * 返回N中有可迭代余地的变量位置，通过判断c[j]>0，说明增大该位置自变量可以增大目标函数，没有则返回-1.
	 */
	protected int hasIndex(Group group) {
		for (int i = 0; i < group.N.length; i++) {
			int j = group.N[i];
			if (group.c[j] > 0) {
				return j;
			}
		}
		return -1;
	}

	/**
	 * 转动，替出变量xl的下标l，替入变量xe的下标e
	 */
	protected Group pivot(Group group, int l, int e) {
		// Compute the coefficients of the equation for new basic variable xe
		double[][] A2 = new double[group.A.length][group.A[0].length];
		double[] b2 = new double[group.b.length];
		b2[e] = group.b[l] / group.A[l][e];
		for (int m = 0; m < group.N.length; m++) {
			int j = group.N[m];
			if (j == e) {
				continue; // 是要替入的位置
			}
			A2[e][j] = group.A[l][j] / group.A[l][e]; // 新第一行每个系数除以要替出的系数
		}
		A2[e][l] = 1 / group.A[l][e]; // 新第一行要替入的系数
		// Compute the coefficients of the remaining constraints
		for (int m = 0; m < group.B.length; m++) {
			int i = group.B[m];
			if (i == l) {
				continue;
			}
			b2[i] = group.b[i] - group.A[i][e] * b2[e];
			for (int n = 0; n < group.N.length; n++) {
				int j=group.N[n];
				if(j==e){
					continue; //  这个位置是被替出的变量，保持0值
				}
				A2[i][j] = group.A[i][j] - group.A[i][e] * A2[e][j];
			}
			// 书里印刷A2[a][l]但没有a，应该是i，这一句更新了各式里被替入的变量，值是被替出的乘新取过倒数的值
			A2[i][l] = -group.A[i][e] * A2[e][l]; 
		}
		// Compute the objective function
		double v2 = group.v + group.c[e] * b2[e];
		double[] c2 = new double[group.c.length];
		for (int m = 0; m < group.N.length; m++) {
			int j = group.N[m];
			if (j == e) {
				continue;
			}
			c2[j] = group.c[j] - group.c[e] * A2[e][j];
		}
		c2[l] = -group.c[e] * A2[e][l];
		// Compute new sets of basic and nonbasic variables.
		int[] N2 = new int[group.N.length];
		for (int i = 0; i < N2.length; i++) {
			if (group.N[i] == e) { // 为e的位置要去掉，把l补进来
				N2[i] = l;
			} else {
				N2[i] = group.N[i];
			}
		}
		int[] B2 = new int[group.B.length];
		for (int i = 0; i < B2.length; i++) {
			if (group.B[i] == l) { // 为l的位置去掉，把e补进来
				B2[i] = e;
			} else {
				B2[i] = group.B[i];
			}
		}
		return new Group(N2, B2, A2, b2, c2, v2);
	}

	/**
	 * 针对给定的初始参数，给出一个初始可行解。为了对应书里的角标，扩大了每个数组的长度。 如果只用最小的长度，很难从书里伪代码对应过来，很难想。
	 * 终于写出来了，书上16行说明，调了一整天。针对不能得到初始全0解的情况，复杂很多。
	 * 构造辅助线性规划，求解后x0应该是0，这样再消去x0，而保留已经生成的约束和目标函数，因为这是等价转换的。
	 * 通过加上x0的生成不带x0的时候，要注意取值时差了一行一列。
	 * @throws NoSolutionException 无法构造初始解时
	 */
	protected Group initSimplex(double[][] A, double[] b, double[] c) throws NoSolutionException {
		// 找是否有全赋0可以的初始可行解
		int k = 0;
		for (int i = 1; i < b.length; i++) {
			if (b[i] < b[k]) {
				k = i;
			}
		}
		if (b[k] >= 0) {
			int[] N = new int[A[0].length];
			for (int i = 0; i < N.length; i++) {
				N[i] = i;
			}
			int[] B = new int[A.length];
			for (int i = 0; i < B.length; i++) {
				B[i] = i + N.length;
			}
			// 跟书上对不上很难想，干脆扩展到一样大的A
			double[][] A2 = new double[N.length + B.length][N.length + B.length];
			double[] b2 = new double[N.length + B.length];
			double[] c2 = new double[N.length + B.length];
			for (int i = 0; i < A.length; i++) {
				for (int j = 0; j < A[0].length; j++) {
					A2[B[i]][N[j]] = A[i][j]; // A2取左侧下标作行，右侧下标作列
				}
				b2[B[i]] = b[i]; // b2取和左侧系数相同的角标
				c2[N[i]] = c[i]; // c2取和目标函数中相同的角标
			}
			return new Group(N, B, A2, b2, c2, 0);
		}
		// 书29.5中，不能使用基本0解时，需要构成辅助线性规划。还没写
		// throw new RuntimeException("全0不成立的基础解还没写，这需要辅助线性规划");
		
		// 初始值，Laux比原式A多了x0，系数都增加1列，目标函数c只有x0，其余为0，b不变
		double[][] Laux = new double[A.length][A[0].length + 1];
		double[] caux = new double[c.length + 1];
		for (int i = 0; i < Laux.length; i++) {
			Laux[i][0] = -1; // 这里是系数矩阵，还没到松弛型，不需要取负
		}
		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < A[0].length; j++) {
				Laux[i][j + 1] = A[i][j];
			}
		}
		caux[0] = -1;
		for (int i = 0; i < c.length; i++) {
			caux[i + 1] = 0;
		}
		// 为Laux初始化一个松弛结果
		int[] N = new int[Laux[0].length];
		for (int i = 0; i < N.length; i++) {
			N[i] = i;
		}
		int[] B = new int[Laux.length];
		for (int i = 0; i < B.length; i++) {
			B[i] = i + N.length;
		}
		// 跟书上对不上很难想，干脆扩展到一样大的A
		double[][] A2 = new double[N.length + B.length][N.length + B.length];
		double[] b2 = new double[N.length + B.length];
		double[] c2 = new double[N.length + B.length];
		for (int i = 0; i < Laux.length; i++) {
			for (int j = 0; j < Laux[0].length; j++) {
				A2[B[i]][N[j]] = Laux[i][j]; // A2取左侧下标作行，右侧下标作列
			}
			b2[B[i]] = b[i]; // b2取和左侧系数相同的角标
			c2[N[i]] = caux[i]; // c2取和目标函数中相同的角标
		}
		Group res1=new Group(N, B, A2, b2, c2, 0);
		int n=N.length; // 这里Laux有n+1个非基本变量了，而不是A里的n个。这里不需要-1，因为k是B的索引，偏移n不会越界
		int l2=n+k; // 替出的变量会是负值最大的变量 // 命名不和求解里l冲突，取l2
		Group group=pivot(res1, l2, 0); // 转动将改变目标函数和约束
		// 现在基础解法适用于Laux了，现在要对Laux的松弛型求解最优化的目标值-x0
		double[] delta = new double[group.A.length];
		int e = 0;
		while ((e = hasIndex(group)) != -1) {
			for (int m = 0; m < group.B.length; m++) {
				int i = group.B[m];
				if (group.A[i][e] > 0) {
					delta[i] = group.b[i] / group.A[i][e];
				} else {
					delta[i] = INFINITE;
				}
			}
			int l = group.B[0];
			for (int m = 1; m < group.B.length; m++) {
				int cur = group.B[m];
				if (delta[cur] < delta[l]) {
					l = cur;
				}
			}
			if (delta[l] == INFINITE) {
				throw new RuntimeException("unbounded");
			} else {
				group = pivot(group, l, e);
			}
		}
		double[] x = new double[Laux[0].length];
		for (int i = 0; i < Laux[0].length; i++) {
			boolean isIn = false;
			for (int j = 0; j < group.B.length; j++) {
				if (i == group.B[j]) {
					isIn = true;
					break;
				}
			}
			if (isIn) {
				x[i] = group.b[i];
			} else {
				x[i] = 0;
			}
		}
		if(x[0]==0){ // 初始问题是可行的，并且因为x0是0，可以从约束集合移除
			int[] N3 = new int[A[0].length];
			for (int i = 0; i < N3.length; i++) {
				N3[i] = i;
			}
			int[] B3 = new int[A.length];
			for (int i = 0; i < B3.length; i++) {
				B3[i] = i + N3.length;
			}
			boolean x0IsBasic=false;
			int x0Pos=-1;
			for (int j = 0; j < group.B.length; j++) {
				if(0==group.B[j]){
					x0IsBasic=true;
					x0Pos=j;
					break;
				}
			}
			if(x0IsBasic){ 
				// 退化情形，x0是基本变量时，执行一次转动，把x0从基本解中移除，采用任意满足a0e!=0的e属于N作为替入变量
				// 新的基本解仍然可行，退化转动没有改变任何变量的值。
				int e3=0; // 寻找a0e!=0的e值，作为替入变量
				for (;e3 < group.A.length; e3++) {
					if(group.A[0][e3]!=0){
						break;
					}
				}
				group=pivot(group, x0Pos, e3);
			}
			// 系数矩阵去掉x0列
			double[][] A3=new double[N3.length+B3.length][N3.length+B3.length];
			for (int i = 0; i < A3.length; i++) {
				for (int j = 0; j < A3[0].length; j++) {
					A3[i][j]=group.A[i+1][j+1];
				}
			}
			// 目标函数向量，还原回原始，而且如果变量是基本变量，则各系数加上这个基本变量用非基本变量表示时的值
			double[] c3=new double[N3.length+B3.length]; // c3包含所有变量的系数
			for (int i = 0; i < c.length; i++) { // 初始只有c的值
				c3[i]=c[i];
			}
			double[] b3=new double[N3.length+B3.length];
			for (int i = 0; i < b3.length; i++) {
				b3[i]=group.b[i+1];
			}
			double v=0;
			for (int i = 0; i < c3.length; i++) {
				boolean isInB=false;
				for (int j = 0; j < group.B.length; j++) {
					// group.B包含x0，但现在求得c3不包含x0，错开了一行，现在的x0在group.B里是x1
					if((i+1)==group.B[j]){
						isInB=true;
						break;
					}
				}
				// 因为出现在基本变量里的不会出现在非基本变量，所以不用全代入再计算，可以一次一次代入
				if(c3[i]!=0 && isInB){ // 系数不为0并且是基本变量，用非基本变量表示
					for (int j = 0; j < c3.length; j++) {
						if(j==i){ // 暂不更新它，要做系数乘
							continue;
						}
						// 这里注意系数矩阵是取过反的，而代入的时候是直接代入，不取反，所以反回来
						c3[j]+=c3[i]*(-A3[i][j]);
					}
					v+=c3[i]*b3[i];
					c3[i]=0; // 已用非基本变量表示了，不再有系数
				}
			}
			return new Group(N3, B3, A3, b3, c3, v);			
		}else{
			throw new NoSolutionException("infeasible，无法构造初始解");
		}
	}

	/**
	 * 要维护的变量很多，放个类，每次传递。
	 */
	protected static class Group {
		public int[] N;
		public int[] B;
		public double[][] A;
		public double[] b;
		public double[] c;
		public double v;

		public Group(int[] N, int[] B, double[][] A, double[] b, double[] c, double v) {
			super();
			this.N = N;
			this.B = B;
			this.A = A;
			this.b = b;
			this.c = c;
			this.v = v;
		}

	}
	public class UpboundException extends Exception{
		private static final long serialVersionUID = -2887236970264398754L;
		public UpboundException(String msg) {
			super(msg);
		}
	}
	public class NoSolutionException extends Exception{
		private static final long serialVersionUID = 4726125823873349478L;
		public NoSolutionException(String msg) {
			super(msg);
		}
	}
}
