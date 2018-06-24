package org.foresee.Algorithm.graph.ex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
// @formatter:off
/**
 * 对应最大流算法的图结构，有向图，每条边(u, v)有一个非负的容量值c(u,v)，而且，如果存在边(u,v)，那么不存在反向边(v, u)，
 * 为方便，如果不存在边(u,v)，那么c(u,v)=0，并且图中不允许自循环。
 * 流网络中，所有结点中有两个特殊结点：源节点s和汇结点t。方便起见，假定每个结点都在从源节点到汇结点的某条路径上。
 * 所以流网络是联通的，并且除了源节点以外的结点都至少有一条进入的边，E数量>=V数量-1
 * 当存在反向平行边，即(u,v)和(v,u)都存在，那么通过增加新结点v2将其中一条边拆成两段，这两段容量相同。
 * 
 * 一个最大流问题可能有几个源节点和几个汇结点，它可以归为一个普通最大流问题，不会比普通问题更难。转换方法是加入一个超级源节点s，
 * 它到每个源节点s的容量是无穷，同时创建超级汇结点，每个汇结点到它的容量是无穷。
 * 
 * 结构上使用邻接链表，边容易加属性。
 */
public class MaxStreamGraph {
// @formatter:on
	public ArrayList<Vertex> vertexs;
	public ArrayList<Edge> edges;
	public Vertex sourse, dst;
	public ArrayList<Edge> residuals; // 残存网络里的边

	public MaxStreamGraph() {
		vertexs = new ArrayList<>();
		edges = new ArrayList<>();
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
	public void addVertexAndWeight(Vertex src, List<Vertex> linkTo, List<Integer> capacity) {
		if (linkTo == null || capacity == null || linkTo.size() != capacity.size()) {
			throw new RuntimeException("传入的邻接结点数和权重数不相等");
		}
		for (int i = 0; i < linkTo.size(); i++) {
			Edge edge = new Edge(src, linkTo.get(i), capacity.get(i));
			edges.add(edge);
			src.adjacents.add(edge);
		}
		vertexs.add(src);
	}
	// @formatter:off
	/**
	 * 从源节点s出发进行广度优先的搜索，计算出从s到所有可达节点之间的距离。这相当于书里的Kdmonds-Karp算法
	 * 为了避免输出过多，去掉了输出
	 * 引理：残存网络Gf中最短距离随着每次流量增加递增而单调递增，毕竟是广度优先搜索，是这样的。
	 * NOTE：除了原始的广度优先搜索，判断某个结点是否如队列时，还要考虑该边是否还有可增大的余地，
	 * 即残存网络中Edge的c值，c值为0了说明它已经达到最大流量了，不能再走过去。
	 */
	public void broadFirstSearch(Vertex s) {
	// @formatter:on
		for (Vertex u : vertexs) {
			u.color = Color.white;
			u.d = INFINITE_DISTANCE;
			u.parent = null;
		}
		s.color = Color.gray; // 源节点被发现，涂灰色
		s.d = 0;
		s.parent = null;
		Queue<Vertex> Q = new LinkedList<>(); // 使用队列管理灰色节点
		// System.out.println("入队："+s.name+", 深度："+s.d);
		if (!Q.offer(s)) { // 源节点入队，从这里开始
			System.out.println("队列已满");
		}
		while (Q.size() != 0) {
			Vertex u = Q.poll();
			// System.out.println("出队："+u.name);
			// 最大流中，对残存网络搜索
			for (Edge e : u.residuals) {
				if (e.c == 0) { // 对于没有增大余地或原本不存在的边，跳过。
					continue;
				}
				Vertex v = e.link;
				if (v.color == Color.white) {
					v.color = Color.gray;
					v.d = u.d + 1;
					v.parent = u;
					if (!Q.offer(v)) {
						System.out.println("队列已满");
					}
					// System.out.println("染灰："+v.name+",
					// 深度："+v.d+"，前驱："+v.parent.name+", 入队"+v.name);
				}
			}
			// 该节点已经考察完邻接节点，涂黑色，表示它的邻接节点都已被发现了
			u.color = Color.black;
			// System.out.println("涂黑："+u.name);
		}
	}

	public static class Vertex {
		public String name; // 结点名称
		public LinkedList<Edge> adjacents; // 邻接结点
		public LinkedList<Edge> residuals; // 结点相关的残存网络中的边
		public Color color; // 颜色，图搜索时标记
		public int d; // 广度优先遍历时表示从源节点s到这个节点的距离，深度优先遍历时存图灰色时间戳
		public Vertex parent; // 图搜索时指向前驱结点
		public int f; // 深度优先遍历时存涂黑色时间戳
		public double cost; // 顶点权值

		public Vertex(String name) {
			this.name = name;
			adjacents = new LinkedList<>();
		}

		/**
		 * 遍历邻接边，查看是否有到v的边存在
		 */
		public Edge containAdjacent(Vertex v) {
			for (Edge edge : adjacents) {
				if (edge.link == v) {
					return edge;
				}
			}
			return null;
		}

		public Edge containResidual(Vertex v) {
			for (Edge edge : residuals) {
				if (edge.link == v) {
					return edge;
				}
			}
			return null;
		}
	}

	public enum Color {
		white, gray, black
	}

	public static class Edge {
		public Vertex src; // 后边需要排序和遍历边，还是加上源节点
		public Vertex link; // 边被起点Vertex使用，这里只存终点就可以了
		public int c; // 流容量，暂时用整数，书里也说整数方便，有理数时乘倍数到整数就行了
		public int f; // 边上流量

		public Edge(Vertex src, Vertex link, int c) {
			super();
			this.src = src;
			this.link = link;
			this.c = c;
		}

	}

	public static final int INFINITE_DISTANCE = 10000;
}
