package com.yeqiu.sys.user.dao;

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;

/**
 * @author 陆昌
 * @time 2019年5月28日上午8:59:44
 * 说明：实现EnterpriseCacheSessionDAO，管理session
 */
public class ManageCacheSessionDao extends EnterpriseCacheSessionDAO{

	@Override
	protected Serializable doCreate(Session session) {		
		return super.doCreate(session);
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {		
		SimpleSession session = new SimpleSession();
		session.setId(sessionId);
		return session;
	}

	@Override
	protected void doUpdate(Session session) {
		super.doUpdate(session);
	}

	@Override
	protected void doDelete(Session session) {
		super.doDelete(session);
	}

}
