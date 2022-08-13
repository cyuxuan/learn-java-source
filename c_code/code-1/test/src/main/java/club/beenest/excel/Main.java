package club.beenest.excel;

import java.util.Scanner;

/**
 * 通过poi读取xml文件的所有内容，并输出
 * @author Administrator
 *
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();

        long startTime = System.currentTimeMillis();
        String path = "D:/x.xlsx";
        try {
            ImportServiceImpl importService = new ImportServiceImpl();
            importService.readBigDataExcel(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("执行完成，耗时" + (endTime - startTime)/1000 + "秒");
    }
}