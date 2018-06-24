package org.foresee.Algorithm.graph.ex;

import java.util.ArrayList;
import java.util.LinkedList;

import org.foresee.Algorithm.graph.ex.MaxStreamGraph.Edge;
import org.foresee.Algorithm.graph.ex.MaxStreamGraph.Vertex;

// @formatter:off
/**
 * 第26章，最大流算法。 
 *  FordFulkerson方法，称它为方法而不是算法，它包含几种运行时间各不相同的具体实现。
 * 该方法依赖于三种重要思想，这些思想是最大流最小切割定理的精髓。
 * FordFulkerson方法循环增加流的值，开始时所有结点f(u,v)=0，每次迭代，将图G的流值进行增加，方法就是在一个关联的
 * “残存网络”Gf中寻找一条“增广路径”。一旦知道图Gf中一条增广路径的边，就可以很容易辨别出G中一些具体的边，对这些边上
 * 流量进行修改，增加流的值。残存网络Gf由那些仍有空间对流量进行调整的边构成，cf为容量减当前流量得到的差值。
 * 还可能Gf包含G中不存在的边，可能缩减某些边的流量，对f(u,v)的缩减会将边(v,u)加入Gf，并将其残存容量
 * cf(u,v)=f(u,v)。一条边所能允许的反向流量最多将其正向流量抵消。相当于将已经发出来的发回去，来缩减流量
 * 
 * 终止条件：（最大流最小切割定理）一个流是最大流当且仅当其残存网络不包含增广路径。
 * 最大流最小切割定理：设f为流网络G=(V, E)中的一个流，该流网络的源节点为s，汇点为t，下面的条件等价：
 * 1.f是G的一个最大流。2.残存网络Gf不包括任何增广路径。3.|f|=c(S, T)，其中(S, T)是流网络G的某个切割。
 * 
 */
public class MaxStream {	 
// @formatter:on
	// @formatter:off
	/**
	 * 每次迭代，寻找增广路径p，使用p对流f进行增加。
	 * 算法导论里的非常概括，这里详细记录一下实现过程，因为有些不完全对应算法导论里的。
	 * 首先是把传入的图结构里当前流值赋0，根书里写的一样。
	 * 然后是生成残存网络，（见下面的函数注释）。
	 * 之后用广度优先搜索（见MaxStreamGraph.broadFirstSearch）寻找了一条s到t的可行路径，找不到就是终止条件了。
	 * 在可以搜索到某条路径时，沿该路径可以增大流，增大的量为该路径最细的位置，顺parent找到最细的位置的流量，
	 * 再为该路径每条边，以及残存网络中的边和反向边都更新值。
	 * 
	 * 运行时间：假如最后得到的最大流值为fx，总时间为O(E * fx)，因为找路径时间是O(V + E)可视为O(E)因为E至少和V相近，
	 * 如果存在容量为1的边且恰好每次都经过它，那么需要n次加1操作才能到达最大流值fx。
	 * 但找路径时我使用了广度优先搜索，后边Kdmonds-Karp定理说用O(V * E)次就行了，因为总会走短的。总时间O(V * E * E)
	 */
	public void basicFordFulkerson(MaxStreamGraph graph) {
	// @formatter:on
		for (Edge edge : graph.edges) {
			edge.f = 0;
		}
		generateResidual(graph);
		graph.broadFirstSearch(graph.sourse);
		while (graph.dst.d != MaxStreamGraph.INFINITE_DISTANCE) { // 存在一条路径
			int cfp = MaxStreamGraph.INFINITE_DISTANCE;
			Vertex v = graph.dst;
			while (v.parent != null) { // 找到这路径上最细的位置，它限制了整条路径所能增加的流值
				Edge p = v.parent.containResidual(v);
				if (p.c < cfp) {
					cfp = p.c;
				}
				v = v.parent;
			}
			v = graph.dst;
			while (v.parent != null) {
				Edge uv = v.parent.containAdjacent(v);
				Edge uvf = v.parent.containResidual(v);
				Edge vuf = v.containResidual(v.parent);
				if (uv != null) {
					uv.f = uv.f + cfp; // 该边上增加了流值
				}
				if (uvf != null) {
					uvf.c = uvf.c - cfp; // 残存网络中，可增大的流值余地，减小了。
				}
				if (vuf != null) {
					vuf.f = vuf.f + cfp; // 反向边的流值，增大了
				}
				v = v.parent;
			}
			graph.broadFirstSearch(graph.sourse);
		}
	}

	// @formatter:off
	/**
	 * 生成残存网络，注意，按书里的约定，不存在反向边，即(u,v)和(v,u)不同时存在。
	 * 这个方法只在最开始调用一次，所以当存在(u,v)时，添加了反向的(v,u)边且赋0值，原因是此时f(u,v)都是0
	 * 方法是按（26.2）的公式，但逻辑反了一下，求cf(u,v)时不是去找(v,u)再生成，而是在遍历到(v,u)时生成的，
	 * 这样循环简单一些，而且生成的东西一样。
	 * 对于不存在的边，即（26.2）里的“其它”，没有生成0值的边，而是直接忽略，方便调试，而且不会随边数量指数增加。
	 * NOTE：方法生成的graph.residuals.size()会是原图边数量的2倍，每一条边都加了反向边且初始为0值，因为它们
	 * 都是有可能需要反向边的，后期会单独维护graph.residuals的每一个，所以即使不会用到的反向边也有0值且存在着。
	 */
	protected void generateResidual(MaxStreamGraph graph) {
	// @formatter:on
		graph.residuals = new ArrayList<>();
		for (Vertex v : graph.vertexs) {
			v.residuals = new LinkedList<>();
		}
		for (Vertex u : graph.vertexs) {
			for (Vertex v : graph.vertexs) {
				if (u == v) {
					continue;
				}
				Edge uv = u.containAdjacent(v);
				if (uv != null) { // (u, v)属于E
					Edge edge = new Edge(u, v, uv.c - uv.f);
					u.residuals.add(edge);
					graph.residuals.add(edge);
					Edge edgeRev = new Edge(v, u, 0); // 可能存在反向边，赋0初始化
					v.residuals.add(edgeRev);
					graph.residuals.add(edgeRev);
				}
			}
		}
	}
}
