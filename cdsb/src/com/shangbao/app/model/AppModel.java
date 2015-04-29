package com.shangbao.app.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.shangbao.dao.ArticleDao;
import com.shangbao.dao.ChannelDao;
import com.shangbao.dao.CommendDao;
import com.shangbao.dao.StartPicturesDao;
import com.shangbao.model.ArticleState;
import com.shangbao.model.ChannelState;
import com.shangbao.model.CommendState;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.Channel;
import com.shangbao.model.persistence.Commend;
import com.shangbao.model.persistence.NewsCommend;
import com.shangbao.model.persistence.StartPictures;
import com.shangbao.model.show.SingleCommend;
import com.shangbao.service.ClickCountService;
import com.shangbao.service.ClickService;
import com.shangbao.service.PendTagService;

@Component
@Scope("singleton")
public class AppModel {
	
	private ArticleDao articleDaoImp;
	private ChannelDao channelDaoImp;
	private CommendDao commendDaoImp;
	private StartPicturesDao startPicturesDaoImp;
	@Resource
	private PendTagService pendTagServiceImp;
	@Resource
	private ClickCountService clickCountServiceImp;
	@Resource
	private ClickService clickServiceImp;
	
	private Map<String, List<String>> startPictures = new ConcurrentHashMap<String, List<String>>();//启动显示图片
	private ReentrantReadWriteLock startPicLock = new ReentrantReadWriteLock();
	private Map<String, List<Article>> appMap = new ConcurrentHashMap<String, List<Article>>();//每个channel包含的文章
	private ReentrantReadWriteLock appMapLock = new ReentrantReadWriteLock();
	private List<ChannelModel> channelModels = new CopyOnWriteArrayList<ChannelModel>();//所有的channel
	private ReentrantReadWriteLock channelModelsLock = new ReentrantReadWriteLock();
	private Map<Long, List<SingleCommend>> commends = new ConcurrentHashMap<Long, List<SingleCommend>>();//每篇文章的评论
	private ReentrantReadWriteLock commendsLock = new ReentrantReadWriteLock();
	private List<Channel> activities = new CopyOnWriteArrayList<Channel>();
	private ReentrantReadWriteLock activitiesLock = new ReentrantReadWriteLock();
	private Map<String, String> channelEn_Cn = new ConcurrentHashMap<String, String>();//key:英文名； value:中文名
	private ReentrantReadWriteLock channelEn_CnLock = new ReentrantReadWriteLock();
	private Map<Long, Article> articleMap = new ConcurrentHashMap<>();//key是文章的id，value对应一篇文章
	private ReentrantReadWriteLock articleMapLock = new ReentrantReadWriteLock();
	
	
	@Autowired
	public AppModel(@Qualifier("articleDaoImp") ArticleDao articleDaoImp, 
			@Qualifier("channelDaoImp") ChannelDao channelDaoImp,
			@Qualifier("commendDaoImp") CommendDao commendDaoImp,
			@Qualifier("startPicturesDaoImp") StartPicturesDao startPicturesDaoImp){
		this.articleDaoImp = articleDaoImp;
		this.channelDaoImp = channelDaoImp;
		this.commendDaoImp = commendDaoImp;
		this.startPicturesDaoImp = startPicturesDaoImp;
		
		System.out.println("Init!");
		redeployStartPictures();
		//初始化appMap，articleMap
		//List<Channel> channels = channelDaoImp.find(new Channel());
		List<Channel> channels = getChannelOrdered();
		List<String> channelNames = new ArrayList<>();
		for(Channel channel : channels){
			channelNames.add(channel.getChannelName());
		}
		redeployChannelArticles(channelNames);
		
		//打印
//		for(String key : appMap.keySet()){
//			System.out.println("key: " + key + "  value: ");
//			for(Article article : appMap.get(key)){
//				System.out.println("   " + article.getId() + "  " + article.getChannelIndex());
//			}
//		}
		
		//初始化commends
		Article criteriaArticle = new Article();
		criteriaArticle.setState(ArticleState.Published);
		List<Article> articles = articleDaoImp.find(criteriaArticle);
		List<Long> articleIds = new ArrayList<>();
		for(Article article : articles){
			//redeployComment(article.getId());
			articleIds.add(article.getId());
		}
		redeployComment(articleIds);
				
		//初始化channelModels channelEn_Cn activities
		redeployChannels();
		//打印
//		for(String key : channelEn_Cn.keySet()){
//			System.out.println("key: " + key + "  value: " + channelEn_Cn.get(key));
//		}
	}
	
