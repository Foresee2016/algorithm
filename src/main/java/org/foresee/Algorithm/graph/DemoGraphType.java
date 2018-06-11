package org.foresee.Algorithm.graph;

import org.foresee.Algorithm.graph.AdjacencyGraph.Vertex;

public class DemoGraphType {
	public static void main(String[] args) {
		Vertex a=new Vertex("a");
		Vertex b=new Vertex("b");
		Vertex c=new Vertex("c");
		AdjacencyGraph directGraph=new AdjacencyGraph();
		directGraph.addVertex(a, b);
		directGraph.addVertex(b, c);
		directGraph.addVertex(c);
		AdjacencyGraph undirectGraph=new AdjacencyGraph();
		Vertex x=new Vertex("x");
		Vertex y=new Vertex("y");
		Vertex z=new Vertex("z");
		undirectGraph.addVertex(x, y);
		undirectGraph.addVertex(y, x);
		undirectGraph.addVertex(z);
		boolean isUndirectGraph;
		isUndirectGraph=GraphType.isUndirectedGraph(directGraph);
		System.out.println("directGraph:"+isUndirectGraph);
		isUndirectGraph=GraphType.isUndirectedGraph(undirectGraph);
		System.out.println("undirectGraph:"+isUndirectGraph);
	}
}
