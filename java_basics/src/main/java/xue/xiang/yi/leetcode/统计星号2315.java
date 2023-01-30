package xue.xiang.yi.leetcode;

/**
 * @author : 薛向毅
 * @date : 14:31 2023/1/29
 */
public class 统计星号2315 {

    public static void main(String[] args) {
        int i = countAsterisks("*fda|fds*|fdsf");
        System.out.println(i);
    }

       static int countAsterisks(String s) {
            int i = 0;
            boolean isValid = true;
            for (Character c : s.toCharArray()) {
                if (isValid && c.toString().equals("*")) {
                    i++;
                }
                if (c.toString().equals("|")) {
                    if (isValid) {
                        isValid = false;
                        continue;
                    }
                    if (!isValid) {
                        isValid = true;
                    }
                }
            }

            return i;

        }
}
