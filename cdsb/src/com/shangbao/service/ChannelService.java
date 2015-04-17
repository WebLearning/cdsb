package com.shangbao.service;

import java.util.List;

import com.shangbao.model.ChannelState;
import com.shangbao.model.persistence.Channel;

public interface ChannelService {
	
	List<Channel> findAllFatherChannels();
	List<Channel> findAllSonChannels(String fatherChannelName);
	List<Channel> findAllActivities();
	String addChannel(Channel channel);
	String deleteChannel(Channel channel);
	void swapChannel(Channel channelA, Channel channelB);
	Channel findByEnName(String englishName, ChannelState state);
	List<Channel> getLeafChannels(String channelEnName);
}
