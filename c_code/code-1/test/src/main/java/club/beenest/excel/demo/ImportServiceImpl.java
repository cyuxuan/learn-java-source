package club.beenest.excel.demo;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
import java.util.List;

/**
 * 导入excel文件业务层
 * excel读取有两种方式
 * 1.用户模式：使用系列封装好的api操作excel
 * 2.事件驱动：基于sax的读取方式读取excel的xml文件
 */
public class ImportServiceImpl {
    /**
     * 读取大数据量excel
     * @param path 文件路径
     */
    public void readBigDataExcel(String path) throws Exception {
        // 1.根据excel报表获取OPCpackage
        OPCPackage opcPackage = OPCPackage.open(path, PackageAccess.READ);
        // 2.创建XSSFReader
        XSSFReader xssfReader = new XSSFReader(opcPackage);
        // 3.获取SharedStringTable对象
        SharedStrings sharedStrings = xssfReader.getSharedStringsTable();
        // 4.获取styleTable对象
        StylesTable stylesTable = xssfReader.getStylesTable();
        // 5.创建sax xmlReader对象
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        // 6.注册事件驱动处理器
        SheetHandler sheetHandler = new SheetHandler();
        XSSFSheetXMLHandler xssfSheetXMLHandler = new XSSFSheetXMLHandler(stylesTable, sharedStrings, sheetHandler, false);
        xmlReader.setContentHandler(xssfSheetXMLHandler);
        // 7.逐行读取
        XSSFReader.SheetIterator sheetIterator = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        while (sheetIterator.hasNext()) {
            InputStream in = sheetIterator.next();
            InputSource is = new InputSource(in);
            xmlReader.parse(is);
            in.close();
        }
        List<Edata> edataList = sheetHandler.getEdataList();   // 剩余未插入的数据插入
        System.out.println("最后一次插入数据，本次数据量为" + edataList.size());
        System.out.println("共插入" + sheetHandler.getAllCount() + "条数据");
    }
}
