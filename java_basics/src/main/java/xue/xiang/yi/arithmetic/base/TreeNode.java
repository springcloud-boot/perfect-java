package xue.xiang.yi.arithmetic.base;


public class TreeNode {
    public int val;
        public TreeNode left;
    public TreeNode right;

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

    public int Val() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public TreeNode Left() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode Right() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }
}