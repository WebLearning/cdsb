package com.shangbao.dao;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.Commend;
import com.shangbao.model.show.Page;

public interface CommendDao extends MongoDao<Commend> {
	Query getQuery(Commend commend);
	List find(Query query, Commend commend);
	List<Commend> find(Commend criteriaCommend, Direction direction, String property);
	void update(Commend commend, Update update);
	void update(Commend commend, Query query, Update update);
	Page<Article> getPage(int pageNo, int pageSize, Query query);
	void save(Commend commend);
}
