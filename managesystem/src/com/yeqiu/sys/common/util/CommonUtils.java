package com.yeqiu.sys.common.util;

import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
/**
 * 通用工具
 * @author 陆昌
 * @time 2019年5月15日下午4:08:21
 * 说明：通用工具类，包含一些常用的方法
 */
public class CommonUtils {
	/**
	 * 获取验证出错的信息项
	 * @param binding 绑定的错误信息
	 * @return 错误信息
	 */
	public static String getErrors(BindingResult binding){
			 String str = "";
			 List<FieldError> fields = binding.getFieldErrors();
			 for (int i = 0; i < fields.size() ; i++){				
				 str += fields.get(i).getDefaultMessage() + "~";//将所有错误信息项以"~"分隔，便于接收者解析
			 }
			return str.substring(0, str.length() - 1);
	}
	
	/**
	 * 判断map 的 value 为 null 则返回 "" 
	 * @param map
	 * @param key
	 * @return
	 */
	public static String getMapValue(Map<?,?> map,String key){
		if(map == null){
			return null;
		}
		return map.get(key) != null ? map.get(key).toString() : "";
	}
	/**
	 * 判断map是否含有空值
	 * @param map 
	 * @return 是：true；否：false
	 */
	public static boolean checkMapHasNullValue(Map<String,String> map) {
		if( map != null ) {
			for(Object key :  map.keySet()){
				if( map.get(key) == null || "".equals(map.get(key)) ) {
					return true;
				}
			}
		}
		return false;		
	}
}
