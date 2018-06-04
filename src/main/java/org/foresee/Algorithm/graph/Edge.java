package org.foresee.Algorithm.graph;

public enum Edge {
	TreeEdge, // 树边，为深度优先森林Gp中的边，如果结点v是因算法对边（u，v）的探索而被发现，则（u，v）是一条树边
	BackwardEdge, // 后向边，后向边（u，v）是将结点u连接到其在深度优先树中（一个）祖先结点v的边。由于有向图中可以有自循环，自循环也被认为是后向边
	ForwardEdge, // 前向边，是将结点u连接到其在深度优先树中一个后代结点v的边（u，v）
	LateralEdge, // 横向边，指其它所有的边，这些边可以连接同一棵深度优先树中的结点，只要其中一个结点不是另外一个结点的祖先，也可以连接不同深度优先树中的两个结点
	
	/*
	 * NOTE：遇到某些边时，DFS有足够的信息来对这些边进行分类，这里的关键是，当第一次探索边（u，v）时，结点v的颜色能告知关于该边的信息
	 * 1.结点v是白色，表明该边（u，v）是一条树边。（因为v刚刚被发现）
	 * 2.结点v是灰色，表明该边（u，v）是一条后向边。（因为是灰色表明v被发现过了，正在深度搜索子节点）
	 * 3.结点v是黑色，表明该边（u，v）是一条前向边或横向边。（剩下的可能性，（u，v）在u.d<v.d时为前向边，在u.d>v.d时为横向边 ）
	 */
	/*
	 * NOTE：无向图因为（u，v）和（v，u）是一条边，所以先探索到哪个方向，就视为该边时该类型 。
	 * NOTE：无向图搜索深度优先搜索过程中，要么是树边，要么是后向边，不会产生前向边或横向边。（因为没有方向，形成环时会访问回祖先结点，形成横向边时总会再向下形成树边）
	 */
}
