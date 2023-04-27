package xue.xiang.yi.sword.offer;

/**
 * 请实现一个函数，把字符串 s 中的每个空格替换成"%20"。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：s = "We are happy."
 * 输出："We%20are%20happy."
 *  
 *
 * 限制：
 *
 * 0 <= s 的长度 <= 10000
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/ti-huan-kong-ge-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Offer01 {
    public static void main(String[] args) {
        Offer01 offer01 = new Offer01();
        String s = offer01.replaceSpace("We are happy.");
        System.out.println(s);
    }
    public String replaceSpace(String s) {
        char[] charArray = s.toCharArray();
        StringBuffer stringBuffer = new StringBuffer();
        for (Character c : charArray) {
            if (c.equals(' ')) {
                stringBuffer.append("%20");
            } else {
                stringBuffer.append(c);
            }
        }
        return stringBuffer.toString();
    }
}
