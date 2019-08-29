/**
 * 
 */
package yeqiu.springboot.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import yeqiu.springboot.dao.TestDao;
import yeqiu.springboot.dao.UserDao;
import yeqiu.springboot.redis.RedisUtil;

/**
 * @author 陆昌
 * @time 2019年6月21日下午3:59:15
 * 说明：
 */
@Service
public class TestService {
	@Autowired TestDao testDao;
	@Autowired UserDao userDao;
	@Autowired RedisUtil redisUtil;
	public Map<String,Object> getUserInfo(){	
		System.out.println(userDao.findUserByName("yeqiu"));
		return userDao.findUserByName("yeqiu");
	}
	
	public Object testRedis() {
		redisUtil.set("key", "叶秋同学的简介是从Redis拿出来的");
		return redisUtil.get("key");		
	}
}
