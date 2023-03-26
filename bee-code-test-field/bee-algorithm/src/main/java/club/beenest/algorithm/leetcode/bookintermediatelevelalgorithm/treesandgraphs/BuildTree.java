package club.beenest.algorithm.leetcode.bookintermediatelevelalgorithm.treesandgraphs;

import java.util.ArrayList;
import java.util.List;

/**
 * 从前序和中序遍历序列构造二叉树
 * https://leetcode.cn/leetbook/read/top-interview-questions-medium/xvix0d/
 *
 * @author 陈玉轩
 */
public class BuildTree {



    public static void main(String[] args) {
        // 前序
        int[] preorder = {7,-10,-4,3,-1,2,-8,11};
        // 中序
        int[] inorder = {-4,-10,3,-1,7,11,-8,2};
        TreeNode node = doBuilderTree(preorder, inorder);
    }


    public static TreeNode doBuilderTree(int[] preorder, int[] inorder) {
        spreorder = preorder;
        return doIt(inorder);
    }

    private static int[] spreorder;
    private static TreeNode doIt(int[] inorder) {
        // 判断节点,返回空节点
        if (inorder == null || inorder.length == 0) {
            return null;
        }

        // 取前序序列中的index处，为当前区分左右子树的根据
        int rootVal = spreorder[0];
        // 去除第一个元素
        int[] temp = new int[spreorder.length - 1];
        for(int i = 1; i < spreorder.length; i++) {
            temp[i - 1] = spreorder[i];
        }
        spreorder = temp;

        int inLen = inorder.length;

        // 查找 中序序列 中对应参数的位置
        int inOrderIndex = 0;
        while (inOrderIndex < inLen && inorder[inOrderIndex] != rootVal) {
            inOrderIndex++;
        }

        // 找到位置后，计算出左子树和右子树的数量
        // 左子树数量
        int leftNum = inOrderIndex;
        int rightNum = inLen - inOrderIndex - 1;

        // 初始化树
        int[] leftTree = new int[inOrderIndex];
        int[] rightTree = new int[rightNum];

        // 分出左子树
        for (int i = 0; i < inLen; i++) {
            if (i < leftNum) {
                leftTree[i] = inorder[i];
            }
            if (i > leftNum) {
                rightTree[i - leftNum - 1] = inorder[i];
            }
        }

        // 生成当前节点
        TreeNode root = new TreeNode();
        root.val = rootVal;

        // 获取左子树
        root.left = doIt(leftTree);
        // 获取右子树
        root.right = doIt(rightTree);

        // 最终返回根节点
        return root;
    }

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
}



