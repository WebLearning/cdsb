package com.shangbao.model.show;

import java.util.ArrayList;
import java.util.List;

import com.shangbao.model.persistence.ClickCount;

public class ClickList {
	private int PageCount;//页数
	private int currentNo;//当前页码
	private List<ClickInfo> content = new ArrayList<>();
	
	public void add(ClickCount clickCount){
		ClickInfo clickInfo = new ClickInfo(clickCount);
		content.add(clickInfo);
	}
	
	public int getPageCount() {
		return PageCount;
	}

	public void setPageCount(int pageCount) {
		PageCount = pageCount;
	}

	public int getCurrentNo() {
		return currentNo;
	}

	public void setCurrentNo(int currentNo) {
		this.currentNo = currentNo;
	}


	public List<ClickInfo> getContent() {
		return content;
	}


	public void setContent(List<ClickInfo> content) {
		this.content = content;
	}


	class ClickInfo{
		private String articleTitle;
		private long articleId;
		private long dayAppClick;
		private long dayOutClick;
		private long dayUdidClick;
		private long threeDayAppClick;
		private long threeDayOutClick;
		private long threeDayUdidClick;
		private long totalAppClick;
		private long totalOutClick;
		private long totalUdidClick;
		
		public ClickInfo(ClickCount clickCount){
			articleTitle = clickCount.getArticleTitle();
			articleId = clickCount.getArticleId();
			dayAppClick = clickCount.getDayAppClick();
			dayOutClick = clickCount.getDayOutClick();
			dayUdidClick = clickCount.getDayUdidClick();
			threeDayAppClick = clickCount.getThreeDayAppClick();
			threeDayOutClick = clickCount.getThreeDayOutClick();
			threeDayUdidClick = clickCount.getThreeDayUdidClick();
			totalAppClick = clickCount.getTotalAppClick();
			totalOutClick = clickCount.getTotalOutClick();
			totalUdidClick = clickCount.getTotalUdidClick();
		}
		
		public String getArticleTitle() {
			return articleTitle;
		}
		public void setArticleTitle(String articleTitle) {
			this.articleTitle = articleTitle;
		}
		public long getArticleId() {
			return articleId;
		}
		public void setArticleId(long articleId) {
			this.articleId = articleId;
		}
		public long getDayAppClick() {
			return dayAppClick;
		}
		public void setDayAppClick(long dayAppClick) {
			this.dayAppClick = dayAppClick;
		}
		public long getDayOutClick() {
			return dayOutClick;
		}
		public void setDayOutClick(long dayOutClick) {
			this.dayOutClick = dayOutClick;
		}
		public long getDayUdidClick() {
			return dayUdidClick;
		}
		public void setDayUdidClick(long dayUdidClick) {
			this.dayUdidClick = dayUdidClick;
		}
		public long getThreeDayAppClick() {
			return threeDayAppClick;
		}
		public void setThreeDayAppClick(long threeDayAppClick) {
			this.threeDayAppClick = threeDayAppClick;
		}
		public long getThreeDayOutClick() {
			return threeDayOutClick;
		}
		public void setThreeDayOutClick(long threeDayOutClick) {
			this.threeDayOutClick = threeDayOutClick;
		}
		public long getThreeDayUdidClick() {
			return threeDayUdidClick;
		}
		public void setThreeDayUdidClick(long threeDayUdidClick) {
			this.threeDayUdidClick = threeDayUdidClick;
		}
		public long getTotalAppClick() {
			return totalAppClick;
		}
		public void setTotalAppClick(long totalAppClick) {
			this.totalAppClick = totalAppClick;
		}
		public long getTotalOutClick() {
			return totalOutClick;
		}
		public void setTotalOutClick(long totalOutClick) {
			this.totalOutClick = totalOutClick;
		}
		public long getTotalUdidClick() {
			return totalUdidClick;
		}
		public void setTotalUdidClick(long totalUdidClick) {
			this.totalUdidClick = totalUdidClick;
		}
	}
}
