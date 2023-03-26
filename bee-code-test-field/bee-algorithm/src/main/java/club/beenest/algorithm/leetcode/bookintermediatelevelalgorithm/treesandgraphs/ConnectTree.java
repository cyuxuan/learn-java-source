package club.beenest.algorithm.leetcode.bookintermediatelevelalgorithm.treesandgraphs;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 填充每个节点的下一个右侧节点指针
 * https://leetcode.cn/leetbook/read/top-interview-questions-medium/xvijdh/
 *
 * @author 陈玉轩
 */
public class ConnectTree {
    public static void main(String[] args) {
        // 使用广度优先遍历执行该函数
        dfs(root);
    }

    /**
     * 优化的bfs
     * 前提是需要当前树为一个完美二叉树
     */
    public static Node betterBFS(Node root) {
        if (root == null) {
            return root;
        }
        // 记录当前节点
        Node curr = root;
        while (curr.left != null) {
            // 指向子节点的右节点
            Node son = curr.left;
            Node sson = son;
            // 子节点没有右节点则
            while (curr != null && curr.left != null) {
                // 左节点指向右节点
                sson.next = curr.right;
                sson = sson.next;
                // 获取父节点的下一个节点
                curr = curr.next;
                // 右节点指向父节点的右节点的左子节点
                sson.next = curr.left;
                sson = sson.next;
            }
            curr = son.left;
        }
        return root;
    }

    /**
     * 使用二叉树的广度优先遍历执行
     * 即二叉树的层次遍历
     *
     * @param root 根节点
     */
    public static void BFS(Node root) {
        if (root == null) {
            return;
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        while (queue.size() != 0) {
            // 当前数据出队数量
            int size = queue.size();
            // size即为每一层的数量
            Node node = null;
            for (int i = 0; i < size; i++) {
                Node temp = queue.poll();
                if (node != null) {
                    node.next = temp;
                }
                node = temp;

                if (node.left != null) {
                    // 左子节点入队
                    queue.add(node.left);
                }

                if (node.right != null) {
                    // 右子节点入队
                    queue.add(node.right);
                }
            }
        }
    }


    /**
     * 递归执行广度优先遍历
     *
     * @param root 根节点
     * @return 根节点
     */
    private static void dfs(Node root) {
        doDfs(root, null);
    }

    /**
     * 递归执行
     *
     * @param left  当前节点
     * @param right 当前节点的右节点
     * @return 返回根节点
     */
    private static void doDfs(Node left, Node right) {
        if (left == null) {
            return;
        }
        // 当前节点左节点指向右节点
        left.next = right;
        // left 和 right 是当前这次的父节点的左右子节点是必须要连接的
        doDfs(left.left, left.right);
        // 判断父节点是否还有右节点，如果有则当前的父节点的右节点需要指向父节点的右节点的子节点
        doDfs(left.right, left.next == null ? null : left.next.left);
    }

    private static Node root;

    private static class Node {
        public int val;
        public Node left;
        public Node right;
        public Node next;

        public Node() {
        }

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, Node _left, Node _right, Node _next) {
            val = _val;
            left = _left;
            right = _right;
            next = _next;
        }
    }


    static {
        root = new Node();
        root.val = 1;

        root.left = new Node();
        root.left.val = 2;

        root.right = new Node();
        root.right.val = 3;

        root.left.left = new Node();
        root.left.left.val = 4;

        root.left.right = new Node();
        root.left.right.val = 5;

        root.right.left = new Node();
        root.right.left.val = 6;

        root.right.right = new Node();
        root.right.right.val = 7;
    }
}
