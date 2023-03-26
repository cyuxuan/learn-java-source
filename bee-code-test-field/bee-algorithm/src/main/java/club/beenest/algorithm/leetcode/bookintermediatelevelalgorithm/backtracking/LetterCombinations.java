package club.beenest.algorithm.leetcode.bookintermediatelevelalgorithm.backtracking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LetterCombinations {
    public static void main(String[] args) {
        String ss = "234";
        List<String> list = doLetterCombinations(ss);
        for (String item : list) {
            System.out.print("[" + item + "]");
        }
    }

    private static List<String> doLetterCombinations(String digits) {
        // 切割字符串，取出数字
        List<String> list = new ArrayList<>();
        if (digits.length() == 0) {
            return list;
        }
        list.add("");
        for (int i = 0; i < digits.length(); i++) {
            // 取出当前数字
            int k = Integer.parseInt(String.valueOf(digits.charAt(i)));
            // 取出数字
            Character[] cc = map.get(k);
            // 进行遍历
            // 首先将当前数据
            List<String> temp = new ArrayList<>();
            for (char c : cc) {
                for (String item : list) {
                    temp.add(item + c);
                }
            }
            list = temp;
        }
        return list;
    }


    // 键盘字典
    private static Map<Integer, Character[]> map = new HashMap<>();

    static {
        Character[] a2 = new Character[3];
        a2[0] = 'a';
        a2[1] = 'b';
        a2[2] = 'c';
        map.put(2, a2);

        Character[] a3 = new Character[3];
        a3[0] = 'd';
        a3[1] = 'e';
        a3[2] = 'f';
        map.put(3, a3);

        Character[] a4 = new Character[3];
        a4[0] = 'g';
        a4[1] = 'h';
        a4[2] = 'i';
        map.put(4, a4);

        Character[] a5 = new Character[3];
        a5[0] = 'j';
        a5[1] = 'k';
        a5[2] = 'l';
        map.put(5, a5);

        Character[] a6 = new Character[3];
        a6[0] = 'm';
        a6[1] = 'n';
        a6[2] = 'o';
        map.put(6, a6);

        Character[] a7 = new Character[4];
        a7[0] = 'p';
        a7[1] = 'q';
        a7[2] = 'r';
        a7[3] = 's';
        map.put(7, a7);

        Character[] a8 = new Character[3];
        a8[0] = 't';
        a8[1] = 'u';
        a8[2] = 'v';
        map.put(8, a8);

        Character[] a9 = new Character[4];
        a9[0] = 'w';
        a9[1] = 'x';
        a9[2] = 'y';
        a9[3] = 'z';
        map.put(9, a9);
    }

}
