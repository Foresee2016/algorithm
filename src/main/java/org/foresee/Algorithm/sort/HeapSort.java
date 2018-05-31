package org.foresee.Algorithm.sort;

import org.foresee.Algorithm.GenerateNumber;

public class HeapSort {
	public static void main(String[] args) {
		String filename = "E:/JavaSpace/Algorithm/sort.data";
		int[] data = GenerateNumber.getFileData(filename);
		Heap heap = new Heap(data);
		HeapSort.sort(heap);
		for (int i = 0; i < data.length; i++) {
			System.out.println(data[i]);
		}
	}

	/**
	 * 堆排序，使用最大堆（即父节点值比子节点大（或相等），根节点最大。）
	 * 首先构造堆，让A以最大堆的形式呈现，然后把根元素A.data[0]（最大的元素），换到最末尾，
	 * 之后堆元素个数减一，也就是不考虑刚才换到最末尾的元素了，它已经在正确位置。
	 * 交换结束后，较小（但不一定最小）的元素到根元素位置上了，在根节点处刷新堆， 这样减少了一个元素的堆又满足性质了，再继续把最大的根元素换下来。
	 * 最后一个元素就不用换了，所以截止到1而不是i>=0
	 */
	public static void sort(Heap A) {
		buildMaxHeap(A);
		for (int i = A.data.length - 1; i >= 1; i--) {
			int temp = A.data[0];
			A.data[0] = A.data[i];
			A.data[i] = temp;
			A.heapSize = A.heapSize - 1;
			maxHeapify(A, 0);
		}
	}

	/**
	 * 在一个数组上构建堆，构建后数组满足最大堆性质，每个根元素都比叶元素大（或相等）
	 * 构建从数组长度一半的位置开始，让根节点比左右叶节点大。因为树型结构的原因，只有一半的元素是有叶节点的。
	 * 这一条可以从指数函数想，根节点2的0次方，下边1次方...如果树是满的，则最下边一层的节点数是上边所有节点数的和再加一。
	 * 如果树不满，减少两个叶节点，上边会露出一个节点。所以只有向下取整的一半节点有叶节点。
	 */
	public static void buildMaxHeap(Heap A) {
		A.heapSize = A.data.length;
		for (int i = ((A.heapSize - 1) / 2); i >= 0; i--) {
			maxHeapify(A, i);
		}
	}

	/**
	 * 从i处开始向下，让i满足根节点比叶节点大。先取左右节点，看当前是否满足，不满足则和最大的交换。 然后在被交换了的左或右叶节点继续验证是否满足。
	 */
	public static void maxHeapify(Heap A, int i) {
		int l = left(i), r = right(i), largest;
		if (l < A.heapSize && A.data[l] > A.data[i]) {
			largest = l;
		} else {
			largest = i;
		}
		if (r < A.heapSize && A.data[r] > A.data[largest]) {
			largest = r;
		}
		if (largest != i) {
			int temp = A.data[i];
			A.data[i] = A.data[largest];
			A.data[largest] = temp;
			maxHeapify(A, largest);
		}
	}

	public static final int parent(int i) {
		return i >> 1; // 快速计算i/2（向下取整）
	}

	public static final int left(int i) {
		return i << 1; // 快速计算i*2
	}

	public static final int right(int i) {
		return (i << 1) + 1; // 快速计算i*2 + 1
	}
	/**
	 * 堆的结构体定义，通过数组建立堆。 
	 */
	public static class Heap {
		public int[] data;
		public int heapSize;

		public Heap(int[] data) {
			this.data = data;
		}
	}
}
