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
package org.apache.ibatis.builder.xml;

import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.builder.BaseBuilder;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.datasource.DataSourceFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.loader.ProxyFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.AutoMappingUnknownColumnBehavior;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.JdbcType;

/**
 * @author Clinton Begin
 * @author Kazuki Shimizu
 */
public class XMLConfigBuilder extends BaseBuilder {

  private boolean parsed;
  private final XPathParser parser;
  private String environment;
  private final ReflectorFactory localReflectorFactory = new DefaultReflectorFactory();

  public XMLConfigBuilder(Reader reader) {
    this(reader, null, null);
  }

  public XMLConfigBuilder(Reader reader, String environment) {
    this(reader, environment, null);
  }

  public XMLConfigBuilder(Reader reader, String environment, Properties props) {
    this(new XPathParser(reader, true, props, new XMLMapperEntityResolver()), environment, props);
  }

  public XMLConfigBuilder(InputStream inputStream) {
    this(inputStream, null, null);
  }

  public XMLConfigBuilder(InputStream inputStream, String environment) {
    this(inputStream, environment, null);
  }

  /**
   * 通过输入流，获取配置信息
   *
   * @param inputStream 配置文件输入流
   * @param environment 环境信息，默认是null
   * @param props       键值对信息，默认是null，暂时没有确认用途
   */
  public XMLConfigBuilder(InputStream inputStream, String environment, Properties props) {
    // XPathParser 对象用于
    this(new XPathParser(inputStream, true, props, new XMLMapperEntityResolver()), environment, props);
  }

  /**
   * 实例化一个XML配置类
   * @param parser xml解析类
   * @param environment 环境信息，默认是null
   * @param props 参数集合，默认是null
   */
  private XMLConfigBuilder(XPathParser parser, String environment, Properties props) {
    // 实例化一个配置类，初始的配置类里面只有类别名及语言设置
    super(new Configuration());
    ErrorContext.instance().resource("SQL Mapper Configuration");
    //
    this.configuration.setVariables(props);
    // 标记是否已经解析过了
    this.parsed = false;
    this.environment = environment;
    // 解析器，持有配置文件流的DOM对象
    this.parser = parser;
  }

  /**
   * 执行数据解析
   *
   * @return 返回一个配置类对象
   */
  public Configuration parse() {
    // 标记是否已经解析过，全局只解析一次
    if (parsed) {
      throw new BuilderException("Each XMLConfigBuilder can only be used once.");
    }
    parsed = true;
    // 执行解析，并赋予根节点
    parseConfiguration(parser.evalNode("/configuration"));
    return configuration;
  }

