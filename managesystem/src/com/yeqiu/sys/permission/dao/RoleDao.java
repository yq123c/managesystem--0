package com.yeqiu.sys.permission.dao;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
/**
 * @author 陆昌
 * @time 2019年5月20日下午2:43:19
 * 说明：角色权限控制持久层
 */
@Repository
public interface RoleDao {
	/**
	 * 	获取所有的角色列表
	 * @param limit 每次获取的数据条数
	 * @param offset 当前页码
	 * @param sort 排序字段
	 * @param sortOrder 排序放肆
	 * @return  角色列表
	 */
	public List<Map<String,Object>> getRoleList(@Param("limit")int limit,@Param("offset")int offset,@Param("sort")String sort,@Param("sortOrder")String sortOrder);
	/**
	 * 获取角色记录总数
	 * @return 角色记录总数
	 */
	public Integer getRecordCountOfRoles();
	/**
	 * 添加角色
	 * @param roleInfo 角色信息
	 * @return 生效的行数
	 */
	public Integer addRole(@Param("roleInfo") Map<String,String> roleInfo);
	/**
	 * 通过角色id删除角色
	 * @param id 角色id
	 * @return 受影响的行数
	 */
	public Integer deleteRole(String id);
	/**
	 * 获取操作权限列表
	 * @param id
	 * @return
	 */
	public List<Map<String,Object>> getOperationList(String id);	
	/**
	 * 添加操作权限
	 * @param operationInfo 操作权限信息
	 * @return 生效的行数
	 */
	public Integer addOperation( @Param("operationInfo") Map<String,String> operationInfo );
	/**
	 * 根据操作ID删除操作记录
	 * @param id 操作ID
	 * @return 受影响的行数
	 */
	public Integer deleteOperation(String id);
	/**
	 * 根据角色id获取授予该角色的权限id
	 * @param id 角色id
	 * @return 权限id 列表
	 */
	public List<Map<String,String>> getRolePermission(String id);
	/**
	 * 添加授权信息
	 * @param authorizeInfo 授权信息
	 * @return 生效的行数
	 */
	public Integer addAuthorizeInfo(@Param("authorizeInfo")Map<String,String > authorizeInfo);
	
	/**
	 * 删除授权信息
	 * @param authorizeInfo 授权信息
	 * @return 受影响的行数
	 */
	public Integer deleteAuthorizeInfo(@Param("authorizeInfo")Map<String,String > authorizeInfo);
	
	public List<Map<?,?>> findRolesByUserId(String id);
}
