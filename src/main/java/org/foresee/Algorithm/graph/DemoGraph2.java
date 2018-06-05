package org.foresee.Algorithm.graph;

public class DemoGraph2 {
	public static void main(String[] args) {
		Vertex u=new Vertex("u");
		Vertex v=new Vertex("v");
		Vertex w=new Vertex("w");
		Vertex x=new Vertex("x");
		Vertex y=new Vertex("y");
		Vertex z=new Vertex("z");
		Graph graph=new Graph();
		graph.addVertex(u, v,x);
		graph.addVertex(v, y);
		graph.addVertex(w, y,z);
		graph.addVertex(x, v);
		graph.addVertex(y, x);
		graph.addVertex(z, z);
		DeepFirstSearch dfs=new DeepFirstSearch();
		dfs.deepFirstSearch2(graph);
	}
}