  /**
   * 执行配置解析
   * <?xml version="1.0" encoding="UTF-8" ?>
   * <!DOCTYPE configuration
   *         PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
   *         "http://mybatis.org/dtd/mybatis-3-config.dtd">
   * <configuration>
   *
   *     <!--configuration中的这些属性是有顺序的，最好按照本列子中的顺序配置-->
   *
   *     <!--通过properties配置的属性，我们可以在其他的配置中使用${}使用-->
   *     <!--在这边配置的属性，我们还能在Mapper.xml中使用${}形式使用-->
   *
   *     <!--可以在多个地方进行属性配置-->
   *     <!--通过方法参数传递的属性具有最高优先级，
   *         resource/url 属性中指定的配置文件次之，
   *         最低优先级的则是 properties 元素中指定的属性。-->
   *     <properties resource="./app.properties">
   *         <property name="username" value="username1"/>
   *         <property name="password" value="password1"/>
   *         <!--MyBatis支持默认值，假如某些属性没取到值就取默认值-->
   *         <!--开启默认值功能-->
   *         <property name="org.apache.ibatis.parsing.PropertyParser.enable-default-value" value="true"/>
   *         <!--设置分隔符为-->
   *         <property name="org.apache.ibatis.parsing.PropertyParser.default-value-separator" value="？:"/>
   *         <!-- 经过上面的配置，就可以像如下方式使用-->
   *         <!-- 如果配置了username属性就使用这个配置，如果没配置就使用ut_user这个默认值-->
   *         <!-- <property name="username" value="${db:username?:ut_user}"/>-->
   *     </properties>
   *
   *     <!--一些重要的全局配置-->
   *     <!--关于配置的含义，请看下面的注释-->
   *     <settings>
   *         <setting name="cacheEnabled" value="true"/>
   *         <setting name="lazyLoadingEnabled" value="true"/>
   *         <setting name="multipleResultSetsEnabled" value="true"/>
   *         <setting name="useColumnLabel" value="true"/>
   *         <setting name="useGeneratedKeys" value="false"/>
   *         <setting name="autoMappingBehavior" value="PARTIAL"/>
   *         <setting name="autoMappingUnknownColumnBehavior" value="WARNING"/>
   *         <setting name="defaultExecutorType" value="SIMPLE"/>
   *         <setting name="defaultStatementTimeout" value="25"/>
   *         <setting name="defaultFetchSize" value="100"/>
   *         <setting name="safeRowBoundsEnabled" value="false"/>
   *         <setting name="mapUnderscoreToCamelCase" value="false"/>
   *         <setting name="localCacheScope" value="STATEMENT"/>
   *         <setting name="jdbcTypeForNull" value="OTHER"/>
   *         <setting name="lazyLoadTriggerMethods" value="equals,clone,hashCode,toString"/>
   *         <setting name="logImpl" value="STDOUT_LOGGING" />
   *     </settings>
   *
   *     <!--类型别名，指定了类型别名后就可以使用别名来代替全限定名-->
   *     <typeAliases>
   *         <typeAlias alias="Cbondissuer" type="com.csx.demo.spring.boot.entity.Cbondissuer"/>
   *         <!--为一个包下面的所有类设定别名，此时会使用 Bean 的首字母小写的非限定类名来作为它的别名-->
   *         <package name="domain.blog"/>
   *     </typeAliases>
   *
   *     <!--每次 MyBatis 创建结果对象的新实例时，它都会使用一个对象工厂（ObjectFactory）实例来完成实例化工作。
   *         默认的对象工厂需要做的仅仅是实例化目标类，要么通过默认无参构造方法，要么通过存在的参数映射来调用带有
   *         参数的构造方法。 如果想覆盖对象工厂的默认行为，可以通过创建自己的对象工厂来实现。-->
   *     <objectFactory type="org.mybatis.example.ExampleObjectFactory">
   *         <property name="someProperty" value="100"/>
   *     </objectFactory>
   *
   *     <!--设置插件-->
   *     <plugins>
   *         <plugin interceptor="com.github.pagehelper.PageInterceptor">
   *             <!--&lt;!&ndash;默认值为 false，当该参数设置为 true 时，如果 pageSize=0 或者 RowBounds.limit = 0 就会查询出全部的结果&ndash;&gt;-->
   *             <!--如果某些查询数据量非常大，不应该允许查出所有数据-->
   *             <property name="pageSizeZero" value="true"/>
   *         </plugin>
   *     </plugins>
   *
   *     <!--可以配置多个数据源，通过default属性设置哪个生效-->
   *     <environments default="dev">
   *         <environment id="dev">
   *             <transactionManager type="JDBC"/>
   *             <dataSource type="POOLED">
   *                 <property name="driver" value="com.mysql.jdbc.Driver"/>
   *                 <property name="url" value="jdbc:mysql://xx.xx.xx.xx:3308/xx"/>
   *                 <property name="username" value="${username:ut_user}"/>
   *                 <property name="password" value="${password}"/>
   *                 <property name="poolMaximumIdleConnections" value="10"/>
   *             </dataSource>
   *         </environment>
   *         <environment id="stg">
   *             <transactionManager type="JDBC"/>
   *             <dataSource type="POOLED">
   *                 <property name="driver" value="com.mysql.jdbc.Driver"/>
   *                 <property name="url" value="jdbc:mysql://xx.xx.xx.xx:3308/xx"/>
   *                 <property name="username" value="${username:ut_user}"/>
   *                 <property name="password" value="${password}"/>
   *                 <property name="poolMaximumIdleConnections" value="10"/>
   *             </dataSource>
   *         </environment>
   *     </environments>
   *
   *     <!--MyBatis 可以根据不同的数据库厂商执行不同的语句，这种多厂商的支持是基于映射语句中的 databaseId 属性。
   *         MyBatis 会加载不带 databaseId 属性和带有匹配当前数据库 databaseId 属性的所有语句。 如果同时找到
   *         带有 databaseId 和不带 databaseId 的相同语句，则后者会被舍弃。 -->
   *     <databaseIdProvider type="DB_VENDOR">
   *         <!--开启MySQL，加入此时在Mapper文件中有两个id一样的查询语句，但是一条的databaseId是mysql，
   *             另一条的databaseId是oracle，那么databaseId是mysql的将被执行-->
   *         <property name="MySQL" value="mysql" />
   *         <!--<property name="Oracle" value="oracle" />-->
   *     </databaseIdProvider>
   *
   *     <mappers>
   *         <!--这边可以使用package和resource两种方式加载mapper-->
   *         <!--推荐使用resource这种-->
   *         <mapper resource="./mappers/CbondissuerMapper.xml"/>
   *     </mappers>
   *
   * </configuration>
   *
   * @param root 根节点
   */
  private void parseConfiguration(XNode root) {
    try {
      // issue #117 read properties first
      //  <properties />
      propertiesElement(root.evalNode("properties"));
      // 解析settings
      Properties settings = settingsAsProperties(root.evalNode("settings"));
      // 加载自定义虚拟文件系统
      loadCustomVfs(settings);
      // 加载自定义日志实现
      loadCustomLogImpl(settings);
      // 类型别名对象<typeAliases>
      typeAliasesElement(root.evalNode("typeAliases"));
      // 加载插件-对mybatis的一些方法进行拦截
      pluginElement(root.evalNode("plugins"));
      // 结果对象创建工厂
      objectFactoryElement(root.evalNode("objectFactory"));
      // 获取对象加工工厂
      objectWrapperFactoryElement(root.evalNode("objectWrapperFactory"));
      // 用于反射的工厂类
      reflectorFactoryElement(root.evalNode("reflectorFactory"));
      // 执行设置
      settingsElement(settings);
      // read it after objectFactory and objectWrapperFactory issue #631
      // 获取环境配置信息
      environmentsElement(root.evalNode("environments"));
      // 加载 数据库区分
      databaseIdProviderElement(root.evalNode("databaseIdProvider"));
      // 类型处理器
      typeHandlerElement(root.evalNode("typeHandlers"));
      // 获取mapper数据
      mapperElement(root.evalNode("mappers"));
    } catch (Exception e) {
      throw new BuilderException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
    }
  }

