package com.yeqiu.sys.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis2.jaxws.description.xml.handler.ResSharingScopeType;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yeqiu.sys.common.util.ResponseResult;
import com.yeqiu.sys.user.service.UserService;

/**
 * @author 陆昌
 * @time 创建于2019年8月20日下午5:24:26
 * 说明：用户
 */
@Controller
@RequestMapping("web/user")
public class UserController {
	Logger logger = Logger.getLogger(UserController.class);
	@Autowired UserService service;
	
	@RequestMapping("list")
	@ResponseBody
	public Object getUserList(String order,String sort,String offset,String limit,String name,String login_name){
		Map<String,Object> user = new HashMap<String, Object>();
		user.put("total", service.getTotal(name, login_name));
		user.put("rows", service.getUserList(order, sort, offset, limit, name, login_name));
		return user;
		
	}
	@RequestMapping("address")
	@ResponseBody
	public Object getAddressInfoByParentId(String parent_id){
		try {
			if( "".equals(parent_id) || parent_id == null){
				logger.error("查询地址信息，parent_id不可为空");
				return null;
			}
			return service.getAddressInfoByParentId(parent_id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
	}
	@RequestMapping("add")
	@ResponseBody
	public ResponseResult addUser(@RequestBody Map<String, String> userInfo){
		ResponseResult result;
		try {			
			SecurityUtils.getSubject().checkRole("admin");
			result = service.addOrUpdateUser(userInfo);
			return result;
		}catch (AuthorizationException e) {
			result = new ResponseResult(ResponseResult.STATE_FAIL,"对不起，您无此操作权限！！");
			return result;
		}catch (Exception e) {
			result = new ResponseResult(ResponseResult.STATE_FAIL,"服务器出了差错！！");
			e.printStackTrace();
			return result;
		}	
	}
	@RequestMapping("delete")
	@ResponseBody
	public ResponseResult deleteUser( String userId){
		ResponseResult result;
		try {	
			SecurityUtils.getSubject().checkRole("admin");
			result = service.deleteUser(userId);
			return result;
		}catch (AuthorizationException e) {
			result = new ResponseResult(ResponseResult.STATE_FAIL,"对不起，您无此操作权限！！");
			return result;
		} catch (Exception e) {
			result = new ResponseResult(ResponseResult.STATE_FAIL,"服务器出了差错！！");
			e.printStackTrace();
			return result;
		}	
	}
	@RequestMapping("info")
	@ResponseBody
	public Map<String,String> getUserInfo(String userId){
		try {
			return service.getUserInfo(userId);
		} catch (Exception e) {		
			e.printStackTrace();
			return null;
		}
	}
	@RequestMapping("update")
	@ResponseBody
	public ResponseResult updateUser(@RequestBody Map<String, String> userInfo){
		ResponseResult result;
		try {			
			SecurityUtils.getSubject().checkRole("admin");
			result = service.addOrUpdateUser(userInfo);
			return result;
		}catch (AuthorizationException e) {
			result = new ResponseResult(ResponseResult.STATE_FAIL,"对不起，您无此操作权限！！");
			return result;
		}catch (Exception e) {
			result = new ResponseResult(ResponseResult.STATE_FAIL,"服务器出了差错！！");
			e.printStackTrace();
			return result;
		}	
	}
}
