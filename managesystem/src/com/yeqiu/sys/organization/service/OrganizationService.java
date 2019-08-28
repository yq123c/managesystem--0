package com.yeqiu.sys.organization.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeqiu.sys.common.util.JsonUtil;
import com.yeqiu.sys.common.util.ResponseResult;
import com.yeqiu.sys.common.util.SqlDataHandle;
import com.yeqiu.sys.common.util.UUIDUtil;
import com.yeqiu.sys.organization.dao.OrganizationDao;
/**
 * 
 * @author 陆昌
 *创建于：2019年8月25日上午8:28:52
 * 说明：
 */
@Service
public class OrganizationService {	
	@Autowired OrganizationDao dao;
	private final static Logger logger = Logger.getLogger(OrganizationService.class);
	public String getUnitInfoById(String id){
		List<Map<String,Object>> unitList = dao.getUnitInfoById(id);		
		String result = JsonUtil.objectToJson(SqlDataHandle.listDataHandle(unitList));
		return result;
	}
	/**
	 * 根据机构的id删除该机构
	 * @param id 机构id
	 * @return 生效的行数
	 */
	@Transactional
	public ResponseResult deleteUnitById(String id) {
		String error = null;//错误信息
		ResponseResult responseResult = null;//返回结果实体
		if( id == null || "".equals(id)) {
			error = "删除单位时出错了，原因：获取不到单位id";
			responseResult = new ResponseResult(ResponseResult.STATE_FAIL, error);
			logger.error(error);
			return responseResult;
		}	
		
		List<Map<String,Object>> tempUnitList = dao.getUnitInfoById(id);//需要删除的单位下级单位	
		Integer result = 0;
		if(tempUnitList != null) {
			List<Map<String,Object>> unitList = new ArrayList<Map<String,Object>>();
			/*获取所有的下属单位*/
			getSubordinateUnits(tempUnitList,unitList);
			/*遍历结果，删除单位*/
			for(Map<String,Object> unit : unitList) {				
				String underUnitId = unit.get("id").toString();
				result += dao.deleteUnitById(underUnitId);
			}			
		}
		result += dao.deleteUnitById(id);
		if( result > 0 ) {
			responseResult =new ResponseResult(ResponseResult.STATE_OK);
		}else {
			responseResult =new ResponseResult(ResponseResult.STATE_FAIL,"数据库执行出错！");
		}
		return responseResult;
	}
	private void getSubordinateUnits(List<Map<String,Object>> unitList,List<Map<String,Object>> returnUnits) {
		for(Map<String,Object> unit : unitList) {
			int hasNext = Integer.parseInt(unit.get("isparent").toString());//是否还有下级单位
			if( hasNext > 0 ){//有下级单位
				List<Map<String,Object>> tempUnitList = dao.getUnitInfoById(unit.get("id").toString());//获取该单位的下级单位列表
				/*递归调用，当某个单位没有下级单位时退出调用，因此删除的单位所包含下属单位越多，删除时间越长*/
				getSubordinateUnits(tempUnitList,returnUnits);
			}
			returnUnits.add(unit);
		}
	}
	public ResponseResult addDuty(Map<String,String> dutyInfo) {
		ResponseResult returnResult = null;
		try {
			String id = UUIDUtil.getUUID();//新增单位的id
			String parentIds = dutyInfo.get("parent_ids");
			if( parentIds == null ||  "".equals(parentIds)) {
				returnResult = new ResponseResult(ResponseResult.STATE_FAIL,"错误，获取不到父级id列表");
				return returnResult	;
			}
			String parentId = dutyInfo.get("parent_id");
			if( parentId == null ||  "".equals(parentId)) {
				returnResult = new ResponseResult(ResponseResult.STATE_FAIL,"错误，获取不到父级id");
				return returnResult	;
			}
			parentIds += ","+parentId;//新单位的父级id列表
			dutyInfo.put("parent_ids", parentIds);
			dutyInfo.put("id", id);
			@SuppressWarnings("unchecked")
			Map<String,String> user = (Map<String, String>) SecurityUtils.getSubject().getPrincipal();
			dutyInfo.put("create_person", user.get("login_name"));
			dutyInfo.put("create_person_id", user.get("id"));
			int result = dao.addUnit(dutyInfo);
			if( result > 0 ) {
				returnResult = new ResponseResult(ResponseResult.STATE_OK,dutyInfo);
			}else {
				returnResult = new ResponseResult(ResponseResult.STATE_FAIL,"错误，数据库执行出错");
			}	
		} catch ( Exception e) {
			returnResult = new ResponseResult(ResponseResult.STATE_FAIL,"服务器出错");
			logger.error(e);
			return returnResult	;
		}
		return returnResult	;
	}
}
