package com.yeqiu.sys.log.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author 陆昌
 * @time 2019年6月4日下午3:13:10
 * 说明：日志服务持久层
 */
@Repository
public interface LoggerDao {
	/**
	 * 保存日志
	 * @param logInfo 日志信息
	 * @return 生效的行数
	 */
	public Integer saveLog(@Param("logInfo")Map<String,Object> logInfo);
}
