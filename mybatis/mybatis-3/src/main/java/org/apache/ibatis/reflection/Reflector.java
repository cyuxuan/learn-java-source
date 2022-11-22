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
package org.apache.ibatis.reflection;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.ReflectPermission;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.reflection.invoker.AmbiguousMethodInvoker;
import org.apache.ibatis.reflection.invoker.GetFieldInvoker;
import org.apache.ibatis.reflection.invoker.Invoker;
import org.apache.ibatis.reflection.invoker.MethodInvoker;
import org.apache.ibatis.reflection.invoker.SetFieldInvoker;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.apache.ibatis.util.MapUtil;

/**
 * This class represents a cached set of class definition information that
 * allows for easy mapping between property names and getter/setter methods.
 *
 * @author Clinton Begin
 */
public class Reflector {
  // 判断是否为record 关键字标记的 方法的执行器
  private static final MethodHandle isRecordMethodHandle = getIsRecordMethodHandle();
  // 要被反射解析的类
  private final Class<?> type;
  // 能够读的属性列表，即有get方法的属性列表
  private final String[] readablePropertyNames;
  // 能够写的属性列表，即有set方法的属性列表
  private final String[] writablePropertyNames;
  // set方法映射，键为属性名，值为对应的set方法
  private final Map<String, Invoker> setMethods = new HashMap<>();
  // get方法映射，键为属性名，值为对应的get方法
  private final Map<String, Invoker> getMethods = new HashMap<>();
  // set方法输入类型，键为属性名，值为对应的该属性的set方法的类型(实际为set方法的第一个参数的类型)
  private final Map<String, Class<?>> setTypes = new HashMap<>();
  // get方法输出类型，键为属性名，值为对应的该属性的get方法的类型(实际为get方法的返回值类型)
  private final Map<String, Class<?>> getTypes = new HashMap<>();
  // 大小写无关的属性映射表，键为属性名全大写值，值为属性名
  private Constructor<?> defaultConstructor;

  private Map<String, String> caseInsensitivePropertyMap = new HashMap<>();

  /**
   * 解析指定的Class类型 并填充上述的集合信息
   *
   * @param clazz 指定的要解析的class类型
   */
  public Reflector(Class<?> clazz) {
    // 初始化 type字段
    type = clazz;
    // 设置默认的构造方法
    addDefaultConstructor(clazz);
    // 获取 class 类型中的方法
    Method[] classMethods = getClassMethods(clazz);
    // 判断当前类是否是 record 关键字修饰
    if (isRecord(type)) {
      // 如过是则加入record关键字修饰的方法
      addRecordGetMethods(classMethods);
    } else {
      // 获取getter方法
      addGetMethods(classMethods);
      // 获取setter方法
      addSetMethods(classMethods);
      // 处理没有getter/setter方法的字段
      addFields(clazz);
    }
    // 初始化 可读属性名称集合
    readablePropertyNames = getMethods.keySet().toArray(new String[0]);
    // 初始化 可写属性名称集合
    writablePropertyNames = setMethods.keySet().toArray(new String[0]);

    // caseInsensitivePropertyMap记录了所有的可读和可写属性的名称 也就是记录了所有的属性名称
    for (String propName : readablePropertyNames) {
      // 属性名称转大写
      caseInsensitivePropertyMap.put(propName.toUpperCase(Locale.ENGLISH), propName);
    }
    for (String propName : writablePropertyNames) {
      // 属性名称转大写
      caseInsensitivePropertyMap.put(propName.toUpperCase(Locale.ENGLISH), propName);
    }
  }

  /**
   * 加入record关键字修饰的方法到 get 方法中
   *
   * @param methods
   */
  private void addRecordGetMethods(Method[] methods) {
    // 1. 过滤去无参数的方法
    // 2. 循环过滤出的方法
    Arrays.stream(methods).filter(m -> m.getParameterTypes().length == 0)
      .forEach(m -> addGetMethod(m.getName(), m, false));
  }

