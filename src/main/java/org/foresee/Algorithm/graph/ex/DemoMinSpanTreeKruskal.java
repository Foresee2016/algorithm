package org.foresee.Algorithm.graph.ex;

import java.util.HashSet;

import org.foresee.Algorithm.graph.ex.MinSpanTreeGraph.Edge;
import org.foresee.Algorithm.graph.ex.MinSpanTreeGraph.Vertex;

public class DemoMinSpanTreeKruskal {
	public static void main(String[] args) {
		MinSpanTreeGraph graph = new MinSpanTreeGraph();
		Vertex a = new Vertex("a");
		Vertex b = new Vertex("b");
		Vertex c = new Vertex("c");
		Vertex d = new Vertex("d");
		Vertex e = new Vertex("e");
		Vertex f = new Vertex("f");
		Vertex g = new Vertex("g");
		Vertex h = new Vertex("h");
		Vertex i = new Vertex("i");
		graph.addEdge(new Edge(a, b, 4));
		graph.addEdge(new Edge(a, h, 8));
		graph.addEdge(new Edge(b, h, 11));
		graph.addEdge(new Edge(b, c, 8));
		graph.addEdge(new Edge(c, i, 2));
		graph.addEdge(new Edge(c, d, 7));
		graph.addEdge(new Edge(c, f, 4));
		graph.addEdge(new Edge(d, e, 9));
		graph.addEdge(new Edge(d, f, 14));
		graph.addEdge(new Edge(e, f, 10));
		graph.addEdge(new Edge(f, g, 2));
		graph.addEdge(new Edge(g, h, 1));
		graph.addEdge(new Edge(g, i, 6));
		graph.addEdge(new Edge(h, i, 7));

		HashSet<Edge> mst = MinSpanTreeKruskal.mstKruskal(graph);
		System.out.println("---");
		System.out.println("最小生成树的边数量："+mst.size());
	}
}
