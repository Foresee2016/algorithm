package org.foresee.Algorithm.ga;

public class TravelingSalesmanProblem {
	public static void main(String[] args) {
		
	}
	
	public static final int CITY_NUM=150;
	public static final int GROUP_NUM=30;
	public static final int SON_NUM=32;
	
	public static final double P_INHERIATANCE=0.01; // 变异概率
	public static final double P_COPULATION=0.8; // 杂交概率
	public static final int ITERATION_NUM=1500; // 杂交概率
	public static final double MAX_INT=999_9999.0;
	
	public static void initGroup(Graph g) {
		
	}
	
	protected class Graph {
		public int vexNum;
		public int arcNum;
		public int[] vexs=new int[CITY_NUM];
		public double[][] arcs=new double[CITY_NUM][CITY_NUM];
	}
	protected class TspSolution{
		public double lengthPath;
		public int[] path=new int[CITY_NUM];
		public double pReproduction;
	}
}
