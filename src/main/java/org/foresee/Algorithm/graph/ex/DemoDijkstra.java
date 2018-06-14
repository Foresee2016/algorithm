package org.foresee.Algorithm.graph.ex;

import java.util.Arrays;

import org.foresee.Algorithm.graph.ex.ShortestPathGraph.Vertex;

public class DemoDijkstra {
	public static void main(String[] args) {
		Vertex s=new Vertex("s");
		Vertex t=new Vertex("t");
		Vertex x=new Vertex("x");
		Vertex y=new Vertex("y");
		Vertex z=new Vertex("z");
		
		ShortestPathGraph graph=new ShortestPathGraph();
		graph.addVertexAndWeight(s, Arrays.asList(t, y), Arrays.asList(10.0, 5.0));
		graph.addVertexAndWeight(t, Arrays.asList(y, x), Arrays.asList(2.0, 1.0));
		graph.addVertexAndWeight(x, Arrays.asList(z), Arrays.asList(4.0));
		graph.addVertexAndWeight(y, Arrays.asList(t, x, z), Arrays.asList(3.0, 9.0, 2.0));
		graph.addVertexAndWeight(z, Arrays.asList(x, s), Arrays.asList(6.0, 7.0));
		
		SingleSourceShortestPath shortestPath=new SingleSourceShortestPath();
		if(shortestPath.Dijkstra(graph, s)){
			for (Vertex v : graph.vertexs) {
				System.out.println(v.name+", d = "+v.d);
			}
		}else{
			System.out.println("包含负权重的边，不能使用Dijkstra算法");
		}
	}
}
