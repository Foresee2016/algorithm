package org.foresee.Algorithm.think.ch6;

import java.util.Arrays;

/**
 * 思考题6-3，Young氏矩阵。这个感觉可以看成是1.5叉树（这个比喻不恰当但看起来像）。
 * 例如矩阵是A，a11的左孩子是它下面的a21，右孩子是右边的a12，而a21的左孩子是a31，右孩子是a22，a12的左孩子也是a22，右孩子是a13。
 * 过了对角线之后，处于边界的，一边只有右孩子，另一边只有左孩子。这样大概齐和堆有对应关系。
 * 排序的那个没想出别的方法，一个一个插入再一个一个抽出来，是4n*n*n时间，也算是O(n3)了，注意这里是n^2个数，不是n个。
 */
public class YoungTableau {
    public static void main(String[] args) {
        YoungTableau tableau = new YoungTableau(3, 9);
        tableau.mat = new int[][]{{2, 3, 5}, {4, 8, 9}, {12, 14, 16}};
        System.out.println(tableau.hasKey(9));
        System.out.println(tableau.hasKey(10));
        System.out.println(tableau.extractMin());
        tableau.insert(6);
        while (tableau.total > 0) {
            System.out.print(tableau.extractMin() + ", ");
        }
        System.out.println();

    }

    int[][] mat;
    int total;
    final int col;

    public YoungTableau(int col, int total) {
        this.col = col;
        this.total = total;
        int row = (int) Math.ceil((double) total / col);
        mat = new int[row][col];
    }

    /**
     * 判断Young矩阵里是否含有该数值，思路是查到的，没想出来。
     * 每行最右边是最大的，每列最上面是最小的，和右上角元素比，大于它，则这一行不是，去掉。小于它，这一列都不是，去掉。
     *
     * @param key 要查找的元素值
     * @return 是否包含
     */
    public boolean hasKey(int key) {
        int r = 0, c = col - 1;
        while (r <= endRow() && c >= 0) { //因为没有值的地方是无穷大，最后不满一行还是可以，会不断减c
            if (mat[r][c] == key) {
                return true;
            } else if (mat[r][c] < key) {
                r++;
            } else {
                c--;
            }
        }
        return false;
    }

    /**
     * 插入还是按照最大堆里的思路，先放到最后一个，然后向左上换，找左和上二者中大的一个换过去。
     *
     * @param key 新插入的键值
     */
    public void insert(int key) {
        if (total >= mat.length * mat[0].length) {
            throw new RuntimeException("Mat full, cannot insert.");
        }
        total++;
        int r = endRow(), c = endCol();
        mat[r][c] = key;
        while (true) {
            int maxR, maxC;
            if (r > 0 && mat[r - 1][c] > mat[r][c]) {
                maxR = r - 1;
                maxC = c;
            } else {
                maxR = r;
                maxC = c;
            }
            if (c > 0 && mat[r][c - 1] > mat[maxR][maxC]) {
                maxR = r;
                maxC = c - 1;
            }
            if (maxR == r && maxC == c) {
                break;
            } else {
                swap(r, c, maxR, maxC);
                r = maxR;
                c = maxC;
            }
        }
    }

    public int extractMin() {
        if (total <= 0) {
            throw new RuntimeException("No elements, cannot extract");
        }
        int min = mat[0][0];
        int row = endRow();
        int col = endCol();
        mat[0][0] = mat[row][col];
        mat[row][col] = Integer.MAX_VALUE;
        total--;
        minify(0, 0);
        return min;
    }

    /**
     * 向右下，不断交换位置，保持排序规则
     *
     * @param r 行位置
     * @param c 列位置
     */
    private void minify(int r, int c) {
        int minR, minC;
        if (r < endRow() && mat[r + 1][c] < mat[r][c]) {
            minR = r + 1;
            minC = c;
        } else {
            minR = r;
            minC = c;
        }
        if (c < col - 1 && mat[r][c + 1] < mat[minR][minC]) {
            minR = r;
            minC = c + 1;
        }
        if (minR != r || minC != c) {
            swap(r, c, minR, minC);
            minify(minR, minC);
        }
    }

    private int endRow() {
        int remainder = total % col;
        return remainder == 0 ? total / col - 1 : total / col;
    }

    private int endCol() {
        return total % col;
    }

    private void swap(int r1, int c1, int r2, int c2) {
        int temp = mat[r1][c1];
        mat[r1][c1] = mat[r2][c2];
        mat[r2][c2] = temp;
    }
}
