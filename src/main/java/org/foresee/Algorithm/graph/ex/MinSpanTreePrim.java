package org.foresee.Algorithm.graph.ex;

import java.util.HashSet;

import org.foresee.Algorithm.graph.ex.MinSpanTreeGraph.Edge;
/**
 * Prim算法所具有的一个性质是集合A中的边总是构成一棵树，树从任意一个根结点r长大，一直长大到覆盖V中所有结点为止。
 * 算法每一步在连接集合A和A之外的结点的所有边中，选择一条轻量级边加入到A中。这条规则加入的边都是对A安全的边。
 * 本策略也属于贪心策略
 */
public class MinSpanTreePrim {
	/**
	 * NOTE：我看不懂这个算法的伪代码，暂时跳过吧。
	 * Prim算法使用斐波那契堆，时间复杂度为O(E + V * log(V))，如果用二叉最小堆，O(E * log(V))，和Kruskal相同
	 * 当V数量远远小于E时，比使用二叉堆的Kruskal算法有很大改进
	 */
	public static HashSet<Edge> mstPrim(MinSpanTreeGraph graph) {
		return null;
	}
}
