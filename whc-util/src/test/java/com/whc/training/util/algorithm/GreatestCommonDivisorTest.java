package com.whc.training.util.algorithm;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 最大公约数 (欧几里德算法/辗转相除法)
 *
 * @author whc
 * @version 1.0.0
 * @since 2019年06月09 16:06
 */
@Slf4j
public class GreatestCommonDivisorTest implements TimeCalculator {

    @Test
    public void testGcd() {
        long[] params = {24, 21};
        timeCalc(this::testGcd, params);
    }

    private long testGcd(long[] params) {
        return testGcd(params[0], params[1]);
    }

    /**
     * 最大公约数
     * 辗转相除法(大数除以小数取余, 小数除以余数取余...)
     *
     * @param a a
     * @param b b
     * @return result
     */
    private long testGcd(long a, long b) {
        if (a < b) {
            long c = a;
            a = b;
            b = c;
        }
        while (b != 0) {
            long c = a % b;
            a = b;
            b = c;
        }
        return a;
    }
}
