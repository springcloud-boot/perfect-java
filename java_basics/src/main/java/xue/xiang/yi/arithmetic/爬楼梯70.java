package xue.xiang.yi.arithmetic;

/**
 * @author : 薛向毅
 * @date : 10:35 2022/10/20
 */
public class 爬楼梯70 {
    public static void main(String[] args) {
        int i = climbStairs(5);
        System.out.println(i);
    }
        public static int climbStairs(int n) {
            //前二次结果
            int f1 = 1;
            int f2 = 2;
            if (n == 1) {
                return f1;
            }
            if (n == 2) {
                return f2;
            }
            //方程式  f(n) = f(n-1) + f(n-2)
            int f3 = f1 + f2;
            //动态规划. 第三次的结果 = 第二次+第一次结果
            for (int i = 3; i <= n; i++) {
                //第三次结果=前两次结果累加
                f3 = f1 + f2;   //3 5 8
                //往后走一台阶.  //f1 = f2
                f1 = f2;        //2 3 5
                //往后走一台阶. f2= f3;
                f2 = f3;       // 3 5 8
            }

            return f2;


        }
}
