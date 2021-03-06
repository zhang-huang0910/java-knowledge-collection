package io.ziheng.tree;

import io.ziheng.tree.TreeNode;
import io.ziheng.tree.BinarySearchTree;
import io.ziheng.tree.AVLTree;

import java.util.Queue;
import java.util.Random;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

public class BinaryTreeTest {
    public static void main(String[] args) {
        testBinarySearchTree();
        testBinarySearchTreeRemoveNode();
        testBinarySearchTreeRemoveMinAndMax();
        testAVLTree();
        testAVLTreeRemoveNode();
    }
    public static void testAVLTree() {
        AVLTree<Integer> avlTree = new AVLTree<>();
        Random random = new Random();
        int capacity = 1000000;
        long startTime = System.nanoTime();
        for (int i = 0; i < capacity; i++) {
            avlTree.add(random.nextInt(Integer.MAX_VALUE));
        }
        assert avlTree.size() == capacity;
        List<Integer> list = new ArrayList<>();
        while (!avlTree.isEmpty()) {
            list.add(avlTree.removeMin());
        }
        assert list.size() == capacity;
        for (int i = 1; i < capacity; i++) {
            assert list.get(i) > list.get(i - 1);
        }
        long endTime = System.nanoTime();
        System.out.println(
            "Test result: OK, time: " +
            (endTime - startTime) / (1000 * 1000 * 1000 * 1.0)
        );
    }
    public static void testAVLTreeRemoveNode() {
        Integer[] arr = new Integer[]{5, 3, 6, 8, 4, 2, 7, };
        AVLTree<Integer> avlTree = AVLTree.buildWith(arr);
        avlTree.travelsalTree();
        avlTree.remove(3);
        avlTree.remove(5);
        avlTree.remove(6);
        avlTree.travelsalTree();
    }
    public static void testBinarySearchTreeRemoveNode() {
        Integer[] arr = new Integer[]{5, 3, 6, 8, 4, 2, 7, };
        //AVLTree<Integer> avlTree = AVLTree.buildWith(arr);
        BinarySearchTree<Integer> binarySearchTree =
            BinarySearchTree.buildWith(arr);
        binarySearchTree.travelsalTree();
        binarySearchTree.remove(3);
        binarySearchTree.remove(5);
        binarySearchTree.remove(6);
        binarySearchTree.travelsalTree();
    }
    public static void testBinarySearchTreeRemoveMinAndMax() {
        BinarySearchTree<Integer> binarySearchTree = new BinarySearchTree<>();
        Random random = new Random();
        int capacity = 1000000;
        long startTime = System.nanoTime();
        for (int i = 0; i < capacity; i++) {
            binarySearchTree.add(random.nextInt(Integer.MAX_VALUE));
        }
        assert binarySearchTree.size() == capacity;
        List<Integer> list = new ArrayList<>();
        while (!binarySearchTree.isEmpty()) {
            list.add(binarySearchTree.removeMin());
        }
        assert list.size() == capacity;
        for (int i = 1; i < capacity; i++) {
            assert list.get(i) > list.get(i - 1);
        }
        long endTime = System.nanoTime();
        System.out.println(
            "Test result: OK, time: " +
            (endTime - startTime) / (1000 * 1000 * 1000 * 1.0)
        );
    }
    public static void testBinarySearchTree() {
        BinarySearchTree<Integer> binarySearchTree = new BinarySearchTree<>();
        int[] arr = new int[]{5, 3, 6, 8, 4, 2, 7, };
        /**
         *          5
         *        /   \
         *       3     6
         *      /  \    \
         *     2    4    8
         *              /
         *             7
         */
        for (int i : arr) {
            binarySearchTree.add(i);
        }
        /**
         * PreOrder:  [5, 3, 2, 4, 6, 8,]
         * InOrder:   [2, 3, 4, 5, 6, 8,]
         * PostOrder: [2, 4, 3, 8, 6, 5,]
         */
        assert binarySearchTree.contains(5) == true;
        assert binarySearchTree.contains(55) == false;
        binarySearchTree.travelsalTree();
    }
    public static void main2(String[] args) {
        /*
        TreeNode<Integer> root = new TreeNode<Integer>(null, null, null);
        TreeNode<Integer> node1 = new TreeNode<Integer>(1, null, null);
        TreeNode<Integer> node2 = new TreeNode<Integer>(2, null, null);
        root.setLeftNode(node1);
        root.setRightNode(node2);
        levelOrderTraverseBinaryTree(root);
        */

        TreeNode<Integer> root = new TreeNode<Integer>();
        buildBinaryTree(root, new Integer[]{0, 1, 2, 3, 4}, 1);
        levelOrderTraverseBinaryTree(root);
    }
    public static <E> TreeNode<E> buildTreeFromArrayTemplate(E[] arr) {
        TreeNode<E> head = new TreeNode<E>();
        int length = arr.length;
        for (int i = 0; i < length; ++i) {
            // ...
        }
        return null;
    }
    // 用数组建立普通二叉树
    private static <E> TreeNode<E> buildBinaryTree(TreeNode<E> root, E[] arr, int index) {
        if (index > arr.length / 2) {
            return root;
        }
        if (index == 1) {
            root.setElement(arr[0]);
        }
        root.setLeftNode(new TreeNode<E>(arr[index * 2 - 1]));
        root.setRightNode(new TreeNode<E>(arr[index * 2 - 0]));
        buildBinaryTree(root.getLeftNode(), arr, index + 1);
        buildBinaryTree(root.getRightNode(), arr, index + 2);
        return root;
    }
    /* 层序遍历二叉树 */
    public static <E> void levelOrderTraverseBinaryTree(TreeNode<E> root) {
        if (root == null) {
            return;
        }
        Queue<TreeNode<E>> queue = new LinkedList<TreeNode<E>>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode<E> node = queue.poll();
            visit(node);
            if (node.getLeftNode() != null) {
                queue.offer(node.getLeftNode());
            }
            if (node.getRightNode() != null) {
                queue.offer(node.getRightNode());
            }
        }
    }
    public static <E> void visit(TreeNode<E> node) {
        System.out.println(node);
    }
}
/* EOF */