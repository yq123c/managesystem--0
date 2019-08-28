package com.yeqiu.sys.user.realm;

import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;

import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.yeqiu.sys.user.service.UserService;

/**
 * @author 陆昌
 * @time 2019年4月25日上午9:11:50
 * 说明：用户安全数据源的实现
 */
public class UserRealm extends AuthorizingRealm {
	private Logger logger = Logger.getLogger(UserRealm.class);
	@Autowired UserService userService;
	  public void setUserService(UserService userService) {
	        this.userService = userService; 
	    }
	  	/**
	  	 * 用户授权方法
	  	 */
	    @Override
	    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
	    	Map<?,?> user = (Map<?,?>)principals.getPrimaryPrincipal();
	    	logger.info("user:"+user.toString());
	        logger.info("username:"+user.get("login_name"));
	        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
	        /*查询用户的所有角色信息*/
	        Map<String,Set<String>> query = userService.findRolesAndPermissions(user.get("login_name").toString());	        
	        if( query != null ) {
	        	authorizationInfo.setRoles(query.get("roles"));//角色授权  	      
	        	authorizationInfo.setStringPermissions(query.get("permissions")); 
	        }
	        logger.info("roles:"+authorizationInfo.getRoles());
	        logger.info("permissions:"+authorizationInfo.getStringPermissions());
	        return authorizationInfo;
	    }
	    /**
	     * 在登录之前执行doGetAuthenticationInfo方法
	     * 在该方法中完成身份认证
	     * 成功：返回认证信息 AuthenticationInfo
	     */
	    @Override
	    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
	        String username = (String)token.getPrincipal();//登录名
	        
	        Map<String, String> user = userService.getUserByLoginName(username);	       
	        if(user == null || !username.equals(user.get("login_name"))) {
	            throw new UnknownAccountException();
	        }      
	        return new SimpleAuthenticationInfo(user,user.get("password"),ByteSource.Util.bytes(user.get("id")+"sqlt_yeqiu"), getName());
	    }
	    
	    @Override
	    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
	        super.clearCachedAuthorizationInfo(principals);
	    }

	    @Override
	    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
	        super.clearCachedAuthenticationInfo(principals);
	    }

	    @Override
	    public void clearCache(PrincipalCollection principals) {
	        super.clearCache(principals);
	    }
	    /**
	     * 删除授权信息（缓存）
	     */
	    public void clearAllCachedAuthorizationInfo() {
	        getAuthorizationCache().clear();
	    }

	    public void clearAllCachedAuthenticationInfo() {
	        getAuthenticationCache().clear();
	    }

	    public void clearAllCache() {
	        clearAllCachedAuthenticationInfo();
	        clearAllCachedAuthorizationInfo();
	    }
	   
}