  /**
   * 设置默认的构造方法
   *
   * @param clazz 指定解析的类型
   */
  private void addDefaultConstructor(Class<?> clazz) {
    // 获取所有的构造器
    Constructor<?>[] constructors = clazz.getDeclaredConstructors();
    // 遍历获取没有入参的构造方法
    // findAny 获取其中的一个，串行时获取第一个，并行时获取最快的哪个线程获取的值
    // ifPresent 用于对过滤出的数据如果存在。如果经过过滤条件后，有数据的话就可以进行修改
    Arrays.stream(constructors).filter(constructor -> constructor.getParameterTypes().length == 0)
      .findAny().ifPresent(constructor -> this.defaultConstructor = constructor);
  }

  private void addGetMethods(Method[] methods) {
    Map<String, List<Method>> conflictingGetters = new HashMap<>();
    Arrays.stream(methods).filter(m -> m.getParameterTypes().length == 0 && PropertyNamer.isGetter(m.getName()))
      .forEach(m -> addMethodConflict(conflictingGetters, PropertyNamer.methodToProperty(m.getName()), m));
    resolveGetterConflicts(conflictingGetters);
  }

  private void resolveGetterConflicts(Map<String, List<Method>> conflictingGetters) {
    for (Entry<String, List<Method>> entry : conflictingGetters.entrySet()) {
      Method winner = null;
      String propName = entry.getKey();
      boolean isAmbiguous = false;
      for (Method candidate : entry.getValue()) {
        if (winner == null) {
          winner = candidate;
          continue;
        }
        Class<?> winnerType = winner.getReturnType();
        Class<?> candidateType = candidate.getReturnType();
        if (candidateType.equals(winnerType)) {
          if (!boolean.class.equals(candidateType)) {
            isAmbiguous = true;
            break;
          } else if (candidate.getName().startsWith("is")) {
            winner = candidate;
          }
        } else if (candidateType.isAssignableFrom(winnerType)) {
          // OK getter type is descendant
        } else if (winnerType.isAssignableFrom(candidateType)) {
          winner = candidate;
        } else {
          isAmbiguous = true;
          break;
        }
      }
      addGetMethod(propName, winner, isAmbiguous);
    }
  }

  /**
   * 添加方法到get方法集合中
   *
   * @param name 方法名称
   * @param method 方法对象
   * @param isAmbiguous 是否是不确定的类型
   */
  private void addGetMethod(String name, Method method, boolean isAmbiguous) {
    // 确认是模糊不清的吗
    // 如果是则生成一个模糊不清的调用器
    // 不模糊则直接生成一个方法执行器
    MethodInvoker invoker = isAmbiguous
        ? new AmbiguousMethodInvoker(method, MessageFormat.format(
            "Illegal overloaded getter method with ambiguous type for property ''{0}'' in class ''{1}''. " +
              "This breaks the JavaBeans specification and can cause unpredictable results.",
            name, method.getDeclaringClass().getName()))
        : new MethodInvoker(method);
    // 将当前方法放入get方法集合中
    getMethods.put(name, invoker);
    // 获取返回类型
    Type returnType = TypeParameterResolver.resolveReturnType(method, type);
    // 将返回类型存入集合中
    getTypes.put(name, typeToClass(returnType));
  }

  private void addSetMethods(Method[] methods) {
    Map<String, List<Method>> conflictingSetters = new HashMap<>();
    Arrays.stream(methods).filter(m -> m.getParameterTypes().length == 1 && PropertyNamer.isSetter(m.getName()))
      .forEach(m -> addMethodConflict(conflictingSetters, PropertyNamer.methodToProperty(m.getName()), m));
    resolveSetterConflicts(conflictingSetters);
  }

  private void addMethodConflict(Map<String, List<Method>> conflictingMethods, String name, Method method) {
    if (isValidPropertyName(name)) {
      List<Method> list = MapUtil.computeIfAbsent(conflictingMethods, name, k -> new ArrayList<>());
      list.add(method);
    }
  }

