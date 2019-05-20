package com.whc.training.util.domain;

/**
 * 回调函数
 *
 * @author whc
 * @version 1.0.0
 * @since 2019年05月19 16:17
 */
@FunctionalInterface
public interface Callback {

    /**
     * 回调方法
     *
     */
    void apply();
}
