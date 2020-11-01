package com.whc.training.util.test;

import com.whc.util.response.Response;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.Serializable;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Reflection单元测试
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年05月04 11:34
 */
@Slf4j
public class ReflectionTest {

    /**
     * 类信息
     */
    @Test
    public void testClass() throws Exception {
        Class myClass = MyClass.class;
        log.info("类名（含包名）：{}", myClass.getName());
        log.info("类名（不含包名）：{}", myClass.getSimpleName());
        log.info("是否私有：{}", Modifier.isPrivate(myClass.getModifiers()));
        log.info("包信息：{}", myClass.getPackage());
        Class superClass = myClass.getSuperclass();
        log.info("父类名（含包名）：{}", superClass.getName());
        // 当前类实现的接口，父类实现的接口
        log.info("接口：{}", Stream.of(myClass.getInterfaces()).map(Class::getName).collect(Collectors.joining("\r\n\t")));
        // 构造器
        Constructor[] constructors = myClass.getConstructors();
        log.info("方法：{}", Stream.of(myClass.getMethods()).map(Method::toString).collect(Collectors.joining("\r\n\t")));
        log.info("变量：{}", Stream.of(myClass.getFields()).map(Field::getName).collect(Collectors.joining("\r\n\t")));
        log.info("注解：{}", Stream.of(myClass.getAnnotations()).map(Annotation::toString).collect(Collectors.joining("\r\n\t")));
    }

    /**
     * 构造器
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testConstructor() throws Exception {
        Constructor constructor = MyClass.class.getConstructor(ReflectionTest.class, String.class, Integer.class);
        MyClass myClass = (MyClass) constructor.newInstance(this, "", Integer.MAX_VALUE);
        log.info("通过构造器获取实体：{}", myClass);
    }

    /**
     * 变量
     */
    @Test
    public void testField() throws Exception {
        Class myClass = MyClass.class;
        Field string = myClass.getField("string");
        Field integer = myClass.getDeclaredField("integer");
        integer.setAccessible(true);
        MyClass myClassInstance = new MyClass();
        string.set(myClassInstance, "string");
        integer.set(myClassInstance, Integer.MAX_VALUE);
        log.info("通过set获取实体：{}", myClassInstance);
    }

    /**
     * 方法
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testMethod() throws Exception {
        Class myClass = MyClass.class;
        Method setString = myClass.getMethod("setString", String.class);
        Method setInteger = myClass.getMethod("setInteger", Integer.class);
        Method integerDecrease = myClass.getDeclaredMethod("integerDecrease");
        integerDecrease.setAccessible(true);
        MyClass myClassInstance = new MyClass();
        setString.invoke(myClassInstance, "string");
        setInteger.invoke(myClassInstance, Integer.MAX_VALUE);
        integerDecrease.invoke(myClassInstance);
        log.info("通过setter获取实体：{}", myClassInstance);
    }

    /**
     * 泛型
     */
    @Test
    public void testGenerics() throws Exception {
        Class myClass = MyClass.class;
        Method getStringList = myClass.getDeclaredMethod("getStringList");
        Type returnType = getStringList.getGenericReturnType();
        if (returnType instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) returnType;
            Type[] typeArguments = type.getActualTypeArguments();
            log.info("泛型类型：{}", Stream.of(typeArguments).map(t -> ((Class) t).toString()).collect(Collectors.joining("\r\n\t")));
        }
    }

    /**
     * 数组
     */
    @Test
    public void testArray() throws Exception {
        int[] intArray = (int[]) Array.newInstance(int.class, 3);
        Array.set(intArray, 0, 123);
        Array.set(intArray, 1, 456);
        Array.set(intArray, 2, 789);
        log.info("数组数据：{}", IntStream.iterate(0, i -> i + 1).limit(3).mapToObj(i -> Array.get(intArray, i).toString()).collect(Collectors.joining("\r\n\t")));
    }

    /**
     * 动态代理
     */
    @Test
    public void testDynamicProxy() throws Exception {

        MyInvocationHandler myInvocationHandler = new MyInvocationHandler();
        MyInterface proxy = (MyInterface) myInvocationHandler.bind(new MyClass());
        Response<?> response = proxy.doSomething(String.CASE_INSENSITIVE_ORDER);
        log.info("动态代理：{}", response);
    }


    private static class MyInvocationHandler implements InvocationHandler {

        private Object target;

        private Object bind(Object target) {
            this.target = target;
            return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            log.info("调用前操作");
            Object object = method.invoke(target, args);
            log.info("调用后操作");
            return object;
        }
    }

    @Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    private @interface MyAnnotation {
        String name();

        String value();
    }

    private static class MyObject implements Serializable {

        private static final long serialVersionUID = -7706923668056417351L;

    }

    private interface MyInterface {

        Response<?> doSomething(Object param);
    }

    @MyAnnotation(name = "name", value = "value")
    @Data
    @EqualsAndHashCode(callSuper = true)
    private static class MyClass extends MyObject implements MyInterface {

        private static final long serialVersionUID = 3989786430981146549L;

        public MyClass() {
        }

        public MyClass(String string, Integer integer) {
            this.string = string;
            this.integer = integer;
        }

        @MyAnnotation(name = "name", value = "value")
        public String string;

        @MyAnnotation(name = "name", value = "value")
        private Integer integer;

        private List<String> stringList;

        @MyAnnotation(name = "name", value = "value")
        private void integerDecrease() {
            this.integer--;
        }

        @Override
        public Response<?> doSomething(Object param) {
            return Response.ok(param);
        }
    }
}
