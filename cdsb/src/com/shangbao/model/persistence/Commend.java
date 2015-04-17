package com.shangbao.model.persistence;

import java.util.ArrayList;
import java.util.List;

import com.shangbao.model.ArticleState;
import com.shangbao.model.show.SingleCommend;

public class Commend {
	private long articleId;//文章id
	private String articleTitle;//文章标题
	private ArticleState state;//文章状态
	private List<SingleCommend> commendList = new ArrayList<SingleCommend>();//评论列表
	
	public long getArticleId() {
		return articleId;
	}
	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}
	public String getArticleTitle() {
		return articleTitle;
	}
	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}
	public ArticleState getState() {
		return state;
	}
	public void setState(ArticleState state) {
		this.state = state;
	}
	public List<SingleCommend> getCommendList() {
		return commendList;
	}
	public void setCommendList(List<SingleCommend> commendList) {
		this.commendList = commendList;
	}
}
