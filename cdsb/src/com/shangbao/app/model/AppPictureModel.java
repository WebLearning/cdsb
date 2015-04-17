package com.shangbao.app.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.shangbao.model.persistence.Article;

/**
 * 手机图片详细页面
 * @author Administrator
 *
 */
public class AppPictureModel {
	private String title;
	private List<String> pictureUrl = new ArrayList<String>();
	private String author;
	private Date time;
	private String activeName;
	private String content;
	private Long articleId;
	
	public AppPictureModel(){
		
	}
	
	public AppPictureModel(Article article){
		title = article.getTitle();
		pictureUrl = article.getPicturesUrl();
		author = article.getAuthor();
		time = article.getTime();
		activeName = article.getActivity();
		content = article.getContent();
		articleId = article.getId();
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitleString() {
		return title;
	}
	public void setTitleString(String title) {
		this.title = title;
	}
	public List<String> getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(List<String> pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getActiveName() {
		return activeName;
	}
	public void setActiveName(String activeName) {
		this.activeName = activeName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}
}
