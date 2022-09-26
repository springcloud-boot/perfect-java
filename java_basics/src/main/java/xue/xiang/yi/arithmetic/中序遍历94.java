package xue.xiang.yi.arithmetic;

import xue.xiang.yi.arithmetic.base.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class 中序遍历94 {


    //中序遍历. 先左后根最后右 中序遍历的递推公式：inOrder(r) = inOrder(r->left)->print r->inOrder(r->right)
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (Objects.isNull(root)) {
            return result;
        }
        inOrder(root, result);
        return result;
    }

    private void inOrder(TreeNode root, List<Integer> result) {
        if (Objects.isNull(root)) {
            return;
        }
        //先左边节点
        inOrder(root.left, result);
        result.add(root.val);
        inOrder(root.right, result);
    }

    //      1
    //3             5
}
