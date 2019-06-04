package com.whc;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.util.Base64;

/**
 * 工具类测试
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月16 17:35
 */
public class UtilTest {

    @Test
    public void utilTest() throws Exception {
        String s = Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(new File(
                "C:\\Users\\wuhaichao\\Desktop\\1.jpg")));
    }
}
