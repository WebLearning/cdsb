package com.shangbao.dao.Imp;

import java.util.Date;

import javax.annotation.Resource;
import javax.enterprise.inject.New;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.stereotype.Repository;

import com.shangbao.dao.PersistanceTokenDao;

@Repository(value="persistanceTokenDao")
public class PersistanceTokenDaoImp implements PersistanceTokenDao {

	@Resource
	MongoTemplate mongoTemplate;
	
	@Override
	public void insertToken(PersistentRememberMeToken token) {
		mongoTemplate.insert(token, "rememberMeTokens");
	}

	@Override
	public void updateToken(String series, String tokenValue, Date lastUsed) {
		Update update = new Update();
		update.set("tokenValue", tokenValue);
		update.set("date", lastUsed);
		Query query = new Query();
		query.addCriteria(Criteria.where("series").is(series));
		mongoTemplate.updateFirst(query, update, "rememberMeTokens");
	}

	@Override
	public void deleteToken(String username) {
		mongoTemplate.remove(new Query(Criteria.where("username").is(username)), "rememberMeTokens");
	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		PersistentRememberMeToken token = mongoTemplate.findOne(new Query(Criteria.where("series").is(seriesId)), PersistentRememberMeToken.class, "rememberMeTokens");
		return token;
	}

}
