package club.beenest.algorithm.leetcode.bookintermediatelevelalgorithm.list;

/**
 * 奇偶链表
 * https://leetcode.cn/leetbook/read/top-interview-questions-medium/xvdwtj/
 *
 * @author 陈玉轩
 */
public class OddEvenList {
    public static void main(String[] args) {
        ListNode list = doit(listNode1);
        ListNode node = list;
        while (true) {
            System.out.print(node.val + " ");
            node = node.next;
            if (node == null) {
                break;
            }
        }
        System.out.println();
    }

    /**
     * 
     * @return
     */
    public static ListNode doit(ListNode node) {
        // 标记基数与偶数
        boolean even = false;
        ListNode evenNode = new ListNode();
        ListNode oddNode = new ListNode();

        ListNode tempEven = evenNode;
        ListNode tempOdd = oddNode;

        while (true) {
            ListNode tempNode = new ListNode();
            tempNode.val = node.val;
            if(even) {
                // 当前是偶数
                tempEven.next = tempNode;
                tempEven = tempEven.next;
                // 下一个是基数
                even = false;
            } else {
                // 当前是奇数
                tempOdd.next= tempNode;
                tempOdd = tempOdd.next;
                // 下一个是偶数
                even = true;
            }

            if(node.next == null) {
                break;
            } else {
                node = node.next;
            }
        }
        // 去除头节点
        evenNode = evenNode.next;
        // 组合一下
        tempOdd.next = evenNode;
        // 去除头节点
        oddNode = oddNode.next;
        return oddNode;
    }
























    private static int[] data1 = {2,1,3,5,6,4,7};
    private static int[] data2 = {9, 9, 9, 9};
//    private static int[] data1 = {2, 4, 3};
//    private static int[] data2 = {5, 6, 4};

    private static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    private static ListNode listNode1;
    private static ListNode listNode2;

    static {
        listNode1 = new ListNode();
        ListNode templist = listNode1;
        for (int item : data1) {
            templist.next = new ListNode();
            templist.next.val = item;
            templist = templist.next;
        }
        // 去除头节点
        listNode1 = listNode1.next;
        ListNode node = listNode1;
        while (true) {
            System.out.print(node.val + " ");
            node = node.next;
            if (node == null) {
                break;
            }
        }
        System.out.println();

        listNode2 = new ListNode();
        templist = listNode2;
        for (int item : data2) {
            templist.next = new ListNode();
            templist.next.val = item;
            templist = templist.next;
        }
        // 去除头节点
        listNode2 = listNode2.next;

        node = listNode2;
        while (true) {
            System.out.print(node.val + " ");
            node = node.next;
            if (node == null) {
                break;
            }
        }
        System.out.println();
    }
}
