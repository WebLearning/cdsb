package com.shangbao.model.show;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有文章评论页面类
 * @author YangYi
 *
 */
public class CommendPage {
	private int pageCount;
	private int currentNo;
	private List<CommendForArticle> commendList = new ArrayList<CommendForArticle>();
	
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public int getCurrentNo() {
		return currentNo;
	}
	public void setCurrentNo(int currentNo) {
		this.currentNo = currentNo;
	}
	public List<CommendForArticle> getCommendList() {
		return commendList;
	}
	public void setCommendList(List<CommendForArticle> commendList) {
		this.commendList = commendList;
	}
	
	
}
