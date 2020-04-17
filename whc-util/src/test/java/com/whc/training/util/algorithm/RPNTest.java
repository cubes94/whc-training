package com.whc.training.util.algorithm;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 逆波兰式（Reverse Polish notation，RPN，或逆波兰记法）
 * 后缀表达式
 * <p>
 * a+b*c+(d*e+f)*g  ->  abc*+de*f+g*+
 *
 * @author whc
 * @version 1.0.0
 * @since 2019年06月10 14:17
 */
@Slf4j
public class RPNTest implements TimeCalculator {

    @Test
    public void testRPN() {
        char[] pattern = "a+b*c+(d*e+f)*g+(a*b+c)".toCharArray();
        timeCalc(this::doRPN, pattern);
    }

    /**
     * 转换为后缀表达式
     * 允许符号 + - * / ( )
     *
     * @param pattern 普通表达式
     * @return 后缀表达式
     */
    private char[] doRPN(char[] pattern) {
        Stack<Character> stack = new Stack<>();
        List<Character> rpnList = new ArrayList<>();
        for (char c : pattern) {
            if ('+' == c || '-' == c) {
                while (!stack.empty() && (stack.peek() == '*' || stack.peek() == '/' || stack.peek() == '+' || stack.peek() == '-')) {
                    rpnList.add(stack.pop());
                }
                stack.push(c);
            } else if ('*' == c || '/' == c) {
                if (!stack.empty() && (stack.peek() == '*' || stack.peek() == '/')) {
                    rpnList.add(stack.pop());
                }
                stack.push(c);
            } else if ('(' == c) {
                stack.push(c);
            } else if (')' == c) {
                while (!stack.empty() && stack.peek() != '(') {
                    rpnList.add(stack.pop());
                }
                if (stack.empty()) {
                    throw new RuntimeException("wrong pattern");
                }
                stack.pop();
            } else {
                rpnList.add(c);
            }
        }
        while (!stack.empty()) {
            if (stack.peek() == '(' || stack.peek() == ')') {
                stack.pop();
            } else {
                rpnList.add(stack.pop());
            }
        }
        char[] rpn = new char[rpnList.size()];
        for (int i = 0; i < rpnList.size(); i++) {
            rpn[i] = rpnList.get(i);
        }
        return rpn;
    }
}