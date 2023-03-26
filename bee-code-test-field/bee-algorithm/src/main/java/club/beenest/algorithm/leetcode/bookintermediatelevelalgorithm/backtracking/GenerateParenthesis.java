package club.beenest.algorithm.leetcode.bookintermediatelevelalgorithm.backtracking;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * https://leetcode.cn/leetbook/read/top-interview-questions-medium/xv33m7/
 * 括号生成
 *
 * @author 陈玉轩
 */
public class GenerateParenthesis {
    private static List<String> list = new ArrayList<>();
    private static int a = 0;

    public static void main(String[] args) {
        int n = 6;
        doGenerateParenthesis(n);
        System.out.println(list.toString());
        System.out.println(a);
    }

    /**
     * 生成对应的括号对数
     * 采用回溯遍历的方式
     *
     * @param n 括号对的数量
     */
    private static void doGenerateParenthesis(int n) {
        // 生成对应的数据
        StringBuilder sb = new StringBuilder();
        List<Character> charList = new LinkedList<>();
        // 生成可用的所有数据
        for (int i = 0; i < n; i++) {
            charList.add('(');
            charList.add(')');
        }
        // 初始化栈
        Stack<Character> stack = new Stack<>();
        dodo("", stack, charList);
        list = list.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 1. 判断当前字符集是否还有可用的字符
     * 2. 字符集中没有了则可以使用的已经用光了
     * 3. 如果有，则判断是入栈还是出栈
     * 4。栈中没有同事字符集中没有则说明字符消耗光了，为正确数据
     *
     * @param res
     * @param stack
     * @param charList
     */
    private static void dodo(String res, Stack<Character> stack, List<Character> charList) {
        a++;
        if (charList.size() == 0) {
            if (stack.size() == 0) {
                list.add(res);
            }
            return;
        }
        String newRes = "";
        // 当前列表长度
        int len = charList.size();
        // 开始执行回溯
        for (int i = 0; i < len; i++) {
            // 获取当前元素
            char c = charList.remove(i);
            char pushC = '0';
            char popC = '0';
            if(!(stack.size() == 0 && ')' == c)) {
                // 判断当前数据是否配对，配对则出栈，否则入栈记录起来
                if (stack.size() != 0 && '(' == stack.peek() && ')' == c) {
                    // 配对则出栈
                    popC = stack.pop();
                } else {
                    // 否则入栈
                    pushC = stack.push(c);
                }
                newRes = res + c;
                // 进入下一层
                dodo(newRes, stack, charList);
            }

            // 数据恢复
            if (popC != '0') {
                // 说明有数据出栈过，需要加回来
                stack.push(popC);
            }
            if(pushC != '0') {
                // 说明有数据入栈，需要弹出
                stack.pop();
            }
            charList.add(i, c);
        }
    }
}
