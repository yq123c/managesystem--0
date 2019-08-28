package com.yeqiu.sys.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据操作服务工作
 * 	1、db数据的操作调用
 * 	2、返回数据的封装
 * @author jay.d	2018-06-19
 *
 */
public class SqlDataHandle {
	
	/**
	 * 数据处理----将列表map中的key转为小写
	 * @param list
	 * @return
	 */
	public static List<Map<String,Object>> listDataHandle(List<Map<String,Object>> list){
		try{
			if(list==null || list.isEmpty()){
				return null;
			}
			List<Map<String,Object>> new_list = new ArrayList<Map<String,Object>>();
			for(Map<String,Object> map : list){
				new_list.add(DataHandle(map));
			}
		
			return new_list;
		}catch(Exception e){
			throw e;
		}
	}
	
	
	/**
	 * 数据处理----将map中的key转为小写
	 * @param map
	 * @return
	 */
	public static Map<String,Object> DataHandle(Map<String,Object> map){
		try{
			if(map==null || map.isEmpty()){
				return null;
			}
			Map<String,Object> new_map = new HashMap<String,Object>();
			for(String key : map.keySet()){
				new_map.put(key.toLowerCase(), map.get(key));			
			}
			if( new_map.get("isparent") != null ) {//此判断仅对机构有效
				new_map.put("isParent", Integer.parseInt(new_map.get("isparent").toString()) > 0? true:false);
			}
			return new_map;
		}catch(Exception e){
			throw e;
		}
	}
}
