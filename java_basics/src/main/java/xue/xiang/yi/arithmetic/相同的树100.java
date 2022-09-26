package xue.xiang.yi.arithmetic;

import xue.xiang.yi.arithmetic.base.TreeNode;

import java.util.Objects;

/**
 * @author : 薛向毅
 * @date : 11:32 2022/9/23
 */
public class 相同的树100 {

    public boolean isSameTree(TreeNode p, TreeNode q) {
        return inOrder(p, q);
    }

    private boolean inOrder(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        }
        if (p==null&&q!=null) {
            return false;
        }
        if (p!=null && q==null) {
            return false;
        }
        if (p.val != q.val) {
            return false;
        }

        boolean b = inOrder(p.left, q.left);
        if (!b) {
            return false;
        }
        b = p.val == q.val;
        if (!b) {
            return false;
        }

         b = inOrder(p.right, q.right);
        if (!b) {
            return false;
        }
        return true;
    }

}