	public List<Channel> getChannelOrdered(){
		List<Channel> channels = new ArrayList<>();
		Channel criteriaChannel = new Channel();
		criteriaChannel.setState(ChannelState.Father);
		Sort fatherSort = new Sort(Direction.ASC, "channelIndex");
		List<Channel> fathers = channelDaoImp.find(criteriaChannel, fatherSort);
		channels.addAll(fathers);
		if(!fathers.isEmpty()){
			for(Channel father : fathers){
				Channel sonCriteriaChannel = new Channel();
				sonCriteriaChannel.setRelated(father.getChannelName());
				sonCriteriaChannel.setState(ChannelState.Son);
				channels.addAll(channelDaoImp.find(sonCriteriaChannel, fatherSort));
			}
		}
		return channels;
	}
	
	public void redeployStartPictures(){
		//startPictures.clear();
		Map<String, List<String>> startPicTemp = new ConcurrentHashMap<>();
		List<StartPictures> startPictures = startPicturesDaoImp.find(new StartPictures());
		if(!startPictures.isEmpty()){
			for(StartPictures pictures : startPictures){
				//this.startPictures.put(pictures.getId(), pictures.getPictureUrls());
				startPicTemp.put(pictures.getId(), pictures.getPictureUrls());
			}
		}
		startPicLock.writeLock().lock();
		try{
			this.startPictures = startPicTemp;
		}finally{
			startPicLock.writeLock().unlock();
		}
	}
	
	/**
	 * 更新appModel articleMap
	 * @param channelName
	 */
	public void redeployChannelArticles(List<String> channelNames){
		 Map<String, List<Article>> appMapTemp = new ConcurrentHashMap<>();
		 Map<Long, Article> articleMapTemp = new ConcurrentHashMap<>();
		 for(String channelName : channelNames){
			 Article criteriaArticle = new Article();
			 criteriaArticle.addChannel(channelName);
			 criteriaArticle.setState(ArticleState.Published);
			 List<Article> articles = articleDaoImp.find(criteriaArticle, Direction.DESC, "channelIndex." + channelName);
			 if(articles != null && !articles.isEmpty()){
				 appMapTemp.put(channelName, articles);
				 for(Article article : articles){
					 articleMapTemp.put(article.getId(), article);
				 }
			 }
		 }
		 appMapLock.writeLock().lock();
		 try{
			 this.appMap = appMapTemp;
			 //this.appMap.putAll(appMapTemp);
		 }finally{
			 appMapLock.writeLock().unlock();
		 }
		 articleMapLock.writeLock().lock();
		 try{
			 this.articleMap = articleMapTemp;
			 //this.articleMap.putAll(articleMapTemp);
		 }finally{
			 articleMapLock.writeLock().unlock();
		 }
	}
	
	public void redeployChannelArticles(String channelName){
		Article criteriaArticle = new Article();
		criteriaArticle.addChannel(channelName);
		criteriaArticle.setState(ArticleState.Published);
		List<Article> articles = articleDaoImp.find(criteriaArticle, Direction.DESC, "channelIndex." + channelName);
		if(articles != null && !articles.isEmpty()){
			this.appMap.put(channelName, articles);
		}
	}
	
