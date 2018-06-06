package org.foresee.Algorithm.graph;

import java.util.LinkedList;

import org.foresee.Algorithm.graph.AdjacencyGraph.Color;
import org.foresee.Algorithm.graph.AdjacencyGraph.Edge;
import org.foresee.Algorithm.graph.AdjacencyGraph.Vertex;

/**
 * 拓扑排序是一种线性次序，该次序满足：如果图G包含边(u,v)，则结点u在拓扑排序中处于v的前面。
 * 如果G包含环路，则不能排序。可以将它看做将图所有结点排成一条线，所有边从左向右指。
 * 在拓扑排序过程中，判断是否有环路，如果有则抛出异常。根据引理22.11，G=(V,E)包含后向边时，图中有环路
 */
public class TopologicalSort {
	private int time;
	/**
	 * 对图进行拓扑排序，图必须没有环路，否则不能执行。
	 * 返回排序后结点次序 
	 * @throws HasCircleException 当图中有环路时，不能拓扑排序
	 */
	public LinkedList<Vertex> topologicalSort(AdjacencyGraph graph) {
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
	/**
	 * 对节点u进行深度优先的搜索
	 * @throws HasCircleException 当图中有环路时，不能拓扑排序
	 */
	protected void deepFirstSearchVisit(AdjacencyGraph graph, Vertex u, LinkedList<Vertex> sorted) {
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
