package org.foresee.Algorithm.graph.ex;

// @formatter:off
/**
 * 一些组合问题表面上似乎与流网络没有多少关系，但实际上能归约到最大流问题。
 * 最大二分匹配问题：给定一个无向图G=(V, E)，一个匹配是边的一个子集M子集于E，使得所有结点v属于V，子集M中最多有
 * 一条边与结点v相连。如果子集M中的某条边与结点v相连，则称结点v有M所匹配；否则，结点v就是没有匹配的。
 * 最大匹配是最大基数的匹配。
 * 在一个二分图中，结点集合可以划分为V=L并R，其中L和R是不相交的，并且边集合E中所有的边都横跨L和R，
 * 进一步假定结点集合V中每个结点至少有一条边。
 * 实际应用：例如机器集合L和任务集合R，边表示机器对应能执行的任务，最大匹配能让更多机器同时工作。
 * 使用Ford-Fulkerson方法在多项式时间内能找出无向二分图的最大匹配。构建一个流网络G，源结点和汇点是新增加的结点，
 * 且包含各有向边：s到L中各结点，L到R对应结点（无向边变有向边），R中各结点到t，且边容量为一个单位。
 * 求解的最大流即是最大二分匹配。
 */
public class MaxBipartiteMatch { 
// @formatter:on
	// 没啥可写的，书里没给示例，其实它跟最大流一样用，只是个可关联应用的实际问题
}
