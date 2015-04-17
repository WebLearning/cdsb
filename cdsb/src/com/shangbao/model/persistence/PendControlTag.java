package com.shangbao.model.persistence;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="pendtag")
public class PendControlTag {
	private String name;//项目的名称， 如article 以及 comment
	private boolean tag;//是否需要审核：true为需要， false为不需要
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isTag() {
		return tag;
	}
	public void setTag(boolean tag) {
		this.tag = tag;
	}
	
}
