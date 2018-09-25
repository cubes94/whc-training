package com.whc.training.test.util;

import com.whc.training.domain.util.Student;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 对象测试
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年09月20 10:05
 */
@Slf4j
public class ObjectTest {

    /**
     * Builder模式创建对象
     */
    @Test
    public void testBuilderPattern() {
        Student student1 = new Student.Builder(1, "tom").build();
        Student student2 = new Student.Builder(1, "tom")
                .age(23).classNum(1).sex(1).build();
        log.info("Builder模式创建对象:\r\n{}\r\n{}", student1, student2);
    }
}
