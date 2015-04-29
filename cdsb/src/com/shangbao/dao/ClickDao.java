package com.shangbao.dao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;

import com.shangbao.model.persistence.Click;

public interface ClickDao extends MongoDao<Click>{
	List<Click> find(Query query);
	long count(Query query);
}
