package org.foresee.Algorithm.graph;

import java.util.LinkedList;

import org.foresee.Algorithm.graph.AdjacencyGraph.Vertex;

public class DemoTopoSort {
	public static void main(String[] args) {
		Vertex shirt=new Vertex("衬衣");
		Vertex necktie=new Vertex("领带");
		Vertex jacket=new Vertex("夹克");
		Vertex belt=new Vertex("腰带");
		Vertex underwear=new Vertex("内裤");
		Vertex trousers=new Vertex("裤子");
		Vertex socks=new Vertex("袜子");
		Vertex shoes=new Vertex("鞋");
		Vertex watch=new Vertex("手表");
		
		AdjacencyGraph graph=new AdjacencyGraph();
		graph.addVertex(shirt, necktie, belt);
		graph.addVertex(necktie, jacket);
		graph.addVertex(jacket);
		graph.addVertex(belt, jacket);
		graph.addVertex(watch);
		graph.addVertex(underwear, trousers, shoes);
		graph.addVertex(trousers, belt, shoes);
		graph.addVertex(socks, shoes);
		graph.addVertex(shoes);
		TopologicalSort sort=new TopologicalSort();
		LinkedList<Vertex> sorted=sort.topologicalSort(graph);
		for (Vertex v : sorted) {
			System.out.println(v.name);
		}
	}
}
