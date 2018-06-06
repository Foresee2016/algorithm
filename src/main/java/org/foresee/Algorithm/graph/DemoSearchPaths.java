package org.foresee.Algorithm.graph;

import java.util.ArrayList;

import org.foresee.Algorithm.graph.AdjacencyGraph.Vertex;

public class DemoSearchPaths {
	public static void main(String[] args) {
		Vertex m=new Vertex("m");
		Vertex n=new Vertex("n");
		Vertex o=new Vertex("o");
		Vertex p=new Vertex("p");
		Vertex q=new Vertex("q");
		Vertex r=new Vertex("r");
		Vertex s=new Vertex("s");
		Vertex t=new Vertex("t");
		Vertex u=new Vertex("u");
		Vertex v=new Vertex("v");
		Vertex w=new Vertex("w");
		Vertex x=new Vertex("x");
		Vertex y=new Vertex("y");
		Vertex z=new Vertex("z");
		
		AdjacencyGraph graph=new AdjacencyGraph();
		graph.addVertex(m, q,r,x);
		graph.addVertex(n, o,q,u);
		graph.addVertex(o, r,s,v);
		graph.addVertex(p, o,s,z);
		graph.addVertex(q, t);
		graph.addVertex(r, u,y);
		graph.addVertex(s, r);
		graph.addVertex(t);
		graph.addVertex(u, t);
		graph.addVertex(v, w);
		graph.addVertex(w, z);
		graph.addVertex(x);
		graph.addVertex(y, v);
		graph.addVertex(z);
		
		SearchPaths searchPaths=new SearchPaths(graph, p, v);
		ArrayList<String> paths=searchPaths.searchPaths();
		for (String path : paths) {
			System.out.println(path);
		}
		searchPaths.printCanTo();
	}
}
