package org.foresee.Algorithm.graph;

public class DemoSimple {

	public static void main(String[] args) {
		Vertex v1=new Vertex(1);
		Vertex v2=new Vertex(2);
		Vertex v3=new Vertex(3);
		Vertex v4=new Vertex(4);
		Vertex v5=new Vertex(5);
		Graph graph=new Graph();
		graph.addVertex(v1, v2,v5);
		graph.addVertex(v2, v1,v5,v3,v4);
		graph.addVertex(v3, v2,v4);
		graph.addVertex(v4, v2,v5,v3);
		graph.addVertex(v5, v4,v1,v2);
		graph.broadFirstSearch(v1);
		graph.printPath(v1, v4);
	}

}
