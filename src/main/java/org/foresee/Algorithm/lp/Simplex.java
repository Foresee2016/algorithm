package org.foresee.Algorithm.lp;

/**
 * 是经典方法，实际中通常相当快速。
 * 与高斯消元法类似，在一定次数迭代后，重写了系统。可看成是关于不等式的高斯消元法。每轮迭代都关联一个“基本解”，很容易从
 * 松弛型里得到此“基本解”，非基本变量设为0即可，并从等式约束中计算基本变量的值。每轮迭代把松弛型转换为等价松弛型。
 * 关联的基本可行解的目标值不会小于上一轮迭代，通常会更大一些。为了增大目标值，选择一个非基本变量，是的如果从0开始增加变量值，
 * 目标值也会增加。变量值受限于其它约束条件，增加它直到某个基本变量变为0，然后重写松弛型，交换此基本变量和选定的非基本变量。
 * 尽管选定了一个特殊的变量来引导算法，但此算法并不显示地保持该解。它简单地重写了线性规划，直到一个最优解变得明显。
 */
public class Simplex {
	private static final int INFINITE=2000;
	/**
	 * 单纯型法求解线性规划，每次找到一个可以通过增大自变量而让目标函数更大的自变量位置，让该自变量沿着限制条件增加到最大，
	 * 然后转动该变量，替出它，让它作为限制条件，并替入左侧限制条件里对应的。
	 * @param A 系数矩阵
	 * @param b 常数列
	 * @param c 目标函数向量
	 * @return n维向量最优解
	 */
	public double[] simplex(double[][] A, double[] b, double[] c) {
		Group group=initSimplex(A, b, c);
		double[] delta=new double[group.A.length];
		int e=0;
		while ((e=hasIndex(group)) !=-1) {
			for (int m = 0; m < group.B.length; m++) {
				int i=group.B[m];
				if(group.A[i][e]>0){
					delta[i]=group.b[i]/group.A[i][e];
				}else{
					delta[i]=INFINITE;
				}
			}
			int l=group.B[0];
			for (int m = 1; m < group.B.length; m++) {
				int cur=group.B[m];
				if(delta[cur]<delta[l]){
					l=cur;
				}
			}
			if(delta[l]==INFINITE){
				throw new RuntimeException("unbounded");
			}else{
				group=pivot(group, l, e);
			}
		}
		double[] x=new double[A[0].length];
		for (int i = 0; i < A[0].length; i++) {
			boolean isIn=false;
			for (int j = 0; j < group.B.length; j++) {
				if(i==group.B[j]){
					isIn=true;
					break;
				}
			}
			if(isIn){
				x[i]=group.b[i];
			}else{
				x[i]=0;
			}
		}
		return x;
	}
	/**
	 * 返回N中有可迭代余地的变量位置，通过判断c[j]>0，说明增大该位置自变量可以增大目标函数，没有则返回-1.
	 */
	protected int hasIndex(Group group) {
		for (int i = 0; i < group.N.length; i++) {
			int j=group.N[i];
			if(group.c[j]>0){
				return j;
			}
		}
		return -1;
	}
	
	/**
	 * 转动，替出变量xl的下标l，替入变量xe的下标e
	 */
	protected Group pivot(Group group, int l, int e) {
		//Compute the coefficients of the equation for new basic variable xe
		double[][] A2=new double[group.A.length][group.A[0].length];
		double[] b2=new double[group.b.length];
		b2[e]=group.b[l]/group.A[l][e];
		for (int m = 0; m < group.N.length; m++) {
			int j=group.N[m];
			if(j==e){ 
				continue; // 是要替入的位置
			}
			A2[e][j]=group.A[l][j]/group.A[l][e]; // 新第一行每个系数除以要替出的系数
		}
		A2[e][l]=1/group.A[l][e]; // 新第一行要替入的系数
		// Compute the coefficients of the remaining constraints
		for (int m = 0; m < group.B.length; m++) {
			int i=group.B[m];
			if(i==l){
				continue;
			}
			b2[i]=group.b[i]-group.A[i][e]*b2[e];
			for (int n = 0; n < group.N.length; n++) {
				int j=group.N[n];
				if(j==e){
					continue;
				}
				A2[i][j]=group.A[i][j]-group.A[i][e]*A2[e][j];
			}
			A2[e][l]=-group.A[i][e]*A2[e][l];
		}
		// Compute the objective function
		double v2=group.v+group.c[e]*b2[e];		
		double[] c2=new double[group.c.length];
		for (int m = 0; m < group.N.length; m++) {
			int j=group.N[m];
			if(j==e){
				continue;
			}
			c2[j]=group.c[j]-group.c[e]*A2[e][j];
		}
		c2[l]=-group.c[e]*A2[e][l];
		// Compute new sets of basic and nonbasic variables.
		int[] N2=new int[group.N.length];
		for (int i = 0; i < N2.length; i++) {
			if(group.N[i]==e){ // 为e的位置要去掉，把l补进来
				N2[i]=l;
			}else{
				N2[i]=group.N[i];
			}
		}
		int[] B2=new int[group.B.length];
		for (int i = 0; i < B2.length; i++) {
			if(group.B[i]==l){ // 为l的位置去掉，把e补进来
				B2[i]=e;
			}else {
				B2[i]=group.B[i];
			}
		}
		return new Group(N2, B2, A2, b2, c2, v2);
	}
	/**
	 * 针对给定的初始参数，给出一个初始可行解。为了对应书里的角标，扩大了每个数组的长度。
	 * 如果只用最小的长度，很难从书里伪代码对应过来，很难想。
	 */
	protected Group initSimplex(double[][] A, double[] b, double[] c) {
		// 找是否有全赋0可以的初始可行解
		int k=0;
		for (int i = 1; i < b.length; i++) {
			if(b[i]>b[k]){
				k=i;
			}
		}
		if(b[k]>=0){
			int[] N=new int[A[0].length];
			for (int i = 0; i < N.length; i++) {
				N[i]=i;
			}
			int[] B=new int[A.length];
			for (int i = 0; i < B.length; i++) {
				B[i]=i+N.length;
			}
			// 跟书上对不上很难想，干脆扩展到一样大的A
			double[][] A2=new double[N.length+B.length][N.length+B.length];
			double[] b2=new double[N.length+B.length];
			double[] c2=new double[N.length+B.length];
			for (int i = 0; i < A.length; i++) {
				for (int j = 0; j < A[0].length; j++) {
					A2[B[i]][N[j]]=A[i][j]; // A2取左侧下标作行，右侧下标作列
				}
				b2[B[i]]=b[i]; // b2取和左侧系数相同的角标
				c2[N[i]]=c[i]; // c2取和目标函数中相同的角标
			}
			return new Group(N, B, A2, b2, c2, 0);
		}
		// TODO：书29.5中，不能使用基本0解时，需要构成辅助线性规划。还没写
		throw new RuntimeException("全0不成立的基础解还没写，这需要辅助线性规划");
	}
	/**
	 * 要维护的变量很多，放个类，每次传递。
	 */
	protected static class Group{
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
}
