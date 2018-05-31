package org.foresee.Algorithm.struct;

/**
 * 未完成它，
 * 方法都参照BinaryTree里的，只是key不一定必须是int了 
 */
public class BinarySearchTree {
	public static void main(String[] args) {
//		BinarySearchTreeNode<Comparable<T>>
	}
	public static void inorderTreeWalk(BinarySearchTreeNode<?> x){
		if(x!=null){
			inorderTreeWalk(x.left);
			System.out.println(x.key);
			inorderTreeWalk(x.right);
		}
	}
	public static class BinarySearchTreeNode<T extends Comparable<T>> {
		T key; //键
		BinarySearchTreeNode<T> parent;
		BinarySearchTreeNode<T> left;
		BinarySearchTreeNode<T> right;

		public BinarySearchTreeNode(T key, BinarySearchTreeNode<T> parent, BinarySearchTreeNode<T> left,
				BinarySearchTreeNode<T> right) {
			this.key=key;
			this.parent=parent;
			this.left=left;
			this.right=right;
		}
	}
}
