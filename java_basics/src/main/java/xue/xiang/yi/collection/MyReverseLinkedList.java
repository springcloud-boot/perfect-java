package xue.xiang.yi.collection;

import java.util.LinkedList;

public class MyReverseLinkedList {

    public static void main(String[] args) {
        Linked linked = new Linked(1);
        linked.next = new Linked(2);
        linked.next.next = new Linked(3);
        linked.next.next.next = new Linked(4);

        Linked head = linked;

        Linked beg = linked;
        Linked end = linked.next;
        while (end != null) {
            beg.next = end.next;
            end.next = head;
            head = end;
            end = beg.next;
        }

        System.out.println(head);

    }

    static class Linked {
        public Linked(Object val) {
            this.val = val;
        }

        private Linked pre;
        private Linked next;
        private Object val;

        public Linked getPre() {
            return pre;
        }

        public void setPre(Linked pre) {
            this.pre = pre;
        }

        public Linked getNext() {
            return next;
        }

        public void setNext(Linked next) {
            this.next = next;
        }

        public Object getVal() {
            return val;
        }

        public void setVal(Object val) {
            this.val = val;
        }
    }
}
