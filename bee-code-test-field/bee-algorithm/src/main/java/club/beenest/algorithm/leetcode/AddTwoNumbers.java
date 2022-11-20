package club.beenest.algorithm.leetcode;

/**
 * 两数相加
 * https://leetcode.cn/leetbook/read/top-interview-questions-medium/xvw73v/
 *
 * @author 陈玉轩
 */
public class AddTwoNumbers {
    public static void main(String[] args) {
        ListNode list = doit(listNode1, listNode2);
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
     * 直接执行处理
     *
     * @param l1
     * @param l2
     * @return
     */
    private static ListNode doit(ListNode l1, ListNode l2) {
        // 记录进位值
        int m = 0;
        ListNode x = l1;
        ListNode y = l2;
        ListNode z = new ListNode();
        ListNode tempNode = z;
        while (x != null || y != null || m != 0) {
            // 循环获取值并进行加法运算
            // 取值，如果为null则取0
            int tempx = (x != null ? x.val : 0);
            int tempy = (y != null ? y.val : 0);

            int tempz = tempx + tempy + m;
            // 进位区的值消耗了，重新置零
            m = 0;

            if (tempz >= 10) {
                m = tempz / 10;
                tempz = tempz % 10;
            }

            // 取z的下一个节点
            tempNode.next = new ListNode();
            tempNode.next.val = tempz;
            // 指向下一个节点
            tempNode = tempNode.next;

            // 向后取节点
            if (x == null || x.next == null) {
                x = null;
            } else {
                x = x.next;
            }

            if (y == null || y.next == null) {
                y = null;
            } else {
                y = y.next;
            }
        }
        // 去除头接点
        z = z.next;

        return z;
    }


    private static int[] data1 = {9, 9, 9, 9, 9, 9, 9};
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
