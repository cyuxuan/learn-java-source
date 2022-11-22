package club.beenest.algorithm.leetcode.bookintermediatelevelalgorithm.list;

/**
 * 相交链表
 * https://leetcode.cn/leetbook/read/top-interview-questions-medium/xv02ut/
 *
 * @author 陈玉轩
 */
public class GetIntersectionNode {
    public static void main(String[] args) {
        ListNode res = twoPointer(listNode1, listNode2);

        System.out.println("-------------------------------------");
        ListNode node = res;
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
     * 双指针法 查找
     * 链表 一: 2, 3, 7, 8, 9
     * 链表 二: 4, 5, 6, 7, 8, 9
     * 7 是相汇点
     * <p>
     * 双指针法， p指针与q指针
     * 让p指针和q指针都 遍历一遍两个链表
     * <p>
     * 例如：
     * （p,q） -> (2,4) -> (3，5) -> (7,6) -> (8,7) -> (9,8)
     * -> (4,9) -> (5,2) -> (6.3) -> (7,7)
     * (7,7) 交汇点
     * <p>
     * 设置
     * x 为链表一 的交汇点前的数量
     * y 为链表二 的交汇点前的数量
     * z 为链表一和链表二的交汇后的数量
     * <p>
     * 分别遍历两次可得
     * 方式一 ：x -> z -> y 计算次数：x + z + y
     * 方式二 ：y -> z -> x 计算次数：y + z + x
     *
     * @param headA 链表一
     * @param headB 链表二
     * @return 结果
     */
    private static ListNode twoPointer(ListNode headA, ListNode headB) {
        // 保持原节点指向不变
        ListNode tempHeadA = headA;
        ListNode tempHeadB = headB;
        // 记录当前数据是否换了链表了
        boolean changeA = false;
        boolean changeB = false;

        // 执行循环判断两个节点是否相等
        while(true) {
            // 相等则返回
            if(tempHeadA == tempHeadB) {
                return tempHeadA;
            } else {
                // 链表A已经切换过，但是还是到队尾了，则未找到
                if(changeB && tempHeadA.next == null) {
                    return null;
                }
                // headA 没有切换过，到链表结尾了则切换
                if(tempHeadA.next == null && !changeB) {
                    tempHeadA = headB;
                    changeB = true;
                } else {
                    // 否则指向下一个节点
                    tempHeadA = tempHeadA.next;
                }


                if (changeA && tempHeadB.next == null) {
                    return null;
                }
                if(tempHeadB.next == null && !changeA) {
                    tempHeadB = headA;
                    changeA = true;
                } else {
                    tempHeadB = tempHeadB.next;
                }
            }
        }
    }


    private static int[] data1 = {4, 1,};
    private static int[] data2 = {5, 6, 1};

    private static int[] data3 = {8, 4, 5};
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
        // 列表一
        listNode1 = new ListNode();
        ListNode templist1 = listNode1;
        for (int item : data1) {
            templist1.next = new ListNode();
            templist1.next.val = item;
            templist1 = templist1.next;
        }
        // 去除头节点
        listNode1 = listNode1.next;


        // 列表二
        listNode2 = new ListNode();
        ListNode templist2 = listNode2;
        for (int item : data2) {
            templist2.next = new ListNode();
            templist2.next.val = item;
            templist2 = templist2.next;
        }
        // 去除头节点
        listNode2 = listNode2.next;

        // 列表尾
        ListNode listNode3 = new ListNode();
        ListNode templist3 = listNode3;
        for (int item : data3) {
            templist3.next = new ListNode();
            templist3.next.val = item;
            templist3 = templist3.next;
        }
        // 去除头节点
        listNode3 = listNode3.next;

        // 组合数据 头 + 尾
        templist1.next = listNode3;
        templist2.next = listNode3;


        ListNode node = listNode1;
        while (true) {
            System.out.print(node.val + " ");
            node = node.next;
            if (node == null) {
                break;
            }
        }
        System.out.println();


        node = listNode2;
        while (true) {
            System.out.print(node.val + " ");
            node = node.next;
            if (node == null) {
                break;
            }
        }
        System.out.println();


        node = listNode3;
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
