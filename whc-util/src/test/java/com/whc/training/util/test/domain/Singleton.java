package com.whc.training.util.test.domain;

import java.io.Serializable;

/**
 * 饿汉式 类加载时就初始化对象
 * 避免反序列化产生多个对象，可以添加readResolve方法
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年10月10 10:14
 */
public class Singleton implements Serializable {

    private static final long serialVersionUID = 6381464950090215799L;

    private static Singleton instance = new Singleton();

    private Singleton() {
    }

    public static Singleton getInstance() {
        return instance;
    }

    public Object readResolve() {
        return instance;
    }
}

/**
 * 懒汉式（线程安全-双重校验锁）
 */
class SyncSingleton {
    private volatile static SyncSingleton instance;

    private SyncSingleton() {
    }

    public static SyncSingleton getInstance() {
        if (instance == null) {
            synchronized (SyncSingleton.class) {
                if (instance == null) {
                    instance = new SyncSingleton();
                }
            }
        }
        return instance;
    }
}
