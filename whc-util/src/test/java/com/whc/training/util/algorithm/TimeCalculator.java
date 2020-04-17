package com.whc.training.util.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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

    default <T> void timeCalc(Consumer<T> consumer, T t) {
        long start = System.currentTimeMillis();
        consumer.accept(t);
        long end = System.currentTimeMillis();
        logger.info("param: {}, time: {}", t, end - start);
    }

    default <R> void timeCalc(Supplier<R> supplier) {
        long start = System.currentTimeMillis();
        R r = supplier.get();
        long end = System.currentTimeMillis();
        logger.info("result: {}, time: {}", r, end - start);
    }

    default void timeCalc(Runnable runnable) {
        long start = System.currentTimeMillis();
        runnable.run();
        long end = System.currentTimeMillis();
        logger.info("time: {}", end - start);
    }
}