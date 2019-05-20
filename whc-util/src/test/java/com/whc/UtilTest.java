package com.whc;

import org.junit.Test;

/**
 * 工具类测试
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月16 17:35
 */
public class UtilTest {

    @Test
    public void utilTest() {

    }

    private void testSync() {
        synchronized (this) {
            System.out.println("synchronized");
        }
    }
}
