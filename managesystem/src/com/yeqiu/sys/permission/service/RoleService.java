package com.yeqiu.sys.permission.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeqiu.sys.common.util.JsonUtil;
import com.yeqiu.sys.common.util.ResponseResult;
import com.yeqiu.sys.common.util.SqlDataHandle;
import com.yeqiu.sys.common.util.UUIDUtil;
import com.yeqiu.sys.permission.dao.RoleDao;

/**
 * 
 * @author LC
 *创建时间：2019年8月29日下午4:52:41
 *说明：角色权限控制service层
 */
@Service
public class RoleService {
	private static Logger logger = Logger.getLogger(RoleService.class);
	@Autowired RoleDao roleDao;
	/**
	 * 获取所有的角色列表
	 * @return 角色列表
	 */
	public Map<String,Object> getRoleList(int limit,int offset,String sort,String sortOrder){
		//limit需要加上offset，方便sql查询处理
		List<Map<String,Object>>roleList = roleDao.getRoleList(limit+offset, offset, sort, sortOrder);		
		Map<String,Object> list = new HashMap<String,Object>();
		list.put("total", roleDao.getRecordCountOfRoles());
		list.put("rows", SqlDataHandle.listDataHandle(roleList));		
		return list;
	}
	/**
	 * 添加角色
	 * @param roleInfo 角色信息
	 * @return
	 */
	public ResponseResult addRole( Map<String,String> roleInfo) {
		ResponseResult returnResult = null;		
		roleInfo.put("id", UUIDUtil.getUUID());
		try {
			int res = roleDao.addRole(roleInfo);
			if( res > 0 ) {
				returnResult = new ResponseResult(ResponseResult.STATE_OK);
			}else {
				returnResult = new ResponseResult(ResponseResult.STATE_FAIL,"角色添加失败");
			}
		} catch (Exception e) {
			String message = "数据库执行出错！！";
			returnResult = new ResponseResult(ResponseResult.STATE_FAIL,message);
			logger.error(message, e);
			return returnResult;	
		}
		return returnResult;		
	}
	/**
	 * 通过角色id删除角色
	 * @param id 角色id
	 * @return
	 */
	public ResponseResult deleteRole(String id) {	
		ResponseResult returnResult = null;
		try {
			int res = roleDao.deleteRole(id);
			if( res > 0 ) {
				returnResult = new ResponseResult(ResponseResult.STATE_OK);	
			}else {
				returnResult = new ResponseResult(ResponseResult.STATE_FAIL,"角色删除失败");
			}
		} catch (Exception e) {
			String message = "数据库执行出错！！";
			returnResult = new ResponseResult(ResponseResult.STATE_FAIL,message);
			logger.error(message, e);
			return returnResult;	
		}		
		return returnResult;	
	}
	/**
	 * 获取操作权限列表
	 * @param id
	 * @return
	 */
	public List<Map<String,Object>> getOperationList(String id) {
		try {
			List<Map<String,Object>> operationList = roleDao.getOperationList(id);	
			return operationList;
		} catch (Exception e) {
			logger.error("出错了", e);
			return null;
		}
	
	}
	/**
	 * 添加操作权限
	 * @param operationInfo 操作权限信息
	 * @return
	 */
	public ResponseResult addOperation(Map<String,String> operationInfo) {
		ResponseResult returnResult = null;
		operationInfo.put("id", UUIDUtil.getUUID());
		Object temp = SecurityUtils.getSubject().getPrincipal();
		try {
			int res = roleDao.addOperation(operationInfo);
			if( res > 0 ) {
				operationInfo.put("isParent", "true");
				returnResult = new ResponseResult(ResponseResult.STATE_OK,operationInfo);
			}else {
				returnResult = new ResponseResult(ResponseResult.STATE_FAIL,"对不起，操作失败");
			}
		} catch (Exception e) {
			returnResult = new ResponseResult(ResponseResult.STATE_FAIL,"服务器发生异常");
			logger.error(e);
			return returnResult;		
		}		
		return returnResult;		
	}
	/**
	 * 根据操作ID删除操作
	 * @param id 
	 * @return
	 */
	@Transactional
	public ResponseResult deleteOperation(String id) {
		ResponseResult returnResult = null;
		Set<String> idList = new HashSet<String>();
		idList.add(id);
		getChildOfOperation(idList, id);//获取所有的下级id
		int res = 0;
		for( String oId : idList ) {
			res += roleDao.deleteOperation(oId);
		}
		if( res > 0 ) {
			returnResult = new ResponseResult(ResponseResult.STATE_OK);
		}else {
			returnResult = new ResponseResult(ResponseResult.STATE_FAIL,"删除失败");
		}
		return returnResult;		
	}
	/**
	 * 获取某个操作下边的所有子操作id
	 * @param idList 返回的id列表
	 * @param id 操作id
	 */
	private void getChildOfOperation(Set<String> idList,String id) {	
		List<Map<String,Object>> operationList = roleDao.getOperationList(id);//获取下级
		if( operationList != null ) {
			for( Map<String,Object> o : operationList ) {
				idList.add(o.get("ID").toString());//添加id查询结果
				if( Integer.parseInt( o.get("ISPARENT").toString()) > 0 ) {//如果还有下级				
					getChildOfOperation(idList,o.get("ID").toString());//递归继续查询
				}
			}
		}		
	}
	/**
	 * 为角色授权
	 * @param authorizeInfo 授权信息
	 * @return
	 */
	@Transactional
	public ResponseResult authorize(List<Map<String,String>> authorizeInfo) {
		ResponseResult returnResult = null;
		try {
			String id = authorizeInfo.get(0).get("role_id").toString();//角色id
			/*已经存在的授权信息*/
			List<Map<String,String>> existAuthorizeInfo = roleDao.getRolePermission(id);
			for( Map<String,String> aboutToAdd : authorizeInfo  ) {		
				/*
				 * 判断将要授权的信息是否已经存在
				 */
				boolean exist = existAuthorizeInfo.contains(aboutToAdd);
				if( !exist ) {//如果不存在，则新增
					roleDao.addAuthorizeInfo(aboutToAdd);
				}else {
					/*
					 * 如果已经存在，把该记录在existAuthorizeInfo中移除
					 * 那么本次遍历完之后existAuthorizeInfo剩下的就是要删除的授权信息
					 */
					existAuthorizeInfo.remove(aboutToAdd);
				}							
			}
			for( Map<String,String> aboutToDelete : existAuthorizeInfo  ) {		
				roleDao.deleteAuthorizeInfo(aboutToDelete);//删除			
			}
			returnResult = new ResponseResult(ResponseResult.STATE_OK);
		} catch (Exception e) {
			logger.error("出错了", e);
			returnResult = new ResponseResult(ResponseResult.STATE_FAIL,"服务器出错了");
			return returnResult;		
		}		
		return returnResult;		
	}
	/**
	 * 获取角色的授权信息
	 * @param roleId 角色id
	 * @return
	 */
	public ResponseResult getAuthorizeInfo(String roleId) {
		ResponseResult returnResult =null;
		List<Map<String, String>> list= roleDao.getRolePermission(roleId);
		returnResult = new ResponseResult(ResponseResult.STATE_OK,JsonUtil.objectToJson(list));
		return returnResult;
		
	}
	
	public List<Map<?, ?>> findRolesByUserId(String id){
		return roleDao.findRolesByUserId(id);
	}
}
