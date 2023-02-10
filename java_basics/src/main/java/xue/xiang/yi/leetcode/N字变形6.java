package xue.xiang.yi.leetcode;

/**
 * @author : 薛向毅
 * @date : 15:30 2023/2/6
 */
public class N字变形6 {

    public static void main(String[] args) {
        convert("AB",
                1);
    }
    /**
     * res[i] += c： 把每个字符 c 填入对应行
     * i += flag： 更新当前字符 c 对应的行索引；
     * flag = - flag： 在达到Z 字形转折点时，执行反向。
     * @param s
     * @param numRows
     * @return
     */
    public static String convert(String s, int numRows) {
        if (numRows < 2) {
            return s;
        }
        String[] resultArr = new String[numRows];
        int flag = -1;
        int nowIndex = 0;
        for (int i = 0; i < s.length(); i++) {
            resultArr[nowIndex]  = resultArr[nowIndex];
            //第一行 或者 已经达到Z字的翻转
            if (i == 0 || i % (numRows-1) == 0) {
                flag = -flag;
            }
            nowIndex += flag;
        }
        StringBuffer result = new StringBuffer();
        for (String s1 : resultArr) {
            result.append(s1);
        }
        return result.toString().replace("null", "");

    }
}
