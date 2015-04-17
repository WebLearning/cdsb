package com.shangbao.model.persistence;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.shangbao.model.ChannelState;

@Document(collection="channel")
public class Channel {
	private String channelName;//栏目名称
	private String summary;//栏目介绍
	private ChannelState state;//栏目状态
	private String related;//相关栏目(父栏目，子栏目)
	private String englishName;//栏目英文名(用于生产URI)
	private Date startDate;//活动的开始时间
	private Date endDate;//活动的结束时间
	private int channelIndex;//栏目的顺序
	
	public String getEnglishName() {
		return englishName;
	}
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public ChannelState getState() {
		return state;
	}
	public void setState(ChannelState state) {
		this.state = state;
	}
	public String getRelated() {
		return related;
	}
	public void setRelated(String related) {
		this.related = related;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public int getChannelIndex() {
		return channelIndex;
	}
	public void setChannelIndex(int channelIndex) {
		this.channelIndex = channelIndex;
	}
	
}
