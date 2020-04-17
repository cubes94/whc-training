package com.whc.training.util.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Jvm单元测试
 *
 * @author whc
 * @version 1.0.0
 * @since 2019年04月06 09:06
 */
@Slf4j
public class JvmTest {

    /**
     * VM args: -Xms20M -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
     * <p>
     * 测试堆溢出
     */
    @Test
    public void testHeapOOM() {
        List<JvmTest> list = new ArrayList<>();
        while (true)
            list.add(new JvmTest());
//        java.lang.OutOfMemoryError: Java heap space
    }


}
