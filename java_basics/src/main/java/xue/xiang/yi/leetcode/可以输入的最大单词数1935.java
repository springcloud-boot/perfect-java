package xue.xiang.yi.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : 薛向毅
 * @date : 16:17 2023/1/29
 */
public class 可以输入的最大单词数1935 {

    public static void main(String[] args) {
        int i = canBeTypedWords("hello world", "ad");
        System.out.println(i);
    }

    public static int canBeTypedWords(String text, String brokenLetters) {
        Map<Character, Integer> brokenMap = new HashMap<>();
        for (int i = 0; i < brokenLetters.length(); i++) {
            brokenMap.put(brokenLetters.charAt(i), 0);
        }

        int j = 0;
        String[] s = text.split(" ");
        for (String s1 : s) {
            for (int i = 0; i < s1.length(); i++) {
                char c = s1.charAt(i);
                if (brokenMap.containsKey(c)) {
                    break;
                }
                if ( i == s1.length() -1 ) {
                    j++;
                }
            }
        }
        return j;
    }
}
