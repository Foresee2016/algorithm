package org.foresee.Algorithm;

import java.util.Arrays;

/**
 * 给定一个字符串，打印它的全排列，例如给定a b c，全排列是： abc,acd,bac,bca,cab,cba
 * 
 */
public class Permutation {
	public static void main(String[] args) {
		permute("abc");
	}
	public static void permute(String str) {
		char[] chars=str.toCharArray();
		permute(chars, 0, chars.length-1);
	}
	public static void permute(char[] chars, int pos, int last) {
		if(pos==last){
			System.out.println(Arrays.toString(chars));
		}else {
			for(int i=pos; i<=last; i++){
				swap(chars, i, pos);
				permute(chars, pos+1, last);
				swap(chars, i, pos);
			}
		}
	}
	public static void swap(char[] chars, int i, int j) {
		char temp=chars[i];
		chars[i]=chars[j];
		chars[j]=temp;
	}
}
