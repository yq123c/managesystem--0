package yeqiu.springboot.redis;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * @author 陆昌
 * @time 创建于2019年8月7日上午10:02:32
 * 说明：注解式使用redis缓存，配置
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport{
	/**
	 * 此为Springboot2.0的配置方式（1.0有所不同）
	 * @param factory
	 * @return
	 */
	@Bean
	public CacheManager cacheManager(RedisConnectionFactory factory) {
		System.out.println("开始配置redis");
		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();  // 生成一个默认配置，通过config对象即可对缓存进行自定义配置
	    config = config.entryTtl(Duration.ofMinutes(1))     // 设置缓存的默认过期时间，也是使用Duration设置
	            .disableCachingNullValues();     // 不缓存空值

	    // 设置一个初始化的缓存空间set集合
	    Set<String> cacheNames =  new HashSet<>();
	    cacheNames.add("my-redis-cache1");
	    cacheNames.add("my-redis-cache2");

	    // 对每个缓存空间应用不同的配置
	    Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
	    configMap.put("my-redis-cache1", config);
	    configMap.put("my-redis-cache2", config.entryTtl(Duration.ofSeconds(120)));

	    RedisCacheManager cacheManager = RedisCacheManager.builder(factory)     // 使用自定义的缓存配置初始化一个cacheManager
	            .initialCacheNames(cacheNames)  // 注意这两句的调用顺序，一定要先调用该方法设置初始化的缓存名，再初始化相关的配置
	            .withInitialCacheConfigurations(configMap)
	            .build();
	    System.out.println("配置完成");
	    return cacheManager;
	}
}
