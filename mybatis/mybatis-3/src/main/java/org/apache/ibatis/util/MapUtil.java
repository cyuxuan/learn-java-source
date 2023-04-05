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
package org.apache.ibatis.util;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

public class MapUtil {
  /**
   * A temporary(临时) workaround(解决方案) for Java 8 specific performance(性能) issue JDK-8161372 .<br>
   * This class should be removed once we drop Java 8 support.
   *
   * 这是一个对于java8性能问题的临时解决方案
   * 放弃对java8支持的时候就去除该类
   *
   * @see <a href="https://bugs.openjdk.java.net/browse/JDK-8161372">https://bugs.openjdk.java.net/browse/JDK-8161372</a>
   */
  public static <K, V> V computeIfAbsent(Map<K, V> map, K key, Function<K, V> mappingFunction) {
    V value = map.get(key);
    if (value != null) {
      return value;
    }
    return map.computeIfAbsent(key, mappingFunction);
  }

  /**
   * Map.entry(key, value) alternative for Java 8.
   */
  public static <K, V> Entry<K, V> entry(K key, V value) {
    return new AbstractMap.SimpleImmutableEntry<>(key, value);
  }

  private MapUtil() {
    super();
  }
}
