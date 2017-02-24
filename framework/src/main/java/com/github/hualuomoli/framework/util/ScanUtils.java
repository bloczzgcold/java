package com.github.hualuomoli.framework.util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import com.google.common.collect.Sets;

/**
 * spring扫码工具类
 * @author lbq
 *
 */
public class ScanUtils {

	/**
	 * 根据扫码包获取资源
	 * @param scanPackages 扫码包,多个包使用逗号(,)分割 
	 * @return 资源集合
	 * @throws IOException 获取资源错误
	 */
	public static Set<Resource> findResources(String scanPackages) throws IOException {

		Set<Resource> resources = Sets.newHashSet();

		if (StringUtils.isEmpty(scanPackages)) {
			return resources;
		}

		// 扫码的包
		Set<String> packages = Sets.newHashSet(StringUtils.split(scanPackages, ","));
		//获取Spring资源解析器
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

		for (String basePackage : packages) {
			if (StringUtils.isEmpty(basePackage)) {
				continue;
			}
			String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX //
					+ ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage))//
					+ "/**/*.class";

			//获取packageSearchPath下的Resource，这里得到的Resource是Class信息
			Resource[] rs = resourcePatternResolver.getResources(packageSearchPath);
			resources.addAll(Sets.newHashSet(rs));
		}
		return resources;
	}

	/**
	 * 根据扫码包获取资源类
	 * @param scanPackages 扫码包,多个包使用逗号(,)分割 
	 * @return 资源类集合
	 * @throws IOException 获取资源错误
	 * @throws ClassNotFoundException 类未找到
	 */
	public static Set<Class<?>> findClass(String scanPackages) throws IOException, ClassNotFoundException {

		Set<Class<?>> clazzSet = Sets.newHashSet();

		Set<Resource> resources = findResources(scanPackages);

		//获取Spring资源解析器
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		//创建Spring中用来读取resource为class的工具类
		MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

		for (Resource resource : resources) {
			//检查resource，这里的resource都是class
			Class<?> clazz = loadClassName(metadataReaderFactory, resource);
			clazzSet.add(clazz);
		}
		return clazzSet;
	}

	/**
	 * 查找指定包下使用注解的类
	 * @param scanPackages 扫码的包,多个包使用逗号(,)分割
	 * @param annotation 注解类
	 * @return 包含注解的类
	 * @throws IOException 获取资源错误
	 * @throws ClassNotFoundException 类未找到
	 */
	public static Set<Class<?>> findByAnnotation(String scanPackages, Class<? extends Annotation> annotation) throws IOException, ClassNotFoundException {

		Set<Class<?>> clazzSet = Sets.newHashSet();

		Set<Class<?>> sets = findClass(scanPackages);

		for (Class<?> clazz : sets) {
			if (clazz != null && clazz.getAnnotation(annotation) != null) {
				clazzSet.add(clazz);
			}
		}

		return clazzSet;
	}

	/**
	 * 查找指定包下实现接口的类
	 * @param scanPackages 扫码的包,多个包使用逗号(,)分割
	 * @param interfaceClazz 接口类
	 * @return 包含注解的类
	 * @throws IOException 获取资源错误
	 * @throws ClassNotFoundException 类未找到
	 */
	@SuppressWarnings("unchecked")
	public static <T> Set<Class<T>> findByInterface(String scanPackages, Class<T> interfaceClazz) throws IOException, ClassNotFoundException {

		Set<Class<T>> clazzSet = Sets.newHashSet();

		Set<Class<?>> sets = findClass(scanPackages);

		for (Class<?> clazz : sets) {
			if (clazz != null && interfaceClazz.isAssignableFrom(clazz)) {
				clazzSet.add((Class<T>) clazz);
			}
		}

		return clazzSet;
	}

	/**
	* 加载资源，根据resource获取class
	* @param metadataReaderFactory spring中用来读取resource为class的工具
	* @param resource 这里的资源就是一个Class
	 * @throws IOException 获取资源错误
	 * @throws ClassNotFoundException 类未找到
	*/
	private static Class<?> loadClassName(MetadataReaderFactory metadataReaderFactory, Resource resource) throws IOException, ClassNotFoundException {
		if (!resource.isReadable()) {
			return null;
		}
		MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
		if (metadataReader == null) {
			return null;
		}

		String className = metadataReader.getClassMetadata().getClassName();
		return Class.forName(className);
	}

}
