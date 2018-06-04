package org.foresee.Algorithm.graph;

import java.util.LinkedList;

/**
 * 深度优先搜索的前驱子图形成一个由多棵深度优先树构成的深度优先森林。
 * 深度优先树的结构和visit递归调用的结构完全对应。
 * 另一个性质是，节点的发现时间和完成时间具有括号化结构，规整地嵌套在一起。 
 */
public class DeepFirstSearch {
	/**
	 * 对图所有节点进行深度优先的搜索
	 */
	int time;
	public void deepFirstSearch(Graph graph) {
		for (LinkedList<Vertex> list : graph.adjoin) {
			Vertex u=list.get(0);
			u.color=Color.white;
			u.p=null;
		}
		time=0;
		for (LinkedList<Vertex> list : graph.adjoin) {
			Vertex u=list.get(0);
			if(u.color==Color.white){
				deepFirstSearchVisit(graph, u);
			}
		}
	}
	/**
	 * 对节点u进行深度优先的搜索 
	 */
	public void deepFirstSearchVisit(Graph graph, Vertex u) {
		time++;
		u.d=time;
		u.color=Color.gray;
		LinkedList<Vertex> list=graph.adjoin.get(u.pos);
		for (Vertex v : list) {
			if(v.color==Color.white){
				v.p=u;
				deepFirstSearchVisit(graph, v);
			}
		}
		u.color=Color.black;
		time++;
		u.f=time;
	}
	
	
}
