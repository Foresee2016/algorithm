package org.foresee.Algorithm.graph.ex;

import org.foresee.Algorithm.graph.ex.MatrixGraph.Vertex;

public class DemoGlobalShortestPath {
	public static void main(String[] args) {
		Vertex a=new Vertex("a");
		Vertex b=new Vertex("b");
		Vertex c=new Vertex("c");
		Vertex d=new Vertex("d");
		Vertex e=new Vertex("e");
		MatrixGraph graph=new MatrixGraph(5);
		graph.addVertex(a,b,c,d,e);
		graph.addEdge(a, b, 3);
		graph.addEdge(a, c, 8);
		graph.addEdge(a, e, -4);
		graph.addEdge(b, d, 1);
		graph.addEdge(b, e, 7);
		graph.addEdge(c, b, 4);
		graph.addEdge(d, a, 2);
		graph.addEdge(d, c, -5);
		graph.addEdge(e, d, 6);
		
//		MatrixGraph.outputWeightMatrix(graph.weights);
		GlobalShortestPath shortestPath=new GlobalShortestPath();
		MatrixGraph.outputWeightMatrix(shortestPath.slowAllPairsShortestPaths(graph));
	}
}
