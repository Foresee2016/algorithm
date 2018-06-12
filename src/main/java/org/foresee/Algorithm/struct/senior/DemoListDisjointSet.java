package org.foresee.Algorithm.struct.senior;

import org.foresee.Algorithm.struct.senior.ListDisjointSet.Member;

public class DemoListDisjointSet {
	public static void main(String[] args) {
		Member f=new Member("f");
		Member g=new Member("g");
		Member d=new Member("d");
		Member c=new Member("c");
		Member h=new Member("h");
		Member e=new Member("e");
		Member b=new Member("b");
		ListDisjointSet fs = ListDisjointSet.makeSet(f);
		ListDisjointSet gs = ListDisjointSet.makeSet(g);
		ListDisjointSet ds = ListDisjointSet.makeSet(d);
		ListDisjointSet cs = ListDisjointSet.makeSet(c);
		ListDisjointSet hs = ListDisjointSet.makeSet(h);
		ListDisjointSet es = ListDisjointSet.makeSet(e);
		ListDisjointSet bs = ListDisjointSet.makeSet(b);
		
		fs=ListDisjointSet.union(fs, gs);
		fs=ListDisjointSet.union(fs, ds);
		
		cs=ListDisjointSet.union(cs, hs);
		cs=ListDisjointSet.union(cs, es);
		cs=ListDisjointSet.union(cs, bs);
		
		ListDisjointSet.output(fs);
		ListDisjointSet.output(cs);
		
		fs=ListDisjointSet.union(fs, cs);
		ListDisjointSet.output(fs);
	}
}
