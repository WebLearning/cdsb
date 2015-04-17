package com.shangbao.dao;

import com.shangbao.model.persistence.ReadLog;

public interface ReadLogDao extends MongoDao<ReadLog>{
	void upsert(ReadLog criteriaReadLog, ReadLog updateReadLog);
}
