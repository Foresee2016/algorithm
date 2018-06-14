package org.foresee.Algorithm.graph.ex;

import org.foresee.Algorithm.graph.ex.ShortestPathGraph.Vertex;

public class DemoDifferenceConstraints {
	public static void main(String[] args) {
		Vertex x1=new Vertex("x1");
		Vertex x2=new Vertex("x2");
		Vertex x3=new Vertex("x3");
		Vertex x4=new Vertex("x4");
		Vertex x5=new Vertex("x5");
		
		DifferenceConstraints dif=new DifferenceConstraints();
		dif.addInequality(x1, x2, 0);
		dif.addInequality(x1, x5, -1);
		dif.addInequality(x2, x5, 1);
		dif.addInequality(x3, x1, 5);
		dif.addInequality(x4, x1, 4);
		dif.addInequality(x4, x3, -1);
		dif.addInequality(x5, x3, -3);
		dif.addInequality(x5, x4, -3);
		
		dif.findSolution();
	}
}
