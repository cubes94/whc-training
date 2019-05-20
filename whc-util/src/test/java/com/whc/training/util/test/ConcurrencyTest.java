package com.whc.training.util.test;

import com.alibaba.fastjson.JSON;
import com.whc.training.util.domain.DelayedCallback;
import com.whc.training.util.domain.MySignal;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

/**
 * 并发性测试
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年05月09 10:09
 */
@Slf4j
public class ConcurrencyTest {

    private static List<String> flagList = new CopyOnWriteArrayList<>();

    private static DelayQueue<DelayedCallback> callbackDelayQueue = new DelayQueue<>();

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
     * 多线程捕获异常
     *
     * @throws Exception e
     */
    @Test
    public void testException() throws Exception {
        // thread的run方法不抛出任何检查型异常，但是自身又会因为一个异常而被终止，导致这个线程的终结。
        // 处理方式一，主动try catch处理
        Thread thread1 = new Thread(() -> {
            try {
                System.out.println(3 / 0);
            } catch (Throwable e) {
                log.error("方案一：{}", e.getMessage());
            }
        });
        thread1.start();
        // 处理方式二，使用UncaughtExceptionHandler检测未捕获的异常，通常结合方案一一起使用
        Thread thread2 = new Thread(() -> {
            System.out.println(3 / 0);
        });
        thread2.setUncaughtExceptionHandler((t, ex) ->
                log.error("方案二: {}", ex.getMessage()));
        thread2.start();
        // 处理方式三，为所有的Thread设置一个默认的UncaughtExceptionHandler
        Thread.setDefaultUncaughtExceptionHandler((t, ex) ->
                log.error("方案三: {}", ex.getMessage()));
        Thread thread3 = new Thread(() -> System.out.println(3 / 0));
        thread3.start();
        // 处理方式四，如果采用线程池通过execute方法去捕获异常，需要将异常的捕获封装到Runnable或Callable中
        Thread thread4 = new Thread(() -> {
            Thread.currentThread().setUncaughtExceptionHandler((t, ex) ->
                    log.error("方案四: {}", ex.getMessage()));
            System.out.println(3 / 0);
        });
        ExecutorService exe = Executors.newFixedThreadPool(10);
        exe.execute(thread4);
        // 处理方式五，如果使用submit提交的任务，异常将被Future.get封装在ExecutionException中重新抛出
        Thread thread5 = new Thread(() -> System.out.println(3 / 0));
        Future<?> future = exe.submit(thread5);
        try {
            future.get();
        } catch (InterruptedException | ExecutionException ex) {
            log.error("方案五: {}", ex.getMessage());
        }
        Thread.sleep(1000);
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
     * 同步关键字 synchronized
     * 依赖于JVM实现、可重入锁、非公平锁、悲观锁
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
     * {@link LockSupport}
     */
    @Test
    public void testLockSupport() throws Exception {
        // 延时队列
        new Thread(() -> {
            try {
                while (true) {
                    callbackDelayQueue.take().getCallback().apply();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        Thread t1 = new Thread(() -> {
            // 设置线程中断
            // 线程不会停止, 只是线程的中断标志被设置为true, 中断后再调用wait, join, sleep时, 会抛出InterruptedException, 并且中断标志重新设置为false
            Thread.currentThread().interrupt();
            log.info("t1: interrupted? {}", Thread.currentThread().isInterrupted());
            // 返回中断标志, 并清除中断标志, 重设为false
            boolean oldInterrupted = Thread.interrupted();
            log.info("t1: oldInterrupted? {}", oldInterrupted);
            log.info("t1: interrupted? {}", Thread.currentThread().isInterrupted());
        });
        t1.start();

        // 一次中断操作后无论线程调用多少次LockSupport.park(), 程序都不会挂起, 而是正常运行
        // park源码内判断了当前线程中断标志是true时不会进行操作
        Thread t2 = new Thread(() -> {
            while (!flagList.contains("testLockSupport_t2")) {
            }
            LockSupport.park();
            log.info("t2: after 1st park");
            LockSupport.park();
            log.info("t2: after 2nd park");
        });
        callbackDelayQueue.add(new DelayedCallback(() -> {
            t2.start();
            t2.interrupt();
            flagList.add("testLockSupport_t2");
        }, 100));

        // 多次调用unpark只会提供一个许可
        // unpark源码只会修改标志位为1，不会递增
        Thread t3 = new Thread(() -> {
            while (!flagList.contains("testLockSupport_t3")) {
            }
            LockSupport.park();
            log.info("t3: after 1st park");
            LockSupport.park();
            log.info("t3: after 2nd park");
        });
        callbackDelayQueue.add(new DelayedCallback(() -> {
            t3.start();
            LockSupport.unpark(t3);
            LockSupport.unpark(t3);
            flagList.add("testLockSupport_t3");
        }, 200));

        // interrupt源码内部会调用一次unpark, 所以之后执行的第一个park操作会修改标志位1->0
        // 如果清除了中断标志, 第二个park操作才会进行阻塞(见t5)
        Thread t4 = new Thread(() -> {
            while (!flagList.contains("testLockSupport_t4")) {
            }
            log.info("t4: oldInterrupted? {}", Thread.interrupted());
            LockSupport.park();
            log.info("t4: after 1st park");
        });
        callbackDelayQueue.add(new DelayedCallback(() -> {
            t4.start();
            t4.interrupt();
            flagList.add("testLockSupport_t4");
        }, 300));

        // 中断标志清除后, LockSupport.park()把程序挂起
        Thread t5 = new Thread(() -> {
            while (!flagList.contains("testLockSupport_t5")) {
            }
            LockSupport.park();
            log.info("t5: after 1st park");
            log.info("t5: oldInterrupted? {}", Thread.interrupted());
            LockSupport.park();
            log.info("t5: after 2nd park");
        });
        callbackDelayQueue.add(new DelayedCallback(() -> {
            t5.start();
            t5.interrupt();
            flagList.add("testLockSupport_t5");
        }, 400));
        Thread.sleep(2000);
    }

    /**
     * 可重入锁：自己可以再次获取自己的内部锁。
     * 相比于synchronized, 增加了以下几点：等待可中断；可实现公平锁(先等待的线程先获得锁)；可实现选择性通知(锁可以绑定多个条件)
     *
     * @see java.util.concurrent.locks.ReentrantLock
     */
    @Test
    public void testReenTrantLock() throws Exception {

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
            while (this.count < 1000) {
                synchronized (list) {
                    // 为防止假唤醒，不用if用while，这样的while循环叫做自旋锁（长时间不调用notify方法会十分消耗CPU）
                    while (!list.isEmpty()) {
                        try {
                            // 调用wait()方法，线程会放弃对象锁，进入等待此对象的等待锁定池
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
            }
        });
        // 消费者
        exe.execute(() -> {
            while (this.count < 1000) {
                synchronized (list) {
                    while (list.isEmpty()) {
                        try {
                            list.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    while (!list.isEmpty()) {
                        Integer custom = list.remove(0);
                        log.info("消费：{}，集合：{}", custom, JSON.toJSON(list));
                    }
                    list.notify();
                }
            }
        });
        this.afterTerminated(exe);
        log.info("最终结果：{}", JSON.toJSON(list));
    }

    /**
     * ThreadLocal提供线程局部变量，不同线程通过get/set方法访问时都有自己独立初始化的变量副本
     * <p>
     * {@link ThreadLocal}
     */
    @Test
    public void testThreadLocal() throws Exception {
        // ThreadLocal可以让变量只被同一个线程进行读和写操作
        ExecutorService exe = Executors.newFixedThreadPool(2);
        ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> -1);
        for (int i = 0; i < 10; i++) {
            final int j = i;
            exe.execute(() -> {
                threadLocal.set(j);
                log.info("I WILL NEVER CHANGE, I AM {}", threadLocal.get());
            });
        }
        this.afterTerminated(exe);
        log.info("I WILL NEVER CHANGE, I AM {}", threadLocal.get());
    }

    /**
     * 可取消的异步计算
     * {@link FutureTask}可用于包装{@link Callable}或{@link Runnable}对象
     */
    @Test
    public void testFutureTask() throws Exception {
        FutureTask<Integer> futureTask = new FutureTask<>(() -> {
            Thread.sleep(1000);
            return 1;
        });
        ExecutorService exe = Executors.newFixedThreadPool(2);
        exe.execute(futureTask);
        if (!futureTask.isDone()) {
            log.info("futureTask is not done");
        }
        Integer result = futureTask.get();
        log.info("result:{}", result);
    }

    /**
     * 线程可以通过join方法设置执行顺序
     */
    @Test
    public void testJoin() throws Exception {
        Thread thread1 = new Thread(() -> {
            System.out.println("thread1 run...");
        });
        Thread thread2 = new Thread(() -> {
            System.out.println("thread2 run...");
            thread1.start();
            try {
                // 等待子线程运行完之后执行
                thread1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread2 end...");
        });
        Thread thread3 = new Thread(() -> {
            System.out.println("thread3 run...");
            thread2.start();
            try {
                thread2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread3 end...");
        });
        thread3.start();
        Thread.sleep(2000);
    }

    /**
     * {@link BlockingDeque}阻塞队列，FIFO，线程安全，适用于生产-消费
     */
    @Test
    public void testBlockingQueue() throws Exception {
        BlockingDeque<Integer> sharedQueue = new LinkedBlockingDeque<>();
        int shareMax = 1000;
        Thread produceThread = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    log.info("produce: {}", i);
                    sharedQueue.put(i);
                }
                sharedQueue.put(shareMax);
            } catch (InterruptedException e) {
                log.error("produce error");
            }
        });
        Thread consumeThread = new Thread(() -> {
            while (true) {
                try {
                    int num = sharedQueue.take();
                    log.info("consume: {}", num);
                    if (num == shareMax) {
                        break;
                    }
                } catch (InterruptedException e) {
                    log.error("consume error");
                }
            }
        });
        ExecutorService exe = Executors.newFixedThreadPool(2);
        exe.execute(consumeThread);
        exe.execute(produceThread);
        this.afterTerminated(exe);
    }

    /**
     * {@link CountDownLatch}计数器
     */
    @Test
    public void testCountDownLatch() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        ExecutorService exe = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            final int j = i;
            exe.execute(() -> {
                log.info("I AM {}", j);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        log.info("计数器：{}", countDownLatch.getCount());
        this.afterTerminated(exe);
    }

    /**
     * 多功能非阻塞的Future，比{@link CountDownLatch}计数器的优点在于不需要关注countDown的逻辑
     * {@link CompletableFuture}
     */
    @Test
    public void testCompletableFuture() throws Exception {
        List<CompletableFuture> futures = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final int j = i;
            futures.add(CompletableFuture.runAsync(() -> log.info("I AM {}", j)));
        }
        CompletableFuture allDoneFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allDoneFuture.get();
    }

    private int count = 0;

    private static int static_count = 1;

    private void addCount(int value) {
        this.count += value;
    }

    private synchronized void syncAddCount(int value) {
        this.count += value;
    }

    private static synchronized void syncAddStaticCount(int value) {
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
        log.info("exe shutdown");
    }
}