  private void resolveSetterConflicts(Map<String, List<Method>> conflictingSetters) {
    for (Entry<String, List<Method>> entry : conflictingSetters.entrySet()) {
      String propName = entry.getKey();
      List<Method> setters = entry.getValue();
      Class<?> getterType = getTypes.get(propName);
      boolean isGetterAmbiguous = getMethods.get(propName) instanceof AmbiguousMethodInvoker;
      boolean isSetterAmbiguous = false;
      Method match = null;
      for (Method setter : setters) {
        if (!isGetterAmbiguous && setter.getParameterTypes()[0].equals(getterType)) {
          // should be the best match
          match = setter;
          break;
        }
        if (!isSetterAmbiguous) {
          match = pickBetterSetter(match, setter, propName);
          isSetterAmbiguous = match == null;
        }
      }
      if (match != null) {
        addSetMethod(propName, match);
      }
    }
  }

  private Method pickBetterSetter(Method setter1, Method setter2, String property) {
    if (setter1 == null) {
      return setter2;
    }
    Class<?> paramType1 = setter1.getParameterTypes()[0];
    Class<?> paramType2 = setter2.getParameterTypes()[0];
    if (paramType1.isAssignableFrom(paramType2)) {
      return setter2;
    } else if (paramType2.isAssignableFrom(paramType1)) {
      return setter1;
    }
    MethodInvoker invoker = new AmbiguousMethodInvoker(setter1,
      MessageFormat.format(
        "Ambiguous setters defined for property ''{0}'' in class ''{1}'' with types ''{2}'' and ''{3}''.",
        property, setter2.getDeclaringClass().getName(), paramType1.getName(), paramType2.getName()));
    setMethods.put(property, invoker);
    Type[] paramTypes = TypeParameterResolver.resolveParamTypes(setter1, type);
    setTypes.put(property, typeToClass(paramTypes[0]));
    return null;
  }

  private void addSetMethod(String name, Method method) {
    MethodInvoker invoker = new MethodInvoker(method);
    setMethods.put(name, invoker);
    Type[] paramTypes = TypeParameterResolver.resolveParamTypes(method, type);
    setTypes.put(name, typeToClass(paramTypes[0]));
  }

  private Class<?> typeToClass(Type src) {
    Class<?> result = null;
    if (src instanceof Class) {
      result = (Class<?>) src;
    } else if (src instanceof ParameterizedType) {
      result = (Class<?>) ((ParameterizedType) src).getRawType();
    } else if (src instanceof GenericArrayType) {
      Type componentType = ((GenericArrayType) src).getGenericComponentType();
      if (componentType instanceof Class) {
        result = Array.newInstance((Class<?>) componentType, 0).getClass();
      } else {
        Class<?> componentClass = typeToClass(componentType);
        result = Array.newInstance(componentClass, 0).getClass();
      }
    }
    if (result == null) {
      result = Object.class;
    }
    return result;
  }

