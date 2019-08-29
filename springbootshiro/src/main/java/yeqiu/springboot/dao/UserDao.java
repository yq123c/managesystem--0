package yeqiu.springboot.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * @author LC
 *创建时间：2019年8月5日 上午11:47:00
 *
 */
@Repository
public interface UserDao {
	/**
	 * 查询用户信息
	 * @param username 用户名
	 * @return
	 */
	public Map<String, Object> findUserByName(String username);
}
