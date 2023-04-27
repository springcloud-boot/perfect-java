package xue.xiang.yi.sword.offer;

public class T {
    public static void main(String[] args) {
        T t = new T();
        ListNode a = new ListNode(4);
        a.next = new ListNode(5);
        a.next.next = new ListNode(1);
        a.next.next.next = new ListNode(9);

        t.deleteNode(a, 1);
    }
    public ListNode deleteNode(ListNode head, int val) {
        if(head.val == val) return head.next;
        ListNode pre = head, cur = head.next;
        while(cur != null && cur.val != val) {
            pre = cur;
            cur = cur.next;
        }
        if(cur != null) pre.next = cur.next;
        return head;
    }

    public static class ListNode {
      int val;
      ListNode next;
      ListNode(int x) { val = x; }
  }
}
