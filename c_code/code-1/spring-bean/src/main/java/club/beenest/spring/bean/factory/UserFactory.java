package club.beenest.spring.bean.factory;


import club.beenest.ioc.overview.domain.User;

/**
 * {@link User} 工厂类
 *
 * @author cyuxuan
 * @since
 */
public interface UserFactory {

    default User createUser() {
        return User.createUser();
    }
}
