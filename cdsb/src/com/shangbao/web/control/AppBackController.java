package com.shangbao.web.control;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shangbao.app.service.AppService;
import com.shangbao.app.service.AppService.BackChannelModel;

/**
 * 后台管理app内容的Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/backapp")
public class AppBackController {
	@Resource
	private AppService appService;
	
	/**
	 * 返回当前所有Channel以及文章列表
	 * @return
	 */
	@RequestMapping(value="/all", method=RequestMethod.GET)
	@ResponseBody
	public List<BackChannelModel> getChannelAndArticle(){
		
		return appService.getAllChannels();
	}

	@RequestMapping(value="/setlocation/{channelName}/{index}/{tag}", method=RequestMethod.PUT)
	@ResponseBody
	public String setArticleLocation(@PathVariable("channelName") String channelName,
									 @PathVariable("index") int index,
									 @PathVariable("tag") boolean tag){
		
		appService.setArticleLocation(channelName, index, tag);
		return "done";
	}
	
	@RequestMapping(value="/settop/{channelName}/{index}", method=RequestMethod.PUT)
	@ResponseBody
	public String setTopArticle(@PathVariable("channelName") String channelName,
								@PathVariable("index") int index){
		appService.setArticleTop(channelName, index);
		return "done";
	}
	
	@RequestMapping(value="/unsettop/{channelName}/{index}", method=RequestMethod.PUT)
	@ResponseBody
	public String unSetTopArticle(@PathVariable("channelName") String channelName,
			@PathVariable("index") int index){
		appService.unSetArticleTop(channelName, index);
		return "done";
	}
	
	@RequestMapping(value="/refresh")
	@ResponseBody
	public List<BackChannelModel> refresh(){
		appService.refresh();
		return appService.getAllChannels();
	}
	
	public AppService getAppService() {
		return appService;
	}

	public void setAppService(AppService appService) {
		this.appService = appService;
	}

	
}
