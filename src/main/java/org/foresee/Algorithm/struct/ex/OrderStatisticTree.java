package org.foresee.Algorithm.struct.ex;

/**
 * 顺序统计树（Order Statistic Tree），扩展红黑树，添加size属性，在log2(n)时间内找出任何顺序统计量
 * 即这个树的节点除了key, color, parent, left, right, 还有size属性，存储以该元素为根的子树的节点数。
 * x.size=x.left.size + x.right.size + 1 它自己算在内，null节点size是0.
 * 这个属性能给红黑树带来一些提升，它不是只作为额外信息存储在节点内。
 * 该树中可以有相同的关键字。
 * 定义：秩是该节点在中序遍历中输出的位置
 * 没写完，插入和删除需要改变路径的size，旋转操作也需要调整两节点的size
 */
public class OrderStatisticTree {
	public static void main(String[] args) {
		
	}
	RbNodeEx root;
	/**
	 * 给定节点x，查找它在树中的秩（中序遍历对应的线性序的位置），也就是排第几小。运行时间log2(n) 
	 */
	public int rank(RbNodeEx x) {
		int r=x.left.size+1;
		RbNodeEx y=x;
		while(y!=root){
			if(y==y.parent.right){
				r=r+y.parent.left.size+1;
			}
			y=y.parent;
		}
		return r;
	}
	/**
	 * 辅助方法 
	 */
	public RbNodeEx select(int order) {
		return select(root, order);
	}
	/**
	 * 选择以x为根节点的子树中，第order小的节点。每次递归下降树的一层，最差运行时间log2(n) 
	 */
	public static RbNodeEx select(RbNodeEx x, int order){
		int r=x.left.size +1;
		if(order==r){
			return x;
		}else if(order<r){
			return select(x.left, order);
		}else {
			return select(x.right, order-r);
		}
	}
	public static class RbNodeEx{
		public static final RbNodeEx nil=new RbNodeEx(0, Color.BLACK, 0);
		public Color color;	
		public int key;
		public RbNodeEx parent;
		public RbNodeEx left;
		public RbNodeEx right;
		public int size;
		// 之后可以加一些额外属性，不影响树操作，称为卫星数据
		
		public RbNodeEx(int key){
			super();
			this.key = key;
			this.color=Color.RED;
			this.parent = nil;
			this.left = nil;
			this.right = nil;
			this.size=0;
		}
		public RbNodeEx(int key, Color color, int size) {
			super();
			this.key = key;
			this.color=color;
			this.parent = nil;
			this.left = nil;
			this.right = nil;
			this.size=size;
		}
	}
	public enum Color {
		RED,BLACK
	}
}
