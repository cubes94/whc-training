package com.whc.training.util.algorithm;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AVL树：每个节点的左子树和右子树的高度最多差1的二叉查找树(空树的高度定义为-1)
 *
 * @author whc
 * @version 1.0.0
 * @since 2019年06月27 10:22
 */
@Slf4j
public class AVLTreeTest implements TimeCalculator {

    @Test
    public void testAVLTree() throws Exception {
    }

    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    private TreeNode build(Integer[] xs) {
        if (xs.length == 0) return null;
        List<TreeNode> nodes = Arrays.stream(xs).map(x ->
                x == null ? null : new TreeNode(x)
        ).collect(Collectors.toList());
        for (int i = 1; i < nodes.size(); i++) {
            if (nodes.get(i) != null) {
                if (i % 2 == 0) {
                    nodes.get((i - 2) / 2).right = nodes.get(i);
                } else {
                    nodes.get((i - 1) / 2).left = nodes.get(i);
                }
            }
        }
        return nodes.get(0);
    }
}