  private Properties settingsAsProperties(XNode context) {
    if (context == null) {
      return new Properties();
    }
    // 获取<settings>下面所有的<setting>
    Properties props = context.getChildrenAsProperties();
    // Check that all settings are known to the configuration class
    // 检查所有的配置项都是mybatis定义中的
    MetaClass metaConfig = MetaClass.forClass(Configuration.class, localReflectorFactory);
    for (Object key : props.keySet()) {
      if (!metaConfig.hasSetter(String.valueOf(key))) {
        throw new BuilderException("The setting " + key + " is not known.  Make sure you spelled it correctly (case sensitive).");
      }
    }
    return props;
  }

  /**
   * 加载虚拟文件系统
   * VFS含义是虚拟文件系统；主要是通过程序能够方便读取本地文件系统、FTP文件系统等系统中的文件资源
   * Mybatis中提供了VFS这个配置，主要是通过该配置可以加载自定义的虚拟文件系统应用程序
   *
   * @param props setting 配置文件键值对
   * @throws ClassNotFoundException 抛出class找不到的异常
   */
  private void loadCustomVfs(Properties props) throws ClassNotFoundException {
    String value = props.getProperty("vfsImpl");
    if (value != null) {
      // 表示支持多个类定义
      String[] clazzes = value.split(",");
      for (String clazz : clazzes) {
        if (!clazz.isEmpty()) {
          @SuppressWarnings("unchecked")
          Class<? extends VFS> vfsImpl = (Class<? extends VFS>) Resources.classForName(clazz);
          // 将虚拟文件系统对应的对象放到配置类中
          configuration.setVfsImpl(vfsImpl);
        }
      }
    }
  }

