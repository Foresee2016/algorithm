package org.foresee.Algorithm.dynamic;

public class MatrixChain {
	public static void main(String[] args) {
		MatrixSize[] matrixSizes = new MatrixSize[] { new MatrixSize(30, 35), new MatrixSize(35, 15),
				new MatrixSize(15, 5), new MatrixSize(5, 10), new MatrixSize(10, 20), new MatrixSize(20, 25), };
		CalcResult result = matrixChainOrder(matrixSizes);
		System.out.println(result.m[0][5]);
		printParens(result.s);
		System.out.println();
		
		CalcResult2 result2=matrixChainOrder2(matrixSizes);
		System.out.println(result2.memory.get(0, matrixSizes.length-1));
		String parens=getMatrixParens(result2.cut);
		System.out.println(parens);
	}

	/**
	 * 从矩阵链乘法中选择合适的加括号方式，让计算乘法次数（计算代价）最少。
	 * 思路：对于p中的各个矩阵，Ai..j表示从Ai乘到Aj，从i到j中找到k，i<=k<j，这个k分界能让Ai..j计算次数最少。
	 * 这个k将矩阵计算划分为Ai..k和Ak+1..j，再加上这两部分的结果做乘法，Ai..k的结果矩阵维度是p[i].rows*p[k].cols,
	 * Ak+1..j的结果矩阵维度是p[k].cols*p[j].cols。这样每次用到前边求得的最优值。
	 * length表示当前计算的矩阵链长度，从最少的2个开始，依次累加向上，3个4个直到n个。
	 * 例如，3个时，A0..2有两种种划分情况，A0..0和A1..2，A0..0是初始化过的0，只有自己不用计算，A1..2是前边计算过的，
	 * A1..1和A2..2的乘积次数，这是第一种情况；第二种情况是A0..1和A2..2，同理。
	 */
	public static CalcResult matrixChainOrder(MatrixSize[] p) {
		int n = p.length;
		int[][] m = new int[n][n];
		int[][] s = new int[n][n];
		for (int i = 0; i < n; i++) {
			m[i][i] = 0;
		}
		for (int length = 2; length <= n; length++) { // 当前计算的链长度length
			for (int i = 0; i < n - length + 1; i++) {
				int j=i+length-1;
				m[i][j] = Integer.MAX_VALUE;
				for (int k = i; k < j; k++) {
					int q = m[i][k] + m[k + 1][j] + p[i].rows * p[k].cols * p[j].cols;
					if (q < m[i][j]) {
						m[i][j] = q;
						s[i][j] = k;
					}
				}
			}
		}
		CalcResult res = new CalcResult(m, s);
		return res;
	}
	/**
	 * 打印括号，parens(parenthesis的缩写，意为“括号”) 
	 */
	public static void printParens(int[][] s) {
		printParens(s, 0, s[0].length-1);
	}
	public static void printParens(int[][] s, int i, int j) {
		if(i==j){
			System.out.print("A"+i);
		}else {
			System.out.print("(");
			printParens(s, i, s[i][j]);
			printParens(s, s[i][j]+1, j);
			System.out.print(")");
		}
	}
	public static String getMatrixParens(int[][] cut){
		return getMatrixParens(cut, 0, cut[0].length-1, "");
	}
	public static String getMatrixParens(int[][] cut, int i, int j, String str){
		if(i==j){
			return str+="A"+i;
		}else{
			str+="(";
			str=getMatrixParens(cut, i, cut[i][j], str);
			str=getMatrixParens(cut, cut[i][j]+1, j, str);
			str+=")";
			return str;
		}
	}
	
	/**
	 * 例程里使用n*n数组，浪费了将近一半的内存，这里用一维数组代替，加一些取值和赋值的辅助函数。 
	 */
	public static CalcResult2 matrixChainOrder2(MatrixSize[] p) {
		int n=p.length;
		HalfNn memory2=new HalfNn(n);
		for(int i=0; i<n; i++){	
			memory2.set(i, i, 0);
			for(int j=i+1; j<n; j++){
				memory2.set(i, j, Integer.MAX_VALUE);
			}
		}
		CutResult cut2=new CutResult(n);
		for(int length=2; length <= n; length++){
			for(int i=0;i<=n-length; i++){
				int j=i+length-1;
				for(int k=i; k<j; k++){
					int times2=memory2.get(i, k) + memory2.get(k+1, j) + p[i].rows*p[k].cols*p[j].cols;
					if(times2<memory2.get(i, j)){
						memory2.set(i, j, times2);
						cut2.set(i, j, k);
					}
				}
			}
		}
		return new CalcResult2(memory2, cut2);
	}
	public static String getMatrixParens(CutResult cut){
		return getMatrixParens(cut, 0, cut.n-1, "");
	}
	public static String getMatrixParens(CutResult cut, int i, int j, String str){
		if(i==j){
			return str+="A"+i;
		}else{
			str+="(";
			str=getMatrixParens(cut, i, cut.get(i, j), str);
			str=getMatrixParens(cut, cut.get(i, j)+1, j, str);
			str+=")";
			return str;
		}
	}
	/**
	 * 计算两矩阵相乘的代价，假如A是p*q的，B是q*r的，那结果会是p*r的，且结果每个元素都经过q次乘法， 所以总的乘法计算次数是p*q*r
	 */
	public static int matrixMulti(MatrixSize A, MatrixSize B) {
		if (A.cols != B.rows) {
			return -1;
		} else {
			return A.rows * A.cols * B.cols;
		}
	}

	public static class CalcResult {
		int[][] m;
		int[][] s;

		public CalcResult(int[][] m, int[][] s) {
			this.m = m;
			this.s = s;
		}
	}
	public static class CalcResult2{
		public HalfNn memory;
		public CutResult cut;
		public CalcResult2(HalfNn memory, CutResult cut) {
			this.memory=memory;
			this.cut=cut;
		}
	}
	public static class CutResult {
		protected int[] s;
		public int n;
		public CutResult(int n) {
			this.n=n;
			s=new int[(n-1)*n/2];
		}
		public int get(int row, int col){
			return s[(col-1)*col/2 + row];
		}
		public void set(int row, int col, int val){
			s[(col-1)*col/2+row]=val;
		}
	}
	public static class HalfNn {
		protected int[] m;
		public int n;
		public HalfNn(int n) {
			this.n=n;
			m=new int[(n+1)*n/2];
		}
		public int get(int row, int col) {
			return m[col*(col+1)/2 + row];
		}
		public void set(int row, int col, int val) {
			m[col*(col+1)/2 + row]=val;
		}
	}
	public static class MatrixSize {
		public int rows;
		public int cols;

		public MatrixSize(int rows, int cols) {
			super();
			this.rows = rows;
			this.cols = cols;
		}
	}
}
