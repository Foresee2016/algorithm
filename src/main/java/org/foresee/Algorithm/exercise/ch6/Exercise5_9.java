package org.foresee.Algorithm.exercise.ch6;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 第6章第5节，优先队列，练习5-9，合并k各有序链表成一个，时间复杂度O(n*log(k))
 * 查的思路，从k各链表中每个里取一个，形成size为k的最小优先队列，每次出一个最小的，这个值从哪个链表来，就从哪个链表再拿一个进入最小优先队列。直到取完就不入队了。
 */
public class Exercise5_9 {
    public static void main(String[] args) {
        LinkedList<Integer> list1=new LinkedList<>(Arrays.asList(1,5,9,15,26,50,55));
        LinkedList<Integer> list2=new LinkedList<>(Arrays.asList(2,3,8,10));
        LinkedList<Integer> list3=new LinkedList<>(Arrays.asList(14,18,19,22,36));
        List<LinkedList<Integer>> lists=Arrays.asList(list1, list2, list3);

        LinkedList<Integer> merged=PriorityQueue.merge(lists);
        //Java8 Stream强无敌，真的极好用
        //这里有个泛型推断的问题，stream直接返回Stream<LinkList>后边推断不到LinkList<Integer>，这里赋值一次
        Stream<LinkedList<Integer>> stream=lists.stream();
        LinkedList<Integer> valid = stream.flatMap(Collection::stream)
                .sorted(Integer::compareTo)
                .collect(Collectors.toCollection(LinkedList::new));
        if(merged.size()!=valid.size()){
            System.out.println("Nani? Not equal size");
        }
        for (int i = 0; i < merged.size(); i++) {
            if(!merged.get(i).equals(valid.get(i))){
                System.out.println("A-ou? Wrong result.");
            }
        }
        System.out.println("Finish");
    }


    /**
     * 最小优先队列，仅保留添加和提取最小两个操作
     */
    private static class PriorityQueue {
        /**
         * 将k个有序链表（正序，从小到大）合成一个有序链表
         *
         * @param lists 有序链表的List
         * @return 合成的有序链表
         */
        static LinkedList<Integer> merge(List<LinkedList<Integer>> lists) {
            lists = lists.stream()
                    .filter(x -> x != null && x.size() > 0)
                    .collect(Collectors.toList()); //过滤空链表
            PriorityQueue queue = new PriorityQueue(lists.size());
            LinkedList<Integer> res = new LinkedList<>();
            //初始化，每个链表取一个
            for (int i = 0; i < lists.size(); i++) {
                LinkedList<Integer> list=lists.get(i);
                queue.elems[i] = new Node(list.removeFirst(), i);
            }
            queue.buildMinHeap();
            //每次取最小的排到结果里，从被取出Node的来源拿一个，build
            while(queue.size>0){
                Node node=queue.extractMin();
                LinkedList<Integer> from=lists.get(node.from);
                if(from.size()>0){
                    Node supply=new Node(from.removeFirst(), node.from);
                    queue.insert(supply);
                }
            }
            return res;
        }
        private static class Node{
            int key;
            int from;

            Node(int key, int from) {
                this.key = key;
                this.from = from;
            }
        }
        Node[] elems;
        int size;
        private PriorityQueue(int listCount) {
            elems = new Node[listCount];
            size=listCount;
        }
        private void buildMinHeap(){
            for (int i=size/2; i>=0; i--){
                minHeapify(i);
            }
        }
        private Node extractMin(){
            if(size==0){
                throw new RuntimeException("No element. Cannot extract.");
            }
            Node node=elems[0];
            elems[0]=elems[size-1];
            size--;
            minHeapify(0);
            return node;
        }
        private void insert(Node node){
            int i=size; //假设其为最大的，放最后，再减下去找到应在的位置
            elems[i]=node;
            size++;
            int parent=parent(i);
            while(parent>=0 && elems[parent].key>elems[i].key){
                swap(i, parent);
                i=parent;
                parent=parent(i);
            }
        }
        private void swap(int i, int j) {
            Node temp = elems[i];
            elems[i] = elems[j];
            elems[j] = temp;
        }

        private void minHeapify(int i) {
            while (true) { //用迭代代替递归
                int left = left(i);
                int right = right(i);
                int minimum;
                if (left < elems.length && elems[left].key < elems[i].key) {
                    minimum = left;
                } else {
                    minimum = i;
                }
                if (right < elems.length && elems[right].key < elems[minimum].key) {
                    minimum = right;
                }
                if (minimum == i) {
                    break;
                }
                swap(i, minimum);
                i = minimum;
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
}
