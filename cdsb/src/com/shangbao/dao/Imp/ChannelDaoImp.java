package com.shangbao.dao.Imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.WriteResult;
import com.shangbao.dao.ChannelDao;
import com.shangbao.model.persistence.Channel;
import com.shangbao.model.show.Page;

@Repository
public class ChannelDaoImp implements ChannelDao{

	@Resource
	private MongoTemplate mongoTemplate;
	
	@Override
	public void insert(Channel element) {
		this.mongoTemplate.insert(element);
	}

	@Override
	public void insertAll(List<Channel> elements) {
		this.mongoTemplate.insertAll(elements);
	}

	@Override
	public void delete(Channel criteriaElement) {
		this.mongoTemplate.remove(getQuery(criteriaElement), Channel.class);
	}

	@Override
	public void deleteAll() {
		this.mongoTemplate.remove(new Query(), Channel.class);
	}

	@Override
	public boolean update(Channel criteriaElement, Channel updateElement) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void update(Channel criteriaChannel, Update update){
		Query query = getQuery(criteriaChannel);
		//System.out.println(query.getQueryObject());
		WriteResult result = mongoTemplate.updateFirst(query, update, Channel.class);
		//System.out.println(result.getN());
	}

	@Override
	public List<Channel> find(Channel criteriaElement) {
		return this.mongoTemplate.find(getQuery(criteriaElement), Channel.class);
	}

	@Override
	public List<Channel> find(Channel criteriaChannel, Sort sort){
		Query query = getQuery(criteriaChannel);
		query.with(sort);
		return this.mongoTemplate.find(query, Channel.class);
	}
	
	@Override
	public Channel findById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Channel> findAll() {
		return this.mongoTemplate.findAll(Channel.class);
	}

	@Override
	public List<Channel> find(Channel criteriaElement, int skip, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Channel findAndModify(Channel criteriaElement, Channel updateElement) {
		Update update = new Update();
		update.set("channelName", updateElement.getChannelName());
		update.set("summary", updateElement.getSummary());
		update.set("state", updateElement.getState());
		update.set("related", updateElement.getRelated());
		return this.mongoTemplate.findAndModify(getQuery(updateElement), update, Channel.class);
	}

	@Override
	public Channel findAndRemove(Channel criteriaElement) {
		return this.mongoTemplate.findAndRemove(getQuery(criteriaElement), Channel.class);
	}

	@Override
	public long count(Channel criteriaElement) {
		return this.mongoTemplate.count(getQuery(criteriaElement), Channel.class);
	}
	
	public Page<Channel> getPage(int pageNo, int pageSize, Query query){
		Page<Channel> page = new Page<Channel>(pageNo, pageSize, mongoTemplate.count(query, Channel.class));
		query.skip(page.getFirstResult());// skip相当于从那条记录开始
		query.limit(pageSize);
		List<Channel> datas = mongoTemplate.find(query, Channel.class);
		page.setDatas(datas);
		return page;
	}

	private Query getQuery(Channel criteriaChannel){
		Query query = new Query();
		if(criteriaChannel.getChannelName() != null && (!criteriaChannel.getChannelName().isEmpty())){
			query.addCriteria(Criteria.where("channelName").is(criteriaChannel.getChannelName()));
		}
		if(criteriaChannel.getEnglishName() != null && (!criteriaChannel.getEnglishName().isEmpty())){
			query.addCriteria(Criteria.where("englishName").is(criteriaChannel.getEnglishName()));
		}
		if((criteriaChannel.getSummary() != null) && (!criteriaChannel.getSummary().isEmpty())){
			query.addCriteria(Criteria.where("summary").is(criteriaChannel.getSummary()));
		}
		if((criteriaChannel.getRelated() != null) && (!criteriaChannel.getRelated().isEmpty())){
			query.addCriteria(Criteria.where("related").is(criteriaChannel.getRelated()));
		}
		if((criteriaChannel.getState() != null) && (criteriaChannel.getState() != null)){
			query.addCriteria(Criteria.where("state").is(criteriaChannel.getState().toString()));
		}
		if(criteriaChannel.getChannelIndex() > 0){
			query.addCriteria(Criteria.where("channelIndex").is(criteriaChannel.getChannelIndex()));
		}
		return query;
	}
}
