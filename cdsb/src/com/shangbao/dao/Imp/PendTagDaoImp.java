package com.shangbao.dao.Imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.shangbao.dao.PendTagDao;
import com.shangbao.model.persistence.PendControlTag;

@Repository
public class PendTagDaoImp implements PendTagDao{

	@Resource
	private MongoTemplate mongoTemplate;
	
	@Override
	public void insert(PendControlTag element) {
		if(element.getName() != null){
			if(find(element).isEmpty()){
				mongoTemplate.insert(element);
			}else{
				delete(element);
				mongoTemplate.insert(element);
			}
		}
	}

	@Override
	public void insertAll(List<PendControlTag> elements) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(PendControlTag criteriaElement) {
		if(criteriaElement.getName() != null){
			Query query = new Query();
			query.addCriteria(Criteria.where("name").is(criteriaElement.getName()));
			mongoTemplate.remove(query, PendControlTag.class);
		}
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean update(PendControlTag criteriaElement,
			PendControlTag updateElement) {
		if(criteriaElement.getName() != null){
			Query query = new Query();
			query.addCriteria(Criteria.where("name").is(criteriaElement.getName()));
			Update update = new Update();
			update.set("tag", updateElement.isTag());
			mongoTemplate.updateFirst(query, update, PendControlTag.class);
		}
		return false;
	}

	@Override
	public List<PendControlTag> find(PendControlTag criteriaElement) {
		if(criteriaElement.getName() != null){
			Query query = new Query();
			query.addCriteria(Criteria.where("name").is(criteriaElement.getName()));
			List<PendControlTag> list = mongoTemplate.find(query, PendControlTag.class);
			return list;
		}
		
		return null;
	}

	@Override
	public PendControlTag findById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PendControlTag> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PendControlTag> find(PendControlTag criteriaElement, int skip,
			int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PendControlTag findAndModify(PendControlTag criteriaElement,
			PendControlTag updateElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PendControlTag findAndRemove(PendControlTag criteriaElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count(PendControlTag criteriaElement) {
		// TODO Auto-generated method stub
		return 0;
	}

}
