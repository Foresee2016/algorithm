package org.foresee.Algorithm.struct;

import java.util.Stack;

/**
 * 数据结构：栈（Stack），后进先出（LIFO，last in first out） 
 * 因为java.util包已经有Stack类了，为了区分，叫MyStack，用于练习和理解
 */
public class MyStack {
	public static void main(String[] args) {
		Stack<Integer> stack=new Stack<>();
		stack.push(20);
		stack.push(10);
		if(!stack.isEmpty()){
			System.out.println(stack.peek());
			System.out.println(stack.pop());
		}
	}
}
