package org.foresee.Algorithm.think.ch15;

/**
 * 算法导论第十五章动态规划，思考题15-9，字符串拆分。
 * 一个n位字符串，从几个位置分开，每次分开的代价是执行切分时字符串的长度，确定指定切分的最小代价。
 * 例如长度为20的字符串，切分位置2,8,11，如果顺序为先切位置2，代价20，剩余3-20，再切8，代价18，剩余9-20，再切11，代价12，剩余12-20，结束，总代价20+18+12=50.
 * 先切后面明显会减小代价。
 * 我的思路，首先是考虑被切后的长度，而不是位置，从2和8位置切，就得维护一段的两个端点，比较麻烦，只维护长度会好一些。
 * 比如上面切割，最终会形成长度为2,6,3,9四段，然后往上和，左边合起来更短，2+6=8，而6+3=9，所以左边三段在被分割时，先切3这一段，留下长度为8的一段更合适。
 * 最优子结构：中括号表示角标，以y[i][j]表示当前要在i到j之间执行切割了，那i到j这一段长度l[i][j]是一定会计入代价的，而其下面如何确定呢？
 * 假设k是其中的一个位置，则y[i][j]的一个可行切割方式是从k切割成y[i][k]，右面y[k+1][j]。k的位置决定怎样取到最小代价。
 * 从i+1开始到j-1，取其最小值就好了。
 * 为了和矩阵常用的下标对应，这里从1开始计数，注意中括号只是下标不好表示才这样写，不是数组。
 * 上面举例的2,6,3,9四段，首先填充矩阵对角线，为其自身的代价（这里不是代价值，只是这样初始化）。矩阵如下
 * 2  -  -  -
 * -  6  -  -
 * -  -  3  -
 * -  -  -  9
 * 而y[1][2]只有一种求法，切割为y[1][1]的2和y[2][2]的6，得到代价8，以此类推，向右上方填充一次得到矩阵如下
 * 2  8  -  -
 * -  6  9  -
 * -  -  3  12
 * -  -  -  9
 * y[1][3]有两种求法，其自身一定有代价11，从y[1][1]的2加上y[2][3]已有的代价9，则此处代价为20，而从y[1][2]的代价8加上y[3][3]的3，此处代价为19，依次类推，得到矩阵
 * 2  8  19 -
 * -  6  9  27
 * -  -  3  12
 * -  -  -  9
 * 最后的y[1][4]有三种求法了，自身代价是20，
 * 第一种：y[1][1]的2加上y[2][4]已有代价27，则此处代价为47，总体次序就是第一次切了左边2，代价20，第二次切了右边9，代价18，第三次切了左边6，代价9，得到4段，总和47.
 * 第二种：y[1][2]的已有代价8加上y[3][4]的已有代价12，总代价40，总体次序就是第一次切左边8，代价20，第二次切左段左边2，代价8，第三次切右段右边9，代价12，得到4段，总和40.
 * 第三种：y[1][3]的已有代价19加上y[4][4]的9，总代价39，最小。总体次序先切右边9，代价20，第二次切右边3，代价11，第三次切左边2，代价8，总和39.
 * 即y[i][j]中间取k，k为分割位置，y[i][j]=l[i][j] + min(y[i][i]
 * 更多的切割时矩阵更长，时间代价不会算，但想着是O(n^3)，因为每个位置遍历一次，代价约等于n，总共计算右上部分，n*n的一半，得三次方量级。
 * 大概可以对照矩阵乘法那一节了，计算代价的方式不一样。
 *
 * 唉，一个题前前后后推了一整天，智商下降严重。廉颇老矣，尚能饭否？
 */
public class StringSplit15_9 {
    public static void main(String[] args) {
        final int[] pos = new int[]{1, 7, 10, 15, 16};
        System.out.println("split str length=20: "+stringSplit(pos, 20));
        System.out.println("split str length=28: "+stringSplit(pos, 28));
    }

