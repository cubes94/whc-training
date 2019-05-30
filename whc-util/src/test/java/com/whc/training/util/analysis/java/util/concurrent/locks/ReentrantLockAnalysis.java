package com.whc.training.util.analysis.java.util.concurrent.locks;

import com.whc.training.util.analysis.Analysis;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * ReentrantLock源码分析
 *
 * @author whc
 * @version 1.0.0
 * @since 2019年05月23 16:01
 */
public class ReentrantLockAnalysis implements Analysis, Lock, java.io.Serializable {

    private static final long serialVersionUID = -3958555146201762353L;

    /**
     * 提供同步操作的类
     */
    private final Sync sync;

    /**
     * 默认非公平锁
     */
    public ReentrantLockAnalysis() {
        sync = new NonfairSync();
    }

    public ReentrantLockAnalysis(boolean fair) {
        sync = fair ? new FairSync() : new NonfairSync();
    }

    /**
     * 提供同步操作的类
     * 子类分为 公平锁 和 非公平锁 两种
     * 使用AQS state属性 表明 持有lock的线程数
     */
    abstract static class Sync extends AbstractQueuedSynchronizerAnalysis {

        private static final long serialVersionUID = 434686627731367938L;

        /**
         * 上锁
         */
        abstract void lock();

        /**
         * tryAcquire 尝试获取独占锁的非公平版本
         *
         * @param acquires 获取标记(在NonfairSync和FairSync中传的是1，可理解为持有线程lock的计数+1)
         * @return 获取结果
         */
        final boolean nonfairTryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                // 如果计数为0，则直接尝试CAS修改计数为acquires，成功则独占锁（体现非公平）
                if (compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            // 如果当前线程占有锁，则计数+acquires
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0) // overflow
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            // 如果计数不为0且不是当前线程占有锁，则返回false，即未获取到锁
            return false;
        }

        /**
         * 尝试释放锁
         *
         * @param releases 释放标记(在NonfairSync和FairSync中传的是1，可理解为持有线程lock的计数-1)
         * @return 释放结果
         */
        protected final boolean tryRelease(int releases) {
            int c = getState() - releases;
            if (Thread.currentThread() != getExclusiveOwnerThread())
                throw new IllegalMonitorStateException();
            boolean free = false;
            // 如果计数清0，则当前线程释放锁
            if (c == 0) {
                free = true;
                setExclusiveOwnerThread(null);
            }
            // 计数-releases
            setState(c);
            return free;
        }

        protected final boolean isHeldExclusively() {
            return getExclusiveOwnerThread() == Thread.currentThread();
        }

        final ConditionObject newCondition() {
            return new ConditionObject();
        }

        final Thread getOwner() {
            return getState() == 0 ? null : getExclusiveOwnerThread();
        }

        final int getHoldCount() {
            return isHeldExclusively() ? getState() : 0;
        }

        final boolean isLocked() {
            return getState() != 0;
        }

        private void readObject(java.io.ObjectInputStream s)
                throws java.io.IOException, ClassNotFoundException {
            s.defaultReadObject();
            setState(0); // reset to unlocked state
        }
    }

    /**
     * 非公平锁
     */
    static final class NonfairSync extends Sync {

        private static final long serialVersionUID = -8093998654837544459L;

        /**
         * 上锁
         */
        @Override
        void lock() {
            // 直接尝试CAS修改计数为1, 成功则独占锁(体现非公平)
            if (compareAndSetState(0, 1))
                setExclusiveOwnerThread(Thread.currentThread());
            else
                // 不响应中断的独占锁
                acquire(1);
        }

        protected final boolean tryAcquire(int acquires) {
            return nonfairTryAcquire(acquires);
        }
    }

    /**
     * 公平锁
     */
    static final class FairSync extends Sync {

        private static final long serialVersionUID = -8093998654837544459L;

        @Override
        void lock() {
            // 不响应中断的独占锁
            acquire(1);
        }

        /**
         * tryAcquire 尝试获取独占锁的公平版本
         *
         * @param acquires 获取标记(在NonfairSync和FairSync中传的是1，可理解为持有线程lock的计数+1)
         * @return 获取结果
         */
        protected final boolean tryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                // 如果没有前驱节点且计数为0，则直接尝试CAS修改计数为acquires，成功则独占锁（公平）
                if (!hasQueuedPredecessors() &&
                        compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            // 如果当前线程占有锁，则计数+acquires
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0)
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            // 如果计数不为0且不是当前线程占有锁，则返回false，即未获取到锁
            return false;
        }
    }

    /**
     * 获取锁(不响应中断的独占锁)
     */
    public void lock() {
        sync.lock();
    }

    /**
     * 获取锁(响应中断的独占锁)
     *
     * @throws InterruptedException ex
     */
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    public boolean tryLock() {
        return sync.nonfairTryAcquire(1);
    }

    public boolean tryLock(long timeout, TimeUnit unit)
            throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(timeout));
    }

    /**
     * 释放锁
     */
    public void unlock() {
        sync.release(1);
    }

    public Condition newCondition() {
        return sync.newCondition();
    }

    public int getHoldCount() {
        return sync.getHoldCount();
    }

    public boolean isHeldByCurrentThread() {
        return sync.isHeldExclusively();
    }

    public boolean isLocked() {
        return sync.isLocked();
    }

    public final boolean isFair() {
        return sync instanceof FairSync;
    }

    protected Thread getOwner() {
        return sync.getOwner();
    }

    public final boolean hasQueuedThreads() {
        return sync.hasQueuedThreads();
    }

    public final boolean hasQueuedThread(Thread thread) {
        return sync.isQueued(thread);
    }

    public final int getQueueLength() {
        return sync.getQueueLength();
    }

    protected Collection<Thread> getQueuedThreads() {
        return sync.getQueuedThreads();
    }

    public boolean hasWaiters(Condition condition) {
        if (condition == null)
            throw new NullPointerException();
        if (!(condition instanceof AbstractQueuedSynchronizerAnalysis.ConditionObject))
            throw new IllegalArgumentException("not owner");
        return sync.hasWaiters((AbstractQueuedSynchronizerAnalysis.ConditionObject) condition);
    }

    public int getWaitQueueLength(Condition condition) {
        if (condition == null)
            throw new NullPointerException();
        if (!(condition instanceof AbstractQueuedSynchronizerAnalysis.ConditionObject))
            throw new IllegalArgumentException("not owner");
        return sync.getWaitQueueLength((AbstractQueuedSynchronizerAnalysis.ConditionObject) condition);
    }

    protected Collection<Thread> getWaitingThreads(Condition condition) {
        if (condition == null)
            throw new NullPointerException();
        if (!(condition instanceof AbstractQueuedSynchronizerAnalysis.ConditionObject))
            throw new IllegalArgumentException("not owner");
        return sync.getWaitingThreads((AbstractQueuedSynchronizerAnalysis.ConditionObject) condition);
    }

    public String toString() {
        Thread o = sync.getOwner();
        return super.toString() + ((o == null) ?
                "[Unlocked]" :
                "[Locked by thread " + o.getName() + "]");
    }
}
