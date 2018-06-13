package org.foresee.Algorithm.graph.ex;

import java.util.ArrayList;
import java.util.HashSet;


/**
 * 最小生成树里要求图的结点和边的属性，和基本图算法里有很多不同的地方，所以单独写一个结构，而不改原先的AdjacencyGraph了
 * 必须是无向图，所以边的方向也没作用
 */
public class MinSpanTreeGraph {
	public HashSet<Vertex> vertexs;
	public ArrayList<Edge> edges;
	public MinSpanTreeGraph() {
		vertexs=new HashSet<>();
		edges=new ArrayList<>();
	}
	/**
	 * 辅助方法，添加边，同时判断是否要添加结点
	 */
	public void addEdge(Edge edge) {
		edges.add(edge);
		if(!vertexs.contains(edge.src)){
			vertexs.add(edge.src);
		}
		if(!vertexs.contains(edge.link)){
			vertexs.add(edge.link);
		}
	}
	
	public static Vertex makeSet(Vertex x) {
		x.parent=x;
		x.rank=0;
		return x;
	}
	public static void union(Vertex x, Vertex y) {
		link(findSet(x), findSet(y));
	}
	public static Vertex link(Vertex x, Vertex y) {
		if(x.rank>y.rank){
			y.parent=x;
			return x;
		}else{
			x.parent=y;
			if(x.rank==y.rank){
				y.rank++;
			}
			return y;
		}
	}
	/**
	 * 是一种两趟方法（two-pass method）：
	 * 递归时，第一趟查找路径向上直到找到根，
	 * 递归回溯时，第二趟沿着搜索树向下更新每个结点，使其指向根。
	 */
	public static Vertex findSet(Vertex x) {
		if(x.parent!=x){
			x.parent=findSet(x.parent);
		}
		return x.parent;
	}
	public static class Vertex {
		public String name; // 结点名称
		public Vertex parent;
		public int rank;
		public Vertex(String name) {
			this.name = name;
		}
	}
	public static class Edge {
		public Vertex src; // 后边需要排序和遍历边，还是加上源节点
		public Vertex link; // 边被起点Vertex使用，这里只存终点就可以了
		public double weight; // 权值
		boolean visited = false; // 边是否访问过

		public Edge(Vertex src, Vertex link) {
			super();
			this.src=src;
			this.link = link;
		}

		public Edge(Vertex src, Vertex link, double weight) {
			super();
			this.src = src;
			this.link = link;
			this.weight = weight;
		}

	}
}
