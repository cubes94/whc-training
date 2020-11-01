package com.whc;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UtilTest {

    @Test
    public void utilTest() throws Exception {
        final File[] files = new File("C:\\Users\\wuhaichao\\Desktop").listFiles((dir, name) -> name.split("\\.")[0].equals("1"));
        String s = null;
        if (files != null && files.length > 0) {
            s = Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(files[0]));
        }
    }


}