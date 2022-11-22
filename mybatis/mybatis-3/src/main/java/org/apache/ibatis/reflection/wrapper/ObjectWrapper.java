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
package org.apache.ibatis.reflection.wrapper;

import java.util.List;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.property.PropertyTokenizer;

/**
 * 对象包装器-使用装饰器模式对对象进行功能扩展
 *
 * @author Clinton Begin
 */
public interface ObjectWrapper {

  /**
   * 获取被包装对象某个属性的值
   * @param prop 属性标记器
   * @return 对应的值
   */
  Object get(PropertyTokenizer prop);

  /**
   * 设置被包装对象某个属性的值
   * @param prop 属性标记器
   * @param value 要设置的值
   */
  void set(PropertyTokenizer prop, Object value);

  /**
   * 查询对应的属性名称
   * @param name 名字
   * @param useCamelCaseMapping 是否使用驼峰进行查找
   * @return 返回对应名称
   */
  String findProperty(String name, boolean useCamelCaseMapping);

  /**
   * 获取所有属性的get方法名称
   * @return
   */
  String[] getGetterNames();

  /**
   * 获取所有属性的set方法名称
   * @return
   */
  String[] getSetterNames();

  /**
   * 获取执行属性的set方法的类型
   * @param name 指定的属性名称
   * @return
   */
  Class<?> getSetterType(String name);

  /**
   * 获取指定属性的get方法的类型
   * @param name 指定的属性名称
   * @return
   */
  Class<?> getGetterType(String name);

  /**
   * 判断某个属性是否有对应的set方法
   * @param name 指定的属性
   * @return
   */
  boolean hasSetter(String name);

  /**
   * 判断某个属性是否有对应的get方法
   * @param name 指定的属性名称
   * @return
   */
  boolean hasGetter(String name);

  /**
   * 实例化某个属性的值
   *
   * @param name          指定的属性
   * @param prop          属性标记器
   * @param objectFactory 对象工厂
   * @return
   */
  MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory);

  boolean isCollection();

  void add(Object element);

  <E> void addAll(List<E> element);

}
