/**
 * 
 */
package yeqiu.springboot.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import yeqiu.springboot.service.TestService;

/**
 * @author 陆昌
 * @time 2019年6月21日上午11:03:06
 * 说明：
 */
@Controller
//@EnableAutoConfiguration
public class TestController {
	@Autowired TestService testService;
	
	@RequestMapping("/hello")
	@ResponseBody
	public String hello() {
		return "hello 叶秋同学，你好";
	}
	@RequestMapping("/index")
	public String index(Model model) {
		Map<String,Object> user = testService.getUserInfo();
		System.out.println(user);
		model.addAttribute("name", user.get("username").toString());
		model.addAttribute("age", "22");	
		try {//在连接redis时，需要注意捕获异常，否则出错比较难排查
			model.addAttribute("info", testService.testRedis().toString());	
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return "index";
	}
}
