package xue.xiang.yi.leetcode;

/**
 * @author : 薛向毅
 * @date : 15:14 2023/2/6
 */
public class 计算布尔二叉树的值2331 {

    public boolean evaluateTree(TreeNode root) {
        if (null == root.left) {
            return root.val == 1;
        }
        if (root.val == 2) {
            return evaluateTree(root.left) || evaluateTree(root.right);
        } else {
            return evaluateTree(root.left) && evaluateTree(root.right);
        }
    }

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
}


