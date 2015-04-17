package com.shangbao.service.imp;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.shangbao.dao.ChannelDao;
import com.shangbao.model.ChannelState;
import com.shangbao.model.persistence.Channel;
import com.shangbao.service.ChannelService;

@Service
public class ChannelServiceImp implements ChannelService{
	@Resource
	private ChannelDao channelDaoImp;

	public ChannelDao getChannelDaoImp() {
		return channelDaoImp;
	}

	public void setChannelDaoImp(ChannelDao channelDaoImp) {
		this.channelDaoImp = channelDaoImp;
	}

	@Override
	public List<Channel> findAllFatherChannels() {
		Channel channel = new Channel();
		channel.setState(ChannelState.Father);
		Sort sort = new Sort(Direction.ASC, "channelIndex");
		return channelDaoImp.find(channel, sort);
	}

	@Override
	public List<Channel> findAllSonChannels(String fatherChannelName) {
		Channel channel = new Channel();
		Channel fatherChannel = new Channel();
		fatherChannel.setEnglishName(fatherChannelName);
		List<Channel> fathers = channelDaoImp.find(fatherChannel);
		if(fathers.isEmpty() || fathers == null)
			return null;
		String fatherCnName = fathers.get(0).getChannelName();
		channel.setRelated(fatherCnName);
		channel.setState(ChannelState.Son);
		Sort sort = new Sort(Direction.ASC, "channelIndex");
		return channelDaoImp.find(channel, sort);
	}
	
	@Override
	public List<Channel> findAllActivities(){
		Channel channel = new Channel();
		channel.setState(ChannelState.Activity);
		Sort sort = new Sort(Direction.ASC, "channelIndex");
		List<Channel> channels = channelDaoImp.find(channel, sort);
		return channels;
	}

	@Override
	public Channel findByEnName(String englishName, ChannelState state){
		Channel criteriaChannel = new Channel();
		criteriaChannel.setEnglishName(englishName);
		if(state != null){
			criteriaChannel.setState(state);
		}
		List<Channel> channels = channelDaoImp.find(criteriaChannel);
		if(channels!= null && !channels.isEmpty())
			return channels.get(0);
		return null;
	}
	
	@Override
	public List<Channel> getLeafChannels(String channelEnName){
		List<Channel> channels = new ArrayList<>();
		Channel criteriaChannel = new Channel();
		criteriaChannel.setEnglishName(channelEnName);
		List<Channel> findChannels = channelDaoImp.find(criteriaChannel);
		if(!findChannels.isEmpty()){
			Channel channel = findChannels.get(0);
			if(channel.getState().equals(ChannelState.Son)){
				channels.add(channel);
			}else if(channel.getState().equals(ChannelState.Father)){
				Channel criteriaSonChannel = new Channel();
				criteriaSonChannel.setState(ChannelState.Son);
				criteriaSonChannel.setRelated(channel.getChannelName());
				List<Channel> sonChannels = channelDaoImp.find(criteriaSonChannel);
				if(sonChannels.isEmpty()){
					channels.add(channel);
				}else{
					for(Channel sonChannel : sonChannels){
						channels.add(sonChannel);
					}
				}
			}
		}
		return channels;
	}
	
	@Override
	public String addChannel(Channel channel) {
		Channel criteriaChannel1 = new Channel();
		Channel criteriaChannel2 = new Channel();
		criteriaChannel1.setChannelName(channel.getChannelName());
		criteriaChannel2.setEnglishName(channel.getEnglishName());
		if(!channelDaoImp.find(criteriaChannel1).isEmpty() || !channelDaoImp.find(criteriaChannel2).isEmpty()){
			return "already exist";
		}
		if(channel.getState().equals(ChannelState.Son)){
			Channel relateChannel = new Channel();
			relateChannel.setState(ChannelState.Father);
			relateChannel.setChannelName(channel.getRelated());
			List<Channel> fChannels = this.channelDaoImp.find(relateChannel);
			if(fChannels.isEmpty()){//没有父分类
				return "no father channel";
			}else{
				Channel sonChannel = new Channel();
				sonChannel.setChannelName(channel.getChannelName());
				sonChannel.setRelated(channel.getRelated());
				sonChannel.setState(ChannelState.Son);
				if(!this.channelDaoImp.find(sonChannel).isEmpty()){
					return channel.getChannelName() + "is already exist";
				}else{
					channel.setChannelIndex(findSonCount(channel.getRelated()) + 1);
					this.channelDaoImp.insert(channel);
					return "OK";
				}
			}
		}else if(channel.getState().equals(ChannelState.Father)){
			Channel fatherChannel = new Channel();
			fatherChannel.setChannelName(channel.getChannelName());
			fatherChannel.setState(ChannelState.Father);
			if(this.channelDaoImp.find(fatherChannel).isEmpty()){
				channel.setChannelIndex(findFatherCount() + 1);
				this.channelDaoImp.insert(channel);
				return "OK";
			}
			return channel.getChannelName() + "is already exist";
		}else if(channel.getState().equals(ChannelState.Activity)){
			Channel activityChannel = new Channel();
			activityChannel.setChannelName(channel.getChannelName());
//			activityChannel.setChannelIndex(findActivityCount() + 1);
			activityChannel.setState(ChannelState.Activity);
			if(this.channelDaoImp.find(activityChannel).isEmpty()){
				channel.setChannelIndex(findActivityCount() + 1);
				this.channelDaoImp.insert(channel);
				return "OK";
			}
		}
		return "error";
	}

