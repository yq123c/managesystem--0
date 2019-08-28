package com.yeqiu.sys.user.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yeqiu.sys.common.util.ResponseResult;
/**
 * 
 * @author 陆昌
 * @time 创建于2019年8月23日上午10:29:21
 * 说明：
 */
@Repository
public interface UserDao {
		/**
		 * 通过登录名查找用户信息（登录名不可重复）
		 * @param username 登录名
		 * @return
		 */
		public Map<String,String> getUserByLoginName(@Param("loginName")String loginName);
		/**
		 * 通过id查找用户信息
		 * @param userId 用户ID
		 * @return
		 */
		public Map<String,String> getUserByuserId(@Param("userId")String userId);
	    /**
	     * 通过用户id查找用户信息
	     * @param id 用户id
	     * @return
	     */
	    public Map<String,String> findById(String id);	    	  
	    /**
	     * 通过登录名查找用户角色
	     * @param loginName 用户登录名
	     * @return 角色列表
	     */
	    public List<Map<String,String>> findRoles(@Param("loginName")String loginName);
	    /**
	     * 通过角色id查找用户权限
	     * @param roleId 角色id
	     * @return
	     */
	    public List<Map<String,String>> findPermissions(String roleId);	    	    
	    /**
	     * 根据用户id获取用户的角色id列表
	     * @param id 用户id
	     * @return 角色id列表
	     */
	    public Set<String> getRolesByUserId(String id);
	    /**
	     *  添加用户角色信息
	     * @param userId 用户id
	     * @param roleId 角色id
	     * @return 生效的行数
	     */
	    public Integer addUserRoleInfo(@Param("userId")String userId,@Param("roleId")String roleId);
	    /**
	     *  删除用户角色信息
	     * @param userId 用户id
	     * @param roleId 角色id
	     * @return 受影响的行数
	     */
	    public Integer deleteUserRoleInfo(@Param("userId")String userId,@Param("roleId")String roleId);
	    /**
	     * 获取用户列表
	     * @param order 排序方式
	     * @param sort 排序字段
	     * @param offset 偏移量
	     * @param limit 分页数量
	     * @return
	     */
	    List<Map<String,String>> getUserList(@Param("order")String order,@Param("sort")String sort,@Param("offset")String offset,@Param("limit")String limit ,@Param("name")String name,@Param("login_name")String login_name);
	    /**
	     * 获取用户列表的总记录数
	     * @return
	     */
	    public Integer getTotal(@Param("name")String name,@Param("login_name")String login_name);
	    /**
	     * 通过父级id获取地址信息列表
	     * @param parent_id 
	     * @return
	     */
	    public List<Map<String,String>> getAddressInfoByParentId(@Param("parent_id")String parent_id);
	    /**
	              * 添加新用户
	     * @param userInfo 用户信息
	     * @return
	     */
	    public Integer addUser(@Param("userInfo") Map<String, String> userInfo);
	    /**
	     * 根据ID删除用户
	     * @param userId
	     * @return
	     */
		public Integer deleteUser(@Param("userId")String userId);
		/**
		 * 更新用户信息
		 * @param userInfo 用户新信息
		 * @return
		 */
		public Integer updateUser(@Param("userInfo")Map<String, String> userInfo);
}
