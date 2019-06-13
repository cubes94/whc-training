package com.whc.training.util.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

/**
 * 时间计算
 *
 * @author whc
 * @version 1.0.0
 * @since 2019年06月07 15:25
 */
public interface TimeCalculator {

    Logger logger = LoggerFactory.getLogger(TimeCalculator.class);

    default <T, R> void timeCalc(Function<T, R> function, T t) {
        long start = System.currentTimeMillis();
        R r = function.apply(t);
        long end = System.currentTimeMillis();
        logger.info("param: {}, result: {}, time: {}", t, r, end - start);
    }
}