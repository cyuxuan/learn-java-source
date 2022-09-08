package club.beenest.ioc.overview.dependency;

import club.beenest.ioc.overview.domain.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Cmain {
    public static void main(String[] args) {
        // 实例化应用上下文
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 进行注册
        applicationContext.register(User.class);
        // 启动 Spring 应用上下文
        applicationContext.refresh();
        // 获取对象
        System.out.println(applicationContext.getBean("user"));
        // 关闭上下文
    }
}
