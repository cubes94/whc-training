package com.whc.training.util.algorithm;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 幂运算
 *
 * @author whc
 * @version 1.0.0
 * @since 2019年06月09 16:19
 */
@Slf4j
public class PowerTest implements TimeCalculator {

    @Test
    public void testPower() {
        long x = 2;
        int pow = 38;
        timeCalc(this::power, new Object[]{x, pow});
    }

    private long power(Object[] params) {
        return doPower((long) params[0], (int) params[1]);
    }

    /**
     * 幂运算
     * 递归算法 N为偶数时: X^N = (X^2)^(N/2), N为奇数时: (X^2)^(N/2) * X
     *
     * @param x   x
     * @param pow 幂
     * @return result
     */
    public static long doPower(long x, int pow) {
        if (pow == 0) return 1;
        if (pow == 1) return x;
        if (pow % 2 == 0) {
            return doPower(x * x, pow / 2);
        } else {
            return doPower(x * x, pow / 2) * x;
        }
    }
}
