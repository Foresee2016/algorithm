package org.foresee.Algorithm.graph.ex;

import java.util.Arrays;

import org.foresee.Algorithm.graph.ex.ShortestPathGraph.Vertex;

/**
 * 使用简单示例图，验证Bellman-Ford程序写的对不对
 */
public class DemoBellmanFord {
	public static void main(String[] args) {
		Vertex s = new Vertex("s");
		Vertex t = new Vertex("t");
		Vertex x = new Vertex("x");
		Vertex y = new Vertex("y");
		Vertex z = new Vertex("z");
		ShortestPathGraph graph = new ShortestPathGraph();
		graph.addVertexAndWeight(s, Arrays.asList(t, y), Arrays.asList(6.0, 7.0));
		graph.addVertexAndWeight(t, Arrays.asList(x, y, z), Arrays.asList(5.0, 8.0, -4.0));
		graph.addVertexAndWeight(x, Arrays.asList(t), Arrays.asList(-2.0));
		graph.addVertexAndWeight(y, Arrays.asList(x, z), Arrays.asList(-3.0, 9.0));
		graph.addVertexAndWeight(z, Arrays.asList(x, s), Arrays.asList(7.0, 2.0));

		SingleSourceShortestPath shortestPath = new SingleSourceShortestPath();
		shortestPath.BellmanFord(graph, s);
		for (Vertex v : graph.vertexs) {
			System.out.println(v.name + ", d = " + v.d);
		}
	}
}
