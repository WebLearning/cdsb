package com.shangbao.model.persistence;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="clickCount")
public class ClickCount {
	@Id
	private long articleId;
	private String articleTitle;
	@Indexed
	private Date firstTime;
	private long dayAppClick;
	private long dayOutClick;
	private long dayUdidClick;
	private long threeDayAppClick;
	private long threeDayOutClick;
	private long threeDayUdidClick;
	private long totalAppClick;
	private long totalOutClick;
	private long totalUdidClick;
	
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
	public Date getFirstTime() {
		return firstTime;
	}
	public void setFirstTime(Date firstTime) {
		this.firstTime = firstTime;
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
