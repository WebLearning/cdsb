package com.shangbao.app.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.shangbao.model.persistence.Article;

/**
 * 点击大类后获得的页面(不包括商报原创)
 * @author Administrator
 *
 */
public class AppChannelModel {
	private String channelName;
	private List<Column> contentColumns = new ArrayList<Column>();
	
	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public List<Column> getContentColumns() {
		return contentColumns;
	}

	public void setContentColumns(List<Column> contentColumns) {
		this.contentColumns = contentColumns;
	}
	
	public void addColumn(String columnName, String columnEnName, List<Article> articles){
		Column column = new Column();
		column.columnName = columnName;
		column.columnEnglishName = columnEnName;
		if(!articles.isEmpty()){
			int index = 1;
			for(Article article : articles){
				AppTitle appTitle = new AppTitle(article, index);
				column.content.add(appTitle);
				index ++;
			}
		}
		this.contentColumns.add(column);
	}

	/*
	 * 分栏
	 */
	class Column{
		public String columnName;
		public String columnEnglishName;
		public List<AppTitle> content = new ArrayList<AppTitle>();
	}
	
	/*
	 * 首页新闻显示标题
	 */
	class AppTitle{
		public String title;
		public String author;
		public List<String> simPicUrl = new ArrayList<>();
		public List<String> midPicUrl = new ArrayList<>();
		public String summary;
		public Date time;
		public int clicks;
		public int comments;
		public Integer indexId;
		public Long newsId;
		public AppTitle(){	
		}
		public AppTitle(Article article, Integer id){
			this.title = article.getTitle().replace("\\n", "\n");
			this.author = article.getAuthor();
			//this.picUrl = article.getPicturesUrl();
			if(article.getPicturesUrl() != null && !article.getPicturesUrl().isEmpty()){
				for(String url : article.getPicturesUrl()){
//					if(!article.isTag()){
//						String newUrl = url.substring(0, url.lastIndexOf("/")) + "/sim" + url.substring(url.lastIndexOf("/"));
//						picUrl.add(newUrl);
//					}else{
//						picUrl.add(url);
//					}
//					midPicUrl.add(url);
					if(url.contains("/mid/")){
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
			this.comments = article.getCrawlerCommendsPublish() + article.getNewsCommendsPublish();
			indexId = id;
			this.newsId = article.getId();
		}
	}
}
