package com.shangbao.dao.Imp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.inject.New;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.WriteResult;
import com.shangbao.dao.ReadLogDao;
import com.shangbao.model.persistence.ReadLog;

@Repository
public class ReadLogDaoImp implements ReadLogDao{
	@Resource
	private MongoTemplate mongoTemplate;
	
	@Override
	public void insert(ReadLog element) {
		// TODO Auto-generated method stub
		mongoTemplate.insert(element);
	}

	@Override
	public void insertAll(List<ReadLog> elements) {
		// TODO Auto-generated method stub
		mongoTemplate.insertAll(elements);
	}

	@Override
	public void delete(ReadLog criteriaElement) {
		// TODO Auto-generated method stub
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(criteriaElement.getId()));
		mongoTemplate.remove(query, ReadLog.class);
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean update(ReadLog criteriaElement, ReadLog updateElement) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(criteriaElement.getId()));
		Update update = new Update();
		if(!updateElement.getClickIP().isEmpty()){
			update.pushAll("clickIP", updateElement.getClickIP().toArray());
			update.inc("clicks", updateElement.getClickIP().size());
		}
		if(!updateElement.getLikeIP().isEmpty()){
			SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
			update.pushAll("likeIP", updateElement.getLikeIP().toArray());
			update.inc("likes", updateElement.getLikeIP().size());
			update.inc("dateLike." + dft.format(new Date()), updateElement.getLikeIP().size());
		}
		WriteResult result = mongoTemplate.updateFirst(query, update, ReadLog.class);
		if(result.getN() > 0)
			return true;
		return false;
	}

	@Override
	public List<ReadLog> find(ReadLog criteriaElement) {
		Query query = new Query();
		if(criteriaElement.getId() == null || criteriaElement.getId() <= 0)
			return null;
		query.addCriteria(Criteria.where("id").is(criteriaElement.getId()));
		return mongoTemplate.find(query, ReadLog.class);
	}

	@Override
	public ReadLog findById(long id) {
		return mongoTemplate.findById(id, ReadLog.class);
	}

	@Override
	public List<ReadLog> findAll() {
		
		return mongoTemplate.findAll(ReadLog.class);
	}

	@Override
	public List<ReadLog> find(ReadLog criteriaElement, int skip, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReadLog findAndModify(ReadLog criteriaElement, ReadLog updateElement) {
		Query query = new Query();
		if(criteriaElement.getId() > 0){
			query.addCriteria(Criteria.where("id").is(criteriaElement.getId()));
			Update update = new Update();
			if(!updateElement.getClickIP().isEmpty()){
				update.pushAll("clickIP", updateElement.getClickIP().toArray());
				update.inc("clicks", updateElement.getClickIP().size());
			}
			if(!updateElement.getLikeIP().isEmpty()){
				SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
				update.pushAll("likeIP", updateElement.getLikeIP().toArray());
				update.inc("likes", updateElement.getLikeIP().size());
				update.inc("dateLike." + dft.format(new Date()), updateElement.getLikeIP().size());
			}
			return mongoTemplate.findAndModify(query, update, ReadLog.class);
		}
		return null;
	}

	@Override
	public ReadLog findAndRemove(ReadLog criteriaElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count(ReadLog criteriaElement) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void upsert(ReadLog criteriaReadLog, ReadLog updateReadLog) {
		if(criteriaReadLog.getId() > 0){
			Query query = new Query();
			query.addCriteria(Criteria.where("id").is(criteriaReadLog.getId()));
			Update update = new Update();
			if(!updateReadLog.getClickIP().isEmpty()){
				update.pushAll("clickIP", updateReadLog.getClickIP().toArray());
				update.inc("clicks", updateReadLog.getClickIP().size());
			}
			if(!updateReadLog.getLikeIP().isEmpty()){
				SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
				update.pushAll("likeIP", updateReadLog.getLikeIP().toArray());
				update.inc("likes", updateReadLog.getLikeIP().size());
				update.inc("dateLike." + dft.format(new Date()), updateReadLog.getLikeIP().size());
			}
			mongoTemplate.upsert(query, update, ReadLog.class);
		}
	}

}
