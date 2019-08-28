package com.yeqiu.sys.webservice;

import org.springframework.stereotype.Component;

/**
 * @author 陆昌
 * @time 创建于2019年1月21日上午9:30:20
 * 说明：
 */
@Component("webservice")
public class Webservice {
	public String test(){
		return "test OK";	
	}
}
