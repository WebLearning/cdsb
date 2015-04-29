package com.shangbao.model.persistence;

import java.util.Date;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="click")
public class Click {
	@Indexed
	private long articleId;
	@Indexed
	private Date clickTime;
	private String fromIp;
	private String udid;
	private boolean tag; //true代表来自app  false代表来自js
	
	public long getArticleId() {
		return articleId;
	}
	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}
	public Date getClickTime() {
		return clickTime;
	}
	public void setClickTime(Date clickTime) {
		this.clickTime = clickTime;
	}
	public String getFromIp() {
		return fromIp;
	}
	public void setFromIp(String fromIp) {
		this.fromIp = fromIp;
	}
	public String getUdid() {
		return udid;
	}
	public void setUDID(String udid) {
		this.udid = udid;
	}
	public boolean isTag() {
		return tag;
	}
	public void setTag(boolean tag) {
		this.tag = tag;
	}
}
