package org.foresee.Algorithm.graph;

import java.util.LinkedList;
import java.util.Stack;

/**
 * 深度优先搜索的前驱子图形成一个由多棵深度优先树构成的深度优先森林。 深度优先树的结构和visit递归调用的结构完全对应。
 * 另一个性质是，节点的发现时间和完成时间具有括号化结构，规整地嵌套在一起。
 */
public class DeepFirstSearch {
	/**
	 * 对图所有节点进行深度优先的搜索
	 */
	int time;

	public void deepFirstSearch(Graph graph) {
		for (LinkedList<Vertex> list : graph.adjoin) {
			Vertex u = list.get(0);
			u.color = Color.white;
			u.p = null;
		}
		time = 0;
		for (LinkedList<Vertex> list : graph.adjoin) {
			Vertex u = list.get(0);
			if (u.color == Color.white) {
				deepFirstSearchVisit(graph, u);
			}
		}
	}

	/**
	 * 对节点u进行深度优先的搜索
	 */
	public void deepFirstSearchVisit(Graph graph, Vertex u) {
		time++;
		u.d = time;
		u.color = Color.gray;
		LinkedList<Vertex> list = graph.adjoin.get(u.pos);
		for (Vertex v : list) {
			if (v.color == Color.white) {
				v.p = u;
				deepFirstSearchVisit(graph, v);
			}
		}
		u.color = Color.black;
		time++;
		u.f = time;
	}

	/**
	 * 使用栈操作代替递归调用
	 */
	public void deepFirstSearch2(Graph graph) {
		for (LinkedList<Vertex> list : graph.adjoin) {
			Vertex u = list.get(0);
			u.color = Color.white;
			u.p = null;
		}
		time = 0;
		for (LinkedList<Vertex> list : graph.adjoin) {
			Vertex u = list.get(0);
			if (u.color == Color.white) {
				deepFirstSearchVisit3(graph, u);
			}
		}
	}
	/**
	 * 使用栈操作来避免递归调用。每访问到一个结点时，遍历该结点下所有白色结点，发现有白色时，入栈该白色，跳出，再去遍历该白色
	 * 存在问题：在遍历时，每次触底回程后，又再次遍历原结点的所有邻接节点，多遍历了几次。
	 * 所以每次压入新白色结点并访问该新结点时，压入该遍历的当前位置，之后回程要继续遍历时再弹出。 在deepFirstSearchVisit3()里修正。
	 */
	public void deepFirstSearchVisit2(Graph graph, Vertex u) {
		Stack<Vertex> stack = new Stack<>();
		time++;
		u.d=time; //赋初始u条件，压入
		u.color=Color.gray;
		System.out.println("涂灰" + u.name+", d = "+u.d);
		stack.push(u); 
		while (!stack.isEmpty()) { // stack为空，则该条线上所有节点都访问到了
			Vertex v = stack.peek();		
			LinkedList<Vertex> links = graph.adjoin.get(v.pos);
			boolean pushedNew = false;
			for (Vertex link : links) { // 存在问题见本方法的注释
				if (link.color == Color.white) { // 新访问到时，push
					time++;
					link.d = time;	
					link.color = Color.gray;
					link.p=v; // 此时产生树边
					System.out.println("涂灰" + link.name+", d = "+link.d);
					stack.push(link);
					pushedNew = true;
					break; // 已发现新推入的结点，继续遍历该节点
				}
				// 这里link.color==Color.gray时，是后向边。NOTE：自循环的边如x出去的边指向x自己，也视为后向边
				// 而link.color==Color.black时，是横向边
				// 而link.color==Color.black时，一个节点被两条路径访问到，第二次访问是前向边
			}
			if (pushedNew == false) { // 如果没push新的，说明已处于回退涂黑阶段，涂黑并pop
				time++;
				v.f = time;
				v.color = Color.black;
				stack.pop();
				System.out.println("涂黑" + v.name + ", f = " + v.f);
			}
		}
	}
	/**
	 * 改进 deepFirstSearchVisit2()，通过记录当前扫描位置，回程时取出，减少每次回程时遍历次数。
	 * 比deepFirstSearchVisit2()难看懂，所以保留了两个，先看懂2以后，3只改了一点。
	 */
	public void deepFirstSearchVisit3(Graph graph, Vertex u) {
		Stack<Vertex> stack = new Stack<>();
		Stack<Integer> curPos=new Stack<>();
		time++;
		u.d=time; //赋初始u条件，压入
		u.color=Color.gray;
		System.out.println("涂灰" + u.name+", d = "+u.d);
		stack.push(u); 
		curPos.push(0);
		while (!stack.isEmpty()) { // stack为空，则该条线上所有节点都访问到了
			Vertex v = stack.peek();
			LinkedList<Vertex> links = graph.adjoin.get(v.pos);
			boolean pushedNew = false;
			for (int i=curPos.peek(); i<links.size(); i++) { 
				Vertex link = links.get(i);
				if (link.color == Color.white) { // 新访问到时，push
					time++;
					link.d = time;	
					link.color = Color.gray;
					link.p=v; // 此时产生树边
					System.out.println("涂灰" + link.name+", d = "+link.d);
					stack.push(link);
					curPos.push(i);
					pushedNew = true;
					break; // 已发现新推入的结点，继续遍历该节点
				}
				// 这里link.color==Color.gray时，是后向边。NOTE：自循环的边如x出去的边指向x自己，也视为后向边
				// 而link.color==Color.black时，是横向边
				// 而link.color==Color.black时，一个节点被两条路径访问到，第二次访问是前向边
			}
			if (pushedNew == false) { // 如果没push新的，说明已处于回退涂黑阶段，涂黑并pop
				time++;
				v.f = time;
				v.color = Color.black;
				stack.pop();
				curPos.pop();
				System.out.println("涂黑" + v.name + ", f = " + v.f);
			}
		}
	}
}
