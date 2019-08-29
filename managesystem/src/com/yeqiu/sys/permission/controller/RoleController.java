package com.yeqiu.sys.permission.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yeqiu.sys.common.util.CommonUtils;
import com.yeqiu.sys.common.util.ResponseResult;
import com.yeqiu.sys.permission.service.RoleService;
import com.yeqiu.sys.user.realm.UserRealm;

/**
 * 
 * @author LC
 *创建时间：2019年8月29日下午3:55:47
 *说明：基于资源的访问控制器，权限集合在角色中
 */
@Controller
@RequestMapping("role")
public class RoleController extends ApplicationObjectSupport{
	@Autowired RoleService service;
	/**
	 * 获取角色列表
	 * @param limit 单页记录数
	 * @param offset 当前页码数
	 * @param sort 排序字段（依据）
	 * @param sortOrder 排序方式（asc-升序排序；desc-降序排序）
	 * @return
	 */
	@RequestMapping("get_list")
	@ResponseBody
	public Map<String,Object> getRoleList(int limit,int offset,String sort,String sortOrder){
		try {
			return service.getRoleList(limit, offset, sort, sortOrder);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	/**
	 * 添加角色
	 * @param roleInfo 角色信息
	 * @return 添加结果
	 */
	@RequestMapping("add")
	@ResponseBody
	public ResponseResult addRole(@RequestBody Map<String,String> roleInfo) {		
		try {
			SecurityUtils.getSubject().checkRole("admin");
			return service.addRole(roleInfo);
		} catch (AuthorizationException e) {
			return new ResponseResult(ResponseResult.STATE_FAIL,"对不起，您无此操作权限");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseResult(ResponseResult.STATE_FAIL,"对不起，服务器出了差错");
		}				
	}
	/**
	 * 删除角色
	 * @param id 角色id 
	 * @return
	 */
	@RequestMapping("delete")
	@ResponseBody
	public ResponseResult deleteRole(String id) {		
		if( id== null || "".equals(id) ) {
			return  new ResponseResult(ResponseResult.STATE_OK,"获取不到操作id，请检查");
		}
		try {
			SecurityUtils.getSubject().checkRole("admin");
			return service.deleteRole(id);	
		} catch (Exception e) {
			return new ResponseResult(ResponseResult.STATE_FAIL,"对不起，您无此操作权限");
		}						
	}
	/**
	 * 获取操作权限列表
	 * @param id id
	 * @return
	 */
	@RequestMapping(value="operation_list",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getOperationList(String id) {
		try {
			if( id== null || "".equals(id) ) {
				id="-1";
			}
			return service.getOperationList(id.trim());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 添加操作权限
	 * @param operationInfo 操作权限信息
	 * @return
	 */
	@RequestMapping(value="operation_add",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ResponseResult addOperation(@RequestBody Map<String,String> operationInfo) {
		boolean check = CommonUtils.checkMapHasNullValue(operationInfo);
		if( check ) {
			return new ResponseResult(ResponseResult.STATE_FAIL,"参数信息不完整，请检查");	
		}
		try {
			SecurityUtils.getSubject().checkRole("admin");
			return service.addOperation(operationInfo);		
		} catch (Exception e) {
			return new ResponseResult(ResponseResult.STATE_FAIL,"对不起，您无此操作权限");
		}					
	}
	/**
	 * 通过id删除操作权限
	 * @param id
	 * @return
	 */
	@RequestMapping(value="operation_del",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ResponseResult deleteOperation( String id) {
		if( id == null || "".equals(id) ) {
			return new ResponseResult(ResponseResult.STATE_FAIL,"获取不到操作id，请检查");	
		}	
		try {
			SecurityUtils.getSubject().checkRole("admin");
			return service.deleteOperation(id);	
		} catch (Exception e) {
			return new ResponseResult(ResponseResult.STATE_FAIL,"对不起，您无此操作权限");
		}				
	}
	/**
	 * 为角色授权
	 * @param authorizeInfo 授权信息
	 * @return
	 */
	@RequestMapping(value="authorize",produces="application/json;charset=UTF-8")
	@ResponseBody
	@ExceptionHandler()
	public ResponseResult authorize(@RequestBody List<Map<String,String>> authorizeInfo) {
		if( authorizeInfo == null || authorizeInfo.size() == 0 ) {
			return new ResponseResult(ResponseResult.STATE_FAIL,"错误，请给角色至少授予一种权限");	
		}
		try {
			SecurityUtils.getSubject().checkRole("admin");
			/*
			 * 用户的授权信息只有在调用到了需要验证的方法时才会初始化，初始化完成就存在缓存中，下一次需要则直接从缓存中读取
			 * 通过继承ApplicationObjectSupport获取spring管理的userRealm（用户安全信息域），
			 * 调用userRealm的方法删除缓存中的授权信息，当缓存中没有授权信息时，
			 * shiro会自动再从realm中获取授权信息，保证新的授权信息及时生效
			 * */
			((UserRealm) getApplicationContext().getBean("userRealm")).clearAllCachedAuthorizationInfo();
			return service.authorize(authorizeInfo);	
		} catch (Exception e) {
			return new ResponseResult(ResponseResult.STATE_FAIL,"对不起，您无此操作权限");
		}		
	}
	/**
	 * 获取角色的授权信息
	 * @param roleId 角色id
	 * @return
	 */
	@RequestMapping(value="already_authorize",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ResponseResult getAuthorizeInfo(String roleId) {
		try {
			if( roleId == null || "".equals(roleId) ) {
				return new ResponseResult(ResponseResult.STATE_FAIL,"错误，角色id不能为空");	
			}
			return service.getAuthorizeInfo(roleId);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseResult(ResponseResult.STATE_FAIL,"服务器出了点差错");	
		}
	
	}
	
	@ResponseBody
	@RequestMapping(value="findRolesByUserId")
	public List<Map<?, ?>> findRolesByUserId(String id){
		return service.findRolesByUserId(id);
	}
	
}
