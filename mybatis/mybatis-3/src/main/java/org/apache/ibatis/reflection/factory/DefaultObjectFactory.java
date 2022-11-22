/*
 *    Copyright 2009-2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.reflection.factory;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.ibatis.reflection.ReflectionException;
import org.apache.ibatis.reflection.Reflector;

/**
 * @author Clinton Begin
 */
public class DefaultObjectFactory implements ObjectFactory, Serializable {

  private static final long serialVersionUID = -8855120656740914948L;

  @Override
  public <T> T create(Class<T> type) {
    return create(type, null, null);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
    // 获取当前要实例化的类型
    Class<?> classToCreate = resolveInterface(type);
    // we know types are assignable
    return (T) instantiateClass(classToCreate, constructorArgTypes, constructorArgs);
  }

  /**
   * 通过反射找到与参数匹配的构造方法，然后基于反射调用该构造方法生成一个对象
   * @param type 目标对象的类型描述
   * @param constructorArgTypes 构造器参数类型列表
   * @param constructorArgs 构造器参数
   * @param <T> 目标类型
   * @return 目标类型实例
   */
  private  <T> T instantiateClass(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
    try {
      // 构造器对象
      Constructor<T> constructor;
      // 构造器参数类型列表和构造器参数都为空则使用无参构造器
      if (constructorArgTypes == null || constructorArgs == null) {
        // 获取构造器的类中的无参构造器
        constructor = type.getDeclaredConstructor();
        try {
          // 返回无参构造器构造的实例
          return constructor.newInstance();
        } catch (IllegalAccessException e) {
          if (Reflector.canControlMemberAccessible()) {
            // 如果是无权访问异常则设置开启权限再生成实例
            constructor.setAccessible(true);
            return constructor.newInstance();
          } else {
            // 其它异常则直接抛出
            throw e;
          }
        }
      }
      // 否则获取对应参数类型的构造器
      constructor = type.getDeclaredConstructor(constructorArgTypes.toArray(new Class[0]));
      try {
        // 注入构造器参数，并返回对应实例
        return constructor.newInstance(constructorArgs.toArray(new Object[0]));
      } catch (IllegalAccessException e) {
        if (Reflector.canControlMemberAccessible()) {
          // 如果是无权限异常则开启权限后再次返回构造实例并返回
          constructor.setAccessible(true);
          return constructor.newInstance(constructorArgs.toArray(new Object[0]));
        } else {
          // 其它异常直接抛出
          throw e;
        }
      }
    } catch (Exception e) {
      // 构造器参数类型字符串
      String argTypes = Optional.ofNullable(constructorArgTypes).orElseGet(Collections::emptyList)
          .stream().map(Class::getSimpleName).collect(Collectors.joining(","));
      // 构造器参数值字符串
      String argValues = Optional.ofNullable(constructorArgs).orElseGet(Collections::emptyList)
          .stream().map(String::valueOf).collect(Collectors.joining(","));
      // 抛出反射器异常
      throw new ReflectionException("Error instantiating " + type + " with invalid types (" + argTypes + ") or values (" + argValues + "). Cause: " + e, e);
    }
  }

  /**
   * 当前传入的类型是一个接口时该方法给出一个符合该接口类型的实现类型
   *
   * @param type 目标类型
   * @return 对应的实现类型
   */
  protected Class<?> resolveInterface(Class<?> type) {
    Class<?> classToCreate;
    // 判断传入类型，
    if (type == List.class || type == Collection.class || type == Iterable.class) {
      classToCreate = ArrayList.class;
    } else if (type == Map.class) {
      classToCreate = HashMap.class;
    } else if (type == SortedSet.class) { // issue #510 Collections Support
      classToCreate = TreeSet.class;
    } else if (type == Set.class) {
      classToCreate = HashSet.class;
    } else {
      classToCreate = type;
    }
    return classToCreate;
  }

  @Override
  public <T> boolean isCollection(Class<T> type) {
    // 判断当前类型是不是java集合类的子类
    return Collection.class.isAssignableFrom(type);
  }

}
