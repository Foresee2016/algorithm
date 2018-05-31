package org.foresee.Algorithm.struct;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 数据结构：队列（Queue），先进先出（FIFO，first in first out）  
 * java.util有Queue接口和LinkedQueue那些，所以这个叫MyQueue，只用于练习和理解
 */
public class MyQueue {
	public static void main(String[] args) {
		Queue<Integer> queue=new LinkedList<>();
		queue.offer(10);
		queue.offer(20);
		queue.offer(30);
		System.out.println(queue.peek());
		System.out.println(queue.element());
		System.out.println(queue.poll());
	}
}
