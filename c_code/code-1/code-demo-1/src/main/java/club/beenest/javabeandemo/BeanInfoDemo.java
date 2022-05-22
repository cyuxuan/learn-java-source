package club.beenest.javabeandemo;

import java.beans.*;
import java.util.stream.Stream;

/**
 * java 自省（内省 外省）也可以叫做反省机制；
 * 提供一个javabean，在java里面通过api来获取javabean里面的属性（属性包括：可读性或者可写性），可以通过反射去完成读写功能
 * <p>
 * 内省(Introspector) 是Java 语言对 JavaBean 类属性、事件的一种缺省处理方法
 * JavaBean是一种特殊的类，主要用于传递数据信息，这种类中的方法主要用于访问私有的字段，且方法名符合某种命名规则。
 * 如果在两个模块之间传递信息，可以将信息封装进JavaBean中，这种对象称为“值对象”(Value Object)，或“VO”。
 * 方法比较少。这些信息储存在类的私有变量中，通过set()、get()获得。
 * <p>
 * 在类UserInfo中有属性 userName, 那我们可以通过 getUserName,setUserName来得到其值或者设置新的值。
 * 通过 getUserName/setUserName来访问 userName属性，这就是默认的规则。
 * Java JDK中提供了一套 API 用来访问某个属性的 getter/setter 方法，这就是内省
 */

public class BeanInfoDemo {
    public static void main(String[] args) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(Person.class, Object.class);
        // 查看加载出来的类的属性描述
        Stream.of(beanInfo.getPropertyDescriptors()).forEach(propertyDescriptor -> {
            // PropertyDescriptor允许添加属性编辑器PropertyEditor
            // GUI -> text(String) -> PropertyType
            // name -> String
            // age -> Integer
            Class<?> propertyType = propertyDescriptor.getPropertyType();
            String propertyName = propertyDescriptor.getName();
            if ("age".equals(propertyName)) {
                // 输入String -> 输出Integer
                propertyDescriptor.setPropertyEditorClass(StringToIntegerPropertyEditor.class);
                // propertyDescriptor.createPropertyEditor()
            }
        });
    }

    static class StringToIntegerPropertyEditor extends PropertyEditorSupport {
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            Integer value = Integer.valueOf(text);
            setValue(value);
        }
    }

}
