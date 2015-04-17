package com.shangbao.dao;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

@Component(value="mongoDBTokenRepository")
public class MongoDBTokenRepository implements PersistentTokenRepository{

	@Autowired
	PersistanceTokenDao persistanceTokenDao;
	
	@Override
	public void createNewToken(PersistentRememberMeToken token) {
		persistanceTokenDao.insertToken(token);
	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		return persistanceTokenDao.getTokenForSeries(seriesId);
	}

	@Override
	public void removeUserTokens(String username) {
		persistanceTokenDao.deleteToken(username);
	}

	@Override
	public void updateToken(String series, String tokenValue, Date lastUsed) {
		persistanceTokenDao.updateToken(series, tokenValue, lastUsed);
	}

}
