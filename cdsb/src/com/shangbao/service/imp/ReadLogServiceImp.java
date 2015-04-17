package com.shangbao.service.imp;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shangbao.dao.ReadLogDao;
import com.shangbao.model.persistence.ReadLog;
import com.shangbao.service.ReadLogService;

@Service
public class ReadLogServiceImp implements ReadLogService{

	@Resource
	private ReadLogDao readLogDaoImp;
	
	@Override
	public void addClick(Long id, String ip) {
		// TODO Auto-generated method stub
		if(ip == null)
			return;
		ReadLog criteriaReadLog = new ReadLog();
		criteriaReadLog.setId(id);
		ReadLog updateReadLog = new ReadLog();
		updateReadLog.addClick(ip);
		readLogDaoImp.upsert(criteriaReadLog, updateReadLog);
	}

	@Override
	public void addLike(Long id, String ip) {
		if(ip == null)
			return;
		ReadLog criteriaReadLog = new ReadLog();
		criteriaReadLog.setId(id);
		ReadLog updateReadLog = new ReadLog();
		updateReadLog.addLike(ip);
		readLogDaoImp.upsert(criteriaReadLog, updateReadLog);
	}

}
