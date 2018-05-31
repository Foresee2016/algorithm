package org.foresee.Algorithm.greed;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
/**
 * 贪心算法：一组活动占用同一个教室，不能同时进行，每个活动有起止时间，选择最多的活动。 
 *
 */
public class ActivitySelect {
	public static void main(String[] args) {
		List<Activity> activities=new ArrayList<>();//活动按结束时间已排好序
		activities.add(new Activity(0, 0));	//虚拟活动，为了从0开始比较
		activities.add(new Activity(1, 4));
		activities.add(new Activity(3, 5));
		activities.add(new Activity(0, 6));
		activities.add(new Activity(5, 7));
		activities.add(new Activity(3, 9));
		activities.add(new Activity(5, 9));
		activities.add(new Activity(6, 10));
		activities.add(new Activity(8, 11));
		activities.add(new Activity(8, 12));
		activities.add(new Activity(2, 14));
		activities.add(new Activity(12, 16));
		List<Activity> result=new ArrayList<>();
		Activity[] acts=activities.toArray(new Activity[activities.size()]);
		maxSub(acts, 0, activities.size()-1, result);
		for (Activity act : result) {
			System.out.println("("+act.start+","+act.end+")");
		}
		System.out.println("---迭代方法---");
		activities.remove(0);
		Activity[] acts2=activities.toArray(new Activity[activities.size()]);
		List<Activity> result2=new Vector<>();
		maxSubIterate(acts2, result2);
		for (Activity act : result2) {
			System.out.println(act.start+","+act.end);
		}
		System.out.println("---按起始时间选择---");
//		sort(acts2);
//		for (Activity act : acts2) {
//			System.out.println(act.start);
//		}
		Activity[] acts3=activities.toArray(new Activity[activities.size()]);
		List<Activity> result3=new LinkedList<>();
		another(acts3, result3);
		for (Activity act : result3) {
			System.out.println(act.start+","+act.end);
		}
	}
	/**
	 * 用动态规划的方法求最大活动集合：递归式c[i,j]=c[i,k]+c[k,j]+1
	 * 思路是从集合中找出一个活动 ak，它的起始和终止时间把整个时间[i,j]分为左右两个部分，再在两部分中找，
	 * 最后取最大值。
	 * 但是，利用贪心选择更好，在已有活动基础上，选择后续中结束时间更早的，能留下更多资源给后续使用。
	 * 因为传入的已经按照结束时间排序了，所以第一个就选择a1
	 * 如果是乱序的，先按结束时间排序
	 */
	public static void maxSub(Activity[] acts, int k, int n, List<Activity> result) {
		int m=k+1;
		while(m<=n && acts[m].start < acts[k].end){
			m=m+1;
		}
		if(m<=n){
			result.add(acts[m]);
			maxSub(acts, m, n, result);//竟然是尾递归，哈哈
		}
	}
	// 迭代方法，不包含0-0虚拟活动
	public static void maxSubIterate(Activity[] acts, List<Activity> result) {
		int n=acts.length;
		result.add(acts[0]);
		int k=acts[0].end;
		for(int m=1; m<n; m++){
			if(acts[m].start>k){
				result.add(acts[m]);
				k=acts[m].end;
			}
		}
	}
	// 作为练习，换成按活动开始时间排序，然后每次选择最晚开始的活动，这个思路跟前边一样，改成了给前边留下更多资源
	public static void another(Activity[] acts, List<Activity> result) {
		sort(acts);
		int n=acts.length;
		result.add(acts[0]);
		int k=acts[0].start;
		for(int m=1; m<n; m++){
			if(acts[m].end<k){
				result.add(acts[m]);
				k=acts[m].start;
			}
		}
	}
	// 按start降序排列，使用插入排序，因为写着简单
	public static void sort(Activity[] acts) {
		int n=acts.length;
		for(int i=1; i<n;i++){
			Activity temp=acts[i];
			int j=i-1;
			while(j>=0 && acts[j].start<temp.start){
				acts[j+1]=acts[j];
				j--;
			}
			acts[j+1]=temp;
		}
	}
	// 不会写动态规划的，不会举一反三，唉
	public static void maxSub2(List<Activity> acts) {
		int start=Integer.MAX_VALUE,end=Integer.MIN_VALUE;
		for (Activity activity : acts) {
			if(activity.start<start){
				start=activity.start;
			}
			if(activity.end>end){
				end=activity.end;
			}
		}
		maxSub2(acts, start, end);
	}
	public static void maxSub2(List<Activity> acts, int start, int end) {
		
	}
	public static class Activity{
		public int start;
		public int end;
		public Activity(int start, int end) {
			super();
			this.start = start;
			this.end = end;
		}		
	}
}
