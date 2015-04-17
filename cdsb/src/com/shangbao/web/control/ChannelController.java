package com.shangbao.web.control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.shangbao.model.ChannelState;
import com.shangbao.model.persistence.Channel;
import com.shangbao.model.persistence.StartPictures;
import com.shangbao.service.ChannelService;
import com.shangbao.service.StartPicturesService;

@Controller
@RequestMapping("/channel")
public class ChannelController {
	@Resource
	private ChannelService channelServiceImp;
	
	@Resource
	private StartPicturesService startPicturesServiceImp;
	
	public ChannelService getChannelServiceImp() {
		return channelServiceImp;
	}

	public void setChannelServiceImp(ChannelService channelServiceImp) {
		this.channelServiceImp = channelServiceImp;
	}

	public StartPicturesService getStartPicturesServiceImp() {
		return startPicturesServiceImp;
	}

	public void setStartPicturesServiceImp(
			StartPicturesService startPicturesServiceImp) {
		this.startPicturesServiceImp = startPicturesServiceImp;
	}

	/**
	 * 获取所有的顶级分类
	 * @return
	 */
	@RequestMapping(value="/channels", method=RequestMethod.GET)
	@ResponseBody
	public List<Channel> getFatherChannel(){
		return channelServiceImp.findAllFatherChannels();
	}
	
	/**
	 * 获取一个顶级分类下的所有子分类
	 * @param fatherChannelName
	 * @return
	 */
	@RequestMapping(value="/{fatherChannelName}/channels", method=RequestMethod.GET)
	@ResponseBody
	public List<Channel> getSonChannel(@PathVariable("fatherChannelName") String fatherChannelName){
//		System.out.println(fatherChannelName);
		return channelServiceImp.findAllSonChannels(fatherChannelName);
	}
	
	/**
	 * 获取所有的活动
	 * @return
	 */
//	@RequestMapping(value="/activities")
//	@ResponseBody
//	public List<Channel> getActivities(){
//		return channelServiceImp.findAllActivities();
//	}
	
