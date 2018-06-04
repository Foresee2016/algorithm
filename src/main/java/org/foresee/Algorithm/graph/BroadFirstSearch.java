package org.foresee.Algorithm.graph;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BroadFirstSearch {
	/**
	 * 从源节点s出发进行广度优先的搜索
	 * 可以计算出从s到所有可达节点之间的距离 。
	 * 广度优先搜索过程中将创建一棵广度优先搜索树，通过parent属性，各叶子连进来.
	 * 运行时间V+E
	 */
	public void broadFirstSearch(Graph graph, Vertex s) {
		for (List<Vertex> list : graph.adjoin) { //初始化，所有节点涂白色，节点距离设置为无穷，父节点设置NIL
			Vertex u=list.get(0);
			u.color=Color.white;
			u.d=Graph.INFINITY;
			u.p=null;
		}
		s.color=Color.gray; //源节点被发现，涂灰色
		s.d=0;
		s.p=null;
		Queue<Vertex> Q=new LinkedList<>(); //使用队列管理灰色节点
		System.out.println("入队："+s.num+", 深度："+s.d);
		if(!Q.offer(s)){ //源节点入队，从这里开始
			System.out.println("队列已满");
		}
		while(Q.size()!=0){
			Vertex u=Q.poll(); 
			System.out.println("出队："+u.num);
			// 考察该节点的所有邻接节点，如果是白色的，则它还没被发现，涂灰色表示已发现，设置距离和父节点，加入队列末尾
			for (Vertex v : graph.adjoin.get(u.pos)) {
				if(v.color==Color.white){
					v.color=Color.gray;
					v.d=u.d+1;
					v.p=u;
					if(!Q.offer(v)){
						System.out.println("队列已满");
					}
					System.out.println("染灰："+v.num+", 深度："+v.d+"，前驱："+v.p.num+", 入队"+v.num);
				}
			}
			// 该节点已经考察完邻接节点，涂黑色，表示它的邻接节点都已被发现了
			u.color=Color.black;
			System.out.println("涂黑："+u.num);
		}
	}
	
	/**
	 * 打印源节点s到目标节点v的最短路径上的所有节点, 沿parent向上打印即可。即打印出了广度优先树的一段树枝
	 */
	public void printPath(Vertex s, Vertex v) {
		if(v==s){
			System.out.print(s.num);
		}else if(v.p==null) {
			System.out.println("No Path from "+s.num+" to "+v.num);
		}else {
			printPath(s, v.p);
			System.out.print("-"+v.num);
		}
	}
	/**
	 * 一棵树T=(V, E)的直径定义为：树中所有最短路径的距离的最大值。
	 * 思路： 最简单的方法应该是遍历所有节点，广度优先搜索完，找最大距离。然后找所有最大距离里最大的那个。
	 * 算是暴力求解了，时间复杂度V*(V+E)
	 */
	public int diameter(Graph graph) {
		int diameter=-1;
		for (LinkedList<Vertex> linkedList : graph.adjoin) { 
			broadFirstSearch(graph, linkedList.getFirst());
			for (LinkedList<Vertex> linkedList2 : graph.adjoin) { // 再遍历每一个，看谁距离最大
				Vertex vertex=linkedList2.getFirst();
				if (vertex.d>diameter) {
					diameter=vertex.d;
				}
			}
			System.out.println("---");
		}
		return diameter;
	}
	
}
