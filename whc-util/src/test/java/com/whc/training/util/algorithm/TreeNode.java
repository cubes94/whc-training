package com.whc.training.util.algorithm;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 树节点
 *
 * @author whc
 * @version 1.0.0
 * @since 2019年06月12 16:55
 */
public class TreeNode<E> {
    private E element;
    private TreeNode<E> leftChild;
    private TreeNode<E> rightChild;

    TreeNode(E element, TreeNode<E> leftChild, TreeNode<E> rightChild) {
        this.element = element;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    TreeNode(E element) {
        this.element = element;
    }

    public E getElement() {
        return element;
    }

    public TreeNode<E> getLeftChild() {
        return leftChild;
    }

    public TreeNode<E> getRightChild() {
        return rightChild;
    }

    public void setElement(E element) {
        this.element = element;
    }

    public void setLeftChild(TreeNode<E> leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(TreeNode<E> rightChild) {
        this.rightChild = rightChild;
    }

    @Override
    public String toString() {
        Map<TreeNode<E>, Point> nodeLevelMap = new HashMap<>();
        LinkedList<TreeNode<E>> queue = new LinkedList<>();
        queue.offer(this);
        nodeLevelMap.put(this, new Point(0, 0));
        while (!queue.isEmpty()) {
            TreeNode<E> node = queue.poll();
            if (node.leftChild != null) {
                queue.offer(node.leftChild);
                nodeLevelMap.put(node.leftChild, new Point(nodeLevelMap.get(node).x * 2, nodeLevelMap.get(node).y + 1));
            }
            if (node.rightChild != null) {
                queue.offer(node.rightChild);
                nodeLevelMap.put(node.rightChild, new Point(nodeLevelMap.get(node).x * 2 + 1, nodeLevelMap.get(node).y + 1));
            }
        }
        int depth = nodeLevelMap.entrySet().stream().map(e -> e.getValue().y).max((y1, y2) -> y1 - y2).get();
        int outputX = (int) (PowerTest.doPower(2, depth) * 4 - 3);
        int outputY = 2 * depth + 1;
        String[][] output = new String[outputX][outputY];
        for (Map.Entry<TreeNode<E>, Point> entry : nodeLevelMap.entrySet()) {
            int x = (4 * entry.getValue().x + 2) * ((int) PowerTest.doPower(2, depth - entry.getValue().y)) - 2;
            int y = 2 * entry.getValue().y;
            output[x][y] = entry.getKey().element.toString();
            if (entry.getValue().y != 0) {
                if ((entry.getValue().x & 1) == 0) {
                    output[x + 1][y - 1] = "/";
                } else {
                    output[x - 1][y - 1] = "\\";
                }
            }
        }
        StringBuilder sb = new StringBuilder("\r\n");
        for (int i = 0; i < outputY; i++) {
            for (int j = 0; j < outputX; j++) {
                sb.append(output[j][i] == null ? " " : output[j][i]);
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }
}
