package com.shangbao.dao.Imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.shangbao.dao.ClickDao;
import com.shangbao.model.persistence.Click;

@Repository
public class ClickDaoImp implements ClickDao{

	@Resource
	private MongoTemplate mongoTemplate;
	
	@Override
	public void insert(Click element) {
		mongoTemplate.insert(element);
	}

	@Override
	public void insertAll(List<Click> elements) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Click criteriaElement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean update(Click criteriaElement, Click updateElement) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Click> find(Click criteriaElement) {
		return mongoTemplate.find(getQuery(criteriaElement), Click.class);
	}

	@Override
	public List<Click> find(Query query){
		return mongoTemplate.find(query, Click.class);
	}
	
	@Override
	public Click findById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Click> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Click> find(Click criteriaElement, int skip, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Click findAndModify(Click criteriaElement, Click updateElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Click findAndRemove(Click criteriaElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count(Click criteriaElement) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public long count(Query query){
		return mongoTemplate.count(query, Click.class);
	}

	private Query getQuery(Click click){
		if(click == null){
			click = new Click();
		}
		Query query = new Query();
		if(click.getArticleId() > 0){
			query.addCriteria(Criteria.where("articleId").is(click.getArticleId()));
		}
		if(click.getClickTime() != null){
			query.addCriteria(Criteria.where("clickTime").is(click.getClickTime()));
		}
		if(click.getFromIp() != null){
			query.addCriteria(Criteria.where("fromIp").is(click.getFromIp()));
		}
		if(click.getUdid() != null){
			query.addCriteria(Criteria.where("udid").is(click.getUdid()));
		}
		return query;
	}
}
