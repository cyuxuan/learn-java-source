package club.beenest.algorithm.leetcode.bookintermediatelevelalgorithm.treesandgraphs;

import java.util.*;

/**
 * 二叉树的中序遍历
 * https://leetcode.cn/leetbook/read/top-interview-questions-medium/xv7pir/
 *
 * @author 陈玉轩
 */
public class InorderTraversal {
    public static void main(String[] args) {
        List<Integer> res = doInorderTraversal(tree);
        System.out.println(res.toString());
    }

    /**
     * 顺序循环方式
     * 中序遍历
     *
     * @param tree 待处理数据
     * @return 中序遍历顺序数组
     */
    public static List<Integer> doInorderTraversal(TreeNode tree) {
        // 循环进行中序遍历
        List<Integer> res = new LinkedList<>();
        // 初始化两个栈，一个读取栈
        // 读取栈
        Stack<TreeNode> stack = new Stack<>();
        // 一直读取到读取栈不为空
        do {
            // 当前节点非空，则入栈
            if (tree != null) {
                // 节点入栈
                stack.push(tree);
                // 继续访问左节点
                tree = tree.left;
            } else {
                // 如果当前节点为空则出栈读取
                TreeNode temp = stack.pop();
                // 读取
                res.add(temp.val);
                // 查看自己的右节点
                tree = temp.right;
            }
        } while (stack.size() != 0 || tree != null);
        return res;
    }


    /**
     * 递归方式解决
     * 中序遍历
     *
     * @param tree 待处理数据
     * @return 中序遍历顺序数组
     */
    public static List<Integer> doByrecursive(TreeNode tree) {
        return null;
    }


    private static TreeNode tree;

    private static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    static {
        tree = new TreeNode();
        tree.val = 3;

        TreeNode temp1 = new TreeNode();
        tree.right = temp1;
        temp1.val = 2;

        TreeNode temp2 = new TreeNode();
        tree.left = temp2;
        temp2.val = 1;
    }
}
