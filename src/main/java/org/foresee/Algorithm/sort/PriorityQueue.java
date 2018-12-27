package org.foresee.Algorithm.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 算法导论6.5节，优先队列，使用堆实现。这里内部维护堆，跟HeapSort里不关联。
 */
public class PriorityQueue {
    public static void main(String[] args) {
        Integer[] integers=new Integer[]{16,14,10,8,7,9,3,2,4,1};
        PriorityQueue priorityQueue=PriorityQueue.create(integers);
        System.out.println(priorityQueue.heapMaximum());
        System.out.println(priorityQueue.heapExtractMax());
        priorityQueue.heapInsert(20);
        System.out.println(priorityQueue.heapExtractMax());
        priorityQueue.heapIncreaseKey(5, 55);
        System.out.println(priorityQueue.heapExtractMax());

        priorityQueue.heapDelete(1); // 删掉了10
        System.out.println(priorityQueue.heapExtractMax());
        System.out.println(priorityQueue.heapExtractMax());
    }

    private int heapSize=0;
    private List<Integer> elems=new ArrayList<>();

    /**
     * 通过无序的数组创建优先队列，数组值作为队列键值。不改变源数据
     *
     * @param arr 无序数组
     * @return 优先队列
     */
    public static PriorityQueue create(Integer[] arr) {
        PriorityQueue priorityQueue = new PriorityQueue();
        priorityQueue.buildMaxHeap(arr);
        return priorityQueue;
    }

    public int heapMaximum() {
        return elems.get(0);
    }

    public int heapExtractMax() {
        if (heapSize < 0) {
            throw new RuntimeException("No elements in PriorityQueue");
        }
        int max = elems.get(0);
        elems.set(0, elems.get(heapSize-1));
        heapSize--;
        maxHeapify(0);
        return max;
    }
    public void heapDelete(int i){
        //删除，直接赋最小，然后maxHeapify()不行，会让树不是左满的
        //这里先赋最大，转到最上面，然后extract出去，这时最小的换到上面，再逐步下降，还是左满的
        heapIncreaseKey(i, Integer.MAX_VALUE);
        heapExtractMax();
    }
    public void heapIncreaseKey(int i, int key) {
        if (key < elems.get(i)) {
            throw new RuntimeException("key is lower, cannot increase");
        }
        elems.set(i, key);
        int parent = parent(i);
        while (parent >= 0 && elems.get(parent) < elems.get(i)) {
            swap(i, parent);
            i = parent;
            parent = parent(i);
        }
    }

    public void heapInsert(int key) {
        heapSize++;
        elems.add(Integer.MIN_VALUE);
        heapIncreaseKey(heapSize - 1, key);
    }

    private void buildMaxHeap(Integer[] arr) {
        elems=Arrays.stream(arr).collect(Collectors.toList());
        heapSize=elems.size();
        for (int i=heapSize/2; i>=0; i--){
            maxHeapify(i);
        }
    }

    private void swap(int i, int j) {
        int temp = elems.get(i);
        elems.set(i, elems.get(j));
        elems.set(j, temp);
    }

    private void maxHeapify(int i) {
        while (true) { //用迭代代替递归
            int left = left(i);
            int right = right(i);
            int largest;
            if (left < elems.size() && elems.get(left) > elems.get(i)) {
                largest = left;
            } else {
                largest = i;
            }
            if (right < elems.size() && elems.get(right) > elems.get(largest)) {
                largest = right;
            }
            if (largest == i) {
                break;
            }
            swap(i, largest);
            i = largest;
        }
    }

    int left(int i) {
        return i << 1;
    }

    int right(int i) {
        return (i << 1) + 1;
    }

    public int parent(int i) {
        return i != 0 ? i >> 1 : -1;
    }
}
