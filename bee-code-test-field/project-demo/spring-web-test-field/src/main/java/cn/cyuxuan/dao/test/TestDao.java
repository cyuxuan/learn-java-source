package cn.cyuxuan.dao.test;

import org.springframework.stereotype.Repository;

//@Mapper
@Repository
public interface TestDao {
    String selectNameById(int id);

    String selectSexById(int id);
}
