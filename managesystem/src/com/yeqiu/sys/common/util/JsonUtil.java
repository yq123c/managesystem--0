package com.yeqiu.sys.common.util;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author 陆昌
 * @time 2019年5月7日下午5:19:34
 * 说明：json工具包
 */
public class JsonUtil {
	private final static Logger logger = Logger.getLogger(JsonUtil.class);
	/**
	 * java对象转json
	 * @param object 对象
	 * @return 
	 */
	public static String objectToJson(Object object) {
		try {
			Gson gson = new GsonBuilder().serializeNulls().create();//空值也参与序列化
			return gson.toJson(object);		
		} catch (Exception e) {
			logger.info("在转json的时候发生了错误", e);
		}
		return null;		
	}
}
