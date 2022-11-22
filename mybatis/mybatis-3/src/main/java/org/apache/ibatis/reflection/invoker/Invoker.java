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
package org.apache.ibatis.reflection.invoker;

import java.lang.reflect.InvocationTargetException;

/**
 * 执行器子包，定义基于反射实现对象方法的调用和对象属性的读写能力
 *
 * @author Clinton Begin
 */
public interface Invoker {
  /**
   * 执行方法，该方法负责完成对象方法的调用及对象属性的读写
   *
   * @param target 目标对象
   * @param args   目标参数
   * @return 执行结果
   * @throws IllegalAccessException    读取权限异常
   * @throws InvocationTargetException 执行异常
   */
  Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException;

  /**
   * 获取目标类型
   *
   * @return 返回目标类型
   */
  Class<?> getType();
}
