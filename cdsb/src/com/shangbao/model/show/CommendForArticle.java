package com.shangbao.model.show;

import com.shangbao.model.ArticleState;
import com.shangbao.model.persistence.Article;

/**
 * 一篇文章的所有评论信息
 * 用于转换为Json，传递给前端
 * 点击评论后显示所有文章的评论状态
 * @author YangYi
 *
 */
public class CommendForArticle {
	private String title;//文章标题
	private long articleId;//文章Id
	private ArticleState state;//文章状态
	private int newsCommends;//商报评论数
	private int newsCommendsUnpublish;
	private int newsCommendsPublish;//商报评论发表数
	private int crawlerCommends;//爬虫评论数
	private int crawlerCommendsUnpublish;
	private int crawlerCommendsPublish;//爬虫评论发表数
	
	public CommendForArticle(){
		
	}
	
	public CommendForArticle(Article article){
		this.setArticleId(article.getId());
		this.setCrawlerCommends(article.getCrawlerCommends());
		this.setCrawlerCommendsUnpublish(article.getCrawlerCommendsUnpublish());
		this.setCrawlerCommendsPublish(article.getCrawlerCommendsPublish());
		this.setNewsCommends(article.getNewsCommends());
		this.setNewsCommendsUnpublish(article.getNewsCommendsUnpublish());
		this.setNewsCommendsPublish(article.getNewsCommendsPublish());
		this.setState(article.getState());
		this.setTitle(article.getTitle());
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getArticleId() {
		return articleId;
	}
	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}
	public ArticleState getState() {
		return state;
	}
	public void setState(ArticleState state) {
		this.state = state;
	}
	public int getNewsCommends() {
		return newsCommends;
	}
	public void setNewsCommends(int newsCommends) {
		this.newsCommends = newsCommends;
	}
	public int getNewsCommendsPublish() {
		return newsCommendsPublish;
	}
	public void setNewsCommendsPublish(int newsCommendsPublish) {
		this.newsCommendsPublish = newsCommendsPublish;
	}
	public int getCrawlerCommends() {
		return crawlerCommends;
	}
	public void setCrawlerCommends(int crawlerCommends) {
		this.crawlerCommends = crawlerCommends;
	}
	public int getCrawlerCommendsPublish() {
		return crawlerCommendsPublish;
	}
	public void setCrawlerCommendsPublish(int crawlerCommendsPublish) {
		this.crawlerCommendsPublish = crawlerCommendsPublish;
	}

	public int getNewsCommendsUnpublish() {
		return newsCommendsUnpublish;
	}

	public void setNewsCommendsUnpublish(int newsCommendsUnpublish) {
		this.newsCommendsUnpublish = newsCommendsUnpublish;
	}

	public int getCrawlerCommendsUnpublish() {
		return crawlerCommendsUnpublish;
	}

	public void setCrawlerCommendsUnpublish(int crawlerCommendsUnpublish) {
		this.crawlerCommendsUnpublish = crawlerCommendsUnpublish;
	}
	
	
}
