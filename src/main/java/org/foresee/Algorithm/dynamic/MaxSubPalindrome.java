package org.foresee.Algorithm.dynamic;
/**
 * 求最长回文子序列：回文是正逆序一致的非空字符串，比如civic 
 * 目标是在给定字符串里找最长的子序列，让它是回文，比如给character，应该返回carac
 */
public class MaxSubPalindrome {
	public static void main(String[] args) {
		String string="civic";
		System.out.println(string+" is palindrome: "+isPalindrome(string));
		String str="character";		
		String palind=findSumPalind(str);
		System.out.println(palind);
		
//		String abc="abc";
//		byte[] bytes=abc.getBytes();
//		StringBuffer stringBuffer=new StringBuffer();
//		for(int i=0; i<bytes.length; i++){
//			stringBuffer.append((char)bytes[i]);
//		}
//		System.out.println(stringBuffer.toString());
//		String abc2="";
//		for(int i=0; i<bytes.length; i++){
//			abc2+=(char)bytes[i];
//		}
//		System.out.println(abc2);
	}
	/**
	 * 思路：把string倒序，然后找最长公共子序列 
	 */
	public static String findSumPalind(String string) {
		StringBuffer stringBuffer=new StringBuffer(string);
		String reverse=stringBuffer.reverse().toString();
		byte[] x=string.toLowerCase().getBytes();
		byte[] y=reverse.toLowerCase().getBytes();
		return lcsString(x, y);
	}
	public static String lcsString(byte[] x, byte[] y) {
		int m=x.length, n=y.length;
		int[][] c=new int[m+1][n+1];
		// 最左一列和最上一行赋0
		for(int i=1; i<=m; i++){
			c[i][0]=0;
		}
		for(int j=0; j<=n; j++){
			c[0][j]=0;
		}
		for(int i=1; i<=m; i++){
			for(int j=1; j<=n; j++){
				if(x[i-1]==y[j-1]){
					c[i][j]=c[i-1][j-1]+1;
				}else if (c[i-1][j]>=c[i][j-1]) {
					c[i][j]=c[i-1][j];
				}else {
					c[i][j]=c[i][j-1];
				}
			}
		}
//		String string="";		
//		return makeSubseq(x, y, c, m, n, string);
		StringBuffer strBuf=new StringBuffer();
		return makeSubseq(x, y, c, m, n, strBuf).toString();
	}
	public static String makeSubseq(byte[] x, byte[] y, int[][] c, int i, int j, String str) {
		if(i==0 || j==0){
			return str;
		}
		if(c[i][j]> c[i-1][j] && c[i][j]>c[i][j-1] && c[i][j]>c[i-1][j-1]){
			// 当前i,j比左斜上都大，说明这个位置有跳变，那么这个i，j是子数组元素
			str=makeSubseq(x, y, c, i-1, j-1, str);
			str+=(char)x[i-1];
			return str;
		}else if (c[i][j]==c[i-1][j]) {
			return makeSubseq(x, y, c, i-1, j, str);
		}else{
			return makeSubseq(x, y, c, i, j-1, str);
		}
	}
	// 这个字符数组经常改变，换为StringBuffer
	public static StringBuffer makeSubseq(byte[] x, byte[] y, int[][] c, int i, int j, StringBuffer strBuf) {
		if(i==0 || j==0){
			return strBuf;
		}
		if(c[i][j]> c[i-1][j] && c[i][j]>c[i][j-1] && c[i][j]>c[i-1][j-1]){
			// 当前i,j比左右斜上都大，说明这个位置有跳变，那么这个i，j是子数组元素
			strBuf=makeSubseq(x, y, c, i-1, j-1, strBuf);
			strBuf.append((char)x[i-1]);
			return strBuf;
		}else if (c[i][j]==c[i-1][j]) {
			return makeSubseq(x, y, c, i-1, j, strBuf);
		}else{
			return makeSubseq(x, y, c, i, j-1, strBuf);
		}
	}
	public static boolean isPalindrome(String str){
		if(str==""){
			return false;
		}
		byte[] bytes=str.getBytes();
		for(int i=0, j=str.length()-1; i<=j; i++,j--){ //两边向中间推
			if(bytes[i]!=bytes[j]){
				return false;
			}
		}
		return true;
	}
}
