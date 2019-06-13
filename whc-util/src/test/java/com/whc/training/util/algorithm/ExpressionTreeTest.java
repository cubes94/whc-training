package com.whc.training.util.algorithm;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Stack;

/**
 * 表达式树
 * <p>
 * a+b*c+(d*e+f)*g ->
 * =          +
 * =     /         \
 * =    +           *
 * =   / \         / \
 * =  a   *       +   g
 * =     / \     / \
 * =    b   c   *   f
 * =           / \
 * =          d   e
 * <p>
 * 前序遍历: ++a*bc*+*defg
 * 中序遍历: a+b*c+d*e+f*g  a+b*c+(d*e+f)*g
 * 后序遍历: abc*+de*f+g*+  {@link RPNTest 即后缀表达式}
 *
 * @author whc
 * @version 1.0.0
 * @since 2019年06月11 14:15
 */
@Slf4j
public class ExpressionTreeTest implements TimeCalculator {

    /**
     * {@link RPNTest}中已经有了将中缀表达式转换为后缀表达式的算法，故此处将后缀表达式转换为表达式树
     * 允许符号+ - * /
     */
    @Test
    public void testExpressionTree() {
        char[] rpn = "abc*+de*f+g*+".toCharArray();
        timeCalc(this::testExpressionTree, rpn);
    }

    private TreeNode<Character> testExpressionTree(char[] rpn) {
        Stack<TreeNode<Character>> stack = new Stack<>();
        for (char c : rpn) {
            if (isSymbol(c)) {
                TreeNode<Character> rightChild = stack.pop();
                TreeNode<Character> leftChild = stack.pop();
                stack.push(new TreeNode<>(c, leftChild, rightChild));
            } else {
                stack.push(new TreeNode<>(c));
            }
        }
        return stack.pop();
    }

    private boolean isSymbol(char c) {
        return '+' == c || '-' == c || '*' == c || '/' == c;
    }
}