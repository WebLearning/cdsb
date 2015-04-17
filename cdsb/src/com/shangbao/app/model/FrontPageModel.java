package com.shangbao.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * app打开初始页面，大类分类
 * @author Administrator
 *
 */
public class FrontPageModel {
	private List<String> pictureUrl = new ArrayList<String>();
	private List<ChannelModel> channel = new ArrayList<ChannelModel>();

	public List<String> getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(List<String> pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public List<ChannelModel> getChannel() {
		return channel;
	}

	public void setChannel(List<ChannelModel> channel) {
		this.channel = channel;
	}

	public void addPictureUrl(String pictureUrl){
		if(! this.pictureUrl.contains(pictureUrl)){
			this.pictureUrl.add(pictureUrl);
		}
	}
	
	public void deletePictureUrl(String pictureUrl){
		if(this.pictureUrl.contains(pictureUrl)){
			this.pictureUrl.remove(pictureUrl);
		}
	}
	
	public void addChannel(String channelName, String channelUrl){
		ChannelModel channelModel = new ChannelModel(channelName, channelUrl);
		this.channel.add(channelModel);
	}
	
	public void deleteChannel(String chanelName){
		ChannelModel targetChannel = new ChannelModel();
		for(ChannelModel channel : this.channel){
			if(channel.channelName.equals(chanelName)){
				targetChannel = channel;
			}
		}
		this.channel.remove(targetChannel);
	}

	class ChannelModel{
		public String channelName;
		public String channelUrl;
		public ChannelModel(){
			
		}
		public ChannelModel(String channelName, String channelUrl){
			this.channelName = channelName;
			this.channelUrl = channelUrl;
		}
 	}
}
