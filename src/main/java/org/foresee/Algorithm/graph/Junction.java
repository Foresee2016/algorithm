package org.foresee.Algorithm.graph;

import java.util.LinkedList;

import org.foresee.Algorithm.graph.AdjacencyGraph.Color;
import org.foresee.Algorithm.graph.AdjacencyGraph.Edge;
import org.foresee.Algorithm.graph.AdjacencyGraph.EdgeType;
import org.foresee.Algorithm.graph.AdjacencyGraph.Vertex;

public class Junction {
	int time=0;
	Vertex curRoot;
	Vertex curParent;
	/**
	 * 没写出来 
	 */
	public void findJunction(AdjacencyGraph graph) {
		if(!GraphType.isUndirectedGraph(graph)){
			throw new FindJunctionException("不是无向图，衔接点无定义");
		}
		for (Vertex u : graph.vertexs) {
			u.color = Color.white;
			u.parent = null;
		}
		time = 0;
		int rootCnt=0;
		for (Vertex u : graph.vertexs) {
			if (u.color == Color.white) {
				DeepFirstNode root=new DeepFirstNode(u);
				deepFirstSearchVisit(graph, u, root);
				// 根节点是衔接点，条件是它有至少两个子结点
				if(root.childs.size()>=2){
					u.isJunction=true;
				}
				rootCnt++;
			}
		}
		if(rootCnt!=1){
			throw new FindJunctionException("不是连通图，不能通过一个根访问到所有结点");
		}
//		LinkedList<Vertex> junctions=new LinkedList<>();
	}
	/**
	 * v是Gp的一个非根节点，v是G的衔接点当且仅当节点v有一个子结点s，且没有任何从结点s或任何s的后代结点指向v的真祖先的后向边。
	 * 这个条件不好实现，翻过来，v的所有子节点s已及后代结点，都有指向v直接祖先的后向边 
	 */
	protected void deepFirstSearchVisit(AdjacencyGraph graph, Vertex u, DeepFirstNode root) {
		time++;
		u.d = time;
		u.color = Color.gray;
		u.low=u.d;
		System.out.println("涂灰" + u.name + ", d = " + u.d);
		LinkedList<Edge> edges = u.adjacents;
//		boolean hasBkEdge=false;
		for (Edge edge : edges) {
			Vertex v = edge.link;
			if (!edge.visited) {
				edge.visited = true;
				switch (v.color) {
				case white:
					edge.edgeType=EdgeType.TreeEdge;
					System.out.println("Edge (" + u.name + "," + v.name + ") 是：树边");
					break;
				case gray:
					edge.edgeType=EdgeType.BackwardEdge;
					System.out.println("Edge (" + u.name + "," + v.name + ") 是：后向边");
					u.parent.low=v.d;
//					hasBkEdge=true;
					break;
				case black:
					if (u.d < v.d) {
						edge.edgeType=EdgeType.ForwardEdge;
						System.out.println("Edge (" + u.name + "," + v.name + ") 是：前向边");
					} else {
						edge.edgeType=EdgeType.LateralEdge;
						System.out.println("Edge (" + u.name + "," + v.name + ") 是：横向边");
					}
					break;
				default:
					System.out.println("这里应该执行不到" + v.color.name());
					break;
				}
			}
			if (v.color == Color.white) {
				v.parent = u;
				DeepFirstNode child=new DeepFirstNode(v);
				root.childs.add(child); // v是u的子节点，加到childs里
				deepFirstSearchVisit(graph, v, child); // 再搜索当前v的child并挂在v下
			}
		}
		u.color = Color.black;
		time++;
		u.f = time;
		System.out.println("涂黑" + u.name + ", f = " + u.f);
	}
	public class FindJunctionException extends RuntimeException{
		private static final long serialVersionUID = -3530768472822679682L;

		public FindJunctionException(String msg) {
			super(msg);
		}
	}
}
