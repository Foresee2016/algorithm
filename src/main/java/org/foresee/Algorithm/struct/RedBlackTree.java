package org.foresee.Algorithm.struct;

/**
 * 红黑树，一种平衡搜索树，算是学习数据结构的一个里程碑吧，虽然一直想不明白为啥这么操作
 * java.util.TreeMap是使用红黑树实现的，java.util.TreeSet是在TreeMap的基础上实现的
 * 在插入和删除的代码中有很多情况要考虑，算法导论的解释很长，没记在这里。
 * 感觉这些也看不明白，像是分析各种情况总结出来的，而不是按着一定的思路方向做。
 * 有一个是定的，在调整时只通过改变颜色、左右旋和树移植维持红黑树那5条性质。
 */
public class RedBlackTree {
	public static void main(String[] args) {
		RedBlackTree tree=new RedBlackTree();
		tree.rbInsert(new RbNode(15));
		tree.rbInsert(new RbNode(6));
		tree.rbInsert(new RbNode(3));
		RbNode node=new RbNode(7);
		tree.rbInsert(node);
		tree.rbInsert(new RbNode(2));
		tree.rbInsert(new RbNode(4));
		tree.rbInsert(new RbNode(13));
		tree.rbInsert(new RbNode(9));
		tree.rbInsert(new RbNode(18));
		tree.rbInsert(new RbNode(17));
		tree.rbInsert(new RbNode(20));
		
		tree.rbDelete(node);
		tree.inorderTreeWalk();
	}
	RbNode root=RbNode.nil;
	/**
	 * 类内的中序遍历，辅助方法，以调用根元素。
	 */
	public void inorderTreeWalk(){
		inorderTreeWalk(root);
	}
	/**
	 * 中序遍历，先打印左子树，再当前，再右子树。类似有先序遍历和后序遍历。
	 * 静态方法，因为用不到任何类内变量，所有用到的都能在参数中，像工具方法一样 
	 */
	public static void inorderTreeWalk(RbNode node){
		if(node!=RbNode.nil){
			inorderTreeWalk(node.left);
			System.out.println(node.key);
			inorderTreeWalk(node.right);
		}
	}
	/**
	 * 比BinaryTree里多了记录y和颜色等信息。 情况太多了，真的没看懂，只是照着伪代码实现的。
	 */
	public void rbDelete(RbNode node) {
		RbNode y=node, x=null;
		Color yOriginColor=y.color;
		if(node.left==RbNode.nil){
			x=node.right;
			rbTransplant(node, node.right);
		}else if (node.right==RbNode.nil) {
			x=node.left;
			rbTransplant(node, node.left);
		}else{
			y=treeMinimum(node.right);
			yOriginColor=y.color;
			x=y.right;
			if(y.parent==node){
				x.parent=y;
			}else{
				rbTransplant(y, y.right);
				y.right=node.right;
				y.right.parent=y;
			}
			rbTransplant(node, y);
			y.left=node.left;
			y.left.parent=y;
			y.color=node.color;
		}
		if(yOriginColor==Color.BLACK){
			rbDeleteFixup(x);
		}
	}
	/**
	 * 照着算法导论写的，不知道这咋想出来的，可能是分析了所有可能情况，找出了一种都适用的方法。 
	 */
	public void rbDeleteFixup(RbNode x) {
		while(x!=root && x.color==Color.BLACK){
			if(x==x.parent.left){
				RbNode w=x.parent.right;
				if(w.color==Color.RED){
					w.color=Color.BLACK;
					x.parent.color=Color.RED;
					leftRotate(x.parent);
					w=x.parent.right;
				}
				if (w.left.color==Color.BLACK && w.right.color==Color.BLACK) {
					w.color=Color.RED;
					x=x.parent;
				}else {
					if(w.right.color==Color.BLACK){
						w.left.color=Color.BLACK;
						w.color=Color.RED;
						rightRotate(w);
						w=x.parent.right;
					}
					w.color=x.parent.color;
					x.parent.color=Color.BLACK;
					w.right.color=Color.BLACK;
					leftRotate(x.parent);
					x=root;
				}
			}else {
				RbNode w=x.parent.left;
				if(w.color==Color.RED){
					w.color=Color.BLACK;
					x.parent.color=Color.RED;
					rightRotate(x.parent);
					w=x.parent.left;
				}
				if (w.left.color==Color.BLACK && w.right.color==Color.BLACK) {
					w.color=Color.RED;
					x=x.parent;
				}else {
					if(w.left.color==Color.BLACK){
						w.right.color=Color.BLACK;
						w.color=Color.RED;
						leftRotate(w);
						w=x.parent.left;
					}
					w.color=x.parent.color;
					x.parent.color=Color.BLACK;
					w.left.color=Color.BLACK;
					rightRotate(x.parent);
					x=root;
				}
			}
		}
		x.color=Color.BLACK;
	}
	/**
	 * 用v子树代替u子树的位置 
	 * 引入了哨兵变量RbNode.nil，所以与之前BinaryTree不同了
	 */
	public void rbTransplant(RbNode u, RbNode v){ 
		if(u.parent==RbNode.nil){
			root=v;
		}else if (u==u.parent.left) {
			u.parent.left=v;
		}else{
			u.parent.right=v;
		}
		v.parent=u.parent;
	}
	public static RbNode treeMinimum(RbNode node){
		while(node.left!=null){
			node=node.left;
		}
		return node;
	}
	/**
	 * 插入一个节点，并保持红黑树性质 
	 */
	public void rbInsert(RbNode node) {
		RbNode y=RbNode.nil;
		RbNode x=root;
		while(x!=RbNode.nil){
			y=x;
			if(node.key<x.key){
				x=x.left;
			}else {
				x=x.right;
			}
		}
		node.parent=y;
		if(y==RbNode.nil){
			root=node;
		}else if (node.key<y.key) {
			y.left=node;
		}else{
			y.right=node;
		}
		node.left=RbNode.nil;
		node.right=RbNode.nil;
		node.color=Color.RED;
		rbInsertFixup(node);
	}
	public void rbInsertFixup(RbNode node) {
		while(node.parent.color==Color.RED){
			if(node.parent==node.parent.parent.left){
				RbNode y=node.parent.parent.right;
				if(y.color==Color.RED){
					node.parent.color=Color.BLACK; 		//case 1
					y.color=Color.BLACK; 				//case 1
					node.parent.parent.color=Color.RED;	//case 1
					node=node.parent.parent;			//case 1
				}else {
					if (node==node.parent.right) {
						node=node.parent;					//case 2
						leftRotate(node);					//case 2
					}					
					node.parent.color=Color.BLACK;			//case 3
					node.parent.parent.color=Color.RED;		//case 3
					rightRotate(node.parent.parent);		//case 3
				}				
			}else{
				RbNode y=node.parent.parent.left;
				if(y.color==Color.RED){
					node.parent.color=Color.BLACK;
					y.color=Color.BLACK;
					node.parent.parent.color=Color.RED;
					node=node.parent.parent;
				}else{
					if(node==node.parent.left){	//下边要左转，所以把它到右子树
						node=node.parent;
						rightRotate(node);
					}
					node.parent.color=Color.BLACK;
					node.parent.parent.color=Color.RED;
					leftRotate(node.parent.parent);
				}
			}
		}
		root.color=Color.BLACK;
	}
	/**
	 * 左旋转操作，假设node的right不为null 
	 */
	public void leftRotate(RbNode node) {
		RbNode y=node.right;
		node.right=y.left;
		if(y.left!=RbNode.nil){
			y.left.parent=node;
		}
		y.parent=node.parent;
		if(node.parent==RbNode.nil){
			root=y;
		}else if (node==node.parent.left) {
			node.parent.left=y;
		}else{
			node.parent.right=y;
		}
		y.left=node;
		node.parent=y;
	}
	/**
	 * 右旋转操作，假设node的left不为null 
	 */
	public void rightRotate(RbNode node){
		RbNode y=node.left;
		node.left=y.right;
		if(y.right!=RbNode.nil){
			y.right.parent=node;
		}
		y.parent=node.parent;
		if(node.parent==RbNode.nil){
			root=y;
		}else if (node==node.parent.left) {
			node.parent.left=y;
		}else{
			node.parent.right=y;
		}
		y.right=node;
		node.parent=y;
	}
	public static class RbNode {
		public static final RbNode nil=new RbNode(0, Color.BLACK);
		public Color color;	
		public int key;
		public RbNode parent;
		public RbNode left;
		public RbNode right;
		// 之后可以加一些额外属性，不影响树操作，称为卫星数据
		
		public RbNode(int key){
			super();
			this.key = key;
			this.color=Color.RED;
			this.parent = nil;
			this.left = nil;
			this.right = nil;
		}
		public RbNode(int key, Color color) {
			super();
			this.key = key;
			this.color=color;
			this.parent = nil;
			this.left = nil;
			this.right = nil;
		}
	}
	public enum Color {
		RED,BLACK
	}
}
