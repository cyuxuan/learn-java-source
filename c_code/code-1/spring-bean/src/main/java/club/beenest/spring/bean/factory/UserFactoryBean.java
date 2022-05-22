package club.beenest.spring.bean.factory;

import club.beenest.ioc.overview.domain.User;
import org.springframework.beans.factory.FactoryBean;

/**
 * {@link User} Bean 的 {@link org.springframework.beans.factory.FactoryBean} 实现
 *
 * @author cyuxuan
 * @since
 */
public class UserFactoryBean implements FactoryBean {

    @Override
    public Object getObject() throws Exception {
        return User.createUser();
    }

    @Override
    public Class<?> getObjectType() {
        return User.class;
    }
}
