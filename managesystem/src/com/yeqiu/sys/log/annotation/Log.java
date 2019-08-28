package com.yeqiu.sys.log.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 陆昌
 * @time 2019年6月3日下午4:49:00
 * 说明：自定义日志记录注解
 */
@Documented//注解声明，表明这个注解应该被 javadoc工具记录
@Target({ElementType.PARAMETER,ElementType.METHOD})//标识注解修饰范围：参数、方法
@Retention(RetentionPolicy.RUNTIME)//标识注解在运行时有效
public @interface Log {
	/**
	 * 操作类型，如：add、delete。默认是""
	 * @return String 
	 */
	public String operationType() default "";
	/**
	 * 具体的操作，如：添加用户、删除单位。默认是""
	 * @return
	 */
	public String operationName() default "";
}