	/**
	 * 添加一个分类
	 */
	@RequestMapping(value="/{channelState:Father|Son|Activity}", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String addChannel(@RequestBody Channel channel, @PathVariable("channelState") ChannelState state){
		channel.setState(state);
		return this.channelServiceImp.addChannel(channel);
	}
	
	@RequestMapping(value="/admin/addall", method = RequestMethod.GET)
	@ResponseBody
	public String finalAddChannels(){
		if(channelServiceImp.findAllFatherChannels() == null || channelServiceImp.findAllFatherChannels().isEmpty()){
			Channel channel1 = new Channel();
			channel1.setChannelName("爬虫");
			channel1.setEnglishName("pacong");
			channel1.setState(ChannelState.Father);
			channelServiceImp.addChannel(channel1);
			Channel channel2 = new Channel();
			channel2.setChannelName("要闻");
			channel2.setEnglishName("yaowen");
			channel2.setState(ChannelState.Father);
			channelServiceImp.addChannel(channel2);
			Channel channel3 = new Channel();
			channel3.setChannelName("投诉");
			channel3.setEnglishName("tousu");
			channel3.setState(ChannelState.Father);
			channelServiceImp.addChannel(channel3);
			Channel channel4 = new Channel();
			channel4.setChannelName("活动");
			channel4.setEnglishName("huodong");
			channel4.setState(ChannelState.Father);
			channelServiceImp.addChannel(channel4);
			Channel channel5 = new Channel();
			channel5.setChannelName("快拍");
			channel5.setEnglishName("kuaipai");
			channel5.setState(ChannelState.Father);
			channelServiceImp.addChannel(channel5);
		}
		return "OK";
	}
	
	
	/**
	 * 删除一个分类
	 */
	@RequestMapping(value="/delete/{channelState:Father|Son|Activity}", method=RequestMethod.POST)
	@ResponseBody
	public String deleteActivity(@RequestBody Channel channel, @PathVariable("channelState") ChannelState state){
		channel.setState(state);
		return this.channelServiceImp.deleteChannel(channel);
	}
	
	/**
	 * 交换两个分类位置
	 * @param fatherName
	 * @param state
	 * @param indexA
	 * @param indexB
	 * @return
	 */
	@RequestMapping(value="/{father}/{channelState:Father|Son|Activity}/swap/{indexA}/{indexB}", method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String swapChannels(@PathVariable("father") String fatherName,
								@PathVariable("channelState") ChannelState state,
								@PathVariable("indexA") int indexA,
								@PathVariable("indexB") int indexB){
		Channel channelA = new Channel();
		Channel channelB = new Channel();
		if(fatherName.equals("top")){
			List<Channel> fathers = channelServiceImp.findAllFatherChannels();
			if(fathers == null || fathers.isEmpty())
				return "failed";
			for(Channel father : fathers){
				if(father.getChannelIndex() == indexA)
					channelA = father;
				if(father.getChannelIndex() == indexB)
					channelB = father;
			}
			if(channelA.getChannelIndex() == 0 || channelB.getChannelIndex() == 0)
				return "failed";
			channelServiceImp.swapChannel(channelA, channelB);
			return "done";
		}else if(state.equals(ChannelState.Son)){
			Channel fatherChannel = channelServiceImp.findByEnName(fatherName, ChannelState.Father);
			List<Channel> sonChannels = channelServiceImp.findAllSonChannels(fatherChannel.getEnglishName());
			if(sonChannels != null && !sonChannels.isEmpty() && fatherChannel != null){
				for(Channel sonChannel : sonChannels){
					if(sonChannel.getChannelIndex() == indexA){
						channelA.setChannelName(sonChannel.getChannelName());
					}
					if(sonChannel.getChannelIndex() == indexB){
						channelB.setChannelName(sonChannel.getChannelName());
					}
				}
				channelA.setState(state);
				channelB.setState(state);
				channelA.setRelated(fatherChannel.getChannelName());
				channelB.setRelated(fatherChannel.getChannelName());
				channelA.setChannelIndex(indexA);
				channelB.setChannelIndex(indexB);
				channelServiceImp.swapChannel(channelA, channelB);
				return "done";
			}
		}else if(state.equals(ChannelState.Activity)){
			List<Channel> activities = channelServiceImp.findAllActivities();
			if(activities != null && !activities.isEmpty()){
				for(Channel activity : activities){
					if(activity.getChannelIndex() == indexA)
						channelA = activity;
					if(activity.getChannelIndex() == indexB)
						channelB = activity;
					if(channelA.getChannelIndex() != 0 && channelB.getChannelIndex() != 0){
						channelServiceImp.swapChannel(channelA, channelB);
					}
				}
			}
		}
		return "failed";
	}
	
	@RequestMapping(value="/startpictures", method=RequestMethod.GET)
	@ResponseBody
	public List<StartPictures> getStartPictures(){
		List<StartPictures> startPicturesList = new ArrayList<>();
		startPicturesList = startPicturesServiceImp.getAll();
		return startPicturesList;
	}
	
	@RequestMapping(value="/startpictures", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void creatPictures(@RequestBody StartPictures pictures){
		if(!pictures.getId().isEmpty()){
			startPicturesServiceImp.addStartPictures(pictures);
		}
	}
	
	@RequestMapping(value="/startpictures", method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void addPictures(@RequestBody StartPictures pictures){
		if(!pictures.getId().isEmpty() && !pictures.getPictureUrls().isEmpty()){
			startPicturesServiceImp.addPicture(pictures, pictures.getPictureUrls());
		}
	}
	
	@RequestMapping(value="/startpictures/deleteall", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deletePictures(@RequestBody StartPictures pictures){
		if(!pictures.getId().isEmpty()){
			startPicturesServiceImp.deleteAll(pictures);
		}
	}
	
	@RequestMapping(value="/startpictures/delete/{index}", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void deleteOne(@RequestBody StartPictures pictures, @PathVariable("index") int index){
		if(pictures.getId() != null){
			startPicturesServiceImp.delete(pictures, index);
		}
	}
	
	@RequestMapping(value="/startpictures/delete", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteStartPictures(@RequestBody StartPictures pictures){
		if(!pictures.getId().isEmpty() && pictures.getId() != null){
			startPicturesServiceImp.deleteAll(pictures);
		}
	}
	
	@RequestMapping(value = "/uploadstartpicture", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String uploadStartPicture(@RequestParam(value = "file", required = true) MultipartFile file){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmm");
		String returnString = "";
		String localhostString = "";
		Random random = new Random();
		String fileName = sdf.format(new Date()) + file.getSize() + "" + random.nextInt(1000) + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));//保存到本地的文件名
		Properties props = new Properties();
		try {
			props=PropertiesLoaderUtils.loadAllProperties("config.properties");
			String filePath = props.getProperty("pictureDir") + File.separator + "startPic";//目录的路径
			localhostString = props.getProperty("localhost");
			Path path = Paths.get(filePath);
			if(Files.notExists(path)){
				Path filPath = Files.createDirectories(path);
			}
			if(!file.isEmpty()){
				byte[] bytes;
				bytes = file.getBytes();
				FileOutputStream fos = new FileOutputStream(filePath + File.separator + fileName);
				fos.write(bytes); // 写入文件
				fos.close();
				returnString = path.toString().split("cdsb")[1] + File.separator + fileName;
//				System.out.println(returnString);
				return localhostString + returnString.replaceAll("\\\\", "/");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}
}
