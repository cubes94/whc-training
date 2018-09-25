package com.whc.training.domain.util;

/**
 * Builder Pattern
 * 不直接生成想要的对象，而是让客户端利用所有必要的参数调用构造器（或静态工厂），得到一个builder对象。
 * 然后使用者在builder对象上调用类似于setter的方法，来设置每个相关的可选参数。最后，使用者调用无参的build方法来生成不可变对象。
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年09月20 09:52
 */
public class Student {

    private final int serNo;
    private final String name;
    private final Integer age;
    private final Integer sex;
    private final Integer classNum;

    private Student(Builder builder) {
        this.serNo = builder.serNo;
        this.name = builder.name;
        this.age = builder.age;
        this.sex = builder.sex;
        this.classNum = builder.classNum;
    }

    @Override
    public String toString() {
        return "Student{" +
                "serNo=" + serNo +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                ", classNum=" + classNum +
                '}';
    }

    public static class Builder implements javafx.util.Builder<Student> {
        // 必选参数
        private final int serNo;
        private final String name;
        // 可选参数
        private Integer age;
        private Integer sex;
        private Integer classNum = 1;

        public Builder(int serNo, String name) {
            this.serNo = serNo;
            this.name = name;
        }

        public Builder age(Integer age) {
            this.age = age;
            return this;
        }

        public Builder sex(Integer sex) {
            this.sex = sex;
            return this;
        }

        public Builder classNum(Integer classNum) {
            this.classNum = classNum;
            return this;
        }

        public Student build() {
            return new Student(this);
        }
    }
}
