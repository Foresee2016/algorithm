package org.foresee.Algorithm.struct.senior;

/**
 * 算法导论书，第21章，用于不相交集合的数据结构，21.2链表表示
 */
public class ListDisjointSet {
	public Member head; // 指向集合内第一个成员
	public Member tail; // 指向集合内最后一个成员
	public int length; // 集合长度即内部成员数量
	public static class Member{
		public String name; // 成员名字，唯一标识
		public Member next; // 下一个成员，它是最后一个时，值为null
		public ListDisjointSet set; // 指回所属集合
		public Member(String name) {
			this.name=name;
		}
	}
	
	public static ListDisjointSet makeSet(Member member) {
		ListDisjointSet set=new ListDisjointSet();
		member.next=null;
		member.set=set;
		set.head=member;
		set.tail=member;
		set.length++;
		return set;
	}
	
	public static Member findSet(Member member){
		return member.set.head;
	}
	/**
	 * 合并set1和set2，把短的集合合进长的，返回合并后的结果。
	 * 对n个对象的全集操作，普通合并，总时间O(n*n)，加权合并的（短进长）总时间O(m + n*log(n))
	 */
	public static ListDisjointSet union(ListDisjointSet set1, ListDisjointSet set2) {
		if(set1.length < set2.length){ // 让set1更长，set2合进set1
			ListDisjointSet temp=set1;
			set1=set2;
			set2=temp;
		}
		Member m=set2.head;
		while(m!=null){
			m.set=set1;
			m=m.next;
		}
		set1.tail.next=set2.head;
		set1.tail=set2.tail;
		set1.length=set1.length+set2.length;
		return set1;
	}
	public static void output(ListDisjointSet set) {
		Member m=set.head;
		while(m!=null){
			System.out.print(m.name+"->");
			m=m.next;
		}
		System.out.println();
	}
}
