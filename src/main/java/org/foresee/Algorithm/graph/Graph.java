package org.foresee.Algorithm.graph;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 有两种表示图的方法：邻接链表，邻接矩阵 邻接链表适合于表示稀疏图（边E的数目远小于定点V的平方）
 * 邻接矩阵适合表示稠密图（边E的数目接近V的平方），另外，如果需要快速判断任意两个结点是否有边相连，适合邻接矩阵
 */
public class Graph {
	public static final int INFINITY=1000;
	public ArrayList<LinkedList<Vertex>> adjoin;
	public Graph() {
		adjoin = new ArrayList<>();
	}
	public void addVertex(Vertex v, Vertex... linkTo) {
		LinkedList<Vertex> list=new LinkedList<>();
		list.add(v);//在开头加上这个新添加的节点
		for (Vertex vertex : linkTo) {
			list.add(vertex);
		}
		adjoin.add(list);
		v.pos=adjoin.size()-1;
	}
	
}
