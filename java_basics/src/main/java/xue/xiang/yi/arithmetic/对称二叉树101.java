package xue.xiang.yi.arithmetic;

import xue.xiang.yi.arithmetic.base.TreeNode;

import java.util.Objects;

/**
 * @author : 薛向毅
 * @date : 13:51 2022/9/26
 */
public class 对称二叉树101 {
    public boolean isSymmetric(TreeNode root) {

        return isSymmetric(root.left, root.right);
    }

    private boolean isSymmetric(TreeNode left, TreeNode right) {
        if (Objects.isNull(left) && Objects.isNull(right)) {
            return true;
        }
        if (Objects.isNull(left) || Objects.isNull(right)) {
            return false;
        }
        return left.val == right.val && isSymmetric(left.left, right.right) && isSymmetric(left.right, right.left);
    }
}