  private void loadCustomLogImpl(Properties props) {
    Class<? extends Log> logImpl = resolveClass(props.getProperty("logImpl"));
    configuration.setLogImpl(logImpl);
  }

  private void typeAliasesElement(XNode parent) {
    if (parent != null) {
      for (XNode child : parent.getChildren()) {
        if ("package".equals(child.getName())) {
          String typeAliasPackage = child.getStringAttribute("name");
          configuration.getTypeAliasRegistry().registerAliases(typeAliasPackage);
        } else {
          String alias = child.getStringAttribute("alias");
          String type = child.getStringAttribute("type");
          try {
            Class<?> clazz = Resources.classForName(type);
            if (alias == null) {
              typeAliasRegistry.registerAlias(clazz);
            } else {
              typeAliasRegistry.registerAlias(alias, clazz);
            }
          } catch (ClassNotFoundException e) {
            throw new BuilderException("Error registering typeAlias for '" + alias + "'. Cause: " + e, e);
          }
        }
      }
    }
  }

  /**
   * 获取 插件，插件主要是对mybatis的方法进行拦截
   *
   * @param parent 父节点 <plugins>
   * @throws Exception 获取过程中的异常
   */
  private void pluginElement(XNode parent) throws Exception {
    if (parent != null) {
      for (XNode child : parent.getChildren()) {
        String interceptor = child.getStringAttribute("interceptor");
        Properties properties = child.getChildrenAsProperties();
        Interceptor interceptorInstance = (Interceptor) resolveClass(interceptor).getDeclaredConstructor().newInstance();
        interceptorInstance.setProperties(properties);
        configuration.addInterceptor(interceptorInstance);
      }
    }
  }

  /**
   * 每次 MyBatis 创建结果对象的新实例时，它都会使用一个对象工厂（ObjectFactory）实例来完成实例化工作。
   * 默认的对象工厂需要做的仅仅是实例化目标类，要么通过默认无参构造方法，要么通过存在的参数映射来调用带有
   * 参数的构造方法。 如果想覆盖对象工厂的默认行为，可以通过创建自己的对象工厂来实现。
   *
   * @param context
   * @throws Exception
   */
  private void objectFactoryElement(XNode context) throws Exception {
    if (context != null) {
      String type = context.getStringAttribute("type");
      Properties properties = context.getChildrenAsProperties();
      ObjectFactory factory = (ObjectFactory) resolveClass(type).getDeclaredConstructor().newInstance();
      factory.setProperties(properties);
      configuration.setObjectFactory(factory);
    }
  }

  private void objectWrapperFactoryElement(XNode context) throws Exception {
    if (context != null) {
      String type = context.getStringAttribute("type");
      ObjectWrapperFactory factory = (ObjectWrapperFactory) resolveClass(type).getDeclaredConstructor().newInstance();
      configuration.setObjectWrapperFactory(factory);
    }
  }

  private void reflectorFactoryElement(XNode context) throws Exception {
    if (context != null) {
      String type = context.getStringAttribute("type");
      ReflectorFactory factory = (ReflectorFactory) resolveClass(type).getDeclaredConstructor().newInstance();
      configuration.setReflectorFactory(factory);
    }
  }

