
package club.beenest.ioc.overview.dependency.injection;

import club.beenest.ioc.overview.repository.UserRepository;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;

/**
 * 依赖注入示例
 *
 * @author cyuxuan
 * @since
 */
public class DependencyInjectionDemo {

    public static void main(String[] args) {
        // 配置 XML 配置文件
        // 启动 Spring 应用上下文
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:/META-INF/dependency-injection-context.xml");

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:/META-INF/dependency-injection-context.xml");

        // 依赖来源一：自定义 Bean
        UserRepository userRepository = applicationContext.getBean("userRepository", UserRepository.class);

//        System.out.println(userRepository.getUsers());

        // 依赖来源二：依赖注入（內建依赖）
        System.out.println(userRepository.getBeanFactory());


        ObjectFactory userFactory = userRepository.getObjectFactory();

        // 这里应该明确一个问题，依赖查找和依赖注入获取到的不是同一个对象
        System.out.println(userFactory.getObject() == applicationContext);

        // 依赖查找（错误）
//        System.out.println(beanFactory.getBean(BeanFactory.class));

        // 依赖来源三：容器內建 Bean
//        Environment environment = applicationContext.getBean(Environment.class);
//        System.out.println("获取 Environment 类型的 Bean：" + environment);
    }

    /**
     * 谁才是真正的IOC
     * 结论，BeanFactory是一个底层的IOC容器，ApplicationContext实在它的基础上增加了一些特性
     * BeanFactory更多的提供配置框架，并且是一个基本的特性
     * ApplicationContext更多的是提供一些企业级的特性
     * ApplicationContext更像是BeanFactory的一个超集
     *
     * @param userRepository     用户仓库对象
     * @param applicationContext 应用上下文
     */
    private static void whoIsIoCContainer(UserRepository userRepository, ApplicationContext applicationContext) {

        // <- 继承关系
        // ConfigurableApplicationContext <- ApplicationContext <- BeanFactory

        // ConfigurableApplicationContext#getBeanFactory()


        // 思考：这个表达式为什么不会成立
        System.out.println(userRepository.getBeanFactory() == applicationContext);

        // ApplicationContext is BeanFactory

    }

}
