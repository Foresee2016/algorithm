package org.foresee.Algorithm.struct;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * 算法导论第11章-散列表的一些注意事项，记在这里。
 * 选择的散列函数应该尽量避免冲突（两个关键字映射到一个槽里），即尽量让hash函数随机。
 * 会有一些方法解决冲突（比如链接法chaining），但链越长性能越差。
 * 定义n个元素映射到m个槽里，装载因子T=n/m，链长度不应该比T大太多。
 * 好的散列函数：
 * 1.如果知道关键字的概率分布，能让它尽量均匀分布，例如关键字都是随机实数k且均匀分布与0<=k<1范围，那么h(k)=k*m就挺好
 * 2.实际应用中常常可以用启发式的方法来构造，例如一个编译器符号表中，关键字都是字符串，经常出现相近的符号，如pt和pts
 * 散列函数应该能将它们分开到不同的槽里。
 * 3.除法散列：用一个特定的素数除给定关键字，用余数作为散列值，通常能给出好的结果。h(k)=k % m，用该方法时，
 * 要避免选择m的某些值，例如m不应该是2的幂（那样相当于取二进制的低数字位），一个不太接近2的整数幂的素数，
 * 通常是好的选择。例如散列表要存放n=2000个字符串，每个字符串8位，允许一次不成功的查找需要平均检查3个元素，
 * 那就取m=n/3附近且远离2的整数幂的素数，比如701，所以散列函数h(k)=k % 701
 * 4.乘法散列：用关键字k乘上常数A(0<A<1)，提取kA的小数部分（kA % 1），用m乘这个值，再向下取整。
 * 用式子表达：h(k)=floor(m*(kA % 1))，优点是对m的选择不是太关键，一般选2的整数幂，这样可以移位计算乘法
 * 数值A的选择，Knuth认为(sqrt(5)-1)/2约等于0.618附近的值是比较理想的值。
 */
public class MyHashTable {
	public static void main(String[] args) {
		HashMap<String, Integer> map=new HashMap<>();
		map.put("aa", 1);
		Hashtable<String, Integer> hashtable=new Hashtable<>();
		hashtable.put("bb", 2);
	}
}
