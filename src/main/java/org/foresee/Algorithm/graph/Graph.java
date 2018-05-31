package org.foresee.Algorithm.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 有两种表示图的方法：邻接链表，邻接矩阵 邻接链表适合于表示稀疏图（边E的数目远小于定点V的平方）
 * 邻接矩阵适合表示稠密图（边E的数目接近V的平方），另外，如果需要快速判断任意两个结点是否有边相连，适合邻接矩阵
 */
public class Graph {
	public static void main(String[] args) {
		Vertex v1=new Vertex(1);
		Vertex v2=new Vertex(2);
		Vertex v3=new Vertex(3);
		Vertex v4=new Vertex(4);
		Vertex v5=new Vertex(5);
		Graph graph=new Graph();
		graph.addVertex(v1, v2,v5);
		graph.addVertex(v2, v1,v5,v3,v4);
		graph.addVertex(v3, v2,v4);
		graph.addVertex(v4, v2,v5,v3);
		graph.addVertex(v5, v4,v1,v2);
		graph.broadFirstSearch(v1);
		graph.printPath(v1, v4);
	}
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
	/**
	 * 对图所有节点进行深度优先的搜索
	 */
	int time;
	public void deepFirstSearch() {
		for (LinkedList<Vertex> list : adjoin) {
			Vertex u=list.get(0);
			u.color=Color.white;
			u.p=null;
		}
		time=0;
		for (LinkedList<Vertex> list : adjoin) {
			Vertex u=list.get(0);
			if(u.color==Color.white){
				deepFirstSearchVisit(u);
			}
		}
	}
	/**
	 * 对节点u进行深度优先的搜索 
	 */
	public void deepFirstSearchVisit(Vertex u) {
		time++;
		u.d=time;
		u.color=Color.gray;
		LinkedList<Vertex> list=adjoin.get(u.pos);
		for (Vertex v : list) {
			if(v.color==Color.white){
				v.p=u;
				deepFirstSearchVisit(v);
			}
		}
		u.color=Color.black;
		time++;
		u.f=time;
	}
	/**
	 * 练习22.2-9：给出一个O(V+E)时间的算法来计算图G中一条这样的路径，该路径正反向通过E中每条边恰好一次。
	 * 思路：以任意节点s按广度优先搜索，搜索后找到了一棵搜索树，对s到任意除节点s以外的节点的路径是有作用的，
	 * 其它路径都不会产生决定作用，走过去再走回来就行了。写出了好难。。。先看书去了
	 */
	public void printPath2() { 
		Vertex s=adjoin.get(0).get(0);
		broadFirstSearch(s);
		for(int i=1; i<adjoin.size(); i++){
			LinkedList<Vertex> list=adjoin.get(i);
			Vertex v=list.get(0);
			printPath(s, v);
			System.out.println();
		}
	}
	/**
	 * 在广度搜索之后，打印源节点s到目标节点v的最短路径上的所有节点 
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
	 * 从源节点s出发进行广度优先的搜索
	 * 可以计算出从s到所有可达节点之间的距离 
	 */
	public void broadFirstSearch(Vertex s) {
		for (List<Vertex> list : adjoin) {
			Vertex u=list.get(0);
			u.color=Color.white;
			u.d=INFINITY;
			u.p=null;
		}
		s.color=Color.gray;
		s.d=0;
		s.p=null;
		Queue<Vertex> Q=new LinkedList<>();
		System.out.println("入队："+s.num+", 深度："+s.d);
		if(!Q.offer(s)){
			System.out.println("队列已满");
		}
		while(Q.size()!=0){
			Vertex u=Q.poll();
			System.out.println("出队："+u.num);
			for (Vertex v : adjoin.get(u.pos)) {
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
			u.color=Color.black;
			System.out.println("涂黑："+u.num);
		}
	}
}
