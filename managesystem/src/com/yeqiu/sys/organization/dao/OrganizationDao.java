package com.yeqiu.sys.organization.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author 陆昌
 *创建于：2019年8月25日上午8:28:22
 * 说明：
 */
@Repository
public interface OrganizationDao {
	/**
	 * 根据id获取此id的下级机构信息
	 * @param id 机构id
	 * @return 下级机构信息列表
	 */
	public List<Map<String,Object>> getUnitInfoById(@Param("id")String id);
	/**
	 * 根据机构的id删除该机构
	 * @param id 机构id
	 * @return 生效的行数
	 */
	public Integer deleteUnitById(String id);
	/**
	 * 添加一个新的机构
	 * @param dutyInfo 新机构信息
	 * @return 生效的行数
	 */
	public Integer addUnit(@Param("dutyInfo")Map<String,String> dutyInfo);
	
	/**
	 * 根据机构的id查询机构名
	 * @param id
	 * @return
	 */
	public String getNameById(@Param("id")String id);
}
