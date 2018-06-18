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
		
		GlobalShortestPath shortestPath=new GlobalShortestPath();
		double[][] L=shortestPath.slowAllPairsShortestPaths(graph);
		MatrixGraph.outputWeightMatrix(graph.weights);
		MatrixGraph.outputWeightMatrix(L);
		MatrixGraph.outputWeightMatrix(shortestPath.fasterAllPairsShortestPaths(graph));
		MatrixGraph.outputWeightMatrix(shortestPath.FloydWarshall(graph.weights));
		
		shortestPath.generatePi(L, graph.weights, graph.pi);
		graph.outputPi();
		System.out.print("Path from "+a.name+" to "+b.name+"：");
		graph.printAllPairsShortestPath(a, b); // 用结点，输出也用结点名标识
		System.out.println();
		System.out.print("Path from 0 to 1: ");
		graph.printAllPairsShortestPath(0, 1); //可以用结点的序号值，输出也通过序号值
		System.out.println();
	}
}
