package org.foresee.Algorithm.exercise.ch2;

import java.util.Arrays;

/**
 * 课后练习2.3-7，给定n个整数集合S，和另一个整数x，该算法能在θ(n*log(n))时间内确定是否存在其和为x的元素。
 */
public class Exercise3_7 {
    public static void main(String[] args) {
        int[] S=new int[]{1,3,6,9,14,19,35};
        System.out.println("hasSumTo 10: "+hasSumTo(S,10));
        System.out.println("hasSumTo 2000: "+hasSumTo(S,2000));
        System.out.println("hasSumTo 33: "+hasSumTo(S,33));
    }

    /**
     * 思路：排序S，用x减每个数得到另一个有序集合，查看两者是否存在相等的数。排序用时n*log(n)，相减用时n，查看相等用时最多2*n
     *
     * @param S 整数集合
     * @param x 要计算的整数
     * @return 是否存在两个数和等于x
     */
    private static boolean hasSumTo(int[] S, int x) {
        Arrays.sort(S);
        int[] differ = new int[S.length]; //计算差值，从小到大排序
        for (int i = 0; i < S.length; i++) {
            differ[S.length - i - 1] = x - S[i];
        }
        int i = 0, j = 0; //查看两个有序数组是否包含相等的数
        while(i<S.length && j<differ.length){
            if(S[i]==differ[j]){
                return true;
            }else if(S[i]<differ[j]){
                i++;
            }else {
                j++;
            }
        }
        return false;
    }
}
