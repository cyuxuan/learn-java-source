/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.beans.factory.support;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.core.env.Environment;
import org.springframework.core.env.EnvironmentCapable;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Abstract base class for bean definition readers which implement
 * the {@link BeanDefinitionReader} interface.
 *
 * <p>Provides common properties like the bean factory to work on
 * and the class loader to use for loading bean classes.
 *
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 11.12.2003
 * @see BeanDefinitionReaderUtils
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader, EnvironmentCapable {

	/** Logger available to subclasses. */
	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * Bean定义 注册中心
	 */
	private final BeanDefinitionRegistry registry;

	/**
	 * 资源加载器
	 */
	@Nullable
	private ResourceLoader resourceLoader;

	/**
	 * 类加载器
	 */
	@Nullable
	private ClassLoader beanClassLoader;

	/**
	 * 环境信息
	 */
	private Environment environment;

	/**
	 * bean名字生成器
	 */
	private BeanNameGenerator beanNameGenerator = DefaultBeanNameGenerator.INSTANCE;


	/**
	 * Create a new AbstractBeanDefinitionReader for the given bean factory.
	 * 创建一个新的AbstractBeanDefinitionReader给bean factory
	 *
	 * <p>If the passed-in(传入) bean factory does not only implement the BeanDefinitionRegistry
	 * interface but also the ResourceLoader interface, it will be used as default
	 * ResourceLoader as well. This will usually be the case for
	 * {@link org.springframework.context.ApplicationContext} implementations.
	 * <p>If given(给定，指定) a plain(普通的) BeanDefinitionRegistry(bean定义信息注册器), the default ResourceLoader will be a
	 * {@link org.springframework.core.io.support.PathMatchingResourcePatternResolver}.
	 * <p>If the passed-in bean factory also implements {@link EnvironmentCapable} its
	 * environment will be used by this reader.  Otherwise(否则), the reader will initialize and
	 * use a {@link StandardEnvironment}. All ApplicationContext implementations are
	 * EnvironmentCapable, while normal BeanFactory implementations are not.
	 *
	 * 如果传入的bean factory 不仅实现了BeanDefinitionRegistry接口还实现了ResourceLoader接口，那它将会作为一个ResourceLoader
	 * 这通常也是ApplicationContext的实现方案
	 * 如果指定的是一个普通的bean定义信息注册器，那么默认的资源加载器ResourceLoader将会是PathMatchingResourcePatternResolver
	 * 如果传入的bean facroty同时实现了EnvironmentCapable接口，那么它的环境信息将会被此reader(当前类)使用。即赋值给当前environment属性
	 * 否则该reader的environment属性将会初始化为StandardEnvironment
	 * 所有的ApplicationContext实现都是实现了EnvironmentCapable的，拥有环境信息查看能力，而普通的beanFactory实现则不是如此。
	 *
	 * @param registry the BeanFactory to load bean definitions into,
	 * in the form of a BeanDefinitionRegistry
	 * BeanFactory用于加载进bean定义信息，以BeanDefinitionRegistry的形式
	 *
	 * @see #setResourceLoader
	 * @see #setEnvironment
	 */
	protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
		Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
		this.registry = registry;

		// Determine ResourceLoader to use.
		if (this.registry instanceof ResourceLoader) {
			this.resourceLoader = (ResourceLoader) this.registry;
		}
		else {
			this.resourceLoader = new PathMatchingResourcePatternResolver();
		}

		// Inherit Environment if possible
		if (this.registry instanceof EnvironmentCapable) {
			this.environment = ((EnvironmentCapable) this.registry).getEnvironment();
		}
		else {
			this.environment = new StandardEnvironment();
		}
	}


	@Override
	public final BeanDefinitionRegistry getRegistry() {
		return this.registry;
	}

	/**
	 * Set the ResourceLoader to use for resource locations.
	 * If specifying a ResourcePatternResolver, the bean definition reader
	 * will be capable(有能力的) of resolving resource patterns(模式) to Resource arrays.
	 * <p>Default is PathMatchingResourcePatternResolver, also capable of
	 * resource pattern resolving through the ResourcePatternResolver interface.
	 * <p>Setting this to {@code null} suggests that absolute(绝对的) resource loading
	 * is not available for this bean definition reader.
	 *
	 * 设置资源加载器，用于资源加载
	 * 如果设置了ResourcePatternResolver，此bean定义读取器，需要具有解决 Resource数组形式的资源 的能力
	 * 默认是PathMatchingResourcePatternResolver，也能够通过ResourcePatternResolver接口解析资源模式
	 * 可以将此loader设置为null,表示此bean定义读取器不能读取绝对的资源
	 *
	 * @see org.springframework.core.io.support.ResourcePatternResolver
	 * @see org.springframework.core.io.support.PathMatchingResourcePatternResolver
	 */
	public void setResourceLoader(@Nullable ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	@Nullable
	public ResourceLoader getResourceLoader() {
		return this.resourceLoader;
	}

	/**
	 * Set the ClassLoader to use for bean classes.
	 * 设置用于bean classes的类加载器
	 *
	 * <p>Default is {@code null}, which suggests to not load bean classes
	 * eagerly but rather to(---而是) just register bean definitions with class names,
	 * with the corresponding(相应的) Classes to be resolved later (or never).
	 * 默认是null，这表明不要过早的加载bean classes，而是仅仅注册这些bean信息随着类名字，
	 * 后面再(或者永远不)随着相应的类被处理
	 *
	 * @see Thread#getContextClassLoader()
	 */
	public void setBeanClassLoader(@Nullable ClassLoader beanClassLoader) {
		this.beanClassLoader = beanClassLoader;
	}

	@Override
	@Nullable
	public ClassLoader getBeanClassLoader() {
		return this.beanClassLoader;
	}

	/**
	 * Set the Environment to use when reading bean definitions. Most often(通常) used
	 * for evaluating(评估) profile(配置文件，环境) information to determine(确定) which bean definitions
	 * should be read and which should be omitted(忽略).
	 * 读取bean定义时设置环境对象来使用。通常用于评估配置信息来决定哪些bean应该加载哪些bean应该忽略
	 *
	 */
	public void setEnvironment(Environment environment) {
		Assert.notNull(environment, "Environment must not be null");
		this.environment = environment;
	}

	@Override
	public Environment getEnvironment() {
		return this.environment;
	}

	/**
	 * Set the BeanNameGenerator to use for anonymous beans
	 * (without explicit bean name specified).
	 * <p>Default is a {@link DefaultBeanNameGenerator}.
	 */
	public void setBeanNameGenerator(@Nullable BeanNameGenerator beanNameGenerator) {
		this.beanNameGenerator = (beanNameGenerator != null ? beanNameGenerator : DefaultBeanNameGenerator.INSTANCE);
	}

	@Override
	public BeanNameGenerator getBeanNameGenerator() {
		return this.beanNameGenerator;
	}


	@Override
	public int loadBeanDefinitions(Resource... resources) throws BeanDefinitionStoreException {
		Assert.notNull(resources, "Resource array must not be null");
		int count = 0;
		for (Resource resource : resources) {
			count += loadBeanDefinitions(resource);
		}
		return count;
	}

	@Override
	public int loadBeanDefinitions(String location) throws BeanDefinitionStoreException {
		return loadBeanDefinitions(location, null);
	}

	/**
	 * Load bean definitions from the specified resource location.
	 * 加载bean定义信息到指定的资源位置
	 *
	 * <p>The location can also be a location pattern, provided that the
	 * ResourceLoader of this bean definition reader is a ResourcePatternResolver.
	 * 位置信息也可以是定位模式，前提是该bean定义信息读取器的资源加载器是ResourcePatternResolver
	 *
	 * @param location the resource location, to be loaded with the ResourceLoader
	 * (or ResourcePatternResolver) of this bean definition reader
	 * @param actualResources a Set to be filled with the actual Resource objects
	 * that have been resolved during the loading process. May be {@code null}
	 * to indicate that the caller is not interested in those Resource objects.
	 * @return the number of bean definitions found
	 * @throws BeanDefinitionStoreException in case of loading or parsing errors
	 * @see #getResourceLoader()
	 * @see #loadBeanDefinitions(org.springframework.core.io.Resource)
	 * @see #loadBeanDefinitions(org.springframework.core.io.Resource[])
	 */
	public int loadBeanDefinitions(String location, @Nullable Set<Resource> actualResources) throws BeanDefinitionStoreException {
		// 获取资源加载器
		ResourceLoader resourceLoader = getResourceLoader();
		// 如果资源加载器为空则抛出异常
		if (resourceLoader == null) {
			throw new BeanDefinitionStoreException(
					"Cannot load bean definitions from location [" + location + "]: no ResourceLoader available");
		}
		// 如果资源加载器是ResourcePatternResolver
		if (resourceLoader instanceof ResourcePatternResolver) {
			// Resource pattern matching available.
			// 资源模式匹配可用
			try {

				Resource[] resources = ((ResourcePatternResolver) resourceLoader).getResources(location);
				int count = loadBeanDefinitions(resources);
				if (actualResources != null) {
					Collections.addAll(actualResources, resources);
				}
				if (logger.isTraceEnabled()) {
					logger.trace("Loaded " + count + " bean definitions from location pattern [" + location + "]");
				}
				return count;
			}
			catch (IOException ex) {
				throw new BeanDefinitionStoreException(
						"Could not resolve bean definition resource pattern [" + location + "]", ex);
			}
		}
		else {
			// Can only load single resources by absolute URL.
			Resource resource = resourceLoader.getResource(location);
			int count = loadBeanDefinitions(resource);
			if (actualResources != null) {
				actualResources.add(resource);
			}
			if (logger.isTraceEnabled()) {
				logger.trace("Loaded " + count + " bean definitions from location [" + location + "]");
			}
			return count;
		}
	}

	@Override
	public int loadBeanDefinitions(String... locations) throws BeanDefinitionStoreException {
		Assert.notNull(locations, "Location array must not be null");
		int count = 0;
		for (String location : locations) {
			count += loadBeanDefinitions(location);
		}
		return count;
	}

}
