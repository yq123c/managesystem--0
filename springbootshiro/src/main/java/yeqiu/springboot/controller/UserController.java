package yeqiu.springboot.controller;

import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import yeqiu.springboot.service.UserService;

/**
 * 
 * @author LC
 *创建时间：2019年8月5日上午11:57:32
 *
 */
@Controller
public class UserController {
	@Autowired UserService userService;
	@RequestMapping("doLogin")
	 public Object doLogin(@RequestParam String username, @RequestParam String password) {
	        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
	        Subject subject = SecurityUtils.getSubject();
	        try {
	            subject.login(token);
	        } catch (IncorrectCredentialsException ice) {
	            return "密码错误!";
	        } catch (UnknownAccountException uae) {
	            return "用户名错误!";
	        }

	        Map<String, Object> user = userService.findUserByName(username);
	        subject.getSession().setAttribute("user", user);
	        return "index";
	    }
}
