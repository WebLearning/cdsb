package com.shangbao.app.model;

import java.util.ArrayList;
import java.util.List;

public class ActiveModel {
	private List<Active> activeList = new ArrayList<Active>();
	
	public List<Active> getActiveList() {
		return activeList;
	}

	public void setActiveList(List<Active> activeList) {
		this.activeList = activeList;
	}

	public void addActive(String activeName, String summary){
		for(Active active : this.activeList){
			if(active.activeName.equals(activeName))
				return;
		}
		this.activeList.add(new Active(activeName, summary));
	}

	class Active{
		public String activeName;
		public String summary;
		public Active(){
		}
		public Active(String activeName, String summary){
			this.activeName = activeName;
			this.summary = summary;
		}
	}
}
