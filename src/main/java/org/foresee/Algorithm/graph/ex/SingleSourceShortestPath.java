package org.foresee.Algorithm.graph.ex;

import java.util.HashSet;
import java.util.LinkedList;

import org.foresee.Algorithm.graph.ex.ShortestPathGraph.Edge;
import org.foresee.Algorithm.graph.ex.ShortestPathGraph.Vertex;
import org.foresee.Algorithm.graph.ex.TopologicalSort.HasCircleException;

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
	 * 这是人名，不遵守小写开头的命名规则了，以示尊重*^o^* 
	 * Bellman-Ford算法解决一般情况下的单源最短路径问题，权重可以为负值。
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

	// @formatter:off
	/**
	 * 有向无环图的单源最短路径问题，根据结点的拓扑排序次序对带权重的有向无环图G进行边的松弛操作，
	 * 可以在θ(V + E)时间内计算出从单个源节点到所有结点之间的最短路径。
	 * 先对五环图进行拓扑排序，以便确定结点之间的线性次序。然后按照拓扑排序次序遍历一遍结点，对结点每条边松弛操作。 
	 * NOTE：DAG是Direct Acyclic Graph的缩写。
	 */
	public boolean dagShortestPaths(ShortestPathGraph graph, Vertex s) { 
	// @formatter:on
		TopologicalSort topoSort = new TopologicalSort();
		try {
			LinkedList<Vertex> sorted = topoSort.topologicalSort(graph);
			initializeSingleSource(graph, s); // 这里虽然改变了所有v.d值，但拓扑排序结果已经在sorted里了。
			for (Vertex u : sorted) {
				for (Edge e : u.adjacents) {
					relax(e);
				}
			}
			return true;
		} catch (HasCircleException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	// @formatter:off
	/**
	 * 人名Dijkstra，所以使用了首字母大写命名该方法。
	 * 要求所有边权重都是非负值。
	 * 算法运行过程是维持关键信息是一组结点集合S，从源结点s到集合S中的每个结点最短路径已经被找到，
	 * 重复从V-S中选择最短路径估计最小的u，对u出发的边进行松弛操作。
	 * 使用最小优先级队列Q保存结点集合，关键值为v.d值
	 * NOTE：程序里没有使用高性能的最小优先队列算法，所以当前程序性能较差。
	 */
	public boolean Dijkstra(ShortestPathGraph graph, Vertex s) {
	// @formatter:on
		for (Edge edge : graph.edges) {
			if (edge.weight < 0) {
				return false;
			}
		}
		initializeSingleSource(graph, s);
		HashSet<Vertex> S = new HashSet<>();
		// 这里用LinkedList，PriorityQueue需要Comparator，但更新v时不能自动再排序。遍历取最小实现简单
		LinkedList<Vertex> Q = new LinkedList<>(graph.vertexs);
		while (!Q.isEmpty()) {
			Vertex u = extractMin(Q);
			S.add(u);
			for (Edge edge : u.adjacents) {
				relax(edge);
			}
		}
		return true;
	}

	// @formatter:off
	/**
	 * 从队列中选择出路径估计最小的结点，LinkedList是Queue的一个实现。所以暂时使用最简单的实现，遍历取最小。
	 * 书里写用最小优先队列性能较差，最小二叉堆较好，斐波那契堆最好。
	 * 但维护复杂，每次对u的邻接节点更新v.d时都要更新队列，
	 * 遍历需要O(V)时间，导致Dijkstra算法运行时间O(V * V + E)。
	 * 使用二叉堆时，Dijkstra算法运行时间O(E * log(V))。
	 * 使用斐波那契堆时，Dijkstra算法运行时间O(V*log(V) + E)。
	 */
	private Vertex extractMin(LinkedList<Vertex> Q) {
	// @formatter:on
		Vertex u = Q.peek();
		for (Vertex v : Q) {
			if (v.d < u.d) {
				u = v;
			}
		}
		Q.remove(u);
		return u;
	}
}
