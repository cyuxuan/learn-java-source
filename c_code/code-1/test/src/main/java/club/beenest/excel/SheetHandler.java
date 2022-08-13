package club.beenest.excel;


import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义sheet基于Sax的解析处理器
 */
public class SheetHandler implements XSSFSheetXMLHandler.SheetContentsHandler {
    // 封装实体对象
    private Edata edata;

    // 实体对象集合
    private List<Edata> edataList = new ArrayList<>(MAX_EDATA);

    // 集合最大容量
    private static final int MAX_EDATA = 102400;

    // 第几次插入数据，初始值为1
    private int times = 1;

    // 总数据量
    private int allCount = 0;

    /**
     * 当开始解析某一行的时候出发
     *
     * @param i 行号
     */
    @Override
    public void startRow(int i) {
        System.out.println("当前行号："+i);
        if (i >= 0) {
            edata = new Edata();
        }
    }

    /**
     * 当结束解析某一行的时候出发
     *
     * @param i 行号
     */
    @Override
    public void endRow(int i) {
        // System.out.println(employee);
        edataList.add(edata);
        System.out.println(edata.toString());
        allCount++;
        if (edataList.size() == MAX_EDATA) {
            // 假设有一个批量插入
            System.out.println("执行第" + times + "次插入");
            times++;
            edataList.clear();
        }
    }

    /**
     * 对行中的每一个单元格进行处理
     *
     * @param cellName    单元格名称
     * @param value       数据
     * @param xssfComment 批注
     */
    @Override
    public void cell(String cellName, String value, XSSFComment xssfComment) {
        if (edata != null) {
            String prefix = cellName.substring(0, 1);
            switch (prefix) {
                case "A":
                    edata.setId(Integer.parseInt(value));
                    break;
                case "B":
                    edata.setAddress(String.valueOf(value));
                    break;
            }
        }
    }

    public List<Edata> getEdataList() {
        return edataList;
    }

    public int getAllCount() {
        return allCount;
    }
}
