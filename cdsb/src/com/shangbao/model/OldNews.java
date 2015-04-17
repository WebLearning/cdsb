package com.shangbao.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.shangbao.model.persistence.Article;


public class OldNews {
	public int ResultCode;
	public String ResultMsg = "";
	public OldNewsTitles data = new OldNewsTitles();
	private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
	
	public void add(){
		OldNewsTitle title = new OldNewsTitle();
		title.CommendsUrl = "sdfsdf";
		title.ContentUrlForiPad = "sdfsdf";
		data.Hot.add(title);
		data.Normal.add(title);
	}
	
	public void addHot(Article article, int page, String url){
		if(article != null){
			OldNewsTitle title = new OldNewsTitle();
			title.NewsID = article.getId() + "";
			title.SubHead = article.getSubTitle();
			title.ContentUrlForiPhone = url + "/" + article.getId();
			title.ImageUrl = article.getPicturesUrl().isEmpty() ? article.getPicturesUrl().get(0) : "";
			title.NewsDate = format.format(article.getTime());
			title.SortTime = page;
			title.CommentsCount = article.getCrawlerCommendsPublish() + article.getNewsCommendsPublish() + "";
			data.Hot.add(title);
		}
	}
	
	public void addNormal(Article article, int page, String url){
		if(article != null){
			OldNewsTitle title = new OldNewsTitle();
			title.NewsID = article.getId() + "";
			title.SubHead = article.getSubTitle();
			title.ContentUrlForiPhone = url + "/" + article.getId();
			title.ImageUrl = article.getPicturesUrl().isEmpty() ? article.getPicturesUrl().get(0) : "";
			title.NewsDate = format.format(article.getTime());
			title.SortTime = page;
			title.CommentsCount = article.getCrawlerCommendsPublish() + article.getNewsCommendsPublish() + "";
			data.Normal.add(title);
		}
	}
}

class OldNewsTitles {
	public List<OldNewsTitle> Hot = new ArrayList<>();
	public List<OldNewsTitle> Normal = new ArrayList<>();
}

class OldNewsTitle{
	public String NewsID;
	public String NewsCategory = "50";
	public String NewsType = "30";
	public String Keywords;
	public String SubHead;
	public String ContentUrlForiPhone;
	public String ContentUrlForiPad;
	public String ImageUrl;
	public String NewsDate;
	public int SortTime;
	public String NewsContent;
	public String CommentsCount;
	public String CommendsUrl = "";
	public String CommentsID = "";
}