  private void propertiesElement(XNode context) throws Exception {
    // 如果内容不为空
    if (context != null) {
      // 获取子节点
      Properties defaults = context.getChildrenAsProperties();
      // 获取resource属性，properties文件位置
      String resource = context.getStringAttribute("resource");
      // url 猜测是从网络进行配置加载
      String url = context.getStringAttribute("url");
      // resource和url只能选择其中一种加载方式
      if (resource != null && url != null) {
        throw new BuilderException("The properties element cannot specify both a URL and a resource based property file reference.  Please specify one or the other.");
      }
      if (resource != null) {
        // 解析Property键值对
        defaults.putAll(Resources.getResourceAsProperties(resource));
      } else if (url != null) {
        defaults.putAll(Resources.getUrlAsProperties(url));
      }
      // 取出原本的配置，不为空则把配置全都加入
      Properties vars = configuration.getVariables();
      if (vars != null) {
        defaults.putAll(vars);
      }
      parser.setVariables(defaults);
      configuration.setVariables(defaults);
    }
  }

  /**
   * 执行设置 https://mybatis.net.cn/configuration.html#settings
   * @param props
   */
  private void settingsElement(Properties props) {
    // 指定 MyBatis 应如何自动映射列到字段或属性。默认PARTIAL
    // NONE 表示关闭自动映射；
    // PARTIAL 只会自动映射没有定义嵌套结果映射的字段。
    // FULL 会自动映射任何复杂的结果集（无论是否嵌套）。
    configuration.setAutoMappingBehavior(AutoMappingBehavior.valueOf(props.getProperty("autoMappingBehavior", "PARTIAL")));
    // 指定发现自动映射目标未知列（或未知属性类型）的行为。 默认NONE
    // NONE: 不做任何反应
    // WARNING: 输出警告日志（'org.apache.ibatis.session.AutoMappingUnknownColumnBehavior'的日志等级必须设置为 WARN）
    // FAILING: 映射失败 (抛出 SqlSessionException)
    configuration.setAutoMappingUnknownColumnBehavior(AutoMappingUnknownColumnBehavior.valueOf(props.getProperty("autoMappingUnknownColumnBehavior", "NONE")));
    configuration.setCacheEnabled(booleanValueOf(props.getProperty("cacheEnabled"), true));
    configuration.setProxyFactory((ProxyFactory) createInstance(props.getProperty("proxyFactory")));
    configuration.setLazyLoadingEnabled(booleanValueOf(props.getProperty("lazyLoadingEnabled"), false));
    configuration.setAggressiveLazyLoading(booleanValueOf(props.getProperty("aggressiveLazyLoading"), false));
    configuration.setMultipleResultSetsEnabled(booleanValueOf(props.getProperty("multipleResultSetsEnabled"), true));
    configuration.setUseColumnLabel(booleanValueOf(props.getProperty("useColumnLabel"), true));
    configuration.setUseGeneratedKeys(booleanValueOf(props.getProperty("useGeneratedKeys"), false));
    configuration.setDefaultExecutorType(ExecutorType.valueOf(props.getProperty("defaultExecutorType", "SIMPLE")));
    configuration.setDefaultStatementTimeout(integerValueOf(props.getProperty("defaultStatementTimeout"), null));
    configuration.setDefaultFetchSize(integerValueOf(props.getProperty("defaultFetchSize"), null));
    configuration.setDefaultResultSetType(resolveResultSetType(props.getProperty("defaultResultSetType")));
    configuration.setMapUnderscoreToCamelCase(booleanValueOf(props.getProperty("mapUnderscoreToCamelCase"), false));
    configuration.setSafeRowBoundsEnabled(booleanValueOf(props.getProperty("safeRowBoundsEnabled"), false));
    configuration.setLocalCacheScope(LocalCacheScope.valueOf(props.getProperty("localCacheScope", "SESSION")));
    configuration.setJdbcTypeForNull(JdbcType.valueOf(props.getProperty("jdbcTypeForNull", "OTHER")));
    configuration.setLazyLoadTriggerMethods(stringSetValueOf(props.getProperty("lazyLoadTriggerMethods"), "equals,clone,hashCode,toString"));
    configuration.setSafeResultHandlerEnabled(booleanValueOf(props.getProperty("safeResultHandlerEnabled"), true));
    configuration.setDefaultScriptingLanguage(resolveClass(props.getProperty("defaultScriptingLanguage")));
    configuration.setDefaultEnumTypeHandler(resolveClass(props.getProperty("defaultEnumTypeHandler")));
    configuration.setCallSettersOnNulls(booleanValueOf(props.getProperty("callSettersOnNulls"), false));
    configuration.setUseActualParamName(booleanValueOf(props.getProperty("useActualParamName"), true));
    configuration.setReturnInstanceForEmptyRow(booleanValueOf(props.getProperty("returnInstanceForEmptyRow"), false));
    configuration.setLogPrefix(props.getProperty("logPrefix"));
    configuration.setConfigurationFactory(resolveClass(props.getProperty("configurationFactory")));
    configuration.setShrinkWhitespacesInSql(booleanValueOf(props.getProperty("shrinkWhitespacesInSql"), false));
    configuration.setArgNameBasedConstructorAutoMapping(booleanValueOf(props.getProperty("argNameBasedConstructorAutoMapping"), false));
    configuration.setDefaultSqlProviderType(resolveClass(props.getProperty("defaultSqlProviderType")));
    configuration.setNullableOnForEach(booleanValueOf(props.getProperty("nullableOnForEach"), false));
  }

