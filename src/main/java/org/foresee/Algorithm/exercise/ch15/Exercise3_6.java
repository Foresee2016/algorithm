package org.foresee.Algorithm.exercise.ch15;

/**
 * 算法导论练习题15.3-6，货币兑换问题，需要从货币1兑换到n，可以一系列兑换，1,2,3...n，每种兑换有汇率rij。佣金ck，为0时有最优子结构，不为0时没有。
 * 证明不会写，当个题来做吧，当做没有佣金。
 * 有佣金想着好像是不具有最优子结构，前边已经计算出的y(n-1)表示换到n-1时需要的，如果第3次换佣金10块钱，第4次换佣金1000块钱，那可能还不如前面少换一次，佣金低一些。这样后边还是要算一遍前面的。
 * 但是如果每次涨的是一样的，比如每多换一次，多10块钱，就跟钢条切割里每次切割有代价一样了，有最优子结构。
 */
public class Exercise3_6 {
    public static void main(String[] args) {
        final float[][] rate = new float[][]{
                {1, 2, 3, 4},
                {1, 1, 0.6f, 2.5f},
                {1, 1, 1, 1.5f},
                {1, 1, 1, 1}
        };
        CurencyChange curencyChange=new CurencyChange(rate);
        System.out.println(curencyChange.calcMax(100));
    }

    private static class CurencyChange {
        private final float[][] rate;

        /**
         * 构造时传入汇率矩阵，上三角有效，对角线为1。
         *
         * @param rate 汇率矩阵，对角线的右上部分有效
         */
        public CurencyChange(float[][] rate) {
            if (rate.length == 0 || rate[0].length != rate.length) {
                throw new IllegalArgumentException("rate empty");
            }
            this.rate = rate;
        }

        /**
         * 计算能换到的最多货币值，使用迭代的方式
         *
         * @param cash 转换货币数
         * @return 最大货币值
         */
        public float calcMax(float cash) {
            if (rate.length == 0) {
                return -1;
            }
            float[] res = new float[rate.length];
            String[] path=new String[rate.length]; //构造最优解的路径
            res[0] = 1; //初始值，从0号货币换到0号货币
            for (int i = 1; i < rate.length; i++) {
                res[i] = rate[0][i]; //首先假定直接从0号货币换到i号货币的汇率最大
                path[i]="0->"+i;
                for (int j = 1; j < i; j++) { //从1号货币开始比对，如果先换到j号货币，再从j号换到i号货币，得到汇率大于当前，则取大的
                    float fromJ = res[j] * rate[j][i];
                    if(fromJ>res[i]){
                        res[i]=fromJ;
                        path[i]=path[j]+"->"+i;
                    }
                }
            }
            System.out.println(path[path.length-1]);
            return cash * res[res.length - 1];
        }
    }
}
