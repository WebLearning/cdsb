package com.shangbao.web.control;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shangbao.app.service.AppService;
import com.shangbao.model.persistence.Article;

@Controller
@RequestMapping("/xml")
public class XMLController {
	@Resource
	private AppService appService;
	private String localhost = "http://www.cdsb.mobi/cdsb";
	private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
	
	public XMLController(){
		Properties props = new Properties();
		try {
			props=PropertiesLoaderUtils.loadAllProperties("config.properties");
			if(props.getProperty("localhost") != null && props.getProperty("localhost") != ""){
				localhost = props.getProperty("localhost");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/list", produces="application/xml;charset=UTF-8")
	@ResponseBody
	public String getData(){
		String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		List<Article> articles = appService.getXMLArticles(100);
		xmlData += getXML(articles);
		return xmlData;
	}
	
	private String getXML(List<Article> articles){
		StringBuilder xml = new StringBuilder();
		xml.append("<rss version=\"2.0\"><channel><title>成都商报新闻</title><description /><link>http://www.cdsb.mobi</link><generator></generator>");
		xml.append("<image><url>" + localhost + "/WEB-SRC/icon.png" + "</url><title>成都商报</title><link>http://www.cdsb.mobi</link></image>");
		for(Article article : articles){
			if(article.getOutSideUrl() == null || article.getOutSideUrl() == ""){
				xml.append("<item>");
				xml.append("<title><![CDATA[" + article.getTitle() + "]]></title>");
				xml.append("<link><![CDATA[" + localhost + "/app/android/articledetail/" + article.getId() + "]]></link>");
				xml.append("<description /><source><![CDATA[成都商报客户端]]></source>");
//				xml.append("<pubDate><![CDATA[" + (article.getTime() == null ? format.format(new Date()) : format.format(article.getTime())) + "]]></pubDate>");
				xml.append("<pubDate><![CDATA[" + (article.getTime()) + "]]></pubDate>");
				xml.append("</item>");
			}
		}
		xml.append("</channel></rss>");
		return xml.toString();
	}
	
}
