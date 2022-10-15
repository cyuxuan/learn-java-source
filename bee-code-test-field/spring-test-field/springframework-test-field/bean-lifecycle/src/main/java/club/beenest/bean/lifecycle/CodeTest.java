package club.beenest.bean.lifecycle;

import java.util.LinkedList;

public class CodeTest {
    public static void main(String[] args) {
        LinkedList<String> list = new LinkedList<>();
        list.add("123");
        list.add(null);
        list.add(null);
        System.out.println(list.toString());
    }
}
