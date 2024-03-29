package com.yeqiu.sys.log.aspect;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.yeqiu.sys.common.util.UUIDUtil;
import com.yeqiu.sys.log.annotation.Log;
import com.yeqiu.sys.log.service.LoggerService;

import eu.bitwalker.useragentutils.UserAgent;

/**
 * @author 陆昌
 * @time 2019年6月4日上午9:45:48
 * 说明：日志切面，记录日志信息
 */
@Component
@Aspect
public class LogAspect {
	private final static Logger logger = Logger.getLogger(LogAspect.class);	
	/*日志信息，这条信息分三个阶段赋值：1、执行前 2、异常时 3、执行后*/
	private  Map<String,Object> logInfo = null;
	@Autowired LoggerService service;
	/**
	 * 前置通知，在某个方法执行之前,执行before()
	 * 前提是该方法上有@Log注解
	 * @param point
	 */
	@Before("execution(* com.five.*.*.controller.*.*(..))")
	public void before( JoinPoint  point ) {
		logInfo = new HashMap<String,Object>();
    	try {       	
        	//获取方法名
            String methodName=point.getSignature().getName();
            logInfo.put("methodName", methodName);
            //获取参数列表
            List<Object> parameter = Arrays.asList(point.getArgs());
            logInfo.put("parameter", parameter.toString());
            
            String TargetClassName = point.getTarget().getClass().getName();//目标类名
            /*通过反射获取注解内容*/
        	Class<?> targetClass = Class.forName(TargetClassName);//目标类
        	Method[] methods = targetClass.getMethods();//目标类中所有方法
        	for( Method method : methods ) {
        		if( method.getName().equals(methodName) ) {
        			Log log = method.getAnnotation(Log.class);
        			 logInfo.put("operationType", log == null ?"":log.operationType());
        			 logInfo.put("operationName", log == null ?"":log.operationName());        	
        			break;
        		}
        	}
		} catch (ClassNotFoundException e) {
			logger.error("日志记录出错", e);
		}      
	}
	/**
	 * 执行方法结束前执行，如果出现异常则不执行
	 * @param point
	 */
	@AfterReturning("execution(* com.five.*.*.controller.*.*(..))")
	public void affterReturnning( JoinPoint  point ) {
		logInfo.put("id", UUIDUtil.getUUID());
		setUserInfo(null);
		logger.debug(logInfo);
		service.saveLog(logInfo);
	}
	/**
	 * 执行方法抛出异常时执行
	 * @param point
	 * @param e
	 */
	@AfterThrowing(pointcut="execution(* com.five.*.*.controller.*.*(..))",throwing="e")
	public void exception( JoinPoint point ,Throwable e) {
		logInfo.put("id", UUIDUtil.getUUID());
		logger.error("方法执行出错", e);
		setUserInfo(e);		
		service.saveLog(logInfo);
	}
	/**
	 * 获取登录人信息，并且存到 logInfo
	 * @param e 异常信息
	 */
	private void setUserInfo(Throwable e) {
		logInfo.put("exception", e == null ?"":e.getMessage());//异常信息		
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();  
		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent")); 
		StringBuilder sb = new StringBuilder("操作系统：");
		sb.append(userAgent.getOperatingSystem().getName());
		sb.append("，浏览器：");
		sb.append(userAgent.getBrowser().getName());
		sb.append("，版本："+userAgent.getBrowserVersion());	
    	logInfo.put("requestDevice",sb.toString() ); //客户端设备信息  	   
		logInfo.put("requestIp", request.getRemoteAddr());   			
		Subject subject = SecurityUtils.getSubject();
    	if( subject.getPrincipal() != null ) {
    		@SuppressWarnings("unchecked")
			Map<String,String> user = (Map<String, String>) subject.getPrincipal();
    		logInfo.put("createPerson", user.get("REAL_NAME"));
    		logInfo.put("createPersonId", user.get("ID"));   	    		   	
    	}else {
    		logInfo.put("createPerson", "匿名用户");
    		logInfo.put("createPersonId", "");   		
    	}
	}
}