	/**
	 * 更新评论
	 * @param articleId
	 */
	public void redeployComment(List<Long> articleIds){
		Map<Long, List<SingleCommend>> commendsTemp = new ConcurrentHashMap<>();
		for(Long articleId : articleIds){
			Commend criteriaCommend = new Commend();
			List<SingleCommend> singleCommends = new ArrayList<SingleCommend>();
			criteriaCommend.setArticleId(articleId);
			List<Commend> commends = commendDaoImp.find(criteriaCommend);
			if(commends != null && !commends.isEmpty()){
				for(Commend commend : commends){
					if(commend.getCommendList() != null && !commend.getCommendList().isEmpty()){
						for(SingleCommend singleCommend : commend.getCommendList()){
							if(singleCommend.getState() != null && singleCommend.getState().equals(CommendState.published)){
								singleCommends.add(singleCommend);
							}
						}
					}
				}
			}
			Collections.sort(singleCommends, new Comparator<SingleCommend>() {
				@Override
				public int compare(SingleCommend o1, SingleCommend o2) {
					if(o1.getTimeDate() != null && o2.getTimeDate() != null){
						return o2.getTimeDate().compareTo(o1.getTimeDate());
					}
					return 0;
				}
			});
			commendsTemp.put(articleId, singleCommends);
		}
		//this.commends.put(articleId, singleCommends);
		commendsLock.writeLock().lock();
		try{
			this.commends = commendsTemp;
			//this.commends.putAll(commendsTemp);
		}finally{
			commendsLock.writeLock().unlock();
		}
	}
	
	/**
	 * 更新channels
	 */
	public void redeployChannels(){
		Map<String, String> channelEn_CnTemp = new ConcurrentHashMap<>();
		List<Channel> activitiesTemp = new CopyOnWriteArrayList<>();
		List<ChannelModel> channelModelsTemp = new CopyOnWriteArrayList<>();
		Channel criteriaChannel = new Channel();
		criteriaChannel.setState(ChannelState.Father);
		List<Channel> fatherChannels = channelDaoImp.find(criteriaChannel, new Sort(Direction.ASC, "channelIndex"));
		if(fatherChannels != null && !fatherChannels.isEmpty()){
			for(Channel fatherChannel : fatherChannels){
				//channelEn_Cn.put(fatherChannel.getEnglishName(), fatherChannel.getChannelName());
				channelEn_CnTemp.put(fatherChannel.getEnglishName(), fatherChannel.getChannelName());
				Channel sonCriteriaChannel = new Channel();
				sonCriteriaChannel.setState(ChannelState.Son);
				sonCriteriaChannel.setRelated(fatherChannel.getChannelName());
				List<Channel> sonChannels = channelDaoImp.find(sonCriteriaChannel, new Sort(Direction.ASC, "channelIndex"));
				//addTopChannel(fatherChannel, sonChannels);
				if (!channelModelsTemp.contains(fatherChannel)) {
					ChannelModel channelModel = new ChannelModel();
					channelModel.fatherChannel = fatherChannel;
					if (sonChannels == null || sonChannels.isEmpty()) {
						channelModel.sonChannels = null;
					} else {
						channelModel.sonChannels = sonChannels;
					}
					channelModelsTemp.add(channelModel);
				}
				
				for(Channel sonChannel : sonChannels){
					//channelEn_Cn.put(sonChannel.getEnglishName(), sonChannel.getChannelName());
					channelEn_CnTemp.put(sonChannel.getEnglishName(), sonChannel.getChannelName());
					if(sonChannel.getChannelName().startsWith("#")){
						//activities.add(sonChannel);
						activitiesTemp.add(sonChannel);
					}
				}
			}
		}
		channelEn_CnLock.writeLock().lock();
		try{
			this.channelEn_Cn = channelEn_CnTemp;
			//this.channelEn_Cn.putAll(channelEn_CnTemp);
		}finally{
			channelEn_CnLock.writeLock().unlock();
		}
		activitiesLock.writeLock().lock();
		try{
			this.activities = activitiesTemp;
			//this.activities.addAll(activitiesTemp);
		}finally{
			activitiesLock.writeLock().unlock();
		}
		channelModelsLock.writeLock().lock();
		try{
			this.channelModels = channelModelsTemp;
			//this.channelModels.addAll(channelModelsTemp);
		}finally{
			channelModelsLock.writeLock().unlock();
		}
//		Channel criteriaActivity = new Channel();
//		criteriaActivity.setState(ChannelState.Activity);
//		this.activities.addAll(channelDaoImp.find(criteriaActivity));
	}
	
