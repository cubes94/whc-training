package com.whc;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.util.Base64;
import java.util.HashSet;
import java.util.Objects;

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
        String s = Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(new File(
                "C:\\Users\\wuhaichao\\Desktop\\1.jpg")));
    }

    @Test
    public void test() throws Exception {
        Stu s1 = new Stu("a", "b1");
        Stu s11 = new Stu("a", "b1");
        Stu s2 = new Stu("a", "b2");
        Stu s22 = new Stu("a", "b2");
        Stu s3 = new Stu("a", "b3");
        Stu s4 = new Stu("a", "b4");
        Stu s5 = new Stu("a", "b5");
        Stu ss1 = new Stu("aa", "b1");
        Stu ss2 = new Stu("aa", "b2");
        Stu ss3 = new Stu("aa", "b3");
        Stu ss4 = new Stu("aa", "b4");
        Stu ss5 = new Stu("aa", "b5");
        final HashSet<Stu> stus = new HashSet<>();
        stus.add(s1);
        stus.add(s11);
        stus.add(s2);
        stus.add(s22);
        stus.add(s3);
        stus.add(s4);
        stus.add(s5);
        stus.add(ss1);
        stus.add(ss2);
        stus.add(ss3);
        stus.add(ss4);
        stus.add(ss5);
    }


    @Data
    class Stu {

        private String a;

        private String b;

        public Stu(String a, String b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public int hashCode() {
            return a.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Stu)) return false;
            Stu stu = (Stu) o;
            return Objects.equals(a, stu.a) &&
                    Objects.equals(b, stu.b);
        }
    }
}