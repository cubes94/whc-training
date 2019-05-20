package com.whc.training.util.domain;

/**
 * 共享对象
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年05月10 14:00
 */
public class MySignal {

    protected boolean hasDataToProcess = false;

    public synchronized boolean hasDataToProcess() {
        return this.hasDataToProcess;
    }

    public synchronized void setHasDataToProcess(boolean hasData) {
        this.hasDataToProcess = hasData;
    }
}
