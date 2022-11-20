package club.beenest.mybatis;

import java.sql.*;

/**
 * 测试JDBC连接数据库
 * 思考：mybatis究竟对原生的连接做了什么
 *
 * @author 陈玉轩
 */

public class TestJDBC {
    /**
     * 用户连接信息
     */
    private static final String url = "jdbc:mysql://localhost:3306/beenest?serverTimezone=UTC&useSSL=false";
    private static final String username = "root";
    private static final String password = "123456";
    private static final String className = "com.mysql.cj.jdbc.Driver";


    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //将要执行的sql语句
        String sql = "SELECT * FROM bee_mybatis WHERE id = ?";
        // 1. 加载驱动
        // 关于该方式，是执行类加载来注册驱动 com.mysql.cj.jdbc.Driver 该类中的静态代码块执行注册
        Class.forName(className);
        // 2. 连接数据库
        Connection connection = DriverManager.getConnection(url, username, password);
        // 3. 创建预编译数据库操纵操作对象
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        // 4. 设置占位符的值
        preparedStatement.setInt(1, 1);
        // 5. 获取结果集
        ResultSet resultSet = preparedStatement.executeQuery();
        // 创建数据库操作对象
        Statement statement = connection.createStatement();
//        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            System.out.println(resultSet.getInt("id"));
        }
        resultSet.close();
//        statement.close();
        preparedStatement.close();
        connection.close();
    }

}
