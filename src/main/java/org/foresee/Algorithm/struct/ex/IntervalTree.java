package org.foresee.Algorithm.struct.ex;
/**
 * 区间树：扩展的红黑树。区间就是数学里的闭区间，[t1,t2]，
 * 区间重叠就是有公共部分，但[t1,t2],[t2,t3]只有一点是公共的，不算重叠 
 * 区间树（Interval Tree）每个节点包含一个区间interval，search(i)返回一个指向树内元素的指针x，使x.int与i重叠，不存在返回nil
 * 树节点包含区间属性interval，key选择区间低端点low，还包含一个max值表示子树中所有区间端点的最大值
 * 算法导论里没写实现，这个先放着吧。
 */
public class IntervalTree {

}
