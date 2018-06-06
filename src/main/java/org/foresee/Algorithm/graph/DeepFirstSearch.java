package org.foresee.Algorithm.graph;

import java.util.LinkedList;
import java.util.Stack;

import org.foresee.Algorithm.graph.AdjacencyGraph.Color;
import org.foresee.Algorithm.graph.AdjacencyGraph.Edge;
import org.foresee.Algorithm.graph.AdjacencyGraph.EdgeType;
import org.foresee.Algorithm.graph.AdjacencyGraph.Vertex;

/**
 * 深度优先搜索的前驱子图形成一个由多棵深度优先树构成的深度优先森林。 深度优先树的结构和visit递归调用的结构完全对应。
 * 另一个性质是，节点的发现时间和完成时间具有括号化结构，规整地嵌套在一起。
 */
public class DeepFirstSearch {
	private int time;

	/**
	 * 对图所有节点进行深度优先的搜索
	 */
	public void deepFirstSearch(AdjacencyGraph graph) {
		for (Vertex u : graph.vertexs) {
			u.color = Color.white;
			u.parent = null;
		}
		time = 0;
		for (Vertex u : graph.vertexs) {
			if (u.color == Color.white) {
				deepFirstSearchVisit(graph, u);
			}
		}
	}

	/**
	 * 对节点u进行深度优先的搜索
	 */
	public void deepFirstSearchVisit(AdjacencyGraph graph, Vertex u) {
		time++;
		u.d = time;
		u.color = Color.gray;
		LinkedList<Edge> edges = u.adjacents;
		for (Edge edge : edges) {
			Vertex v = edge.link;
			if (v.color == Color.white) {
				v.parent = u;
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
	public void deepFirstSearch2(AdjacencyGraph graph) {
		for (Vertex u : graph.vertexs) {
			u.color = Color.white;
			u.parent = null;
		}
		time = 0;
		for (Vertex u : graph.vertexs) {
			if (u.color == Color.white) {
				// deepFirstSearchVisit2(graph, u);
				deepFirstSearchVisit3(graph, u);
			}
		}
	}

	/**
	 * 使用栈操作来避免递归调用。每访问到一个结点时，遍历该结点下所有白色结点，发现有白色时，入栈该白色，跳出，再去遍历该白色
	 * 存在问题：在遍历时，每次触底回程后，又再次遍历原结点的所有邻接节点，多遍历了几次。
	 * 所以每次压入新白色结点并访问该新结点时，压入该遍历的当前位置，之后回程要继续遍历时再弹出。
	 * 在deepFirstSearchVisit3()里修正，并在其中加入边类型判断。
	 */
	public void deepFirstSearchVisit2(AdjacencyGraph graph, Vertex u) {
		Stack<Vertex> stack = new Stack<>();
		time++;
		u.d = time; // 赋初始u条件，压入
		u.color = Color.gray;
		System.out.println("涂灰" + u.name + ", d = " + u.d);
		stack.push(u);
		while (!stack.isEmpty()) { // stack为空，则该条线上所有节点都访问到了
			Vertex v = stack.peek();
			LinkedList<Edge> edges = v.adjacents;
			boolean pushedNew = false;
			for (Edge edge : edges) { // 存在问题见本方法的注释
				Vertex link = edge.link;
				if (link.color == Color.white) { // 新访问到时，push
					time++;
					link.d = time;
					link.color = Color.gray;
					link.parent = v; // 此时产生树边
					System.out.println("涂灰" + link.name + ", d = " + link.d);
					edge.visited = true;
					stack.push(link);
					pushedNew = true;
					break; // 已发现新推入的结点，继续遍历该节点
				}
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
	 * 加上课后练习23.3-10，要求打印每条边的分类。书里已解释了，使用颜色区分，前向边和横向边用if(u.d<v.d)区分
	 */
	public void deepFirstSearchVisit3(AdjacencyGraph graph, Vertex u) {
		Stack<Vertex> stack = new Stack<>();
		Stack<Integer> curPos = new Stack<>();
		time++;
		u.d = time; // 赋初始u条件，压入
		u.color = Color.gray;
		System.out.println("涂灰" + u.name + ", d = " + u.d);
		stack.push(u);
		curPos.push(0);
		while (!stack.isEmpty()) { // stack为空，则该条线上所有节点都访问到了
			Vertex v = stack.peek();
			LinkedList<Edge> edges = v.adjacents;
			boolean pushedNew = false;
			for (int i = curPos.pop(); i < edges.size(); i++) {
				Edge edge = edges.get(i);
				Vertex link = edge.link;
				if (!edge.visited) {
					edge.visited = true;
					if (link.color == Color.white) {
						edge.edgeType=EdgeType.TreeEdge;
						System.out.println("Edge (" + v.name + "," + link.name + ") 是：树边");
					} else if (link.color == Color.gray) {// NOTE：自循环的边如x出去的边指向x自己，也视为后向边
						edge.edgeType=EdgeType.BackwardEdge;
						System.out.println("Edge (" + v.name + "," + link.name + ") 是：后向边");
					} else if (link.color == Color.black) {
						if (v.d < link.d) {
							edge.edgeType=EdgeType.ForwardEdge;
							System.out.println("Edge (" + v.name + "," + link.name + ") 是：前向边");
						} else {
							edge.edgeType=EdgeType.LateralEdge;
							System.out.println("Edge (" + v.name + "," + link.name + ") 是：横向边");
						}
					} else {
						System.out.println("这里应该执行不到" + link.color.name());
					}
				}
				if (link.color == Color.white) { // 新访问到时，push
					time++;
					link.d = time;
					link.color = Color.gray;
					link.parent = v;
					System.out.println("涂灰" + link.name + ", d = " + link.d);
					stack.push(link);
					curPos.push(i); // 压入当前记录位置
					curPos.push(0); // 压入0，对新结点DFS起始位置
					pushedNew = true;
					break; // 已发现新推入的结点，继续遍历该节点
				}
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
}
