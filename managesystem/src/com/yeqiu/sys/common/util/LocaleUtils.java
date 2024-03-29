package com.yeqiu.sys.common.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
/**
 * 地区工具类
 * @author 
 * @time 
 * 说明：Locale ：地区，用于获取地区信息（国际化）
 */
public class LocaleUtils {
	/**
	 * 根据servlet请求获取相应地区的数据源
	 * @param request servlet请求
	 * @return ResourceBundle 捆绑好的数据源
	 */
	public static ResourceBundle getResourceBundle(HttpServletRequest request){
		Locale locale = request.getLocale();
		String name = "ValidationMessages_" + locale.toString();//属性文件名初始化
		try {
			return ResourceBundle.getBundle(name,locale);
		} catch(MissingResourceException e){//默认中文
			return ResourceBundle.getBundle("ValidationMessages_zh_CN");
		}
	}
	
	
}
