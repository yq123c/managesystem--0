package com.yeqiu.sys.common.util;

import java.util.UUID;

/**
 * @author 陆昌
 * @time 2019年5月13日上午10:45:09
 * 说明：UUID工具
 */
public class UUIDUtil {
	/**
	 * 获取UUID
	 * @return 返回32位的UUID
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
