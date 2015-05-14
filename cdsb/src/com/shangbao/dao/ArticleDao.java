package com.shangbao.dao;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.shangbao.model.ArticleState;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.show.Page;

public interface ArticleDao extends MongoDao<Article> {
	void update(Article article);
	void setState(ArticleState state, Article criteriaArticle);
	void addMessage(String message, Article criteriaArticle);
	Page<Article> getPage(int pageNo, int pageSize, Query query);
	List<Article> find(Article criteriaArticle, Direction direction, String property);
	List<Article> fuzzyFind(String words, ArticleState state, boolean tag);
	Page<Article> fuzzyFind(String words, ArticleState state, boolean tag, int pageNo, int pageSize);
	Page<Article> fuzzyFind(String words, ArticleState state, List<String> channelNames, boolean tag, int pageNo, int pageSize);
	Page<Article> fuzzyFind(String words, ArticleState state, boolean tag, int pageNo, int pageSize, String order, Direction direction);
	Page<Article> fuzzyFind(String words, ArticleState state, List<String> channelNames, boolean tag, int pageNo, int pageSize, String order, Direction direction);
	void setTopArticle(String channelName, Long articleId);
	void unSetTopArticle(String channelName, Long articleId);
	void swapArticle(String channelName, Long articleAId, Long articleBId);
	void update(Article criteriaArticle, Update update);
	Long insertAndGetId(Article article);
	void fixArticle(long articleid);
}
