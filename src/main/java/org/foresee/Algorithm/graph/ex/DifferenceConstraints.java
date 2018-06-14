package org.foresee.Algorithm.graph.ex;

import org.foresee.Algorithm.graph.ex.ShortestPathGraph.Edge;
import org.foresee.Algorithm.graph.ex.ShortestPathGraph.Vertex;

// @formatter:off
/**
 * 书里24.4节这个思想好神奇啊，用有向图解决线性规划问题。但需要是特殊的差分条件。且仅能给出可行解，而不是最优解。
 * 差分约束系统，设计n个变量的，有m个差额限制条件的系统，约束都是简单的 xj - xi <= bk, 这里1<=i, j<=n, i!=j, 1<=k<=m
 * 这样表示成矩阵向量形式，线性规划矩阵A的每一行包含一个1和一个-1.
 * 而且如果(x1, x2, x3 ... xn)是解，那么(x1 + C, x2 + C, x3 + C ... xn + C)也是解
 * 
 * 约束图：将m行n列的线性规划矩阵A看作是n个结点和m条边构成的图的邻接矩阵的转置。图中每条边对应一个不等式。权重是bk值。
 * 约束图包含一个额外的结点v0，来保证图中至少有一个结点，从其出发可以到达其它任意结点，权重为0，它将作为最短路径查找的源节点。
 * 定理：差分约束系统对应的约束图G，如果G不包含权重为负值的环路，则x=(δ(v0,v1), δ(v0,v1), ... δ(v0,vn))是可行解。
 * 如果包含权重为负值的环路，则没有可行解。
 * 
 * 求解时用Bellman-Ford，因为v0可到任意结点，所以负权重环路能被检测出来。
 * 有n个未知变量和m个约束的差分约束系统，约束图有n+1个结点和n+m条边，时间O((n+1)*(n+m)) = O(n*n + n*m)
 * 练习24.4-5：稍微修改Bellman-Ford算法，使其能够在O(n*m)时间内解决由n个变量和m个约束条件所构成的差分约束系统。
 * TODO: 不会做，之后再想吧。
 * 练习24.4-6：假定存在相等约束xi=xj+bk，如果修改Bellman-Ford算法解决。TODO:不会做。*>_<*
 * 练习24.4-7：如何在没有额外结点v0的约束图上运行Bellman-Ford算法求解差分约束系统。TODO: 唉...
 * 练习24.4-10：对应约束条件是单个变量约束条件时，如xi<=bk，或者-xi<=bk，如何修改Bellman-Ford求解。TODO: duang~~
 * 24章还有挺多思考题的，后边基础学完再回来想吧。基础薄弱，思考不能。     ^|>_<|^
 */
public class DifferenceConstraints { 
// @formatter:on
	ShortestPathGraph graph;
	Vertex v0;
	public DifferenceConstraints() {
		graph=new ShortestPathGraph();
		v0=new Vertex("v0");
		graph.addVertex(v0);
	}
	public void addInequality(Vertex substractor, Vertex minuend, double differ) {
		// 这里不能直接用图类型里的辅助方法，因为是一个一个添加的，用HashSet判断已添加过好一些，这里为简单不改了。
		if(!graph.vertexs.contains(substractor)){
			graph.vertexs.add(substractor);
		}
		if(!graph.vertexs.contains(minuend)){
			graph.vertexs.add(minuend);
		}
		// 边方向由被减数指向减数，权重为差值
		Edge edge=new Edge(minuend, substractor, differ);
		graph.edges.add(edge);
	}
	public boolean findSolution() {
		for (Vertex v : graph.vertexs) { // v0连接所有
			// NOTE：v0到其它结点的边的权重值，可以是0或任意正值c，只会导致结点最短路径整体增加c
			Edge edge=new Edge(v0, v, 0);
			graph.edges.add(edge);
			v0.adjacents.add(edge);
		}
		SingleSourceShortestPath shortestPath=new SingleSourceShortestPath();
		if(!shortestPath.BellmanFord(graph, v0)){
			System.out.println("包含权重为负值的环路，没有可行解");
			return false;
		}
		System.out.println("-----得到其中一个可行解-----");
		for (Vertex v : graph.vertexs) {
			if(v==v0){
				continue;
			}
			System.out.println(v.name+" = "+v.d);
		}
		return true;
	}
}
