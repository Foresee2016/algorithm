package org.foresee.Algorithm.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import org.foresee.Algorithm.graph.AdjacencyGraph.Color;
import org.foresee.Algorithm.graph.AdjacencyGraph.Edge;
import org.foresee.Algorithm.graph.AdjacencyGraph.EdgeType;
import org.foresee.Algorithm.graph.AdjacencyGraph.Vertex;

/**
 * 课后练习22.4-2，寻找从p到v有几条简单路径。
 * 思路：从p开始做深度优先搜索，找到v时则将该条路径上所有结点加入canTo这个Set中。
 * 或者找到了可以达到canTo中的结点，因为canTo中的结点均能到达v，所以该路径也能到达v，并将该路径上所有结点加入canTo
 * NOTE：存在问题，加入canTo时，并没有记录该可达到v的结点是以何种路径到达v的，所以之后到canTo中结点时，
 * 只能知道能到v，不能再通过该可达到v的结点沿其路径到v。可以在加入Set时记录额外的如何到达信息，以求出完整路径。
 */
public class SearchPaths {
	private int time;
	private AdjacencyGraph graph;
	private Vertex from;
	private Vertex to;
	private ArrayList<String> paths;
	private HashSet<Vertex> canTo; // 可以到达to结点的结点
	public SearchPaths(AdjacencyGraph graph, Vertex from, Vertex to) {
		super();
		this.graph = graph;
		this.from = from;
		this.to = to;
	}
	public ArrayList<String> searchPaths(){
		paths=new ArrayList<>();
		canTo=new HashSet<>();
		for (Vertex u : graph.vertexs) {
			u.color = Color.white;
			for (Edge edge : u.adjacents) {
				edge.visited=false;
			}
			u.parent = null;
		}
		time = 0;
		searchPaths(from);
		return paths;
	}
	public void searchPaths(Vertex u) {
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
					if(v==to || canTo.contains(v)){
						addPath(u, v);
					}
					break;
				case gray:
					edge.edgeType=EdgeType.BackwardEdge;
					System.out.println("Edge (" + u.name + "," + v.name + ") 是：后向边");
					break;
				case black:
					if (u.d < v.d) {
						edge.edgeType=EdgeType.ForwardEdge;
						System.out.println("Edge (" + u.name + "," + v.name + ") 是：前向边");
						if(v==to || canTo.contains(v)){
							addPath(u, v);
						}
					} else {
						edge.edgeType=EdgeType.LateralEdge;
						System.out.println("Edge (" + u.name + "," + v.name + ") 是：横向边");
						if(v==to || canTo.contains(v)){
							addPath(u, v);
						}
					}
					break;
				default:
					System.out.println("这里应该执行不到" + v.color.name());
					break;
				}
			}
			if (v.color == Color.white) {
				v.parent = u;
				searchPaths(v);
			}
		}
		u.color = Color.black;
		time++;
		u.f = time;
		System.out.println("涂黑" + u.name + ", f = " + u.f);
	}
	private void addPath(Vertex u, Vertex v) {
		String path="->"+v.name;;
		Vertex vp=u;
		while(vp!=null){
			if(vp==from){
				path=vp.name+path;
				break;
			}
			path="->"+vp.name+path;
			if(!canTo.contains(vp)){
				canTo.add(vp);
			}
			vp=vp.parent;
		}
		paths.add(path);
	}
	public void printCanTo() {
		System.out.println("----- CanTo -----");
		for (Vertex v : canTo) {
			System.out.println(v.name);
		}
		System.out.println("-----");
	}
}
