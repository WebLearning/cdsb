package com.shangbao.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.shangbao.app.model.ColumnPageModel;
import com.shangbao.model.ArticleState;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.show.Page;
import com.shangbao.model.show.TitleList;

public interface ArticleService {
	/**
	 * 添加一篇文章
	 * @param article
	 */
	public void add(Article article);
	
	public Long addGetId(Article article);
	/**
	 * 列出文章标题
	 * @return
	 */
	public Map<Long, String> showTitles();
	
	/**
	 * 查找一篇文章
	 * @param id
	 */
	public Article findOne(Long id);
	
	
	public List<Article> find(Article criteriaArticle);
	
	public List<Article> find(Article criteriaArticle, Direction direction, String property);
	
	public TitleList fuzzyFind(String words, ArticleState state, int pageNo, int pageSize);
	
	public TitleList fuzzyFind(String words, ArticleState state, String channelEnName, int pageNo, int pageSize);
	
	public TitleList fuzzyFindOrder(String words, ArticleState state, int pageNo, int pageSize, String order, String direction);
	
	public TitleList fuzzyFindOrder(String words, ArticleState state, String channelEnName, int pageNo, int pageSize, String order, String direction);
	
	public ColumnPageModel appFuzzyFind(String words, boolean tag, int pageNo, int pageSize);
	/**
	 * 更新一篇文章
	 * @param article
	 */
	public void update(Article article);
	
	/**
	 * 删除一篇文章
	 * @param id
	 */
	public void deleteOne(Article article);
	
	/**
	 * 获得标题列表
	 * @return
	 */
	public TitleList getTiltList(ArticleState articleState, int pageNo);
	
	public TitleList getTitleList(ArticleState articleState, String channelEnName, int pageNo);
	
	/**
	 * 获得排序后的标题列表
	 * @param articleState
	 * @param pageNo
	 * @param order
	 * @return
	 */
	public TitleList getOrderedList(ArticleState articleState, int pageNo, String order, String direction);
	
	public TitleList getOrderedList(ArticleState articleState, String channelEnName, int pageNo, String order, String direction);
	
	/**
	 * 设置文章的状态
	 * @param articleState
	 * @param id
	 */
	public void setPutState(ArticleState articleState, List<Long> idList, String message);
	
	public void setDeleteState(ArticleState articleState, List<Long> idList, String message);
	
	void updateCrawler(Article article);
}
