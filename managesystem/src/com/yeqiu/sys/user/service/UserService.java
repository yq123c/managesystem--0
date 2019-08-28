package com.yeqiu.sys.user.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.yeqiu.sys.common.util.ResponseResult;
import com.yeqiu.sys.common.util.UUIDUtil;
import com.yeqiu.sys.user.dao.UserDao;
/**
 * 
 * @author 陆昌
 * @time 创建于2019年8月23日上午10:28:11
 * 说明：
 */
@Service
public class UserService {
	private Logger logger = Logger.getLogger(UserService.class);
	@Autowired UserDao userDao;
	/**
	 * 通过登录名查找用户信息
	 * @param username 登录名
	 * @return
	 */
	public Map<String,String> getUserByLoginName(String loginName){
		try {
			return userDao.getUserByLoginName(loginName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
    /**
     * 根据用户id查找用户
     * @param id
     * @return
     */
    public Map<String,String> findById(String id) {
        return userDao.findById(id);
    }
    
	/**
     * 根据用户名查找其角色和所有权限
     * @param username
     * @return
     */
    public Map<String,Set<String>> findRolesAndPermissions(String username) {
    	 /*查询用户的所有角色信息*/
        List<Map<String,String>> queryRoles = userDao.findRoles(username);
        Set<String> roles = null;//角色
        Set<String> permissions = null;//权限
        Map<String,Set<String>> result = null;//返回结果
        if( queryRoles != null ) {
        	roles = new HashSet<String>();
        	permissions = new HashSet<String>();
        	result = new HashMap<String,Set<String>>();
        	for( Map<String,String> r : queryRoles ) {
        		roles.add(r.get("rolename"));
        		/*通过角色id查询角色拥有的权限*/
        		List<Map<String,String>> queryPermissions= userDao.findPermissions(r.get("roleid"));
        		for( Map<String,String> P : queryPermissions ) {
        			permissions.add(P.get("permission"));
        		}
	    	}	
        	result.put("roles", roles);
        	result.put("permissions", permissions);
        }
        return result;
    }
    
    /**
     * 为用户添加/修改角色
     * @param userId 用户ID
     * @param rolesId 角色ID列表
     */
    public void addRoleForUser(String userId,Set<String> rolesId) {
		/*已经存在的角色信息*/
		Set<String> existRoleInfo = userDao.getRolesByUserId(userId);
		if( rolesId != null ) {
			for( String role : rolesId  ) {		
				/*
				 * 判断用户是否已经存在该角色
				 */
				boolean exist =existRoleInfo.contains(role);
				if( !exist ) {//如果不存在，则新增					
					userDao.addUserRoleInfo(userId, role);
				}else {
					/*
					 * 如果已经存在，把该记录在existRoleInfo中移除
					 * 那么本次遍历完之后existRoleInfo剩下的就是要删除的角色信息
					 */
					existRoleInfo.remove(role);
				}			
			}
		}		
		for( String role : existRoleInfo  ) {		
			userDao.deleteUserRoleInfo(userId, role);//删除			
		}
    }     
    /**
     * 获取用户列表
     * @param order 排序方式
     * @param sort 排序字段
     * @param offset 偏移量
     * @param limit 分页数量
     * @return
     */
    public List<Map<String,String>> getUserList(String order,String sort,String offset,String limit,String name,String login_name){
    	try {
    		return userDao.getUserList(order, sort, offset, limit, name, login_name);
		} catch (Exception e) {
			logger.error("获取用户列表出错",e);
			e.printStackTrace();
			return null;    	
		}		
    }
    public Integer getTotal(String name,String login_name){
    	try {    		
    		return userDao.getTotal(name, login_name);
		} catch (Exception e) {
			logger.error("获取用户记录总数出错",e);
			e.printStackTrace();
			return 0;    	
		}
    }
    /**
     * 通过父级id获取地址信息列表
     * @param parent_id 
     * @return
     */
    public List<Map<String,String>> getAddressInfoByParentId(String parent_id){
    	try {
			return userDao.getAddressInfoByParentId(parent_id);
		} catch (Exception e) {
			logger.error("获取地址信息出错",e);
			e.printStackTrace();
			return null;
		}  	
    }
    /**
     * 添加或者删除用户
     * 参数判断和用户名重名判断两者都需要，因此，添加和更新合并为一个方法
     * @param userInfo
     * @return
     */
    @Transactional
    public ResponseResult addOrUpdateUser(Map<String, String> userInfo){
		ResponseResult result;
		try {
			/*判断参数*/
			if( StringUtils.isEmpty(userInfo.get("user_name").toString())||
				( StringUtils.isEmpty(userInfo.get("login_name").toString())||
				("add".equals(userInfo.get("operation").toString()) && ( StringUtils.isEmpty(userInfo.get("password").toString())))
				)) {
				result = new ResponseResult(ResponseResult.STATE_FAIL,"请将参数填写完整");
				return result;
			}
			/*判断登录名是否重复*/
			Map<String, String> exist = userDao.getUserByLoginName(userInfo.get("login_name").toString());
			if( exist != null ) {
				if( "add".equals(userInfo.get("operation").toString() )) {//新增时
					result = new ResponseResult(ResponseResult.STATE_FAIL,"登录名已存在，请换一个试试！");
					return result;
				}else if("update".equals(userInfo.get("operation").toString())) {//更新时
					/*更新时，userInfo中已经包含用户的id，
					 * 当不修改用户的登录名时，查到的exist不为空，且id相同
					 * 如果与别的登录名重了，那么查询到的必然不相同
					 * */
					if(!exist.get("id").toString().equals(userInfo.get("id").toString())) {
						result = new ResponseResult(ResponseResult.STATE_FAIL,"登录名已存在，请换一个试试！");
						return result;
					}
				}
			}			
			if( "add".equals(userInfo.get("operation").toString())) {
				String id = UUIDUtil.getUUID();//新增用户，生成id
				userInfo.put("id", id);
			}
			/*
			 * 在进行更新操作时，用户是可以不输入密码的（不修改密码）
			 * 则此时不更新已有的密码,userInfo中的password保持为空即可
			 * 假如密码不为空，则userInfo中的password需要加密
			 */
			if( !StringUtils.isEmpty(userInfo.get("password").toString()) ) {
				/*密码加密*/
				String algorithmName = "SHA-1";  
				String password = userInfo.get("password").toString();  
				String salt = userInfo.get("id").toString()+"sqlt_yeqiu";    
				int hashIterations = 5;  			  
				SimpleHash hash = new SimpleHash(algorithmName, password, salt, hashIterations);  
				String encodedPassword = hash.toHex();  
				userInfo.put("password", encodedPassword);
			}
			int row = 0;
			if( "add".equals(userInfo.get("operation").toString())) {//新增
				row = userDao.addUser(userInfo);
			}else if( "update".equals(userInfo.get("operation").toString())) {//更新
				row = userDao.updateUser(userInfo);
			}
			if( row > 0 ) {
				result = new ResponseResult(ResponseResult.STATE_OK,"成功！");
				return result;
			}else {
				result = new ResponseResult(ResponseResult.STATE_FAIL,"数据库执行出错！");
				return result;
			}			
		} catch (Exception e) {
			result = new ResponseResult(ResponseResult.STATE_FAIL,"服务器出了差错！！");
			e.printStackTrace();
			return result;
		}	
	}
    /**
     * 删除用户
     * @param userId
     * @return
     */
	public ResponseResult deleteUser(String userId) {
		ResponseResult result;
		try {
			int ok = userDao.deleteUser(userId);
			if( ok > 0 ) {
				result = new ResponseResult(ResponseResult.STATE_OK,"用户已删除！");
				return result;
			}else {
				result = new ResponseResult(ResponseResult.STATE_FAIL,"数据库执行出错！");
				return result;
			}
		} catch (Exception e) {
			result = new ResponseResult(ResponseResult.STATE_FAIL,"服务器出了差错！！");
			e.printStackTrace();
			return result;
		}		
	}
	/**
	 * 获取用户信息（用于修改用户信息）
	 * @param userId
	 * @return
	 */
	public Map<String,String> getUserInfo(String userId){
		try {
			return userDao.getUserByuserId(userId);
		} catch (Exception e) {		
			e.printStackTrace();
			return null;
		}
	}
}
