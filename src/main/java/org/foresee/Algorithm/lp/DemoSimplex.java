package org.foresee.Algorithm.lp;

import java.util.Arrays;

import org.foresee.Algorithm.lp.Simplex.NoSolutionException;
import org.foresee.Algorithm.lp.Simplex.UpboundException;

public class DemoSimplex {
	public static void main(String[] args) {
		// @formatter:off
		double[][] A = new double[][] { 
			new double[] { 1, 1, 3 }, 
			new double[] { 2, 2, 5 }, 
			new double[] { 4, 1, 2 } 
		};
		// @formatter:on
		double[] b = new double[] { 30, 24, 36 };
		double[] c = new double[] { 3, 1, 2 };
		Simplex simplex = new Simplex();
		try {
			double[] x = simplex.simplex(A, b, c);
			System.out.println(Arrays.toString(x));
		} catch (NoSolutionException e) {
			System.out.println(e.getMessage());
		} catch (UpboundException e) {
			System.out.println(e.getMessage());
		}

		// 这个全0不是基础解，还不能算
		// @formatter:off
		double[][] A2=new double[][]{
			new double[]{2,-8,0,-10},
			new double[]{-5,-2,0,0},
			new double[]{-3,5,-10,2},
		};
		// @formatter:on
		double[] b2 = new double[] { -50, -100, -25 };
		double[] c2 = new double[] { -1, -1, -1, -1 };
		try {
			double[] x2 = simplex.simplex(A2, b2, c2);
			System.out.println(Arrays.toString(x2));
		} catch (NoSolutionException e) {
			System.out.println(e.getMessage());
		} catch (UpboundException e) {
			System.out.println(e.getMessage());
		}
		// @formatter:off
		double[][] A3 = new double[][] { 
			new double[] { 1, 1, -1 }, 
			new double[] { -1, -1, 1 }, 
			new double[] { 1, -2, 2 } 
		};
		// @formatter:on
		double[] b3 = new double[] { 7, -7, 4 };
		double[] c3 = new double[] { 2, -3, 3 };
		try {
			double[] x3 = simplex.simplex(A3, b3, c3);
			System.out.println(Arrays.toString(x3));
		} catch (NoSolutionException e) {
			System.out.println(e.getMessage());
		} catch (UpboundException e) {
			System.out.println(e.getMessage());
		}

		// 书503页有很多1/6数的示例不能用，因为它的角标已经变过了。

		// 测试生成初始解的程序，数29.5的示例
		// @formatter:off
		double[][] A4=new double[][]{
			new double[]{2,-1},
			new double[]{1,-5},
		};
		// @formatter:on
		double[] b4 = new double[] { 2, -4 };
		double[] c4 = new double[] { 2, -1 };
		try {
			double[] x4 = simplex.simplex(A4, b4, c4);
			System.out.println(Arrays.toString(x4));
		} catch (NoSolutionException e) {
			System.out.println(e.getMessage());
		} catch (UpboundException e) {
			System.out.println(e.getMessage());
		}
	}
}