  private void addFields(Class<?> clazz) {
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      if (!setMethods.containsKey(field.getName())) {
        // issue #379 - removed the check for final because JDK 1.5 allows
        // modification of final fields through reflection (JSR-133). (JGB)
        // pr #16 - final static can only be set by the classloader
        int modifiers = field.getModifiers();
        if (!(Modifier.isFinal(modifiers) && Modifier.isStatic(modifiers))) {
          addSetField(field);
        }
      }
      if (!getMethods.containsKey(field.getName())) {
        addGetField(field);
      }
    }
    if (clazz.getSuperclass() != null) {
      addFields(clazz.getSuperclass());
    }
  }

  private void addSetField(Field field) {
    if (isValidPropertyName(field.getName())) {
      setMethods.put(field.getName(), new SetFieldInvoker(field));
      Type fieldType = TypeParameterResolver.resolveFieldType(field, type);
      setTypes.put(field.getName(), typeToClass(fieldType));
    }
  }

  private void addGetField(Field field) {
    if (isValidPropertyName(field.getName())) {
      getMethods.put(field.getName(), new GetFieldInvoker(field));
      Type fieldType = TypeParameterResolver.resolveFieldType(field, type);
      getTypes.put(field.getName(), typeToClass(fieldType));
    }
  }

  private boolean isValidPropertyName(String name) {
    return !(name.startsWith("$") || "serialVersionUID".equals(name) || "class".equals(name));
  }

  /**
   * This method returns an array containing all methods
   * declared in this class and any superclass.
   * We use this method, instead of the simpler <code>Class.getMethods()</code>,
   * because we want to look for private methods as well.
   * 此方法返回一个数组，该数组包含所有定义在这个待解析 类及其父类 中的方法
   * 我们使用这些方法，而不是使用简单的<code>Class.getMethods()</code>，因为想要正常查找private方法
   *
   * @param clazz The class
   * @return An array containing all methods in this class
   */
  private Method[] getClassMethods(Class<?> clazz) {
    // 唯一方法 map
    Map<String, Method> uniqueMethods = new HashMap<>();
    // 待解析的类
    Class<?> currentClass = clazz;
    // 判断当前待解析类 非空且非Object类
    while (currentClass != null && currentClass != Object.class) {
      // 解析添加唯一方法
      addUniqueMethods(uniqueMethods, currentClass.getDeclaredMethods());

      // we also need to look for interface methods -
      // because the class may be abstract
      // 也需要查找接口方法
      // 因为该类可能是抽象类
      // 获取当前类的接口
      Class<?>[] interfaces = currentClass.getInterfaces();
      // 遍历所有的接口
      for (Class<?> anInterface : interfaces) {

        // 解析并添加唯一方法
        addUniqueMethods(uniqueMethods, anInterface.getMethods());
      }

      // 获取当前类的超类
      currentClass = currentClass.getSuperclass();
    }

    // 获取当前唯一方法的Method集合，并转换为集合
    Collection<Method> methods = uniqueMethods.values();

    return methods.toArray(new Method[0]);
  }

  /**
   * 添加唯一方法到集合中
   *
   * @param uniqueMethods 唯一方法键值对
   * @param methods       方法集合
   */
  private void addUniqueMethods(Map<String, Method> uniqueMethods, Method[] methods) {
    // 循环方法集合
    for (Method currentMethod : methods) {
      // 判断是否桥接方法
      if (!currentMethod.isBridge()) {
        // 获取方法签名
        String signature = getSignature(currentMethod);
        // check to see if the method is already known
        // if it is known, then an extended class must have
        // overridden a method
        // 检查该方法是否已经存在
        // 不存在再加入
        if (!uniqueMethods.containsKey(signature)) {
          uniqueMethods.put(signature, currentMethod);
        }
      }
    }
  }

  /**
   * 获取方法签名
   * []表示存在，可选
   * [方法返回值#][方法名称][方法参数1:方法参数2:....]
   *
   * @param method 待获取签名的方法
   * @return 方法签名
   */
  private String getSignature(Method method) {
    StringBuilder sb = new StringBuilder();
    // 获取方法的返回类型
    Class<?> returnType = method.getReturnType();
    // 如果返回类型不为空，则后接一个分隔符#号
    if (returnType != null) {
      sb.append(returnType.getName()).append('#');
    }
    // 获取方法名称
    sb.append(method.getName());
    // 获取方法的所有参数
    Class<?>[] parameters = method.getParameterTypes();
    // 遍历方法参数，使用 : 号连接
    for (int i = 0; i < parameters.length; i++) {
      sb.append(i == 0 ? ':' : ',').append(parameters[i].getName());
    }
    return sb.toString();
  }

  /**
   * Checks whether can control member accessible.
   *
   * @return If can control member accessible, it return {@literal true}
   * @since 3.5.0
   */
  public static boolean canControlMemberAccessible() {
    try {
      SecurityManager securityManager = System.getSecurityManager();
      if (null != securityManager) {
        securityManager.checkPermission(new ReflectPermission("suppressAccessChecks"));
      }
    } catch (SecurityException e) {
      return false;
    }
    return true;
  }

  /**
   * Gets the name of the class the instance provides information for.
   *
   * @return The class name
   */
  public Class<?> getType() {
    return type;
  }

  public Constructor<?> getDefaultConstructor() {
    if (defaultConstructor != null) {
      return defaultConstructor;
    } else {
      throw new ReflectionException("There is no default constructor for " + type);
    }
  }

  public boolean hasDefaultConstructor() {
    return defaultConstructor != null;
  }

  public Invoker getSetInvoker(String propertyName) {
    Invoker method = setMethods.get(propertyName);
    if (method == null) {
      throw new ReflectionException("There is no setter for property named '" + propertyName + "' in '" + type + "'");
    }
    return method;
  }

  public Invoker getGetInvoker(String propertyName) {
    Invoker method = getMethods.get(propertyName);
    if (method == null) {
      throw new ReflectionException("There is no getter for property named '" + propertyName + "' in '" + type + "'");
    }
    return method;
  }

  /**
   * Gets the type for a property setter.
   *
   * @param propertyName - the name of the property
   * @return The Class of the property setter
   */
  public Class<?> getSetterType(String propertyName) {
    Class<?> clazz = setTypes.get(propertyName);
    if (clazz == null) {
      throw new ReflectionException("There is no setter for property named '" + propertyName + "' in '" + type + "'");
    }
    return clazz;
  }

  /**
   * Gets the type for a property getter.
   *
   * @param propertyName - the name of the property
   * @return The Class of the property getter
   */
  public Class<?> getGetterType(String propertyName) {
    Class<?> clazz = getTypes.get(propertyName);
    if (clazz == null) {
      throw new ReflectionException("There is no getter for property named '" + propertyName + "' in '" + type + "'");
    }
    return clazz;
  }

  /**
   * Gets an array of the readable properties for an object.
   *
   * @return The array
   */
  public String[] getGetablePropertyNames() {
    return readablePropertyNames;
  }

  /**
   * Gets an array of the writable properties for an object.
   *
   * @return The array
   */
  public String[] getSetablePropertyNames() {
    return writablePropertyNames;
  }

  /**
   * Check to see if a class has a writable property by name.
   *
   * @param propertyName - the name of the property to check
   * @return True if the object has a writable property by the name
   */
  public boolean hasSetter(String propertyName) {
    return setMethods.containsKey(propertyName);
  }

  /**
   * Check to see if a class has a readable property by name.
   *
   * @param propertyName - the name of the property to check
   * @return True if the object has a readable property by the name
   */
  public boolean hasGetter(String propertyName) {
    return getMethods.containsKey(propertyName);
  }

  public String findPropertyName(String name) {
    return caseInsensitivePropertyMap.get(name.toUpperCase(Locale.ENGLISH));
  }

  /**
   * Class.isRecord() alternative for Java 15 and older.
   * 判断是否有record关键字
   *
   * @param clazz 待解析对象
   * @return 返回是否是
   */
  private static boolean isRecord(Class<?> clazz) {
    try {
      // 处理器，如果处理器为空，则表示当前方法一定不是record方法
      // 然后再使用对应方法判断 待解析类 clazz 中是否存在该方法
      return isRecordMethodHandle != null && (boolean) isRecordMethodHandle.invokeExact(clazz);
    } catch (Throwable e) {
      throw new ReflectionException("Failed to invoke 'Class.isRecord()'.", e);
    }
  }

  /**
   * 获取record方法的识别器
   * @return 返回对应识别器
   */
  private static MethodHandle getIsRecordMethodHandle() {
    // 获取查找器
    MethodHandles.Lookup lookup = MethodHandles.lookup();
    // 方法类型参数
    MethodType mt = MethodType.methodType(boolean.class);
    try {
      // 查找Class类中是否存在 idRecord方法, jdk15及以上才有的
      return lookup.findVirtual(Class.class, "isRecord", mt);
    } catch (NoSuchMethodException | IllegalAccessException e) {
      return null;
    }
  }
}
