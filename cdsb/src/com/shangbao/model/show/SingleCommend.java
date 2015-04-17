package com.shangbao.model.show;

import java.util.Date;

import com.shangbao.model.CommendState;

/**
 * 一条评论
 * @author YangYi
 *
 */
public class SingleCommend {
	private String commendId;
	private String userName;//评论者名字
	private long userId;//评论者ID
	private Date timeDate;//评论时间
	private String level;//等级
	private CommendState state = CommendState.unpublished;//是否发表
	private String from;//评论来源
	private String content;//评论内容
	private String reply;//回复
	
	public String getCommendId() {
		return commendId;
	}
	public void setCommendId(String commendId) {
		this.commendId = commendId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public Date getTimeDate() {
		return timeDate;
	}
	public void setTimeDate(Date timeDate) {
		this.timeDate = timeDate;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public CommendState getState() {
		return state;
	}
	public void setState(CommendState state) {
		this.state = state;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	
	
}
