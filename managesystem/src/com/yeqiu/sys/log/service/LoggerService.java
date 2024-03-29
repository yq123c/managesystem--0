package com.yeqiu.sys.log.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeqiu.sys.log.dao.LoggerDao;



/**
 * @author 陆昌
 * @time 2019年6月4日下午2:37:02
 * 说明：日志服务
 */
@Service
public class LoggerService {
	@Autowired LoggerDao dao;
	/**
	 * 保存日志信息
	 * @param logInfo 日志信息
	 */
	public void saveLog(Map<String,Object> logInfo) {		
		try {
			dao.saveLog(logInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
