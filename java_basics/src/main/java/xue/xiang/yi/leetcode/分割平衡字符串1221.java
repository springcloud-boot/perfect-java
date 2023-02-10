package xue.xiang.yi.leetcode;

/**
 * @author : 薛向毅
 * @date : 16:14 2023/2/3
 * 平衡字符串 中，'L' 和 'R' 字符的数量是相同的。
 *
 * 给你一个平衡字符串 s，请你将它分割成尽可能多的子字符串，并满足：
 *
 * 每个子字符串都是平衡字符串。
 * 返回可以通过分割得到的平衡字符串的 最大数量 。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/split-a-string-in-balanced-strings
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class 分割平衡字符串1221 {
    public int balancedStringSplit(String s) {

        //维护一个flag. 当flag=0 则说明 字符正好相等.即存在一个结果
        int flag = 0, result = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'L') {
                flag ++;
            } else {
                flag --;
            }
            if (flag == 0) {
                result ++;
            }
        }
        return result;
    }
}
