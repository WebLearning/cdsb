package com.shangbao.dao.Imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.shangbao.dao.ClickCountDao;
import com.shangbao.model.persistence.ClickCount;
import com.shangbao.model.show.Page;

@Repository
public class ClickCountDaoImp implements ClickCountDao{

	@Resource
	private MongoTemplate mongoTemplate;
	
	@Override
	public void insert(ClickCount element) {
		mongoTemplate.insert(element);
	}

	@Override
	public void save(ClickCount clickCount){
		mongoTemplate.save(clickCount);
	}
	
	public void init(ClickCount clickCount){
		if(mongoTemplate.findById(clickCount.getArticleId(), ClickCount.class) != null){
			return;
		}else{
			mongoTemplate.save(clickCount);
		}
	}
	
	@Override
	public void insertAll(List<ClickCount> elements) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(ClickCount criteriaElement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean update(ClickCount criteriaElement, ClickCount updateElement) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ClickCount> find(ClickCount criteriaElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override 
	public List<ClickCount> find(Query query){
		return mongoTemplate.find(query, ClickCount.class);
	}
	
	@Override
	public ClickCount findById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ClickCount> findAll() {
		// TODO Auto-generated method stub
		return mongoTemplate.findAll(ClickCount.class);
	}

	@Override
	public List<ClickCount> find(ClickCount criteriaElement, int skip, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClickCount findAndModify(ClickCount criteriaElement,
			ClickCount updateElement) {
		return null;
	}

	@Override
	public ClickCount findAndRemove(ClickCount criteriaElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count(ClickCount criteriaElement) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Page<ClickCount> getPage(int pageNo, int pageSize, Query query) {
		long totalCount = mongoTemplate.count(query, ClickCount.class);
		Page<ClickCount> page = new Page<ClickCount>(pageNo, pageSize, totalCount);
		query.skip(page.getFirstResult());// skip相当于从那条记录开始
		query.limit(pageSize);
		List<ClickCount> clickCounts = mongoTemplate.find(query, ClickCount.class);
		page.setDatas(clickCounts);
		return page;
	}

}
