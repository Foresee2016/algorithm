package org.foresee.Algorithm.graph;

import org.foresee.Algorithm.graph.AdjacencyGraph.Vertex;

public class DemoBfs {
	public static void main(String[] args) {
		Vertex r=new Vertex("r");
		Vertex s=new Vertex("s");
		Vertex t=new Vertex("t");
		Vertex u=new Vertex("u");
		Vertex v=new Vertex("v");
		Vertex w=new Vertex("w");
		Vertex x=new Vertex("x");
		Vertex y=new Vertex("y");
		AdjacencyGraph graph=new AdjacencyGraph();
		graph.addVertex(r, s,v);
		graph.addVertex(s, r,w);
		graph.addVertex(t, u,w,x);
		graph.addVertex(u, t,x,y);
		graph.addVertex(v, r);
		graph.addVertex(w, s,t,x);
		graph.addVertex(x, t,u,w,y);
		graph.addVertex(y, u,x);
		BroadFirstSearch bfs=new BroadFirstSearch();
		bfs.broadFirstSearch(graph, s);
		bfs.printPath(s, y);
		System.out.println();
		System.out.println(bfs.diameter(graph));
	}
}
