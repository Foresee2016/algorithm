package org.foresee.Algorithm.graph.ex;

import java.util.Arrays;

import org.foresee.Algorithm.graph.ex.ShortestPathGraph.Vertex;

public class DemoDagShortestPath {
	public static void main(String[] args) {
		Vertex r = new Vertex("r");
		Vertex s = new Vertex("s");
		Vertex t = new Vertex("t");
		Vertex x = new Vertex("x");
		Vertex y = new Vertex("y");
		Vertex z = new Vertex("z");
		ShortestPathGraph graph = new ShortestPathGraph();
		graph.addVertexAndWeight(r, Arrays.asList(s, t), Arrays.asList(5.0, 3.0));
		graph.addVertexAndWeight(s, Arrays.asList(t, x), Arrays.asList(2.0, 6.0));
		graph.addVertexAndWeight(t, Arrays.asList(x, y, z), Arrays.asList(7.0, 4.0, 2.0));
		graph.addVertexAndWeight(x, Arrays.asList(y, z), Arrays.asList(-1.0, 1.0));
		graph.addVertexAndWeight(y, Arrays.asList(z), Arrays.asList(-2.0));
		graph.addVertex(z);

		SingleSourceShortestPath shortestPath = new SingleSourceShortestPath();
		shortestPath.dagShortestPaths(graph, s);
		for (Vertex v : graph.vertexs) {
			System.out.println(v.name + ", d = " + v.d);
		}
		System.out.println("--------我是分割线--------");
		testMinusCircle();
	}
	/**
	 * 验证dagShortestPath算法能识别负权重值的环路
	 */
	public static void testMinusCircle() {
		Vertex s=new Vertex("s");
		Vertex a=new Vertex("a");
		Vertex b=new Vertex("b");
		Vertex c=new Vertex("c");
		Vertex d=new Vertex("d");
		Vertex e=new Vertex("e");
		Vertex f=new Vertex("f");
		Vertex g=new Vertex("g");
		ShortestPathGraph graph=new ShortestPathGraph();
		graph.addVertexAndWeight(s, Arrays.asList(a,c,e), Arrays.asList(3.0, 5.0, 2.0));
		graph.addVertexAndWeight(a, Arrays.asList(b), Arrays.asList(-4.0));
		graph.addVertexAndWeight(b, Arrays.asList(g), Arrays.asList(4.0));
		graph.addVertexAndWeight(c, Arrays.asList(d), Arrays.asList(6.0));
		graph.addVertexAndWeight(d, Arrays.asList(c, g), Arrays.asList(-3.0, 8.0));
		graph.addVertexAndWeight(e, Arrays.asList(f), Arrays.asList(3.0));
		graph.addVertexAndWeight(f, Arrays.asList(e, g), Arrays.asList(-6.0, 7.0));
		graph.addVertex(g);
		
		SingleSourceShortestPath shortestPath = new SingleSourceShortestPath();
		if (shortestPath.dagShortestPaths(graph, s)) {
			for (Vertex v : graph.vertexs) {
				System.out.println(v.name + ", d = " + v.d);
			}
		} else {
			System.out.println("testMinusCircle()：包含负权重值的环路，不能求得最短路径。");
		}
	}
}
