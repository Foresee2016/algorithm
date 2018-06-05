package org.foresee.Algorithm.graph_bk;

/**
 * 用来表示图的定点，如果结点需要附带属性，可以继承它，在子类中加成员变量表示该属性
 * 但是对于边，书上没说怎么表示属性，可能的思路：Edge类维护两个Vertex成员变量，有或没有方向，Edge的子类中带属性
 * 或者是Vertex中不维护指向其它Vertex的，而是维护一个Edge的列表，Edge连接其它Vertex。暂时不考虑。
 * 按着书上的邻接链表写 
 */
public class Vertex {
	// 这个节点的标识符
	public String name; // 结点的名字，1,2,3，书里多用u，v，x等
	public int pos=-1; // 在addVertex到Graph时记录该Vertex在List中位置，方便之后取它的adjoint
	public Color color;	// 颜色，标志访问状态
	public int d;	//广度优先遍历时使用，从源节点s到这个节点的距离，深度优先遍历时存图灰色时间戳
	public Vertex p;	//广度优先遍历时使用，前驱节点
	public int f;	//深度优先遍历时存涂黑色时间戳
	public Vertex(String name) {
		this.name=name;
	}
}
