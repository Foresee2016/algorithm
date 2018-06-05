package org.foresee.Algorithm.graph;

import java.util.LinkedList;
import java.util.Queue;

import org.foresee.Algorithm.graph.AdjacencyGraph.Color;
import org.foresee.Algorithm.graph.AdjacencyGraph.Edge;
import org.foresee.Algorithm.graph.AdjacencyGraph.Vertex;

public class BroadFirstSearch {
	public static final int INFINITE_DISTANCE=99999;
	public void broadFirstSearch(AdjacencyGraph graph, Vertex s) {
		for (Vertex u : graph.vertexs) {
			u.color=Color.white;
			u.d=INFINITE_DISTANCE;
			u.parent=null;
		}
		s.color=Color.gray; //源节点被发现，涂灰色
		s.d=0;
		s.parent=null;
		Queue<Vertex> Q=new LinkedList<>(); //使用队列管理灰色节点
		System.out.println("入队："+s.name+", 深度："+s.d);
		if(!Q.offer(s)){ //源节点入队，从这里开始
			System.out.println("队列已满");
		}
		while(Q.size()!=0){
			Vertex u=Q.poll(); 
			System.out.println("出队："+u.name);
			// 考察该节点的所有邻接节点，如果是白色的，则它还没被发现，涂灰色表示已发现，设置距离和父节点，加入队列末尾
			for (Edge e : u.adjacents) {
				Vertex v=e.link;
				if(v.color==Color.white){
					v.color=Color.gray;
					v.d=u.d+1;
					v.parent=u;
					if(!Q.offer(v)){
						System.out.println("队列已满");
					}
					System.out.println("染灰："+v.name+", 深度："+v.d+"，前驱："+v.parent.name+", 入队"+v.name);
				}
			}
			// 该节点已经考察完邻接节点，涂黑色，表示它的邻接节点都已被发现了
			u.color=Color.black;
			System.out.println("涂黑："+u.name);
		}
	}
	/**
	 * 打印源节点s到目标节点v的最短路径上的所有节点, 沿parent向上打印即可。即打印出了广度优先树的一段树枝
	 */
	public void printPath(Vertex s, Vertex v) {
		if(v==s){
			System.out.print(s.name);
		}else if(v.parent==null) {
			System.out.println("No Path from "+s.name+" to "+v.name);
		}else {
			printPath(s, v.parent);
			System.out.print("-"+v.name);
		}
	}
	/**
	 * 一棵树T=(V, E)的直径定义为：树中所有最短路径的距离的最大值。
	 * 思路： 最简单的方法应该是遍历所有节点，广度优先搜索完，找最大距离。然后找所有最大距离里最大的那个。
	 * 算是暴力求解了，时间复杂度V*(V+E)
	 */
	public int diameter(AdjacencyGraph graph) {
		int diameter=-1;
		for (Vertex v : graph.vertexs) {
			broadFirstSearch(graph, v);
			for (Vertex cur : graph.vertexs) { // 再遍历每一个结点，看谁距离最大
				if(cur.d>diameter){
					diameter=cur.d;
				}
			}
			System.out.println("---");
		}
		return diameter;
	}
}
