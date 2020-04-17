package com.whc.training.util.algorithm;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 斐波那契数
 * <p>
 * 1、1、2、3、5、8、13、21、...
 * F0=1，F1=1，Fn=Fn-1+Fn-2（n>=2，n∈N*）
 *
 * @author whc
 * @version 1.0.0
 * @since 2019年06月07 15:09
 */
@Slf4j
public class FibTest implements TimeCalculator {

    @Test
    public void testRecursion() {
        int n = 43;
        // 时间复杂度Ω((3/2)^n) (n>4)，指数增长
        timeCalc(this::recursion, n);
        // 时间复杂度O(n)
        timeCalc(this::recursionUseCache, n);
    }

    /**
     * 递归计算, 时间复杂度为Ω((3/2)^n) (n>4)
     * (3/2)^k + (3/2)^(k+1) = (3/2)^k * (5/2) > (3/2)^k * (9/4) = (3/2)^(k+2) 即(3/2)^k + (3/2)^(k+1) > (3/2)^(k+2)
     * fib(4) = 5 ≥ (3/2)^4
     * fib(5) = 8 ≥ (3/2)^5
     * fib(6) = fib(4) + fib(5) = (3/2)^4 + (3/2)^5 > (3/2)^6
     * ...
     * fib(n) > (3/2)^n
     * <p>
     * 下面方法调用的时间复杂度T(n) = T(n-1) + T(n-2) + 2 (其中2指的是n<=1的判断和加法运算)， 则T(n)≥fib(n)≥(3/2)^n，即以下运算的时间复杂度最低是(3/2)^n，以指数的速度增长。
     *
     * @param n n
     * @return fib(n)
     */
    private long recursion(int n) {
        return n <= 1 ? 1 : recursion(n - 1) + recursion(n - 2);
    }

    /**
     * 递归计算，使用缓存，即把会重复计算的接口存储下来
     * 每次递归需要一次判断，约n次加法运算，即O(2n) = O(n)
     *
     * @param n n
     * @return fib(n)
     */
    private long recursionUseCache(int n) {
        long[] array = new long[n + 1];
        array[0] = 1;
        array[1] = 1;
        return doRecursionUseCache(n, array);
    }

    /**
     * 递归计算，使用缓存，即把会重复计算的接口存储下来
     *
     * @param n     n
     * @param array 缓存数据
     * @return fib(n)
     */
    private long doRecursionUseCache(int n, long[] array) {
        if (array[n] == 0) {
            array[n] = doRecursionUseCache(n - 1, array) + doRecursionUseCache(n - 2, array);
        }
        return array[n];
    }
}
