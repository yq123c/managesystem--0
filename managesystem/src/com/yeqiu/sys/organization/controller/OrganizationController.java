package com.yeqiu.sys.organization.controller;

import java.util.Map;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yeqiu.sys.common.util.ResponseResult;
import com.yeqiu.sys.log.annotation.Log;
import com.yeqiu.sys.organization.service.OrganizationService;
/**
 * 
 * @author 陆昌
 *创建于：2019年8月25日上午8:28:36
 * 说明：
 */
@Controller
public class OrganizationController {
	@Autowired OrganizationService service;
	
	@Log(operationType="view",operationName="查看所有机构")
	@RequestMapping(value="get_unit_info",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getOrganizationInfo(String id) {
		//页面首次加载传上来的id是空的
		if( id == null || "".equals(id)) {
			id="-1";//初始化根机构id
		}	
		return  service.getUnitInfoById(id.trim());		
	}
		
	@RequestMapping(value="delete_duty",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ResponseResult deleteDuty(String id) {		
		ResponseResult returnResult = null;
		Subject subject = SecurityUtils.getSubject();
		try {
			//subject.checkPermission("sys:unit:delete");
			returnResult = service.deleteUnitById(id);
		}catch (AuthorizationException e) {
			returnResult = new ResponseResult(ResponseResult.STATE_FAIL,"对不起，您无此操作权限");
		}catch (Exception e) {
			e.printStackTrace();
			returnResult = new ResponseResult(ResponseResult.STATE_FAIL,"服务器除了点差错！！");
		}
		return returnResult;	
	}
	
	@RequestMapping(value="add_unit",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ResponseResult addDuty(@RequestBody Map<String,String> dutyInfo) {		
		ResponseResult returnResult = null;
		/*做些参数判断*/
		boolean ok = true;
		if(dutyInfo != null) {
			for( String key : dutyInfo.keySet() ) {
				if( "name".equals(key) || "org_code".equals(key) || "org_type".equals(key) ) {
					if( dutyInfo.get(key) == null || "".equals(dutyInfo.get(key)) ) {						
						ok = false;
						break;
					}
				}
			}
		}
		if( !ok ) {
			return new ResponseResult(ResponseResult.STATE_FAIL,"*号是必填项，请确认填写完整！");
		}
		Subject subject = SecurityUtils.getSubject();
		try {
			//subject.checkPermission("sys:unit:add");
			returnResult = service.addDuty(dutyInfo);
			return returnResult;
		} catch (Exception e) {
			returnResult = new ResponseResult(ResponseResult.STATE_FAIL,"对不起，您无此操作权限");
			return returnResult;
		}		
	}
	@RequestMapping("test")
	@RequiresRoles("user")
	public void test() {
		Subject subject = SecurityUtils.getSubject();
		System.out.println(subject);
		System.out.println(subject.getPrincipal());
		System.out.println(subject.isPermitted("delete"));
		System.out.println(subject.hasRole("admin"));
		subject.checkPermission("sys:unit:delete");
	}
}
