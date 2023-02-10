package xue.xiang.yi.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : 薛向毅
 * @date : 14:57 2023/2/8
 */
public class 二叉树的所有路径257 {
      public static class TreeNode {
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
        public static List<String> binaryTreePaths(TreeNode root) {
            List<String> result = new ArrayList<>();
            //递归来添加结果到result. (当节点, 结果集, 每次递归符合条件的字符)
            recall(root, result, new StringBuffer());
            return result;
        }

    private static void recall(TreeNode root, List<String> result, StringBuffer stringBuffer) {
          //节点为空 跳过递归
        if (null == root) {
            return;
        }

        //new新的字符串 防止更改了其它递归使用的字符
        StringBuffer itemString = new StringBuffer(stringBuffer.toString());
        itemString.append(root.val);
        if (root.left == null && root.right == null) {  // 当前节点是叶子节点
            result.add(itemString.toString());
        } else {
            itemString.append("->");
            recall(root.left, result,itemString);
            recall(root.right, result,itemString);
        }

    }

    public static void main(String[] args) {

    }
}
