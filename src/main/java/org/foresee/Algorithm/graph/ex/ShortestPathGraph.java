package org.foresee.Algorithm.graph.ex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 还是得写一个单独的图结构，原来的AdjacencyGraph边属性不一致
 */
public class ShortestPathGraph {
	public ArrayList<Vertex> vertexs;
	public ArrayList<Edge> edges;
	public ShortestPathGraph() {
		vertexs = new ArrayList<>();
		edges=new ArrayList<>();
	}
	/**
	 * 保留这个方法，在不添加边和权值时使用。
	 */
	public void addVertex(Vertex src) {
		vertexs.add(src);
	}
	/**
	 * 辅助方法，传入源节点src和它连到的各结点，以及相应边的权重
	 */
	public void addVertexAndWeight(Vertex src, List<Vertex> linkTo, List<Double> weights) {
		if(linkTo==null || weights==null || linkTo.size() != weights.size()){
			throw new RuntimeException("传入的邻接结点数和权重数不相等");
		}
		for (int i = 0; i < linkTo.size(); i++) {
			Edge edge = new Edge(src, linkTo.get(i), weights.get(i));
			edges.add(edge);
			src.adjacents.add(edge);
		}
		vertexs.add(src);
	}
	public static class Vertex {
		public String name; // 结点名称
		public LinkedList<Edge> adjacents; // 邻接结点
		// NOTE：单源最短路径算法中表示权重和，而不是距离了。拓扑排序中表示发现时间。
		public double d; 
		public int f; // 深度优先遍历时存涂黑色时间戳
		public Vertex parent; // 图搜索时指向前驱结点
		public Color color; // 颜色，图搜索时标记
		public Vertex(String name) {
			this.name = name;
			adjacents = new LinkedList<>();
		}
	}
	public enum Color {
		white, gray, black
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