	/**
	 * 全部更新
	 */
	public void redeployAll(){
		//appMap.clear();
		//channelModels.clear();
		//commends.clear();
		//activities.clear();
		//channelEn_Cn.clear();
		redeployStartPictures();
		
		redeployChannels();
		
		List<Channel> channels = channelDaoImp.find(new Channel());
		List<String> channelNames = new ArrayList<>();
		for(Channel channel : channels){
			channelNames.add(channel.getChannelName());
		}
		redeployChannelArticles(channelNames);
		
		Article criteriaArticle = new Article();
		criteriaArticle.setState(ArticleState.Published);
		List<Article> articles = articleDaoImp.find(criteriaArticle);
		List<Long> articleIds = new ArrayList<>();
		for(Article article : articles){
//			redeployComment(article.getId());
			articleIds.add(article.getId());
		}
		redeployComment(articleIds);
		
		//redeployChannels();
	}
	
	public Map<String, List<String>> getStartPictures() {
		startPicLock.readLock().lock();
		try{
			return startPictures;
		}finally{
			startPicLock.readLock().unlock();
		}
	}
	
	public List<String> getStartPictures(String id){
		startPicLock.readLock().lock();
		try{
			if(startPictures.containsKey(id)){
				return startPictures.get(id);
			}
		}finally{
			startPicLock.readLock().unlock();
		}
		return null;
	}

//	public void setStartPictures(List<String> startPictures) {
//		this.startPictures = startPictures;
//	}
	
	public Map<String, List<Article>> getAppMap() {
		appMapLock.readLock().lock();
		try{
			return appMap;
		}finally{
			appMapLock.readLock().unlock();
		}
	}

//	public void setAppMap(Map<String, List<Article>> appMap) {
//		this.appMap = appMap;
//	}

	public List<ChannelModel> getChannelModels() {
		channelModelsLock.readLock().lock();
		try{
			return channelModels;
		}finally{
			channelModelsLock.readLock().unlock();
		}
	}

//	public void setChannelModels(List<ChannelModel> channelModels) {
//		this.channelModels = channelModels;
//	}

	public Map<Long, List<SingleCommend>> getCommends() {
		commendsLock.readLock().lock();
		try{
			return commends;
		}finally{
			commendsLock.readLock().unlock();
		}
	}

//	public void setCommends(Map<Long, List<SingleCommend>> commends) {
//		this.commends = commends;
//	}

	public List<Channel> getActivities() {
		activitiesLock.readLock();
		try{
			return activities;
		}finally{
			activitiesLock.readLock().unlock();
		}
	}

//	public void setActivities(List<Channel> activities) {
//		this.activities = activities;
//	}

	public Map<String, String> getChannelEn_Cn() {
		channelEn_CnLock.readLock().lock();
		try{
			return channelEn_Cn;
		}finally{
			channelEn_CnLock.readLock().unlock();
		}
	}


//	public void setChannelEn_Cn(Map<String, String> channelEn_Cn) {
//		this.channelEn_Cn = channelEn_Cn;
//	}


	public Map<Long, Article> getArticleMap() {
		articleMapLock.readLock().lock();
		try{
			return articleMap;
		}finally{
			articleMapLock.readLock().unlock();
		}
	}

	/**
	 * 添加开始图片
	 * @param picUrl
	 */
//	public void addStartPicture(String picUrl){
//		if(!this.startPictures.contains(picUrl)){
//			this.startPictures.add(picUrl);
//		}
//	}
	
