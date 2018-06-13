package org.foresee.Algorithm.graph.ex;

import org.foresee.Algorithm.graph.ex.ShortestPathGraph.Edge;
import org.foresee.Algorithm.graph.ex.ShortestPathGraph.Vertex;

/**
 * 最短路径算法通常依赖最短路径的一个重要性质：两个结点之间的一条最短路径，包含着其他的最短路径。
 * 最短路径有最优子结构，这是使用动态规划和贪心算法的重要指标。
 * 注意负权重是可以的，最短路径仍然有明确定义，但不能有从s可达的权重为负值的环路，会导致无限小，最短路径无法定义。
 * 有些算法如Dijkstra假定输入图的所有权重为非负值。 而有些算法如Bellman-Ford可以有负权重但不能有负权重环，并可以检测负权重环。
 * 最短路径是不会包括环路的，负的环路导致无限小，正的环路去掉得到更短路径，权重为0的环路也可以直接去掉。所以假定最短路径里无环路。
 * 最短路径的表示和广度优先搜索算法类似，但路径以边权重定义，而不是变得条数。这里图使用基础的AdjacencyGraph。
 * 松弛(relaxation)操作：对每个结点v维持v.d，用来记录从源结点s到结点v的最短路径权重的上界。称v.d为s到v的最短路径估计。
 */
public class SingleSourceShortestPath {
	public static final int INFINITE = 1000;

	protected void initializeSingleSource(ShortestPathGraph graph, Vertex s) {
		for (Vertex v : graph.vertexs) {
			v.d = INFINITE;
			v.parent = null;
		}
		s.d = 0;
	}

	/**
	 * 对一条边(u, v)的松弛过程为：首先测试一下是否可以对从s到v的最短路径进行改善。
	 * 测试的方法是：将从结点s到结点u之间的最短路径距离加上结点u与v之间的边权重，并与当前的s和v的最短路径估计进行比较，
	 * 如果前者更小，则对v.d和v.parent进行更新。 松弛步骤可能降低最短路径的估计值v.d并更新v的前驱属性v.parent。
	 * NOTE：边的权值可以从u的邻接边里找到，不作为传入参数。和书里不完全一致。
	 */
	protected void relax(Edge edge) {
		Vertex u = edge.src;
		Vertex v = edge.link;
		// 如果v距离源结点的权重比u加上它们之间权重还要大，说明v从u走可以更近
		if (v.d > u.d + edge.weight) {
			v.d = u.d + edge.weight;
			if (v.d > INFINITE) {
				v.d = INFINITE;
			}
			if (v.d < -INFINITE) {
				v.d = -INFINITE;
			}
			v.parent = u;
		}
	}

	// @formatter:off
	/**
	 * 这是人名，不遵守小写开头的命名规则了，以示尊重*^o^* Bellman-Ford算法解决一般情况下的单源最短路径问题，权重可以为负值。
	 * 返回的boolean值表示该图中是否存在从源节点可达的权重为负值的环路，存在时返回false表示无法进行。
	 * 算法通过对边进行松弛操作，渐近地降低从源节点s到每个结点v的最短路径的估计值v.d， 
	 * 直到该估计值与实际的最短路径权重delta(s, v)相同时为止。
	 * 初始化运行时间O(V)，循环一共|V|-1次，每次时间O(E)，总共时间O(V * E)
	 */ 
	public boolean BellmanFord(ShortestPathGraph graph, Vertex s) {
	// @formatter:on
		initializeSingleSource(graph, s);
		for (int i = 0; i < graph.vertexs.size() - 1; i++) {
			for (Edge edge : graph.edges) {
				relax(edge);
			}
		}
		for (Edge edge : graph.edges) {
			Vertex u = edge.src;
			Vertex v = edge.link;
			if (v.d > u.d + edge.weight) {
				return false;
			}
		}
		return true;
	}
}
