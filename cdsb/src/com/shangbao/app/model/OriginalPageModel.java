package com.shangbao.app.model;

import java.util.ArrayList;
import java.util.List;


public class OriginalPageModel {
	private int pageCount;//总页数
	private int currentNo;//当前页码
	private List<OriginalTitle> content  = new ArrayList<OriginalTitle>();
	
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
	public List<OriginalTitle> getContent() {
		return content;
	}
	public void setContent(List<OriginalTitle> content) {
		this.content = content;
	}
	
	public void addTitle(String title, String pictureUrl, Long newsId){
		OriginalTitle originalTitle = new OriginalTitle(title, pictureUrl, newsId);
		this.content.add(originalTitle);
	}


	class OriginalTitle{
		public String title;
		public String pictureUrl;
		public Long newsId;
		public OriginalTitle(){}
		public OriginalTitle(String title, String pictureUrl, Long newsId){
			this.title = title;
			this.pictureUrl = pictureUrl;
			this.newsId = newsId;
		}
	}
}
