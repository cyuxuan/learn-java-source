package cn.cyuxuan.dao.test;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TestDao {
    String selectNameById(@Param("id") int id);

    String selectSexById(@Param("id") int id);
}
