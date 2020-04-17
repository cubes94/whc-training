package com.whc.training.util.algorithm;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 二叉查找树(Binary Search Tree)/二叉排序树(Binary Sort Tree)/二叉搜索树
 * 对于树中的每个节点X，它的左子树中所有项的值小于X中的项，而它的右子树中所有项的值大于X中的项。
 *
 * @author whc
 * @version 1.0.0
 * @since 2019年06月12 16:30
 */
@Slf4j
public class BinarySearchTreeTest implements TimeCalculator {

    @Test
    public void testBinarySearchTree() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.insert(20);
        tree.insert(10);
        tree.insert(13);
        tree.insert(23);
        tree.insert(28);
        tree.insert(1);
        tree.insert(2);
        tree.insert(45);
        log.info("tree: {}", tree.root);
    }


    class BinarySearchTree<E extends Comparable<? super E>> {

        private TreeNode<E> root;

        public BinarySearchTree() {
        }

        public void makeEmpty() {
            this.root = null;
        }

        public boolean isEmpty() {
            return this.root == null;
        }

        public boolean contains(E e) {
            return this.contains(e, this.root);
        }

        public E findMin() {
            if (this.isEmpty()) throw new RuntimeException("underflow exception");
            return this.findMin(this.root).getElement();
        }

        public E findMax() {
            if (this.isEmpty()) throw new RuntimeException("underflow exception");
            return this.findMax(this.root).getElement();
        }

        public void insert(E e) {
            if (this.isEmpty()) this.root = new TreeNode<>(e);
            insert(e, this.root);
        }

        public void remove(E e) {
            remove(e, this.root);
        }

        private boolean contains(E e, TreeNode<E> node) {
            if (node == null) return false;
            int compareResult = e.compareTo(node.getElement());
            if (compareResult < 0) {
                return contains(e, node.getLeftChild());
            } else if (compareResult > 0) {
                return contains(e, node.getRightChild());
            } else {
                return true;
            }
        }

        private TreeNode<E> findMin(TreeNode<E> node) {
            if (node != null) {
                while (node.getLeftChild() != null) {
                    node = node.getLeftChild();
                }
            }
            return node;
        }

        private TreeNode<E> findMax(TreeNode<E> node) {
            if (node != null) {
                while (node.getRightChild() != null) {
                    node = node.getRightChild();
                }
            }
            return node;
        }

        private TreeNode<E> insert(E e, TreeNode<E> node) {
            if (node == null) {
                return new TreeNode<>(e);
            }
            int compareResult = e.compareTo(node.getElement());
            if (compareResult < 0) {
                node.setLeftChild(insert(e, node.getLeftChild()));
            } else if (compareResult > 0) {
                node.setRightChild(insert(e, node.getRightChild()));
            } else {
                ; // duplicate, do nothing
            }
            return node;
        }

        private TreeNode<E> remove(E e, TreeNode<E> node) {
            if (node == null) {
                return null; // e not found, do nothing
            }
            int compareResult = e.compareTo(node.getElement());
            if (compareResult < 0) {
                node.setLeftChild(remove(e, node.getLeftChild()));
            } else if (compareResult > 0) {
                node.setRightChild(remove(e, node.getRightChild()));
            }
            // 这里将右子树的最小值提出作为新的根节点，在频繁的insert/remove之后可能出现左子树深度过大、右子树深度过小的现象
            else if (node.getLeftChild() != null && node.getRightChild() != null) {
                node.setElement(findMin(node.getRightChild()).getElement());
                node.setRightChild(remove(node.getElement(), node.getRightChild()));
            } else {
                node = (node.getLeftChild() != null) ? node.getLeftChild() : node.getRightChild();
            }
            return node;
        }
    }
}
