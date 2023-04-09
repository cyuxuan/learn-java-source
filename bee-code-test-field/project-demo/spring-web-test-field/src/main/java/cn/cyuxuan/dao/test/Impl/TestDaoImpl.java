package cn.cyuxuan.dao.test.Impl;

import cn.cyuxuan.dao.test.TestDao;
import org.springframework.stereotype.Service;

@Service
public class TestDaoImpl implements TestDao {

//    @Override
    public String selectNameById(int id) {
        try {
            Thread.sleep(700);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "1234";
    }

//    @Override
    public String selectSexById(int id) {
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "abcd";
    }
}