	/**
	 * 添加开始图片
	 * @param picUrl
	 * @param picIndex
	 */
//	public void addStartPicture(String picUrl, int picIndex){
//		if(picIndex > 0){
//			this.startPictures.add(picIndex, picUrl);
//		}
//	}
	
	/**
	 * 删除开始图片
	 * @param picUrl
	 */
//	public void deleteStartPicture(String picUrl){
//		if(this.startPictures.contains(picUrl)){
//			this.startPictures.remove(picUrl);
//		}
//	}
	
	/**
	 * 按位置删除开始图片
	 * @param picIndex
	 */
	public void deleteStartPicture(int picIndex){
		if(picIndex > 0 && picIndex <= this.startPictures.size() + 1){
			this.startPictures.remove(picIndex - 1);
		}
	}

//	public void addTopChannel(Channel fatherChannel, List<Channel> sonChannels) {
//		if (!this.channelModels.contains(fatherChannel)) {
//			ChannelModel channelModel = new ChannelModel();
//			channelModel.fatherChannel = fatherChannel;
//			if (sonChannels == null || sonChannels.isEmpty()) {
//				channelModel.sonChannels = null;
//			} else {
//				channelModel.sonChannels = sonChannels;
//			}
//			channelModelsLock.writeLock().lock();
//			try{
//				this.channelModels.add(channelModel);
//			}finally{
//				channelModelsLock.writeLock().unlock();
//			}
//		}
//	}

	public void deleteTopChannel(Channel fatherChannel) {
		ChannelModel deleteModel = new ChannelModel();
		deleteModel.fatherChannel = fatherChannel;
		if (this.channelModels.contains(deleteModel)) {
			channelModelsLock.writeLock().lock();
			try{
				this.channelModels.remove(deleteModel);
			}finally{
				channelModelsLock.writeLock().unlock();
			}
		}
	}

	
	public void addActivity(Channel activity){
		if(activity.getState().equals(ChannelState.Activity)){
			if(!this.activities.isEmpty()){
				for(Channel channel : this.activities){
					if(channel.getChannelName().equals(activity.getChannelName()))
						return;
				}
			}
			this.activities.add(activity);
		}
	}
	
	/**
	 * 获取所有一级分类
	 * @return
	 */
	public List<Channel> getTopChannels(){
		List<Channel> topChannels = new ArrayList<Channel>();
		if(!this.channelModels.isEmpty()){
			for(ChannelModel channelModel : this.channelModels){
				topChannels.add(channelModel.fatherChannel);
			}
			return topChannels;
		}
		return null;
	}
	
	/**
	 * 获取某个一级分类下的子分类
	 * @param fatherChannel 一级分类名
	 * @return
	 */
	public List<Channel> getSonChannels(Channel fatherChannel){
		ChannelModel channelModel = new ChannelModel();
		channelModel.fatherChannel = fatherChannel;
		if(this.channelModels.contains(channelModel)){
			return this.channelModels.get(this.channelModels.indexOf(channelModel)).sonChannels;
		}
		return null;
	}
	
