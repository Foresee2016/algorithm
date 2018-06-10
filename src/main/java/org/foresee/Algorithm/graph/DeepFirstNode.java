package org.foresee.Algorithm.graph;

import java.util.LinkedList;

import org.foresee.Algorithm.graph.AdjacencyGraph.Vertex;

public class DeepFirstNode{
	public Vertex v;
	public LinkedList<DeepFirstNode> childs;
	public DeepFirstNode(Vertex v) {
		this.v=v;
		childs=new LinkedList<>();
	}
	
	public static void outputRoot(DeepFirstNode root) {
		System.out.print(root.v.name+" child: ");
		for (DeepFirstNode child : root.childs) {
			System.out.print(child.v.name+",");
		}
		System.out.println();
		for (DeepFirstNode child : root.childs) {
			outputRoot(child);
		}
	}
}