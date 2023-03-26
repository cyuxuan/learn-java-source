package club.beenest.algorithm.leetcode.bookintermediatelevelalgorithm.treesandgraphs;

/**
 * 二叉搜索树中第K小的元素
 *
 * @author 陈玉轩
 * @since 22.12.03
 */
public class KthSmallest {
    private static int kn;
    private static int value = -1;

    public static void main(String[] args) {
        int k = 41;
        kn = k;
        doKthSmallest(new TreeNode());
    }

    /**
     * 通过中序遍历获取最小的第 K　个数
     *
     * @param root 　根节点
     */
    private static void doKthSmallest(TreeNode root) {
        // 判断root是否为空
        if (root == null) {
            return;
        }
        // 否则进入左节点
        doKthSmallest(root.left);
        if (root.val > value) {
            kn--;
            if (kn > 0) {
                value = root.val;
            }
        }
        doKthSmallest(root.right);
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
