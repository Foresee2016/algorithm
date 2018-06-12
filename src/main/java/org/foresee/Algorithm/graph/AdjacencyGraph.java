package org.foresee.Algorithm.graph;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 图数据结构，表示形式为：邻接表 结点Vertex类，边Edge类，都写在内部
 */
public class AdjacencyGraph {
	public ArrayList<Vertex> vertexs;

	public AdjacencyGraph() {
		vertexs = new ArrayList<>();
	}

	/**
	 * 辅助方法，快速生成图结构，传入结点，和它直接相连的结点。不添加边的权值
	 */
	public void addVertex(Vertex vertex, Vertex... linkTo) {
		for (Vertex link : linkTo) {
			vertex.adjacents.add(new Edge(vertex, link));
		}
		vertexs.add(vertex);
	}

	public static class Vertex {
		public String name; // 结点名称
		public LinkedList<Edge> adjacents; // 邻接结点
		public Color color; // 颜色，图搜索时标记
		public int d; // 广度优先遍历时表示从源节点s到这个节点的距离，深度优先遍历时存图灰色时间戳
		public Vertex parent; // 图搜索时指向前驱结点
		public int f; // 深度优先遍历时存涂黑色时间戳
		public double cost; // 顶点权值
		public boolean isJunction; // 思考题22-2使用，结点是否为衔接点
		public int low; // 思考题22-2使用

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
		public EdgeType edgeType; // 边类型
		boolean visited = false; // 边是否访问过

		public Edge(Vertex src, Vertex end) {
			super();
			this.src = src;
			this.link = end;
		}

	}

	public static enum EdgeType {
		/**
		 * 树边，为深度优先森林Gp中的边，如果结点v是因算法对边（u，v）的探索而被发现，则（u，v）是一条树边
		 */
		TreeEdge,
		/**
		 * 后向边，后向边（u，v）是将结点u连接到其在深度优先树中（一个）祖先结点v的边。由于有向图中可以有自循环，自循环也被认为是后向边
		 */
		BackwardEdge,
		/**
		 * 前向边，是将结点u连接到其在深度优先树中一个后代结点v的边（u，v）
		 */
		ForwardEdge,
		/**
		 * 横向边，指其它所有的边，这些边可以连接同一棵深度优先树中的结点，
		 * 只要其中一个结点不是另外一个结点的祖先，也可以连接不同深度优先树中的两个结点
		 */
		LateralEdge,
		/*
		 * NOTE：遇到某些边时，DFS有足够的信息来对这些边进行分类，这里的关键是，当第一次探索边（u，v）时，结点v的颜色能告知关于该边的信息
		 * 1.结点v是白色，表明该边（u，v）是一条树边。（因为v刚刚被发现）
		 * 2.结点v是灰色，表明该边（u，v）是一条后向边。（因为是灰色表明v被发现过了，正在深度搜索子节点）
		 * 3.结点v是黑色，表明该边（u，v）是一条前向边或横向边。（剩下的可能性，（u，v）在u.d<v.d时为前向边，在u.d>v.d时为横向边
		 * ） NOTE：无向图因为（u，v）和（v，u）是一条边，所以先探索到哪个方向，就视为该边时该类型 。
		 * NOTE：无向图搜索深度优先搜索过程中，要么是树边，要么是后向边，不会产生前向边或横向边。（因为没有方向，形成环时会访问回祖先结点，
		 * 形成横向边时总会再向下形成树边）
		 */
	}
}
