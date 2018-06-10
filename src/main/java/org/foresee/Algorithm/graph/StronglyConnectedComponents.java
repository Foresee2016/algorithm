package org.foresee.Algorithm.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

import org.foresee.Algorithm.graph.AdjacencyGraph.Color;
import org.foresee.Algorithm.graph.AdjacencyGraph.Edge;
import org.foresee.Algorithm.graph.AdjacencyGraph.EdgeType;
import org.foresee.Algorithm.graph.AdjacencyGraph.Vertex;
/**
 * 按graph中同名结点的结束时间f排序，再对转置后的graphT搜索。
 * 因为从完成最晚的强连通分量C的某个结点x开始，访问C中所有结点，此时graphT不可能包含从C到另一个连通分量C2的边，
 * 因此以x为根的树仅包含C里的所有结点。完成对C中的访问之后，才会再访问其它。
 * NOTE：从原深度搜索结果里生成树比较繁琐，此处重写深度优先搜索。
 * @author Foresee
 *
 */
public class StronglyConnectedComponents {
	public void stronglyConnectedComponents(AdjacencyGraph graph) {
		DeepFirstSearch dfs = new DeepFirstSearch();
		dfs.deepFirstSearch(graph);
		AdjacencyGraph graphT = Transposition.transpositon(graph);
		graphT.vertexs.sort(new Comparator<Vertex>() { 
			@Override
			public int compare(Vertex v1, Vertex v2) {
				int f1 = -1, f2 = -1;
				for (Vertex u : graph.vertexs) {
					if (u.name == v1.name) {
						f1 = u.f;
					}
					if (u.name == v2.name) {
						f2 = u.f;
					}
				}
				if (f1 < 0 || f2 < 0) {
					System.err.println("不应该找不到");
				}
				return f2 - f1; // 按从大到小排序
			}
		});
		System.out.println("----- T -----");
		ArrayList<DeepFirstNode> roots=dfsTree(graphT);
		// 输出graphT的深度优先森林，每组为一个强连通分量
		for (DeepFirstNode root : roots) {
			System.out.println("连通分量：");
			DeepFirstNode.outputRoot(root);
		}
	}
	int time=0;
	protected ArrayList<DeepFirstNode> dfsTree(AdjacencyGraph graph) {
		for (Vertex u : graph.vertexs) {
			u.color = Color.white;
			u.parent = null;
		}
		time = 0;
		ArrayList<DeepFirstNode> roots=new ArrayList<>();
		for (Vertex u : graph.vertexs) {
			if (u.color == Color.white) {
				DeepFirstNode root=new DeepFirstNode(u);
				deepFirstSearchVisit(graph, u, root);
				roots.add(root);
			}
		}
		return roots;
	}
	protected void deepFirstSearchVisit(AdjacencyGraph graph, Vertex u, DeepFirstNode root) {
		time++;
		u.d = time;
		u.color = Color.gray;
		System.out.println("涂灰" + u.name + ", d = " + u.d);
		LinkedList<Edge> edges = u.adjacents;
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
				root.childs.add(child);
				deepFirstSearchVisit(graph, v, child);
			}
		}
		u.color = Color.black;
		time++;
		u.f = time;
		System.out.println("涂黑" + u.name + ", f = " + u.f);
	}

	public static class DeepFirstNode{
		public Vertex v;
		public LinkedList<DeepFirstNode> childs;
		public DeepFirstNode(Vertex v) {
			this.v=v;
			childs=new LinkedList<>();
		}
		
		public static void outputRoot(DeepFirstNode root) {
			System.out.print(root.v.name+" child: ");
			for (DeepFirstNode child : root.childs) {
				System.out.print(child.v.name+",");
			}
			System.out.println();
			for (DeepFirstNode child : root.childs) {
				outputRoot(child);
			}
		}
	}
}