	/**
	 * 添加一个评论
	 * @param articleId 评论文章的id
	 * @param singleCommend 添加的评论
	 */
	public void addComment(Long articleId, SingleCommend singleCommend){
		Update update = new Update();
		if(pendTagServiceImp.isTag("comment")){
			singleCommend.setState(CommendState.unpublished);
		}else{
			singleCommend.setState(CommendState.published);
		}
		singleCommend.setTimeDate(new Date());
		singleCommend.setCommendId("" + new Date().getTime() + singleCommend.getUserId() + (int)(Math.random()*1000));
		NewsCommend newsCommend = new NewsCommend();
		newsCommend.setArticleId(articleId);
		List<Commend> commends = commendDaoImp.find(newsCommend);
		if(commends != null && !commends.isEmpty()){
			update.push("commendList", singleCommend);
			commendDaoImp.update(newsCommend, update);
		}else{
			//如果当前没有该文章的评论
			Article article = new Article();
			article.setId(articleId);
			List<Article> articles = articleDaoImp.find(article);
			if(articles != null && !articles.isEmpty()){
				newsCommend.setArticleTitle(articles.get(0).getTitle());
				newsCommend.getCommendList().add(singleCommend);
				commendDaoImp.insert(newsCommend);
			}
		}
		
		Article criteriaArticle = articleDaoImp.findById(articleId);
		int newsCommends = criteriaArticle.getNewsCommends();
		Update articleUpdate = new Update();
		articleUpdate.set("newsCommends", newsCommends + 1);
		if(pendTagServiceImp.isTag("comment")){
			articleUpdate.set("newsCommendsUnpublish", criteriaArticle.getNewsCommends() - criteriaArticle.getNewsCommendsPublish() + 1);
		}else{
			articleUpdate.inc("newsCommendsPublish", 1);
		}
		Article tempArticle = new Article();
		tempArticle.setId(criteriaArticle.getId());
		articleDaoImp.update(tempArticle, articleUpdate);
	}
	
	/**
	 * 点赞
	 * @param articleId
	 */
	public int addLike(Long articleId){
		Article criteriaArticle = new Article();
		criteriaArticle.setId(articleId);
		Update update = new Update();
		appMapLock.readLock().lock();
		try{
			if(articleMap.get(articleId) != null){
				synchronized (AppModel.class) {
					articleMap.get(articleId).setLikes(articleMap.get(articleId).getLikes() + 1);
				}
				update.set("likes", articleMap.get(articleId).getLikes());
				articleDaoImp.update(criteriaArticle, update);
				return articleMap.get(articleId).getLikes();
			}
		}finally{
			appMapLock.readLock().unlock();
		}
		return 0;
	}
	
	public int getLike(Long articleId){
		appMapLock.readLock().lock();
		try{
			if(articleMap.containsKey(articleId)){
				return articleMap.get(articleId).getLikes();
			}
		}finally{
			appMapLock.readLock().unlock();
		}
		return 0;
	}
	
	/**
	 * 点击
	 * @param articleId
	 */
	public void addClick(Long articleId, String fromIp){
		Article criteriaArticle = new Article();
		criteriaArticle.setId(articleId);
		Update update = new Update();
		Article article = null;
		articleMapLock.readLock().lock();
		try{
			if((article = articleMap.get(articleId)) != null){
				synchronized (AppModel.class) {
					articleMap.get(articleId).setClicks(articleMap.get(articleId).getClicks() + 1);
				}
				update.set("clicks", articleMap.get(articleId).getClicks());
				articleDaoImp.update(criteriaArticle, update);
			}
		}finally{
			articleMapLock.readLock().unlock();
		}
		if(article != null){
			clickCountServiceImp.add(article.getId(), article.getTitle(), new Date());
			clickServiceImp.add(article.getId(), fromIp, null, true);
		}
	}
	
	public int addJsClick(Long articleId, String fromIp, String udid){
		Article criteriaArticle = new Article();
		criteriaArticle.setId(articleId);
		Update update = new Update();
		articleMapLock.readLock().lock();
		try{
			Article article;
			if((article = articleMap.get(articleId)) != null){
				synchronized (this){
					articleMap.get(articleId).setJs_clicks(articleMap.get(articleId).getJs_clicks() + 1);
				}
				update.set("js_clicks", articleMap.get(articleId).getJs_clicks());
				articleDaoImp.update(criteriaArticle, update);
				//return article.getJs_clicks() * 100 + (int)((new Date().getTime() - article.getTime().getTime())/60000);
				if(article != null){
					clickCountServiceImp.add(article.getId(), article.getTitle(), new Date());
					clickServiceImp.add(article.getId(), fromIp, udid, false);
				}
				return articleMap.get(articleId).getJs_clicks();
			}
		}finally{
			articleMapLock.readLock().unlock();
		}
		return 0;
	}
	
