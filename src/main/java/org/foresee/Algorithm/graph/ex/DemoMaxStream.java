package org.foresee.Algorithm.graph.ex;

import java.util.Arrays;

import org.foresee.Algorithm.graph.ex.MaxStreamGraph.Edge;
import org.foresee.Algorithm.graph.ex.MaxStreamGraph.Vertex;

public class DemoMaxStream {
	public static void main(String[] args) {
		Vertex s = new Vertex("s");
		Vertex t = new Vertex("t");
		Vertex v1 = new Vertex("v1");
		Vertex v2 = new Vertex("v2");
		Vertex v3 = new Vertex("v3");
		Vertex v4 = new Vertex("v4");
		MaxStreamGraph graph = new MaxStreamGraph();
		graph.sourse = s;
		graph.dst = t;
		graph.addVertexAndWeight(s, Arrays.asList(v1, v2), Arrays.asList(16, 13));
		graph.addVertexAndWeight(v1, Arrays.asList(v3), Arrays.asList(12));
		graph.addVertexAndWeight(v2, Arrays.asList(v1, v4), Arrays.asList(4, 14));
		graph.addVertexAndWeight(v3, Arrays.asList(v2, t), Arrays.asList(9, 20));
		graph.addVertexAndWeight(v4, Arrays.asList(v3, t), Arrays.asList(7, 4));
		graph.addVertex(t);

		MaxStream maxStream = new MaxStream();
		maxStream.basicFordFulkerson(graph);
		for (Edge edge : graph.edges) {
			System.out.println(edge.src.name + " -> " + edge.link.name + ", " + edge.f + " / " + edge.c);
		}
	}
}
