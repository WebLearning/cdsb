package com.shangbao.dao.Imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.shangbao.dao.StartPicturesDao;
import com.shangbao.model.persistence.StartPictures;

@Repository
public class StartPicturesDaoImp implements StartPicturesDao{
	
	@Resource
	private MongoTemplate mongoTemplate;

	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public void insert(StartPictures startPictures) {
		mongoTemplate.save(startPictures);
	}

	@Override
	public void update(StartPictures criteriaStartPictures, Update update) {
		Query query = getQuery(criteriaStartPictures);
		mongoTemplate.updateFirst(query, update, StartPictures.class);
	}

	@Override
	public void delete(StartPictures criteriaStartPictures) {
		Query query = getQuery(criteriaStartPictures);
		mongoTemplate.remove(query, StartPictures.class);
	}

	@Override
	public List<StartPictures> find(StartPictures criteriaStartPictures) {
		
		return mongoTemplate.find(getQuery(criteriaStartPictures), StartPictures.class);
	}
	
	private Query getQuery(StartPictures criteriaStartPictures){
		Query query = new Query();
		if(criteriaStartPictures.getId() != null && !criteriaStartPictures.getId().isEmpty()){
			query.addCriteria(Criteria.where("id").is(criteriaStartPictures.getId()));
		}
		return query;
	}
	
	
}