  /**
   * 解析 事务和数据源
   * @param context environments节点
   * @throws Exception
   */
  private void environmentsElement(XNode context) throws Exception {

    if (context != null) {
      if (environment == null) {
        // 记录默认的环境信息
        environment = context.getStringAttribute("default");
      }
      for (XNode child : context.getChildren()) {
        String id = child.getStringAttribute("id");
        // 判断当前是否是默认的环境信息
        if (isSpecifiedEnvironment(id)) {
          // 事务节点
          TransactionFactory txFactory = transactionManagerElement(child.evalNode("transactionManager"));
          // 数据源
          DataSourceFactory dsFactory = dataSourceElement(child.evalNode("dataSource"));
          DataSource dataSource = dsFactory.getDataSource();
          Environment.Builder environmentBuilder = new Environment.Builder(id)
              .transactionFactory(txFactory)
              .dataSource(dataSource);
          configuration.setEnvironment(environmentBuilder.build());
          break;
        }
      }
    }
  }

  /**
   * MyBatis 可以根据不同的数据库厂商执行不同的语句，这种多厂商的支持是基于映射语句中的 databaseId 属性。
   * MyBatis 会加载不带 databaseId 属性和带有匹配当前数据库 databaseId 属性的所有语句。 如果同时找到
   * 带有 databaseId 和不带 databaseId 的相同语句，则后者会被舍弃.
   * <databaseIdProvider type="DB_VENDOR">
   * 开启MySQL，加入此时在Mapper文件中有两个id一样的查询语句，但是一条的databaseId是mysql，
   * 另一条的databaseId是oracle，那么databaseId是mysql的将被执行
   * <property name="MySQL" value="mysql" />
   * <property name="Oracle" value="oracle" />
   * </databaseIdProvider>
   *
   * @param context
   * @throws Exception
   */
  private void databaseIdProviderElement(XNode context) throws Exception {
    DatabaseIdProvider databaseIdProvider = null;
    if (context != null) {
      String type = context.getStringAttribute("type");
      // awful patch to keep backward compatibility
      if ("VENDOR".equals(type)) {
        type = "DB_VENDOR";
      }
      Properties properties = context.getChildrenAsProperties();
      databaseIdProvider = (DatabaseIdProvider) resolveClass(type).getDeclaredConstructor().newInstance();
      databaseIdProvider.setProperties(properties);
    }
    Environment environment = configuration.getEnvironment();
    if (environment != null && databaseIdProvider != null) {
      String databaseId = databaseIdProvider.getDatabaseId(environment.getDataSource());
      configuration.setDatabaseId(databaseId);
    }
  }

