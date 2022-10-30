package club.beenest.mybatis.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * 测试mybatis的mapper映射
 * @author 陈玉轩
 */
public interface TestMapper {
    String selectId(@Param("id") int id);
}
