package com.whc.training.util.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 类加载单元测试
 *
 * 类加载7个阶段：加载(Loading) 验证(Verification) 准备(Preparation) 解析(Resolution) 初始化(Initialization) 使用(Using) 卸载(Unloading)
 *
 * @author whc
 * @version 1.0.0
 * @since 2019年04月27 23:06
 */
@Slf4j
public class ClassLoaderTest {

    /**
     * 被动引用
     */
    @Test
    public void testPassiveCitation() {
        // 对于静态字段，只有直接定义这个字段的类才会被初始化
        log.info(SubClass.value + "");

        // 通过数组定义来引用类，不会触发此类的初始化
        SuperClass[] scArray = new SuperClass[10];

        // final常量在编译阶段会存入调用类的常量池中，不会触发定义常量的类的初始化
        log.info(ConstantClass.HELLO_WORLD);
    }
}

@Slf4j
class ConstantClass {
    static {
        log.info("constant class init");
    }

    public static final String HELLO_WORLD = "hello world";
}

@Slf4j
class SuperClass {
    static {
        log.info("super class init");
    }

    public static int value = 1;
}

@Slf4j
class SubClass extends SuperClass {
    static {
        log.info("sub class init");
    }
}