  private TransactionFactory transactionManagerElement(XNode context) throws Exception {
    if (context != null) {
      String type = context.getStringAttribute("type");
      Properties props = context.getChildrenAsProperties();
      TransactionFactory factory = (TransactionFactory) resolveClass(type).getDeclaredConstructor().newInstance();
      factory.setProperties(props);
      return factory;
    }
    throw new BuilderException("Environment declaration requires a TransactionFactory.");
  }

  private DataSourceFactory dataSourceElement(XNode context) throws Exception {
    if (context != null) {
      String type = context.getStringAttribute("type");
      Properties props = context.getChildrenAsProperties();
      DataSourceFactory factory = (DataSourceFactory) resolveClass(type).getDeclaredConstructor().newInstance();
      factory.setProperties(props);
      return factory;
    }
    throw new BuilderException("Environment declaration requires a DataSourceFactory.");
  }

  /**
   * MyBatis 在设置预处理语句（PreparedStatement）中的参数或从结果集中取出一个值时，
   * 都会用类型处理器将获取到的值以合适的方式转换成 Java 类型。下表描述了一些默认的类型处理器
   * @param parent
   */
  private void typeHandlerElement(XNode parent) {
    if (parent != null) {
      for (XNode child : parent.getChildren()) {
        if ("package".equals(child.getName())) {
          String typeHandlerPackage = child.getStringAttribute("name");
          typeHandlerRegistry.register(typeHandlerPackage);
        } else {
          String javaTypeName = child.getStringAttribute("javaType");
          String jdbcTypeName = child.getStringAttribute("jdbcType");
          String handlerTypeName = child.getStringAttribute("handler");
          Class<?> javaTypeClass = resolveClass(javaTypeName);
          JdbcType jdbcType = resolveJdbcType(jdbcTypeName);
          Class<?> typeHandlerClass = resolveClass(handlerTypeName);
          if (javaTypeClass != null) {
            if (jdbcType == null) {
              typeHandlerRegistry.register(javaTypeClass, typeHandlerClass);
            } else {
              typeHandlerRegistry.register(javaTypeClass, jdbcType, typeHandlerClass);
            }
          } else {
            typeHandlerRegistry.register(typeHandlerClass);
          }
        }
      }
    }
  }

  private void mapperElement(XNode parent) throws Exception {
    // parent是mappers
    if (parent != null) {
      // child是mapper
      for (XNode child : parent.getChildren()) {
        // 通过包名进行加载
        if ("package".equals(child.getName())) {
          String mapperPackage = child.getStringAttribute("name");
          configuration.addMappers(mapperPackage);
        } else {
          // 否则使用resource的方式进行加载
          String resource = child.getStringAttribute("resource");
          String url = child.getStringAttribute("url");
          String mapperClass = child.getStringAttribute("class");
          // 只能采用其中的一种方法
          if (resource != null && url == null && mapperClass == null) {
            ErrorContext.instance().resource(resource);
            try(InputStream inputStream = Resources.getResourceAsStream(resource)) {
              XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
              // 执行mapper解析
              mapperParser.parse();
            }
          } else if (resource == null && url != null && mapperClass == null) {
            ErrorContext.instance().resource(url);
            try(InputStream inputStream = Resources.getUrlAsStream(url)){
              XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, url, configuration.getSqlFragments());
              mapperParser.parse();
            }
          } else if (resource == null && url == null && mapperClass != null) {
            Class<?> mapperInterface = Resources.classForName(mapperClass);
            configuration.addMapper(mapperInterface);
          } else {
            throw new BuilderException("A mapper element may only specify a url, resource or class, but not more than one.");
          }
        }
      }
    }
  }

  private boolean isSpecifiedEnvironment(String id) {
    if (environment == null) {
      throw new BuilderException("No environment specified.");
    }
    if (id == null) {
      throw new BuilderException("Environment requires an id attribute.");
    }
    return environment.equals(id);
  }

}
