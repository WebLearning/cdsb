package com.shangbao.model.show;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChannelList {
	private int PageCount;//页数
	private int currentNo;//当前页码
	private List<ChannelTitle> content = new ArrayList<ChannelTitle>();
	
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


	public List<ChannelTitle> getContent() {
		return content;
	}

	public void setContent(List<ChannelTitle> content) {
		this.content = content;
	}

	public void addChannel(String channelName, Long articleNum, Long pictureNum){
		ChannelTitle channelTitle = new ChannelTitle(channelName, articleNum, pictureNum);
		this.content.add(channelTitle);
	}
	
	public void sorted(final String order){
		Comparator<ChannelTitle> comparator = new Comparator<ChannelList.ChannelTitle>() {

			@Override
			public int compare(ChannelTitle o1, ChannelTitle o2) {
				if(order.equals("channelName")){
					return o1.channelName.compareTo(o2.channelName);
				}
				if(order.equals("articleNum")){
					return o1.articleNum.compareTo(o2.articleNum);
				}
				if(order.equals("pictureNum")){
					return o1.pictureNum.compareTo(o2.pictureNum);
				}
				return 0;
			}
		};
		Collections.sort(this.content, comparator);
	}
	
	class ChannelTitle {
		public String channelName;
		public Long articleNum;
		public Long pictureNum;
		public ChannelTitle(String channelName, Long articleNum, Long pictureNum){
			this.channelName = channelName;
			this.articleNum = articleNum;
			this.pictureNum = pictureNum;
		}
	}
}
