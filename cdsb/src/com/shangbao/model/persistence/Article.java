package com.shangbao.model.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.shangbao.model.ArticleState;

@Document(collection="article")
public class Article {
	@Id
	private long id;
	private boolean tag = false;//是否属于图片新闻
	private String author;//作者
	private Long uid;
	private String summary;//摘要
	private String content;//内容
	private String title;//标题
	private String subTitle;//副标题
	private List<String> keyWord = new ArrayList<String>();//关键字
	@Indexed
	private Date time;//时间
	private String titlePicUrl;//标题图片url
	private List<String> picturesUrl = new ArrayList<String>();//图片url
	private ArticleState state;//状态
	private List<String> channel = new ArrayList<String>();//所属栏目
	private String activity; //所属活动
	private String level;//等级
	@Indexed
	private int words;//字数
	private int pictures;//图片数量
	@Indexed
	private int newsCommends;//商报评论数
	private int newsCommendsPublish;//商报评论发表数
	private int newsCommendsUnpublish;//商报评论未发表
	@Indexed
	private int crawlerCommends;//爬虫评论数
	private int crawlerCommendsPublish;//爬虫评论发表数
	private int crawlerCommendsUnpublish;//爬虫评论未发表数
	private int clicks;//点击数
	@Indexed
	private int js_clicks;//前端统计的点击数
	private int likes;//点赞数
	private String from;//来源
	private Map<String, Integer> channelIndex = new HashMap<String, Integer>();//文章在所属目录中的index
	//private Map<String, Boolean> channelTopTag = new HashMap<>();
	private List<String> logs = new ArrayList<>();//文章的操作记录
	private String outSideUrl; //外链文章URL
	
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public boolean isTag() {
		return tag;
	}
	public void setTag(boolean tag) {
		this.tag = tag;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	public List<String> getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(List<String> keyWord) {
		this.keyWord = keyWord;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getTitlePicUrl() {
		return titlePicUrl;
	}
	public void setTitlePicUrl(String titlePicUrl) {
		this.titlePicUrl = titlePicUrl;
	}
	public List<String> getPicturesUrl() {
		return picturesUrl;
	}
	public void setPicturesUrl(List<String> picturesUrl) {
		this.picturesUrl = picturesUrl;
	}
	public ArticleState getState() {
		return state;
	}
	public void setState(ArticleState state) {
		this.state = state;
	}
	public List<String> getChannel() {
		return channel;
	}
	public void setChannel(List<String> channel) {
		this.channel = channel;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public int getWords() {
		return words;
	}
	public void setWords(int words) {
		this.words = words;
	}
	public int getPictures() {
		return pictures;
	}
	public void setPictures(int pictures) {
		this.pictures = pictures;
	}
	public int getNewsCommends() {
		return newsCommends;
	}
	public void setNewsCommends(int newsCommends) {
		this.newsCommends = newsCommends;
	}
	public int getNewsCommendsPublish() {
		return newsCommendsPublish;
	}
	public void setNewsCommendsPublish(int newsCommendsPublish) {
		this.newsCommendsPublish = newsCommendsPublish;
	}
	public int getCrawlerCommends() {
		return crawlerCommends;
	}
	public void setCrawlerCommends(int crawlerCommends) {
		this.crawlerCommends = crawlerCommends;
	}
	public int getCrawlerCommendsPublish() {
		return crawlerCommendsPublish;
	}
	public void setCrawlerCommendsPublish(int crawlerCommendsPublish) {
		this.crawlerCommendsPublish = crawlerCommendsPublish;
	}
	public int getClicks() {
		return clicks;
	}
	public void setClicks(int clicks) {
		this.clicks = clicks;
	}
	public int getJs_clicks() {
		return js_clicks;
	}
	public void setJs_clicks(int js_clicks) {
		this.js_clicks = js_clicks;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	
	public Map<String, Integer> getChannelIndex() {
		return channelIndex;
	}
	public void setChannelIndex(Map<String, Integer> channelIndex) {
		this.channelIndex = channelIndex;
	}
	public List<String> getLogs() {
		return logs;
	}
	public void setLogs(List<String> logs) {
		this.logs = logs;
	}
	public int getNewsCommendsUnpublish() {
		return newsCommendsUnpublish;
	}
	public void setNewsCommendsUnpublish(int newsCommendsUnpublish) {
		this.newsCommendsUnpublish = newsCommendsUnpublish;
	}
	public int getCrawlerCommendsUnpublish() {
		return crawlerCommendsUnpublish;
	}
	public void setCrawlerCommendsUnpublish(int crawlerCommendsUnpublish) {
		this.crawlerCommendsUnpublish = crawlerCommendsUnpublish;
	}
	public String getOutSideUrl() {
		return outSideUrl;
	}
	public void setOutSideUrl(String outSideUrl) {
		this.outSideUrl = outSideUrl;
	}
	/**
	 * 添加关键字
	 * @param keyWord
	 */
	public void addKeyWord(String keyWord){
		if(!this.keyWord.contains(keyWord))
			this.keyWord.add(keyWord);
	}
	/**
	 * 删除关键字
	 * @param keyWord
	 */
	public void deleteKeyWord(String keyWord){
		if(this.keyWord.contains(keyWord))
			this.keyWord.remove(keyWord);
	}
	
	/**
	 * 添加图片
	 * @param url
	 */
	public void addPicture(String url){
		this.picturesUrl.add(url);
	}
	/**
	 * 删除图片
	 * @param url
	 */
	public void deletePicture(String url){
		if(this.picturesUrl.contains(url)){
			this.picturesUrl.remove(this.picturesUrl.indexOf(url));
		}
	}
	
	/**
	 * 添加一个所属类别
	 * @param channelName
	 */
	public void addChannel(String channelName){
		if(!this.channel.contains(channelName))
			this.channel.add(channelName);
	}
	/**
	 * 删除一个所属类别
	 * @param channelName
	 */
	public void deleteChannel(String channelName){
		if(this.channel.contains(channelName))
			this.channel.remove(channelName);
	}
}
