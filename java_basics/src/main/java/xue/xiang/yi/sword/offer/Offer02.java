package xue.xiang.yi.sword.offer;

public class Offer02 {
    public static void main(String[] args) {
        String s = new Offer02().reverseLeftWords("12345", 2);
    }
        public String reverseLeftWords(String s, int n) {
            return s.substring(n, s.length()) +   s.substring(0, n);
        }
}
