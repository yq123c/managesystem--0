package yeqiu.springboot.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import yeqiu.springboot.dao.UserDao;

/**
 * @author LC
 *创建时间：2019年8月5日 上午11:38:41
 *
 */
@Service
public class UserService {
	@Autowired UserDao dao;
	public Map<String, Object> findUserByName(String username) {
		return dao.findUserByName(username);
	}

}
