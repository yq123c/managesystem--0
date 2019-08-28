package com.yeqiu.sys.common.util;

import java.util.Map;
/**
 * 返回前端的相应数据实体类
 * @author 
 * @time 2019年5月7日下午5:03:59
 * 说明：保证返回的数据格式有一个统一的规范
 */
public class ResponseResult {
	public static final int STATE_OK = 1;//状态常量 -- 成功
	public static final int STATE_FAIL = 0;//状态常量 -- 失败
	private Integer state;//状态码  成功（1） 失败（0）
	private String messages;//信息
	private Map<String,?> data;
	
	public ResponseResult() {
		super();
	}	
	//构造器 ,只返回状态
	public ResponseResult(Integer state) {
		super();
		this.state = state;
	}
	//构造器，返回状态加返回数据
	public ResponseResult(Integer state, Map<String,?> data) {
		super();
		this.state = state;
		this.data = data;
	}
	//构造器，返回状态加失败信息
	public ResponseResult(Integer state, String messages) {
		super();
		this.state = state;
		this.messages = messages;
	}
	//发生异常时，返回状态加异常信息
	public ResponseResult(Throwable throwable ) {
		super();
		this.state = STATE_FAIL;
		this.messages = throwable.getMessage();
	}	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getMessages() {
		return messages;
	}
	public void setMessages(String messages) {
		this.messages = messages;
	}
	public Map<?, ?> getData() {
		return data;
	}
	public void setData(Map<String,Object> data) {
		this.data = data;
	}
	
}
