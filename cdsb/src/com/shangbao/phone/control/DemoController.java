package com.shangbao.phone.control;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shangbao.dao.ArticleDao;
import com.shangbao.model.persistence.Article;
import com.shangbao.service.ArticleService;

@Controller
@RequestMapping("/demo")
public class DemoController {
	@Resource
	private ArticleService articleServiceImp;
	
	@RequestMapping(value=("/{newsId}"), method = RequestMethod.GET)
	@ResponseBody
	public ArticleHtml getArticle(@PathVariable("newsId") long id){
		Article article = articleServiceImp.findOne(id);
		ArticleHtml html = new ArticleHtml(article);		
		return html;
	}
	
	class ArticleHtml{
		private String html;
		
		public ArticleHtml(Article article){
			this.html = new String("");
			html += "<html>";
			html = html + "<head>";
			html = html + "<title>";
			html = html + article.getTitle();
			html = html + "</title>";
			html = html + "</head>";
			
			html = html + "<body>";
			html = html + article.getContent();
			html = html + "</body>";
			html = html + "</html>";
		}
		public String getHtml(){
			return this.html;
		}
		public void setHtml(String html){
			this.html = html;
		}
	}
}
