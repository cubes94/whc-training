package com.whc.training.util.test;

import com.whc.common.constants.FileConstants;
import com.whc.training.util.domain.Singleton;
import com.whc.training.util.domain.Student;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.function.DoubleBinaryOperator;

/**
 * 对象测试
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年09月20 10:05
 */
@Slf4j
public class ObjectTest extends IOTest {

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

    /**
     * 单例模式 避免反序列化产生多个对象
     */
    @Test
    public void testSingletonPattern() throws Exception {
        Singleton singleton = Singleton.getInstance();
        File txtFile = createFile(FileConstants.FILE_EXT_TXT);
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(txtFile));
        outputStream.writeObject(singleton);
        outputStream.close();
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(txtFile));
        Singleton singleton1 = (Singleton) inputStream.readObject();
        inputStream.close();
        log.info("序列化测试对象是否相同: {}", singleton.equals(singleton1));
        deleteTestFile();

    }

    /**
     * 可以使用枚举来创建单例（在枚举类型中申明抽象方法，并在constant-specific class body中实现具体方法， 或使用接口）
     */
    @Test
    public void testEnum() {
        test(BaseOperation.class, 1, 2);
        test(BaseOperation.class, 3, 4);
        for (LambdaOperation o : LambdaOperation.values()) {
            log.info("{} {} {} = {}", 1, o, 2, o.apply(1, 2));
        }
    }

    private <T extends Enum<T> & Operation> void test(Class<T> tClass, double num1, double num2) {
        for (Operation o : tClass.getEnumConstants()) {
            log.info("{} {} {} = {}", num1, o, num2, o.apply(num1, num2));
        }
    }

    public interface Operation {

        double apply(double x, double y);

    }

    public enum BaseOperation implements Operation {
        PLUS("+") {
            public double apply(double x, double y) {
                return x + y;
            }
        },
        MINUS("-") {
            public double apply(double x, double y) {
                return x - y;
            }
        };

        // 可以将一个常量与具体方法绑定起来，更加优雅
        private final String symbol;

        BaseOperation(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return symbol;
        }

    }

    enum LambdaOperation {
        PLUS("+", (x, y) -> x + y),
        MINUS("-", (x, y) -> x - y);

        private final String symbol;

        private final DoubleBinaryOperator op;

        LambdaOperation(String symbol, DoubleBinaryOperator op) {
            this.symbol = symbol;
            this.op = op;
        }

        public double apply(double x, double y) {
            return op.applyAsDouble(x, y);
        }

        @Override
        public String toString() {
            return symbol;
        }
    }

    /**
     * 测试注解
     */
    @Test
    public void testAnnotation() throws Exception {
        testNPE(this.getClass().getMethod("throwNPE"));
    }

    private void testNPE(Method method) {
        try {
            method.invoke(this);
        } catch (Exception e) {
            Throwable exc = e.getCause();
            Class<? extends Exception> excClass = method.getAnnotation(ExceptionTest.class).value();
            if (excClass.isInstance(exc)) {
                log.info("catch NPE");
            }
        }
    }

    @ExceptionTest(NullPointerException.class)
    public void throwNPE() {
        log.info("{}", "test NPE");
        throw new NullPointerException("test NPE");
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface ExceptionTest {
        Class<? extends Exception> value();
    }
}
