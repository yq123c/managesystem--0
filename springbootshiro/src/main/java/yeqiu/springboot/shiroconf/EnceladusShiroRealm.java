package yeqiu.springboot.shiroconf;
import java.util.Map;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import yeqiu.springboot.service.UserService;



/**
 * @author LC
 *创建时间：2019年8月2日 下午3:27:58
 * shiro 数据源
 */
public class EnceladusShiroRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    /**
     * 验证权限
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String username = (String) principals.getPrimaryPrincipal();
        
        Map<String,Object> user = userService.findUserByName(username);
        //此处应该授权
		/*
		 * for (SysRole role : user.getRoles()) {
		 * authorizationInfo.addRole(role.getRole()); for (SysPermission permission :
		 * role.getPermissions()) {
		 * authorizationInfo.addStringPermission(permission.getName()); } }
		 */
        return authorizationInfo;
    }
    /**
     * 验证身份
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        System.out.println("准备验证："+username);
        Map<String,Object> user = userService.findUserByName(username);
        System.out.println(user);
        if (user == null) {
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user.get("username"), user.get("password"),
                ByteSource.Util.bytes(user.get("salt")), getName());
        return authenticationInfo;
    }

}
