package com.shangbao.dao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;

import com.shangbao.model.persistence.ClickCount;
import com.shangbao.model.show.Page;

public interface ClickCountDao extends MongoDao<ClickCount>{
	List<ClickCount> find(Query query);
	void save(ClickCount clickCount);
	void init(ClickCount clickCount);
	Page<ClickCount> getPage(int pageNo, int pageSize, Query query);
}
