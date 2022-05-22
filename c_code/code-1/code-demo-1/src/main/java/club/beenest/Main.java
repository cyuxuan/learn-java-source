package club.beenest;

public class Main {
    public static void main(String[] args) {
        String s = new String("test");
        System.out.println(s);
        Main main = new Main();
        System.out.println(main.test(1, 2));
    }

    private int test(int a, int b) {
        return a + b;
    }
}