	@Override
	public String deleteChannel(Channel channel) {
//		if(channel.getState().equals(ChannelState.Father) && channel.getChannelName().equals("快拍成都")){
//			return "error";
//		}
		List<Channel> channels = channelDaoImp.find(channel);
		if(channels.isEmpty() || channels == null){
			return "error";
		}
		channel = channels.get(0);
		if(channel.getState().equals(ChannelState.Father)){
			Channel fatherChannel = new Channel();
			fatherChannel.setState(ChannelState.Son);
			fatherChannel.setRelated(channel.getChannelName());
			this.channelDaoImp.delete(fatherChannel);
			this.channelDaoImp.delete(channel);
			updateChannelIndex();
			return "OK";
		}else if(channel.getState().equals(ChannelState.Son)){
			this.channelDaoImp.delete(channel);
			updateSonChannelIndex(channel.getRelated());
			return "OK";
		}else if(channel.getState().equals(ChannelState.Activity)){
			this.channelDaoImp.delete(channel);
			updateActivityIndex();
			return "OK";
		}
		return "error";
	}
	
	@Override
	public void swapChannel(Channel channelA, Channel channelB){
		if(channelA.getState().equals(ChannelState.Father) 
				&& channelB.getState().equals(ChannelState.Father)){
			Update updateA = new Update();
			Update updateB = new Update();
			updateA.set("channelIndex", channelB.getChannelIndex());
			updateB.set("channelIndex", channelA.getChannelIndex());
			channelDaoImp.update(channelA, updateA);
			channelDaoImp.update(channelB, updateB);
		}else if(channelA.getState().equals(ChannelState.Son) 
				&& channelB.getState().equals(ChannelState.Son)){
			if(channelA.getRelated().equals(channelB.getRelated())){
				Update updateA = new Update();
				Update updateB = new Update();
				updateA.set("channelIndex", channelB.getChannelIndex());
				updateB.set("channelIndex", channelA.getChannelIndex());
				//System.out.println(updateA.getUpdateObject());
				//System.out.println(updateB.getUpdateObject());
				channelDaoImp.update(channelA, updateA);
				channelDaoImp.update(channelB, updateB);
			}
		}else if(channelA.getState().equals(ChannelState.Activity) 
				&& channelB.getState().equals(ChannelState.Activity)){
			Update updateA = new Update();
			Update updateB = new Update();
			updateA.set("channelIndex", channelB.getChannelIndex());
			updateB.set("channelIndex", channelA.getChannelIndex());
			channelDaoImp.update(channelA, updateA);
			channelDaoImp.update(channelB, updateB);
		}
	}
	
	private int findFatherCount(){
		int count = 0;
		List<Channel> fathers = findAllFatherChannels();
		if(fathers != null && !fathers.isEmpty())
			return fathers.size();
		return count;
	}
	
	private int findSonCount(String fatherName){
		int count = 0;
		Channel sonChannel = new Channel();
		sonChannel.setRelated(fatherName);
		sonChannel.setState(ChannelState.Son);
		List<Channel> sons = channelDaoImp.find(sonChannel);
		if(sons != null && !sons.isEmpty())
			return sons.size();
		return count;
	}
	
	private int findActivityCount(){
		int count = 0;
		Channel activityChannel = new Channel();
		activityChannel.setState(ChannelState.Activity);
		List<Channel> activities = channelDaoImp.find(activityChannel);
		if(activities != null && !activities.isEmpty())
			return activities.size();
		return count;
	}
	
	private void updateChannelIndex(){
		List<Channel> fatherChannels = findAllFatherChannels();
		if(fatherChannels != null && !fatherChannels.isEmpty()){
			int i = 1;
			for(Channel fatherChannel : fatherChannels){
				Channel updateChannel = new Channel();
				Update update = new Update();
				updateChannel.setChannelName(fatherChannel.getChannelName());
				updateChannel.setState(ChannelState.Father);
				updateChannel.setEnglishName(fatherChannel.getEnglishName());
				update.set("channelIndex", i);
				channelDaoImp.update(updateChannel, update);
				i ++;
			}
		}
	}
	
	private void updateSonChannelIndex(String fatherName){
		Channel fatherChannel = new Channel();
		fatherChannel.setChannelName(fatherName);
		List<Channel> fatherChannels = channelDaoImp.find(fatherChannel);
		if(fatherChannels == null || fatherChannels.isEmpty()){
			return;
		}
		List<Channel> sonChannels = findAllSonChannels(fatherChannels.get(0).getEnglishName());
		if(sonChannels != null && !sonChannels.isEmpty()){
			int i = 1;
			for(Channel sonChannel : sonChannels){
				Channel updateChannel = new Channel();
				Update update = new Update();
				updateChannel.setState(ChannelState.Son);
				updateChannel.setRelated(fatherChannels.get(0).getChannelName());
				updateChannel.setChannelName(sonChannel.getChannelName());
				updateChannel.setEnglishName(sonChannel.getEnglishName());
				update.set("channelIndex", i);
				channelDaoImp.update(updateChannel, update);
				i ++;
			}
		}
	}
	
	private void updateActivityIndex(){
		Channel activityChannel = new Channel();
		activityChannel.setState(ChannelState.Activity);
		List<Channel> activities = channelDaoImp.find(activityChannel);
		if(activities != null && !activities.isEmpty()){
			int i = 1;
			for(Channel activity : activities){
				Channel updateChannel = new Channel();
				Update update = new Update();
				updateChannel.setState(ChannelState.Activity);
				updateChannel.setChannelName(activity.getChannelName());
				updateChannel.setEnglishName(activity.getEnglishName());
				update.set("channelIndex", i);
				channelDaoImp.update(updateChannel, update);
				i ++;
			}
		}
	}
}
