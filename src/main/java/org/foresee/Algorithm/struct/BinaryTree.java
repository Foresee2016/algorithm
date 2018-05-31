package org.foresee.Algorithm.struct;

/**
 * 按算法导论写二叉搜索树结构，暂时key按int值，和书对应，以后再换别的 
 */
public class BinaryTree {
	public static void main(String[] args) {
		BinaryTree tree=new BinaryTree(15);
		tree.insert(new TreeNode(6));
		tree.insert(new TreeNode(18));
		tree.insert(new TreeNode(3));
		tree.insert(new TreeNode(7));
		tree.insert(new TreeNode(2));
		tree.insert(new TreeNode(4));
		tree.insert(new TreeNode(13));
		tree.insert(new TreeNode(9));
		tree.insert(new TreeNode(17));
		tree.insert(new TreeNode(20));		
		
		TreeNode node=tree.treeSearch(6);
		TreeNode nodeSuccessor=BinaryTree.treeSuccessor(node);
		System.out.println(node.key+", "+nodeSuccessor.key);
		tree.treeDelete(node);
		tree.inorderTreeWalk();
	}
	TreeNode root;
	public BinaryTree(int rootKey) {
		root=new TreeNode(rootKey);
	}
	/**
	 * 从数中删除节点node，分三种情况：
	 * 1.如果node没有子节点，就简单删除它，并修改它的父节点，用null替换该子节点
	 * 2.如果只有一个子节点，那么将它提升到node的位置，修改父节点指向该子节点
	 * 3.如果有两个子节点，那么找node的后继节点y（一定在右子树中），让y占据树中node的位置
	 * node的原来右子树部分称为y的新右子树，node的左子树成为y的新左子树。（其中还与y是否为node的右节点相关）
	 * 程序上有几种：
	 * 如果node没有左节点，那么直接用右节点替换node，右节点可以是null，也成立
	 * 如果node仅有一个左节点，就用左节点替换node
	 * 否则，node有左右两节点，查找node的后继y，这个后继位于node的右子树且没有左节点，将y移出原来位置进行拼凑，
	 * 并替换树中的node，两种情况：
	 * (1)如果y是node的右节点，那么用y替换node，并仅留下y的右孩子）
	 * (2)y位于node右子树但不是node的右节点，先用y的右键点替换y，然后用y替换node
	 */
	public void treeDelete(TreeNode node) {
		if(node.left==null){
			transplant(node, node.right);
		}else if(node.right==null){
			transplant(node, node.left);
		}else {
			TreeNode y=treeMinimum(node.right);
			if(y.parent!=node){
				transplant(y, y.right);	//把y分离出来放到上面，之后就可以替换node了
				y.right=node.right;
				y.right.parent=y;
			}
			transplant(node, y);
			y.left=node.left;
			y.left.parent=y;
		}
	}
	/**
	 * 在二叉搜索树内移动子树，用一棵以v为根的子树来替换一棵以u为根的子树，u的父节点以v为子节点，v的parent指向u的父节点。 
	 */
	protected void transplant(TreeNode u, TreeNode v) {
		if(u.parent==null){
			root=v;
		}else if (u==u.parent.left) {
			u.parent.left=v;
		}else {
			u.parent.right=v;
		}
		if(v!=null){
			v.parent=u.parent;
		}
	}
	/**
	 * 寻找一个节点的后继（在所有关键字互不相同的情况下，后继指大于node.key的最小关键字的节点）
	 * 如果传入参数是该树中最大节点，没有后继，返回null。
	 * 思路：分两种情况，一是该节点有右子树，因为右子树都比它大，所以右子树中最小的就是后继。
	 * 二是没有右子树，那么就需要向上找，如果该节点是父节点的左节点，那么父节点就是后继了，
	 * 如果它是父节点的右节点，说明父节点比它小，需要继续向上，直到在左边了，或者到根节点了。
	 * 注：如果有相同值，比如6，那么两个6一个是另一个的左节点，这样取后继会有歧义，对上面一个取，
	 * 会正常取到后一个，对下面一个取，会取到等于它自己key值得父节点。书里没给怎么定义。如果
	 * 一定要取后一个而不是等于它的，可以在最后返回前加判断，如果相等就再往后找一个。
	 */
	public static TreeNode treeSuccessor(TreeNode node) {
		if(node.right!=null){
			return treeMinimum(node.right);
		}
		TreeNode parent=node.parent;
		while(parent!=null && node==parent.right){
			node=parent;
			parent=node.parent;
		}
		return parent;
	}
	public TreeNode treeMaximum(){
		return treeMaximum(root);
	}
	/**
	 * 寻找以node为根的最小元素，一直向右找直到null 
	 */
	public static TreeNode treeMaximum(TreeNode node) {
		while(node.right!=null){
			node=node.right;
		}
		return node;
	}
	/**
	 * 类内辅助方法 
	 */
	public TreeNode treeMimimum(){
		return treeMinimum(root);
	}
	/**
	 * 寻找以node为根的最小元素，一直往左边找直到null 
	 */
	public static TreeNode treeMinimum(TreeNode node){
		while(node.left!=null){
			node=node.left;
		}
		return node;
	}
	/**
	 * 类内的辅助方法 
	 */
	public TreeNode treeSearch(int key){
		return treeSearch(root, key);
	}
	/**
	 * 迭代版本： 以node为树根，查找键为key的节点，找不到时回null
	 */
	public static TreeNode treeSearch(TreeNode node, int key) {
		while(node!=null && key!=node.key){
			if(key<node.key){
				node=node.left;
			}else{
				node=node.right;
			}
		}
		return node;
	}
	/**
	 * 递归版本：以node为树根，查找键为key的节点，找不到时回null
	 * 通常情况，递归版本性能不如迭代版本 
	 */
	public static TreeNode treeSearch2(TreeNode node, int key) {
		if(node==null || key==node.key){
			return node;
		}
		if(key<node.key){
			return treeSearch(node.left, key);
		}else {
			return treeSearch(node.right, key);
		}
	}
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
	public static void inorderTreeWalk(TreeNode node){
		if(node!=null){
			inorderTreeWalk(node.left);
			System.out.println(node.key);
			inorderTreeWalk(node.right);
		}
	}
	/**
	 * 类内的辅助方法，用来从根节点执行插入。
	 */
	public void insert(TreeNode node){
		insert(root,node);
	}
	/**
	 * 按二叉搜索树的规则执行插入，比当前节点小就往左边，大就往右边，直到找到空位置放下
	 * 静态方法 
	 */
	public static void insert(TreeNode ancestor, TreeNode node){
		if(node.key>ancestor.key){
			if(ancestor.right==null){
				ancestor.right=node;
				node.parent=ancestor;
			}else{
				insert(ancestor.right, node);
			}
		}else{
			if(ancestor.left==null){
				ancestor.left=node;
				node.parent=ancestor;
			}else{
				insert(ancestor.left, node);
			}
		}
	}
	public static class TreeNode {
		public int key;
		public TreeNode parent;
		public TreeNode left;
		public TreeNode right;
		// 之后可以加一些额外属性，不影响树操作，称为卫星数据
		
		public TreeNode(int key) {
			super();
			this.key = key;
			this.parent = null;
			this.left = null;
			this.right = null;
		}
	}
}
