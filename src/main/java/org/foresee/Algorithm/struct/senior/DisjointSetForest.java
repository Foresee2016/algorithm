package org.foresee.Algorithm.struct.senior;

/**
 * 不相交集合更快的实现中，使用有根树来表示集合，树中每个结点包含一个成员，每棵树代表一个集合。
 * 每个成员仅指向它的父结点。每棵树的根包含集合的代表，并且是其自己的父结点。
 * 通过两种启发式策略，“按秩合并”和“路径压缩”，得到渐进最优的不相交集合数据结构。
 * 按秩合并说的是两颗树合并的时候，谁的rank高谁做parent，一样高随便选一个，然后更新rank
 * 路径压缩说的是树根下的一串结点不要一串存在，而是parent统一指向这个根，它们同级，都是根的直接子结点
 * 同时使用按秩合并和路径压缩时，运行时间与结点数量m几乎呈线性关系，即摊还代价是O(1)
 */
public class DisjointSetForest {
	
	public static class Node{
		public String name; // 名字
		public Node parent; // 父结点
		public int rank; // 秩，表示该结点高度的上界
		public Node(String name) {
			this.name=name;
		}
	}
	
	public static void makeSet(Node x) {
		x.parent=x;
		x.rank=0;
	}
	public static void union(Node x, Node y) {
		link(findSet(x), findSet(y));
	}
	public static void link(Node x, Node y) {
		if(x.rank>y.rank){
			y.parent=x;
		}else{
			x.parent=y;
			if(x.rank==y.rank){
				y.rank++;
			}
		}
	}
	/**
	 * 是一种两趟方法（two-pass method）：
	 * 递归时，第一趟查找路径向上直到找到根，
	 * 递归回溯时，第二趟沿着搜索树向下更新每个结点，使其指向根。
	 */
	public static Node findSet(Node x) {
		if(x.parent!=x){
			x.parent=findSet(x.parent);
		}
		return x.parent;
	}
}
