package com.shangbao.dao;

import java.util.Date;

import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

public interface PersistanceTokenDao {
	void insertToken(PersistentRememberMeToken token);
	void updateToken(String series, String tokenValue, Date lastUsed);
	void deleteToken(String username);
	PersistentRememberMeToken getTokenForSeries(String seriesId);
}
