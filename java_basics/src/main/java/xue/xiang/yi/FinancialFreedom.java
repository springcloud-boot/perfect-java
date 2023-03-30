package xue.xiang.yi;

/**
 * @author : 薛向毅
 * @date : 10:25 2023/3/10
 * 第120个月剩余金额604944.897625861  利息1498.6157048026457
 * 第120个月剩余金额621380.7017009663  利息1785.2268769895325
 */
public class FinancialFreedom {
    public static void main(String[] args) throws InterruptedException {
        double annualized = 0.0029;
        double startMoney = 30000;
        double saveMoney = 3300;

        int i = 0;
        while (true) {
            double interest  = startMoney * annualized;
            startMoney += interest + saveMoney  ;
            System.out.println("第" + i++ + "个月剩余金额" + startMoney+ "  利息"+interest);
            Thread.sleep(30);

        }


    }
}
