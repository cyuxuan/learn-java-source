
package club.beenest.ioc.overview.domain;

/**
 * 公司类
 *
 * @author cyuxuan
 * @since
 */
public class Company {

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                '}';
    }
}
