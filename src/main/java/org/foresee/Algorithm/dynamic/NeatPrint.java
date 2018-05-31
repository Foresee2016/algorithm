package org.foresee.Algorithm.dynamic;
/**
 * 整齐打印问题： 
 *考虑在一个打印机上整齐地打印一段文章的问题。输入的正文是n个长度分别为L1、L2、……、Ln（以字符个数度量）的单词构成
 *的序列。我们希望将这个段落在一些行上整齐地打印出来，每行至多M个字符。“整齐度”的标准如下：如果某一行包含从i到j的
 *单词（i<j），且单词之间只留一个空格，则在行末多余的空格字符个数为 M - (j-i) - (Li+ …… + Lj)，它必须是非负值
 *才能让该行容纳这些单词。我们希望所有行（除最后一行）的行末多余空格字符个数的立方和最小。请给出一个动态规划的算法，
 *来在打印机整齐地打印一段又n个单词的文章。分析所给算法的执行时间和空间需求。
 */
public class NeatPrint {
	public static void main(String[] args) {
		String[] x=new String[]{"aaaa","bbbbb","cc","dddd","eeee"};
		minSpaceCount(x, 10);
	}
	public static void minSpaceCount(String[] x, int M) {
		
	}
}
