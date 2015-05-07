package com.shangbao.app.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.shangbao.model.persistence.Article;
/**
 * 为商报遗留app提供的数据model
 * @author yangyi
 */
public class CdsbModel {
	public int ResultCode;
	public String ResultMsg;
	public NewsData data;
	private SimpleDateFormat dft = new SimpleDateFormat("yyyy/MM/dd");
	
	public void addHotArticle(Article article){
		if(data.Hot == null){
			data.Hot = new ArrayList<NewsModel>();
		}
		NewsModel model = new NewsModel();
		model.NewsID = article.getId() + "";
		model.NewsTitle = article.getTitle();
		model.SubHead = article.getSubTitle() == null ? "" : article.getSubTitle();
		model.ContentUrlForiPhone = "http://www.cdsb.mobi/cdsb/app/ios/articledetail/" + article.getId();
		model.ImgeUrl = article.getPicturesUrl().isEmpty() ? "" : article.getPicturesUrl().get(0);
		model.NewsDate = dft.format(article.getTime());
		model.SortTime = article.getTime();
		model.CommentsCount = "" + article.getCrawlerCommendsPublish() + article.getNewsCommendsPublish();
		data.Hot.add(model);
	}
	public void addNormalArticle(Article article){
		if(data.Normal == null){
			data.Normal = new ArrayList<NewsModel>();
		}
		NewsModel model = new NewsModel();
		model.NewsID = article.getId() + "";
		model.NewsTitle = article.getTitle();
		model.SubHead = article.getSubTitle() == null ? "" : article.getSubTitle();
		model.ContentUrlForiPhone = "http://www.cdsb.mobi/cdsb/app/ios/articledetail/" + article.getId();
		model.ImgeUrl = article.getPicturesUrl().isEmpty() ? "" : article.getPicturesUrl().get(0);
		model.NewsDate = dft.format(article.getTime());
		model.SortTime = article.getTime();
		model.CommentsCount = "" + article.getCrawlerCommendsPublish() + article.getNewsCommendsPublish();
		data.Normal.add(model);
	}
}
class NewsData{
	public List<NewsModel> Hot;
	public List<NewsModel> Normal;
}
class NewsModel{
	public String NewsID;
	public String NewsCategory = "50";
	public String NewsType = "30";
	public String Keywords = "";
	public String Author = "";
	public String NewsTitle;
	public String SubHead;
	public String ContentUrlForiPhone;
	public String ContentUrlForiPad = "";
	public String ImgeUrl;
	public String NewsDate;
	public Date SortTime;
	public String NewsContent = "";
	public String CommentsCount;
	public String CommentsUrl = "";
	public String CommentsID = "";
}
