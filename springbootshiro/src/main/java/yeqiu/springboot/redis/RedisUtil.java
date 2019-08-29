package yeqiu.springboot.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author 陆昌
 * @time 创建于2019年8月7日上午10:59:53
 * 说明：redis服务工具类，包括增删改
 */
@Service
public class RedisUtil {
	@Autowired RedisTemplate<String, String> redisTemplate;//引入模板
	/**
	 * 添加一个缓存
	 * @param key 键
	 * @param value 值
	 * @return true or false
	 */
	public boolean set(final  String key,String value){
		try {
			redisTemplate.opsForValue().set(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 获取缓存的值
	 * @param key 键
	 * @return
	 */
	public Object get(final String key){
		return redisTemplate.opsForValue().get(key);		
	}
	/**
	 * 更新缓存
	 * @param key 键
	 * @param value 更新值
	 * @return
	 */
	public boolean update(final  String key,String value){
		try {
			redisTemplate.opsForValue().getAndSet(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 删除缓存
	 * @param key 键
	 * @return
	 */
	public boolean delete(final String key) {
		try {
			redisTemplate.delete(key);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
