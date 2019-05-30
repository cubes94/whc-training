package com.whc.training.util.test.domain;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延时回调
 *
 * @author whc
 * @version 1.0.0
 * @since 2019年05月20 11:54
 */
public class DelayedCallback implements Delayed {

    /**
     * 回调方法
     */
    private Callback callback;

    /**
     * 延时时间
     */
    private long delays;

    public DelayedCallback(Callback callback, long delays) {
        this.callback = callback;
        this.delays = TimeUnit.NANOSECONDS.convert(delays, TimeUnit.MILLISECONDS) + System.nanoTime();
    }

    public Callback getCallback() {
        return callback;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.delays - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        long res = this.getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
        return res > 0 ? 1 : (res < 0 ? -1 : 0);
    }
}
