package com.shangbao.model.show;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.shangbao.model.persistence.Article;

public class Title {
	private String title;//标题
	private String author;//作者
	private String level;//等级
	private int words;//字数
	private int pictures;//图片数
	private int newsCommendsPublish;//商报评论发表数
	private int newsCommendsUnpublish;//商报评论未发表数
	private int newsCommends;//商报评论数
	private int crawlerCommendsPublish;//爬虫评论发表数
	private int crawlerCommendsUnpublish;//爬虫评论未发表数
	private int crawlerCommends;//爬虫评论数
	private int clicks;//点击数
	private int likes;//点赞数
	private String from;//来源
	private String summary;//摘要
	private Date time;//时间
	private String titleUrl;//标题图片
	private List<String> channel = new ArrayList<String>();//所属栏目
	private String activity; //所属活动
	private long articleId;//文章ID
	
	public Title(){	
	}
	
	public Title(Article article){
		setTitle(article.getTitle());
		setAuthor(article.getAuthor());
		setLevel(article.getLevel());
		setWords(article.getWords());
		setNewsCommends(article.getNewsCommends());
		setNewsCommendsPublish(article.getNewsCommendsPublish());
		setNewsCommendsUnpublish(article.getNewsCommendsUnpublish());
		setCrawlerCommends(article.getCrawlerCommends());
		setCrawlerCommendsPublish(article.getCrawlerCommendsPublish());
		setCrawlerCommendsUnpublish(article.getCrawlerCommendsUnpublish());
		//setClicks(article.getClicks());
		setFrom(article.getFrom());
		setSummary(article.getSummary());
		setTime(article.getTime());
		setTitleUrl(article.getTitlePicUrl());
		setArticleId(article.getId());
		setChannel(article.getChannel());
		setActivity(article.getActivity());
		setClicks(article.getJs_clicks() > 0 ? article.getJs_clicks() : article.getClicks());
		setLikes(article.getLikes());
		if(article.getPictures() > 0){
			setPictures(article.getPictures());
		}else{
			setPictures(article.getPicturesUrl().size());
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public int getWords() {
		return words;
	}

	public void setWords(int words) {
		this.words = words;
	}

	public int getPictures() {
		return pictures;
	}

	public void setPictures(int pictures) {
		this.pictures = pictures;
	}

	public int getNewsCommends() {
		return newsCommends;
	}

	public void setNewsCommends(int newsCommends) {
		this.newsCommends = newsCommends;
	}

	public int getCrawlerCommends() {
		return crawlerCommends;
	}

	public void setCrawlerCommends(int crawlerCommends) {
		this.crawlerCommends = crawlerCommends;
	}

	public int getClicks() {
		return clicks;
	}

	public void setClicks(int clicks) {
		this.clicks = clicks;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getTitleUrl() {
		return titleUrl;
	}

	public void setTitleUrl(String titleUrl) {
		this.titleUrl = titleUrl;
	}

	public List<String> getChannel() {
		return channel;
	}

	public void setChannel(List<String> channel) {
		this.channel = channel;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public long getArticleId() {
		return articleId;
	}

	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}

	public int getNewsCommendsPublish() {
		return newsCommendsPublish;
	}

	public void setNewsCommendsPublish(int newsCommendsPublish) {
		this.newsCommendsPublish = newsCommendsPublish;
	}

	public int getNewsCommendsUnpublish() {
		return newsCommendsUnpublish;
	}

	public void setNewsCommendsUnpublish(int newsCommendsUnpublish) {
		this.newsCommendsUnpublish = newsCommendsUnpublish;
	}

	public int getCrawlerCommendsPublish() {
		return crawlerCommendsPublish;
	}

	public void setCrawlerCommendsPublish(int crawlerCommendsPublish) {
		this.crawlerCommendsPublish = crawlerCommendsPublish;
	}

	public int getCrawlerCommendsUnpublish() {
		return crawlerCommendsUnpublish;
	}

	public void setCrawlerCommendsUnpublish(int crawlerCommendsUnpublish) {
		this.crawlerCommendsUnpublish = crawlerCommendsUnpublish;
	}
	
}