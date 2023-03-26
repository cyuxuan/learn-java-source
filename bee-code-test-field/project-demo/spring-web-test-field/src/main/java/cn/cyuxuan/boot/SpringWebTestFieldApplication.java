package cn.cyuxuan.boot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ComponentScan("cn.cyuxuan")
//@EnableAspectJAutoProxy
@MapperScan("cn.cyuxuan.dao")
//@ImportResource("classpath:applicationContext.xml")
public class SpringWebTestFieldApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringWebTestFieldApplication.class);
    }
}