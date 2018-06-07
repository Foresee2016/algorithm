package org.foresee.Algorithm.graph;

import org.foresee.Algorithm.graph.AdjacencyGraph.Vertex;

public class DemoTransposition {
	public static void main(String[] args) {
		Vertex a=new Vertex("a");
		Vertex b=new Vertex("b");
		Vertex c=new Vertex("c");
		Vertex d=new Vertex("d");
		Vertex e=new Vertex("e");
		Vertex f=new Vertex("f");
		Vertex g=new Vertex("g");
		Vertex h=new Vertex("h");
		AdjacencyGraph graph=new AdjacencyGraph();
		graph.addVertex(a, b);
		graph.addVertex(b, c,e,f);
		graph.addVertex(c, d,g);
		graph.addVertex(d, c,h);
		graph.addVertex(e, a,f);
		graph.addVertex(f, g);
		graph.addVertex(g, f,h);
		graph.addVertex(h, h);
		
		AdjacencyGraph graphT=Transposition.transpositon(graph);
		Vertex h2=null;
		for (Vertex v : graphT.vertexs) { // 找到转置后图中的a
			if(v.name==h.name){
				h2=v;
			}
		}
		BroadFirstSearch bfs=new BroadFirstSearch();
		bfs.broadFirstSearch(graphT, h2);
	}
}
