package com.whc.training.test.util;

import com.alibaba.fastjson.JSON;
import com.whc.training.domain.util.MySignal;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 并发性测试
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年05月09 10:09
 */
@Slf4j
public class ConcurrencyTest {

    /**
     * 基本测试
     */
    @Test
    public void testBasicThread() throws Exception {
        Thread thread = new Thread(() -> log.info("Thread running"));

        ExecutorService exe = Executors.newFixedThreadPool(10);
        exe.execute(thread);
        this.afterTerminated(exe);
    }

    /**
     * 竞态条件 & 临界区
     * 当两个线程竞争同一资源时，如果对资源的访问顺序敏感，就称存在竞态条件。导致竞态条件发生的代码区称作临界区。
     * 下面Counter.add()方法就是一个临界区，可以使用适当的同步就可以避免竞态条件。
     */
    @Test
    public void testRaceCondition() throws Exception {
        int targetCount = 10;
        this.count = 0;
        ExecutorService exe = Executors.newFixedThreadPool(targetCount);
        for (int i = 0; i < targetCount; i++) {
            exe.execute(() -> {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.addCount(1);
                log.info("+1");
            });
        }
        this.afterTerminated(exe);
        if (this.count == targetCount) {
            this.testRaceCondition();
        } else {
            log.info("最终数值：{}", this.count);
        }
    }

    /**
     * 同步关键字
     */
    @Test
    public void testSynchronized() throws Exception {
        // 实例方法
        this.syncAddCount(1);
        // 静态方法
        syncAddStaticCount(1);
        // 实例方法中的同步块 <=> 等价于addCount
        this.syncAddCount1(1);
        // 静态方法中的同步块 <=> 等价于addStaticCount
        syncAddStaticCount1(1);
    }

    /**
     * 线程通信
     */
    @Test
    public void testThreadSignaling1() throws Exception {
        // 通过共享对象通信
        MySignal mySignal = new MySignal();
        ExecutorService exe = Executors.newFixedThreadPool(2);
        exe.execute(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.addCount(1);
            mySignal.setHasDataToProcess(true);
            log.info("执行线程1完毕：{}", this.count);
        });
        exe.execute(() -> {
            while (!mySignal.hasDataToProcess()) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.addCount(1);
            log.info("执行线程2完毕：{}", this.count);
        });
        this.afterTerminated(exe);
        log.info("最终数值：{}", this.count);

    }

    /**
     * 线程通信
     */
    @Test
    public void testThreadSignaling2() throws Exception {
        // wait() notify() notifyAll()
        ExecutorService exe = Executors.newFixedThreadPool(2);
        // 锁的同步块
        // 不允许使用全局对象或字符常量等作为锁的同步块，应该使用对应唯一的对象，
        // 不然JVM内部会把常量字符串转换成同一个对象，即时有2个不同的实例，这样可能发生假唤醒。通常不使用notifyAll，即使加了自旋锁，应该唤醒的线程可能会被遗漏
        List<Integer> list = new ArrayList<>();

        // 生产者
        exe.execute(() -> {
            synchronized (list) {
                // 为防止假唤醒，不用if用while，这样的while循环叫做自旋锁（长时间不调用notify方法会十分消耗CPU）
                while (!list.isEmpty()) {
                    try {
                        list.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                this.syncAddCount(1);
                list.add(this.count);
                log.info("生产：{}，集合：{}", this.count, JSON.toJSON(list));
                list.notify();
            }
        });
        // 消费者
        exe.execute(() -> {
            synchronized (list) {
                while (list.isEmpty()) {
                    try {
                        list.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Integer custom = list.remove(0);
                log.info("消费：{}，集合：{}", custom, JSON.toJSON(list));
                list.notify();
            }
        });
        this.afterTerminated(exe);
        log.info("最终结果：{}", JSON.toJSON(list));
    }

    /**
     * {@link ThreadLocal}
     */
    @Test
    public void testThreadLocal() throws Exception {
        // ThreadLocal可以让变量只被同一个线程进行读和写操作
        ExecutorService exe = Executors.newFixedThreadPool(2);
        ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
        for (int i = 0; i < 10; i++) {
            final int j = i;
            exe.execute(() -> {
                threadLocal.set(j);
                log.info("I WILL NEVER CHANGE, I AM {}", j);
            });
        }
        this.afterTerminated(exe);
    }


    private int count = 0;

    private static int static_count = 1;

    private void addCount(int value) {
        this.count += value;
    }

    private synchronized void syncAddCount(int value) {
        this.count += value;
    }

    private synchronized void syncAddStaticCount(int value) {
        static_count += value;
    }

    private void syncAddCount1(int value) {
        synchronized (this) {
            this.count += value;
        }
    }

    private void syncAddStaticCount1(int value) {
        synchronized (ConcurrencyTest.class) {
            static_count += value;
        }
    }

    /**
     * 关闭线程池
     *
     * @param exe 线程池
     */
    private void afterTerminated(ExecutorService exe) throws Exception {
        exe.shutdown();
        while (!exe.isTerminated()) {
            Thread.sleep(200);
        }
    }


    @Test
    public void test() {
        log.info("{}",2>>1);
    }
}