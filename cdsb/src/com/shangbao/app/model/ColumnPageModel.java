package com.shangbao.app.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.shangbao.model.persistence.Article;

public class ColumnPageModel {
	private int PageCount;//页数
	private int currentNo;//当前页码
	private List<NewsTitle> content = new ArrayList<NewsTitle>();
	
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

	public List<NewsTitle> getContent() {
		return content;
	}

	public void setContent(List<NewsTitle> content) {
		this.content = content;
	}
	
	public void addNewsTitle(Article article, Integer index){
		NewsTitle newsTitle = new NewsTitle(article, index, article.getId());
		this.content.add(newsTitle);
	}

	class NewsTitle{
		public String title;
		public String author;
		public List<String> midPicUrl = new ArrayList<String>();
		public List<String> simPicUrl = new ArrayList<>();
		public String summary;
		public Date time;
		public int clicks;
		public int comments;
		public Integer indexId;
		public Long newsId;
		public String activity;
		
		public NewsTitle(){
			
		}
		
		public NewsTitle(Article article, Integer indexId, Long newsId){
			this.title = article.getTitle().replace("\\n", "\n");
			this.author = article.getAuthor();
			//this.picUrl = article.getPicturesUrl();
			if(article.getPicturesUrl() != null && !article.getPicturesUrl().isEmpty()){
				for(String url : article.getPicturesUrl()){
//					String newUrl = url;
//					if(!article.isTag()){ //不是快拍成都的新闻
//						newUrl = url.substring(0, url.lastIndexOf("/")) + "/sim" + url.substring(url.lastIndexOf("/"));
//					}
//					picUrl.add(newUrl);
					//System.out.println(article.getTitle() + "  " + article.isTag() + " "  + url);
//					midPicUrl.add(url);
					if(url.substring(url.lastIndexOf("/") - 3, url.lastIndexOf("/")).equals("mid")){
						midPicUrl.add(url.replaceAll("/mid/", "/sim/"));
						simPicUrl.add(url.replaceAll("/mid/", "/sim/"));
					}
				}
			}
			this.summary = article.getSummary();
			this.time = article.getTime() == null ? new Date() : article.getTime();
			if(article.getOutSideUrl() == null || article.getOutSideUrl().isEmpty()){
//				this.clicks = article.getJs_clicks() * 100 + (int)((new Date().getTime() - article.getTime().getTime())/60000);
				this.clicks = article.getJs_clicks();
			}else{
//				this.clicks = article.getClicks() * 100 + (int)((new Date().getTime() - article.getTime().getTime())/60000);
				this.clicks = article.getClicks();
			}
			this.indexId = indexId;
			this.newsId = newsId;
			this.comments = article.getCrawlerCommendsPublish() + article.getNewsCommendsPublish();
			if(!article.getChannel().isEmpty()){
				for(String channel : article.getChannel()){
					if(channel.startsWith("#")){
						this.activity = channel;
					}
				}
			}
		}
	}
}
