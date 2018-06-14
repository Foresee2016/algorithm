package org.foresee.Algorithm.graph.ex;

import java.util.LinkedList;

import org.foresee.Algorithm.graph.ex.ShortestPathGraph.Color;
import org.foresee.Algorithm.graph.ex.ShortestPathGraph.Edge;
import org.foresee.Algorithm.graph.ex.ShortestPathGraph.Vertex;

/**
 * 换了个图数据结构，拓扑排序也得换过来，给dagShortestPath()用
 */
public class TopologicalSort {
	private int time;
	/**
	 * 对图进行拓扑排序，图必须没有环路，否则不能执行。
	 * 返回排序后结点次序 
	 * @throws HasCircleException 当图中有环路时，不能拓扑排序
	 */
	public LinkedList<Vertex> topologicalSort(ShortestPathGraph graph) {
		LinkedList<Vertex> sorted=new LinkedList<>();
		for (Vertex u : graph.vertexs) {
			u.color = Color.white;
			u.parent = null;
		}
		time = 0;
		for (Vertex u : graph.vertexs) {
			if (u.color == Color.white) {
				deepFirstSearchVisit(graph, u, sorted);
			}
		}
		return sorted;
	}
	protected void deepFirstSearchVisit(ShortestPathGraph graph, Vertex u, LinkedList<Vertex> sorted) {
		time++;
		u.d = time;
		u.color = Color.gray;
		LinkedList<Edge> edges = u.adjacents;
		for (Edge edge : edges) {
			Vertex v = edge.link;
			if(!edge.visited){
				edge.visited=true;
				if(v.color==Color.gray){
					throw new HasCircleException("产生后向边，有环路，不能拓扑排序");
				}
			}
			if (v.color == Color.white) {
				v.parent = u;
				deepFirstSearchVisit(graph, v, sorted);
			}
		}
		u.color = Color.black;
		time++;
		u.f = time;
		sorted.addFirst(u);
	}
	public static class HasCircleException extends RuntimeException{
		private static final long serialVersionUID = -9176102678371294550L;
		public HasCircleException(String msg) {
			super(msg);
		}
	}
}
