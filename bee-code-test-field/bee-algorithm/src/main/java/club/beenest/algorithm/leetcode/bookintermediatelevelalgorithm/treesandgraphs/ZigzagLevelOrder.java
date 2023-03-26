package club.beenest.algorithm.leetcode.bookintermediatelevelalgorithm.treesandgraphs;

import java.util.*;

/**
 * 二叉树的锯齿形层次遍历
 * https://leetcode.cn/leetbook/read/top-interview-questions-medium/xvle7s/
 *
 * @author 陈玉轩
 */
public class ZigzagLevelOrder {
    public static void main(String[] args) {
        // 获取结果
        List<List<Integer>> res = doZigzagLevelOrder(tree);
        // 输出结果
        for (List<Integer> item1 : res) {
            for (Integer item2 : item1) {
                System.out.print(item2 + " ");
            }
            System.out.println();
        }
    }

    /**
     * 层序遍历
     *
     * @param root 当前节点
     * @return 结果集
     */
    private static List<List<Integer>> doZigzagLevelOrder(TreeNode root) {
        // root为空则返回空串
        if (root == null) {
            return new ArrayList<>();
        }
        // 初始化一个结果集数组
        List<List<Integer>> res = new ArrayList<>();
        // 初始化一个栈
        Stack<TreeNode> stack = new Stack<>();
        // 首先将根节点入栈
        stack.push(root);
        // 记录当前数据是从左往右边还是从右往左
        boolean leftToRight = true;
        do {
            // 初始化一个栈，将当前节点的所有子节点入临时栈
            Stack<TreeNode> tempStack = new Stack<>();
            // 初始化一个数组，用于存储当前层次的数据
            List<Integer> tempRes = new ArrayList<>();
            while (stack.size() != 0) {
                // 获取当前栈顶元素
                TreeNode tempNode = stack.pop();
                // 获取当前栈结果
                tempRes.add(tempNode.val);
                // 当前节点子节点入栈
                if (leftToRight && tempNode.left != null) {
                    tempStack.push(tempNode.left);
                }
                if (tempNode.right != null) {
                    tempStack.push(tempNode.right);
                }
                if (!leftToRight && tempNode.left != null) {
                    tempStack.push(tempNode.left);
                }
            }
            if (leftToRight) {
                leftToRight = false;
            } else {
                leftToRight = true;
            }
            // 将临时栈赋值给读取栈
            stack = tempStack;
            // 结果存储
            res.add(tempRes);
        } while (stack.size() != 0);
        // 返回最终结果
        return res;
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
        // 根节点
        tree = new TreeNode();
        tree.val = 3;

        tree.left = new TreeNode();
        tree.left.val = 9;

        tree.right = new TreeNode();
        tree.right.val = 20;

        tree.left.left = null;
        tree.left.right = null;

        tree.right.left = new TreeNode();
        tree.right.left.val = 15;

        tree.right.right = new TreeNode();
        tree.right.right.val = 7;
    }
}
