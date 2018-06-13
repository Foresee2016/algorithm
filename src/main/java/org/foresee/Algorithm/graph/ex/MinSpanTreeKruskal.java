package org.foresee.Algorithm.graph.ex;

import java.util.Comparator;
import java.util.HashSet;

import org.foresee.Algorithm.graph.ex.MinSpanTreeGraph.Edge;
import org.foresee.Algorithm.graph.ex.MinSpanTreeGraph.Vertex;

/**
 * 针对寻找安全边的算法，Kruskal算法，集合A是一个森林，其结点就是给定图的结点。每次加入到集合A中的安全边永远是权重最小的
 * 连接两个不同分量的边。
 * 办法是：在所有连接森林中两颗不同树的边里面，找到权重最小的边(u,v)，
 * 和计算连通分量的算法类似，使用一个不相交集合数据结构维护几个互不相交的元素集合，每隔几何代表当前森林的一棵树。
 * Find-Set(u)返回包含元素u的集合的代表元素，测试Find-Set(u)和Find-Set(v)的返回值判断它们是否在同一树中。
 * Union合并两颗树
 */
public class MinSpanTreeKruskal {
	/**
	 * 使用Kruskal算法计算最小生成树，书里入口参数有w表示边权重，这里作为边属性了，不传入。
	 * 返回边集合，其中每条边都属于最小生成树。
	 * 算法时间复杂度O(E * log(V))
	 */
	public static HashSet<Edge> mstKruskal(MinSpanTreeGraph graph) {
		HashSet<Edge> A=new HashSet<>();
		for (Vertex v : graph.vertexs) {
			MinSpanTreeGraph.makeSet(v);
		}
		graph.edges.sort(new Comparator<Edge>() {
			@Override
			public int compare(Edge o1, Edge o2) {
				return (int) (o1.weight - o2.weight); // 升序（即书里nondecreasing）
			}
		});
		for (Edge edge : graph.edges) {
			Vertex u=edge.src;
			Vertex v=edge.link;
			// u，v处于不同树时，连接两棵树。处于相同树时，已经连接了，不用再添加
			if(MinSpanTreeGraph.findSet(u)!=MinSpanTreeGraph.findSet(v)){ 
				A.add(edge);
				MinSpanTreeGraph.union(u, v);
				System.out.println("添加边：("+edge.src.name+", "+edge.link.name+")");
			}
		}
		return A;
	}
}
