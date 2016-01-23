package com.app.finder.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

/**
 * 自定义的配置类  2016-1-22
 * 作用是开启spring的注解缓存@Cacheable可用
 * 
 *
 */
//@Configuration
//@EnableCaching
public class CacheConfigurationCustom {

	// 缓存实现用的是ehcache 不可用出错信息如下 可能是hibernate已经使用重复了
	// No CacheResolver specified, and no unique bean of type CacheManager found. Mark one as primary (or give it the name 'cacheManager') or declare a specific CacheManager to use, that serves as the default one.
	// @Primary
	@Bean
	public CacheManager getEhCacheManager(){
	    return new EhCacheCacheManager(getEhCacheFactory().getObject());
	}
	@Bean
	public EhCacheManagerFactoryBean getEhCacheFactory(){
		EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
		factoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
		factoryBean.setShared(true);
		return factoryBean;
	}
	
	
	//JDK Cache Manager
	/*@Primary
	@Bean
	public CacheManager jdkCacheManager() {
		return new ConcurrentMapCacheManager("hotArticle");
	}*/
}