    /**
     * 字符串切割，返回最小代价。切割方式从控制台输出。输出算法见下面printSplit()
     *
     * @param pos    位置数组，以0为起始，阶段位置算在前面，比如设置1，8，则0到1为第一段，2到8为第二段，9以后为第三段。
     * @param length 字符串长度
     * @return 最小代价值
     */
    private static int stringSplit(int[] pos, int length) {
        int[] seg = new int[pos.length + 1]; //seg是每段长度，例如pos设置1,8，length为20，第一段0-1长为2，第二段2-8长为7，第三段9-末尾为11
        //初始化每段长度
        seg[0] = pos[0] + 1;
        for (int i = 1; i < pos.length; i++) {
            seg[i] = pos[i] - pos[i - 1];
        }
        seg[seg.length - 1] = length - 1 - pos[pos.length - 1];
        //求代价
        int[][] y = new int[seg.length][seg.length];
        int[][] len = new int[seg.length][seg.length]; //每个位置当前各段长度和，比如1-3段和为x，1-4段和为x加上第4段
        int[][] split = new int[seg.length][seg.length]; //分隔位置记录，位置指第几段后面切，以构造最优切割序列
        for (int i = 0; i < seg.length; i++) { //填充对角线
            y[i][i] = seg[i];
            len[i][i] = seg[i];
            split[i][i] = -1;
        }
        for (int i = 0; i < seg.length - 1; i++) { //填充对角线右边斜的一列，代价只能是二者求和
            y[i][i + 1] = y[i][i] + y[i + 1][i + 1];
            len[i][i + 1] = y[i][i + 1];
            split[i][i + 1] = i;
        }
        for (int i = 0; i < seg.length; i++) { //初始化其它位置为最大值
            for (int j = i + 2; j < seg.length; j++) {
                y[i][j] = Integer.MAX_VALUE;
                len[i][j] = -1;
            }
        }
        for (int j = 2; j < seg.length; j++) {
            for (int i = 0; i < seg.length - j; i++) {
                int col = i + j;
                len[i][col] = len[i][col - 1] + len[col][col]; //这几段的长度和，无论从哪些段来，加和后长度一样。
                //初始取两边界之一，边界1：最前面一段被切割，剩下后面的。边界2：最后面一段被切割，剩下前面的。
                if (y[i][col - 1] < y[i + 1][col]) {
                    y[i][col] = y[i][col - 1];
                    split[i][col] = col - 1;
                } else {
                    y[i][col] = y[i + 1][col];
                    split[i][col] = i;
                }
                for (int cut = i + 1; cut < col - 1; cut++) {
                    int cost = y[i][cut] + y[cut + 1][col];
                    if (cost < y[i][col]) {
                        y[i][col] = cost;
                        split[i][col] = cut;
                    }
                }
                y[i][col] += len[i][col]; //加上当前长度和，无论内部怎么切，都需要加上这个代价。
            }
        }
        //就用递归好了，用队列还得键个类保存切割点信息
        System.out.println("split: ");
        printSplit(split, 0, seg.length - 1);
        System.out.println();
        return y[0][seg.length - 1];
    }

    /**
     * 输出切割点的位置，这里的位置是段的后面，比如各段为2,6,3,5,1,3，六段，长为20的分下来的。
     * 输出1,0,3,2,4，表示第一次从数组序号1后面切，即6的后面，2,6为一组，后面3,5,1,3为一组。第二次从数组序号0后面切，即2,6被分成2和6两段。
     * 程序解释：右上角为生成过程的最后一步，即切割的第一步，比如从1开始切，那么被分为两段了，左边一段0-1，右边一段2-n，再递归地看0-1如何切，2-n如何切。
     * 这里虽然是递归，但复杂度为O(n)，大概齐是，不知道怎么证明这个。仅跳着访问了有限数目，没有遍历。
     *
     * @param split 已生成的切割矩阵
     * @param i     当前被切的行
     * @param j     当前被切的列
     */
    private static void printSplit(int[][] split, int i, int j) {
        if (i == j) {
            return;
        }
        System.out.print(split[i][j] + ",");
        printSplit(split, i, split[i][j]); //切左
        printSplit(split, split[i][j] + 1, j); //切右
    }
}
