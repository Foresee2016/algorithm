package org.foresee.Algorithm.graph.ex;

import java.util.HashMap;
import java.util.Map;

// @formatter:off
/**
 * 用邻接矩阵表示的图结构，矩阵W是n行n列的，对角线上都是0，若有边存在，则对应的行列表示权重，没有边存在对应行列值为无穷。
 * 所有结点对的最短路径算法的表格输出也是一个n行n列的矩阵D，i行j列对应从结点i到j的最短路径的权重。
 * δ(i,j)表示i到j最短路径的权重，则算法终止时，矩阵D的i行j列值为δ(i,j)
 * 还需要计算前驱结点矩阵PI，其中i行j列，i=j或者不存在从i到j的路径时为NIL，其他情况时从结点i到j的某条最短路径上结点j的前驱结点。
 * 由矩阵PI的第i行所诱导的子图应该是一棵根结点为i的最短路径树。
 * NOTE：这个图数据结构的二维数组可以用嵌套List做，就可以动态添加任意多行了。
 */
public class MatrixGraph {
// @formatter:on
	public static final int INFINITE = 1000;
	Map<Vertex, Integer> vertexs; // 图中所有结点和它在矩阵中的位置
	double[][] edges; // 图中边的权重
	Vertex[][] pi; // 前驱结点矩阵PI
	double[][] d; // 最短路径算法的表格输出

	public MatrixGraph(int vertexCount) {
		vertexs = new HashMap<>(vertexCount);
		edges = new double[vertexCount][vertexCount];
		pi = new Vertex[vertexCount][vertexCount];
		d = new double[vertexCount][vertexCount];
		for (int i = 0; i < vertexCount; i++) {
			for (int j = 0; j < vertexCount; j++) {
				if (i == j) {
					edges[i][j] = 0;
					d[i][j] = 0;
				} else {
					edges[i][j] = INFINITE;
					d[i][j] = INFINITE;
				}
			}
		}
	}
	/**
	 * 添加边，源节点和它连接的结点，以及边权重。
	 * @throws RuntimeException：当结点数量超过初始化时的结点数量时。
	 */
	public void addEdge(Vertex src, Vertex link, double weight) {
		int srcPos = findPosition(src), linkPos = findPosition(link);
		edges[srcPos][linkPos] = weight;
	}
	
	public void printAllPairsShortestPath(Vertex i, Vertex j) {
		if(i==j){
			System.out.print(i.name);
		}else if(pi[vertexs.get(i)][vertexs.get(j)]==null){
			System.out.print("No path From "+i.name+" to "+j.name);
		}else {
			printAllPairsShortestPath(i, pi[vertexs.get(i)][vertexs.get(j)]);
			System.out.print(j.name);
		}
	}
	/**
	 * 找到结点v在数组中的位置，如果v是新添加的，则顺序加入Map下一个位置，返回该位置。
	 * 
	 * @throws RuntimeException：当要添加新的，但当前数量已经等于初始化时的结点数量时，不能再添加。
	 */
	protected int findPosition(Vertex v) {
		if (vertexs.keySet().contains(v)) {
			return vertexs.get(v);
		} else {
			if (vertexs.size() == edges.length) {
				throw new RuntimeException("结点数量超过了初始化时的数量，不能再添加");
			}
			int pos = vertexs.size();
			vertexs.put(v, pos);
			return pos;
		}
	}

	public static class Vertex {
		public String name; // 结点名称
		public double d;
		public int f; // 深度优先遍历时存涂黑色时间戳
		public Vertex parent; // 图搜索时指向前驱结点
		public Color color; // 颜色，图搜索时标记

		public Vertex(String name) {
			this.name = name;
		}
	}

	public enum Color {
		white, gray, black
	}
}
