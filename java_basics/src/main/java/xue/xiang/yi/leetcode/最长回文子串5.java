package xue.xiang.yi.leetcode;

/**
 * @author : 薛向毅
 * @date : 11:20 2023/1/30
 */
public class 最长回文子串5 {
    public static void main(String[] args) {
        String fdadfsagfdsgfdshwretphdfgabbbbafewdfgggggggggg = longestPalindrome("fdadfsagfdsgfdshwretphdfgabbbbafewdfgggggggggg");
        System.out.println(fdadfsagfdsgfdshwretphdfgabbbbafewdfgggggggggg);
    }

    public static String longestPalindrome(String s) {
        if (s == null || s.length() < 2) {
            return s;
        }
        int strLen = s.length();
        int maxStart = 0;  //最长回文串的起点
        int maxEnd = 0;    //最长回文串的终点
        int maxLen = 1;  //最长回文串的长度

        boolean[][] dp = new boolean[strLen][strLen];

        //右边起始位置
        for (int r = 1; r < strLen; r++) {
            //左边起始位置
            for (int l = 0; l < r; l++) {
                //左右相等. 且左右距离对称(r - l <= 2). 且 靠里边的也是回文串
                if (s.charAt(l) == s.charAt(r) && (r - l <= 2 || dp[l + 1][r - 1])) {
                    dp[l][r] = true;
                    if (r - l + 1 > maxLen) {
                        maxLen = r - l + 1;
                        maxStart = l;
                        maxEnd = r;

                    }
                }

            }

        }
        return s.substring(maxStart, maxEnd + 1);


    }
}
