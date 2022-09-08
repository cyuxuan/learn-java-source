//package club.beenest.excel.demo;
//
//import com.alibaba.excel.exception.ExcelAnalysisException;
//import com.alibaba.excel.exception.ExcelCommonException;
//import org.apache.poi.openxml4j.opc.*;
//import org.apache.poi.util.TempFile;
//import org.apache.poi.xssf.eventusermodel.XSSFReader;
//import org.ehcache.CacheManager;
//import org.ehcache.config.CacheConfiguration;
//import org.ehcache.config.builders.CacheConfigurationBuilder;
//import org.ehcache.config.builders.CacheManagerBuilder;
//import org.ehcache.config.builders.ResourcePoolsBuilder;
//import org.ehcache.config.units.MemoryUnit;
//import org.xml.sax.InputSource;
//import org.xml.sax.XMLReader;
//import org.xml.sax.helpers.DefaultHandler;
//
//import javax.xml.parsers.SAXParser;
//import javax.xml.parsers.SAXParserFactory;
//import java.io.*;
//import java.util.ArrayList;
//import java.util.UUID;
//
///**
// * 导入excel文件业务层
// * excel读取有两种方式
// * 1.用户模式：使用系列封装好的api操作excel
// * 2.事件驱动：基于sax的读取方式读取excel的xml文件
// */
//public class ImportServiceImpl {
//    private File tempFile;
//
//    /**
//     * 读取大数据量excel
//     *
//     * @param path 文件路径
//     */
//    public void readBigDataExcel(String path) throws Exception {
//        // 读入excel文件
//        File file = new File(path);
//        // 生成文件流
//        InputStream inputStream = new FileInputStream(file);
//
//        // 创建临时文件
//        String filePath = System.getProperty(TempFile.JAVA_IO_TMPDIR) + File.separator + UUID.randomUUID().toString() + File.separator;
//        File readTempFile = new File(filePath + UUID.randomUUID().toString());
//        if (!readTempFile.exists() && !readTempFile.mkdirs()) {
//            throw new ExcelCommonException("Cannot create directory:" + filePath.getAbsolutePath());
//        }
//
//        // 修改临时文件为excel文件
//        File tempFile = new File(readTempFile.getPath(), UUID.randomUUID().toString() + ".xlsx");
//
//        // 主动关闭流标记
//        boolean closeInputStream = true;
//        // 将文件写出到磁盘中
//        OutputStream outputStream = null;
//        try {
//            outputStream = new FileOutputStream(tempFile);
//            int bytesRead;
//            byte[] buffer = new byte[8192];
//            while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//            }
//        } catch (Exception e) {
//            throw new ExcelAnalysisException("Can not create temporary file!", e);
//        } finally {
//            if (outputStream != null) {
//                try {
//                    outputStream.close();
//                } catch (IOException e) {
//                    throw new ExcelAnalysisException("Can not close 'outputStream'!", e);
//                }
//            }
//            if (inputStream != null && closeInputStream) {
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    throw new ExcelAnalysisException("Can not close 'inputStream'", e);
//                }
//            }
//        }
//
//        //  获取excel xml文档
//        OPCPackage pkg = OPCPackage.open(tempFile, PackageAccess.READ);
//        // 获取文档信息
//        PackagePartName SHARED_STRINGS_PART_NAME = PackagingURIHelper.createPartName("/xl/sharedStrings.xml");
//        // 文档共享信息
//        PackagePart sharedStringsTablePackagePart = pkg.getPart(SHARED_STRINGS_PART_NAME);
//
//        // 初始化一个缓存对象
//        CacheConfiguration<Integer, ArrayList> activeCacheConfiguration = CacheConfigurationBuilder
//                .newCacheConfigurationBuilder(Integer.class, ArrayList.class,
//                        ResourcePoolsBuilder.newResourcePoolsBuilder().heap(20, MemoryUnit.MB))
//                .withSizeOfMaxObjectGraph(1000 * 1000L).withSizeOfMaxObjectSize(20, MemoryUnit.MB)
//                .build();
//
//        // 记录缓存别名
//        String cacheAlias = UUID.randomUUID().toString();
//        // 生成缓存文件
//        File cacheFile = new File(filePath + UUID.randomUUID().toString());
//        if (!cacheFile.exists() && !cacheFile.mkdirs()) {
//            throw new ExcelCommonException("Cannot create directory:" + cacheFile.getAbsolutePath());
//        }
//        // 缓存管理实例
//        CacheManager FILE_CACHE_MANAGER =
//                CacheManagerBuilder.newCacheManagerBuilder().with(CacheManagerBuilder.persistence(cacheFile)).build(true);
//        CacheManager ACTIVE_CACHE_MANAGER = CacheManagerBuilder.newCacheManagerBuilder().build(true);
//        CacheConfiguration<Integer, ArrayList> FILE_CACHE_CONFIGURATION = CacheConfigurationBuilder
//                .newCacheConfigurationBuilder(Integer.class, ArrayList.class,
//                        ResourcePoolsBuilder.newResourcePoolsBuilder().disk(10, MemoryUnit.GB))
//                .withSizeOfMaxObjectGraph(1000 * 1000L).withSizeOfMaxObjectSize(10, MemoryUnit.GB).build();
//        org.ehcache.Cache<Integer, ArrayList> fileCache = FILE_CACHE_MANAGER.createCache(cacheAlias, FILE_CACHE_CONFIGURATION);
//        org.ehcache.Cache<Integer, ArrayList> activeCache = ACTIVE_CACHE_MANAGER.createCache(cacheAlias, activeCacheConfiguration);
//
//        XSSFReader xssfReader = new XSSFReader(pkg);
//
//
//        InputSource inputSource = new InputSource(inputStream);
//        try {
//            SAXParserFactory saxFactory = SAXParserFactory.newInstance();
//            ;
//            try {
//                saxFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
//            } catch (Throwable ignore) {
//            }
//            try {
//                saxFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
//            } catch (Throwable ignore) {
//            }
//            try {
//                saxFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
//            } catch (Throwable ignore) {
//            }
//            SAXParser saxParser = saxFactory.newSAXParser();
//            XMLReader xmlReader = saxParser.getXMLReader();
//            xmlReader.setContentHandler(new DefaultHandler());
//            xmlReader.parse(inputSource);
//            inputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
