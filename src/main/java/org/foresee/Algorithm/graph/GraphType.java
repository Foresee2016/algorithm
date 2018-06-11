package org.foresee.Algorithm.graph;

import org.foresee.Algorithm.graph.AdjacencyGraph.Edge;
import org.foresee.Algorithm.graph.AdjacencyGraph.Vertex;

public class GraphType {
	public static boolean isUndirectedGraph(AdjacencyGraph graph) {
		for (Vertex u : graph.vertexs) {
			for (Edge edgeU : u.adjacents) { // u所有能连到的，必须有回边
				Vertex v=edgeU.link;
				boolean back=false;
				for (Edge edgeV : v.adjacents) {
					if(edgeV.link==u){
						back=true;
						break;
					}
				}
				if(!back){
					return false;
				}
			}
		}
		return true;
	}
}
