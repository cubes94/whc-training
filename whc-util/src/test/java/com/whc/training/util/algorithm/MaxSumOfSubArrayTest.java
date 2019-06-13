package com.whc.training.util.algorithm;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 最大子序列和
 *
 * @author whc
 * @version 1.0.0
 * @since 2019年06月09 11:38
 */
@Slf4j
public class MaxSumOfSubArrayTest implements TimeCalculator {

    @Test
    public void testMaxSumOfSubArrayTest() {
        int size = 10000;
        int[] base = {4, -3, 5, -2, -1, 2, 6, -2};
        int[] array = new int[base.length * size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(base, 0, array, i * base.length, base.length);
        }
        // 时间复杂度 O(N logN) 分治法+递归
        timeCalc(this::testDivideAndConquer, array);
        // 时间复杂度 O(N) 巧妙地以线性时间运行的算法
        timeCalc(this::testAnotherTrick, array);
    }

    /**
     * 分治法
     *
     * @param array array
     * @return result
     */
    private int testDivideAndConquer(int[] array) {
        return testDivideAndConquer(array, 0, array.length - 1);
    }

    /**
     * 分治法
     * 将序列拆分为两段，分三种情况取最大子序列和：
     * 1. 最大子序列和处于Left子序列
     * 2. 最大子序列和处于Right子序列
     * 3. 最大子序列和处于Left&Right子序列
     * 时间复杂度T(N): T(N) = 2T(N/2) + O(N) ≈ 2T(N/2) + N
     * T(1) = 1 = 1
     * T(2) = 4 = (2^1) * (1+1)
     * T(4) = 12 = (2^2) * (2+1)
     * T(8) = 32 = (2^3) * (3+1)
     * ...
     * T(2^k) = (2^k) * (k+1) 即 T(N) = N * (logN + 1) = O(N logN)
     *
     * @param array array
     * @param lft   lft
     * @param rgt   rgt
     * @return result
     */
    private int testDivideAndConquer(int[] array, int lft, int rgt) {
        if (lft == rgt) {
            return array[lft] > 0 ? array[lft] : 0;
        }
        // 递归获取拆分后的子序列最大和
        int center = (lft + rgt) / 2;
        int maxLeftSum = testDivideAndConquer(array, lft, center);
        int maxRightSum = testDivideAndConquer(array, center + 1, rgt);
        // 判断最大和的子序列的元素是否处于拆分后的两个子序列中
        int maxLeftBorderSum = 0, leftBorderSum = 0;
        for (int i = center; i >= lft; i--) {
            leftBorderSum += array[i];
            if (leftBorderSum > maxLeftBorderSum) maxLeftBorderSum = leftBorderSum;
        }
        int maxRightBorderSum = 0, rightBorderSum = 0;
        for (int i = center + 1; i <= rgt; i++) {
            rightBorderSum += array[i];
            if (rightBorderSum > maxRightBorderSum) maxRightBorderSum = rightBorderSum;
        }
        // 返回三种情况中的最大值
        return Math.max(Math.max(maxLeftSum, maxRightSum), maxLeftBorderSum + maxRightBorderSum);
    }

    /**
     * 更巧妙的方法
     * 从左到右遍历，记录已经遍历的序列的最大值maxSum。thisSum则表示包括已遍历序列的最右端元素的最大值
     * 时间复杂度O(N)
     *
     * @param array array
     * @return result
     */
    private int testAnotherTrick(int[] array) {
        int maxSum = 0, thisSum = 0;
        for (int i : array) {
            thisSum += i;
            if (thisSum > maxSum) {
                maxSum = thisSum;
            } else if (thisSum < 0) {
                thisSum = 0;
            }
        }
        return maxSum;
    }
}
