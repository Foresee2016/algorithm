package org.foresee.Algorithm.graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.foresee.Algorithm.graph.AdjacencyGraph.Edge;
import org.foresee.Algorithm.graph.AdjacencyGraph.Vertex;

public class Transposition {
	public static AdjacencyGraph transpositon(AdjacencyGraph graph) {
		AdjacencyGraph graphT=new AdjacencyGraph();
		Map<String, Vertex> nameMap=new HashMap<>();
		for (Vertex u : graph.vertexs) {
			Vertex u2=new Vertex(u.name);
			nameMap.put(u.name, u2);
		}
		for (Vertex u : graph.vertexs) {
			Vertex u2=nameMap.get(u.name);
			LinkedList<Edge> edges=u.adjacents;
			for (Edge edge : edges) {
				Vertex v=edge.link;
				Vertex v2=nameMap.get(v.name);
				v2.adjacents.add(new Edge(u2));
			}
		}
		for (Vertex u2 : nameMap.values()) {
			graphT.vertexs.add(u2);
		}
		return graphT;
	}
}
