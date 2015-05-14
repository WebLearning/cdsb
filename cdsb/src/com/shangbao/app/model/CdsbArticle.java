package com.shangbao.app.model;

import com.shangbao.model.persistence.Article;

public class CdsbArticle {
	public int ResultCode = 0;
	public String ResultMsg = "";
	public CdsbArticleInfo DATA;
	
	public CdsbArticle(){
		
	}
	
	public CdsbArticle(Article article){
		CdsbArticleInfo data = new CdsbArticleInfo();
		data.NewsTitle = article.getTitle();
		data.SubHead = article.getSubTitle() == null ? "" : article.getSubTitle();
		data.CommentsCount = article.getNewsCommendsPublish() + article.getCrawlerCommendsPublish() + "";
		this.DATA = data;
	}
}

class CdsbArticleInfo{
	public String NewsTitle;
	public String SubHead = "";
	public String CommentsCount = "0";
	public String commentsUrl = "";
}
