package com.yeqiu.sys.user.controller;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yeqiu.sys.common.util.ResponseResult;
import com.yeqiu.sys.common.util.UUIDUtil;
/**
 * 
 * @author 陆昌
 *创建于：2019年8月17日下午12:52:02
 * 说明：登陆
 */
@Controller
@RequestMapping("web")
public class LoginController {
	private Logger logger = Logger.getLogger(LoginController.class);
	@RequestMapping("login")
	@ResponseBody
	public ResponseResult login(String username,String password) {
		ResponseResult responseResult = null;
		 try {
			Subject subject = SecurityUtils.getSubject();
	        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
	        subject.login(token);
	        responseResult = new ResponseResult(ResponseResult.STATE_OK, "成功登陆");
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			responseResult = new ResponseResult(ResponseResult.STATE_FAIL, "登陆出错了");
		}      		
		return responseResult;		
	}
	public static void main(String[] args) {
		String algorithmName = "SHA-1";  
		String id = UUIDUtil.getUUID();
		String password = "admin";  
		String salt = id+"sqlt_yeqiu";    
		int hashIterations = 5;  
		  
		SimpleHash hash = new SimpleHash(algorithmName, password, salt, hashIterations);  
		String encodedPassword = hash.toHex();  
		System.out.println(id);
		System.out.println(encodedPassword);
	}
	/**
	 * 登录信息获取
	 * @return
	 */
	@RequestMapping("get_loginInfo")
	@ResponseBody
	public ResponseResult userInfo(){
		ResponseResult result = null;
		try {
			Map<String, String>  user = (Map<String, String>) SecurityUtils.getSubject().getPrincipal();
			user.remove("password");			
			result = new ResponseResult(ResponseResult.STATE_OK, user ) ;	
			return result;		
		} catch (Exception e) {
			result = new ResponseResult(ResponseResult.STATE_FAIL,"信息获取出错");
			return result;		
		}						
	}
	/**
	 * 退出登录
	 * @return
	 */
	@RequestMapping("login_out")
	@ResponseBody
	public ResponseResult loginOut(){
		ResponseResult result = null;
		try {
			SecurityUtils.getSubject().logout();			
			result = new ResponseResult(ResponseResult.STATE_OK) ;	
			return result;		
		} catch (Exception e) {
			result = new ResponseResult(ResponseResult.STATE_FAIL,"退出登录时出了点差错");
			return result;		
		}						
	}
}
