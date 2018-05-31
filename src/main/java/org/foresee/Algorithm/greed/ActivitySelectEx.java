package org.foresee.Algorithm.greed;

import java.util.ArrayList;
import java.util.List;

/**
 * 一组活动，安排到一些教室，任意活动可以在任意教室进行，希望使用最少教室完成所有活动 
 * 思路：每次贪心选合理的值，删去这些值，再贪心选下一组
 */
public class ActivitySelectEx {
	public static void main(String[] args) {
		List<Activity> activities=new ArrayList<>();//活动按结束时间已排好序
		activities.add(new Activity(1, 4, "a"));
		activities.add(new Activity(3, 5, "b"));
		activities.add(new Activity(0, 6, "c"));
		activities.add(new Activity(5, 7, "d"));
		activities.add(new Activity(3, 9, "e"));
		activities.add(new Activity(5, 9, "f"));
		activities.add(new Activity(6, 10, "g"));
		activities.add(new Activity(8, 11, "h"));
		activities.add(new Activity(8, 12, "i"));
		activities.add(new Activity(2, 14, "j"));
		activities.add(new Activity(12, 16, "k"));
		distribute(activities);
	}
	public static void distribute(List<Activity> acts) {
		List<Activity> result=select(acts);
		int room=0;
		while(result!=null){
			System.out.println("---room "+room+"---");
			for (Activity act : result) {
				System.out.println("活动："+act.name+" from "+act.start+" to "+act.end);
			}
			room++;
			result=select(acts);
		}
	}
	public static List<Activity> select(List<Activity> acts) {
		if(acts.size()==0){
			return null;
		}
		List<Activity> result=new ArrayList<>();
		int n=acts.size();
		Activity act=acts.get(0);
		result.add(act);
		int k=act.end;
		for(int m=1; m<n;m++){
			if(acts.get(m).start>k){
				Activity temp=acts.get(m);
				result.add(temp);
				k=temp.end;
			}
		}
		for (Activity activity : result) {
			acts.remove(activity);
		}
		return result;
	}
	public static class Activity{
		public int start;
		public int end;
		public String name;
		public Activity(int start, int end, String name) {
			super();
			this.start = start;
			this.end = end;
			this.name=name;
		}		
	}
}