	public int getJsClick(Long articleId){
		articleMapLock.readLock().lock();
		try{
			Article article;
			if((article = articleMap.get(articleId)) != null){
				int returnNum = article.getJs_clicks() * 100 + (int)((new Date().getTime() - article.getTime().getTime())/60000);
//				return returnNum;
				return articleMap.get(articleId).getJs_clicks();
			}
		}finally{
			articleMapLock.readLock().unlock();
		}
		return 0;
	}
	
	public void postPictures(Article pictureArticle){
		pictureArticle.setTag(true);
		pictureArticle.addChannel("PK台");
		StringBuilder content = new StringBuilder();
		if(!pictureArticle.getPicturesUrl().isEmpty()){
			for(String url : pictureArticle.getPicturesUrl()){
				content.append("<p style=\"text-align:center\"><img src=\"" + url + "\"/></p>");
			}
			content.append("<p style=\"text-indent:\">" + pictureArticle.getSummary() + "</p>");
		}
		pictureArticle.setContent(content.toString());
		pictureArticle.setState(ArticleState.Published);
		articleDaoImp.insert(pictureArticle);
	}
	
	/**
	 * 根据文章id返回文章评论列表
	 * @param articleId
	 * @return
	 */
	public List<SingleCommend> findComments(Long articleId){
		if(this.commends.containsKey(articleId)){
			return this.commends.get(articleId);
		}
		return null;
	}
	
	/**
	 * 将一个文章置顶
	 * @param channelName 栏目名称
	 * @param articleId 文章Id
	 */
	public void setTopArticle(String channelName, int index){
		if(!appMap.containsKey(channelName)){
			return;
		}
		Long articleId;
		appMapLock.readLock().lock();
		try{
			articleId = appMap.get(channelName).get(index).getId();
		}finally{
			appMapLock.readLock().unlock();
		}
		articleDaoImp.setTopArticle(channelName, articleId);
		redeployChannelArticles(channelName);
	}
	
	public void unSetTopArticle(String channelName, int index){
		if(!appMap.containsKey(channelName)){
			return;
		}
		Long articleId;
		appMapLock.readLock().lock();
		try{
			articleId = appMap.get(channelName).get(index).getId();
		}finally{
			appMapLock.readLock().unlock();
		}
		articleDaoImp.unSetTopArticle(channelName, articleId);
		redeployChannelArticles(channelName);
	}
	
	/**
	 * 交换两个文章的位置
	 * @param channelName 栏目名称
	 * @param articleAId 文章A的Id
	 * @param articleBId 文章B的Id
	 */
	public void swapArticle(String channelName, Long articleAId, Long articleBId){
		if(!appMap.containsKey(channelName)){
			return;
		}
		articleDaoImp.swapArticle(channelName, articleAId, articleBId);
		redeployChannelArticles(channelName);
	}
	
	class ChannelModel {
		public Channel fatherChannel;
		public List<Channel> sonChannels = new LinkedList<Channel>();

		@Override
		public boolean equals(Object object) {
			ChannelModel channelModel = (ChannelModel) object;
			if (fatherChannel != null && channelModel.fatherChannel != null) {
				if (fatherChannel.getChannelName() != null
						&& channelModel.fatherChannel.getChannelName() != null) {
					if (fatherChannel.getEnglishName() != null
							&& channelModel.fatherChannel.getEnglishName() != null) {
						return fatherChannel.getChannelName().equals(
								channelModel.fatherChannel.getChannelName())
								&& fatherChannel.getEnglishName().equals(
										channelModel.fatherChannel
												.getEnglishName());
					} else if (fatherChannel.getEnglishName() == null
							&& channelModel.fatherChannel.getEnglishName() == null) {
						return fatherChannel.getChannelName().equals(
								channelModel.fatherChannel.getChannelName());
					}
				}
			}
			return false;
		}

		@Override
		public int hashCode() {
			if (fatherChannel != null) {
				return fatherChannel.getChannelName().hashCode();
			} else {
				return 0;
			}
		}
	}
}
