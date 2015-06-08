package com.shangbao.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.util.JavaScriptUtils;

import com.shangbao.dao.ArticleDao;
import com.shangbao.dao.ChannelDao;
import com.shangbao.model.ArticleState;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.Channel;

/**
 * 将每天上传的快拍PK的图片按照作者，做成图集，发布到快拍大师
 * @author Administrator
 *
 */
@Service
public class KuaipaiDSTask {
	@Resource
	private ChannelDao channelDaoImp;
	@Resource
	private MongoTemplate mongoTemplate;
	@Resource
	private ArticleDao articleDaoImp;
	private String localhost = "http://www.cdsb.mobi/cdsb";
	
	public KuaipaiDSTask(){
		Properties props;
		try {
			props = PropertiesLoaderUtils.loadAllProperties("config.properties");
			if(!props.getProperty("localhost").isEmpty()){
				localhost = props.getProperty("localhost");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String channelName = "拍客集";
	
	public void method(){
		Channel criteriaChannel = new Channel();
		criteriaChannel.setChannelName(channelName);
		List<Channel> channels = channelDaoImp.find(criteriaChannel);
		if(channels.isEmpty() || channels == null){
			return;
		}
		
		//获取一天前的时间
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(new Date());//把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
		Date oldDate = calendar.getTime();
		
		//查找当日上传的快拍pk的图片
		Query query = new Query();
		query.addCriteria(Criteria.where("tag").is(true));
		query.addCriteria(Criteria.where("channel").is("PK台"));
		query.addCriteria(Criteria.where("from").is("商报网友"));
		query.addCriteria(Criteria.where("state").is(ArticleState.Published));
		query.addCriteria(Criteria.where("time").gt(oldDate));
		query.with(new Sort("author"));
		List<Article> articles = mongoTemplate.find(query, Article.class);
		if(articles.isEmpty() || articles == null){
			return;
		}
		
		String author = "";
		Article articleDS = new Article();
		boolean first = true;
		for(Article article : articles){
			if(article.getAuthor().equals(author) && author != ""){
				String content = articleDS.getContent();
				content += "<p style=\"text-align: center;\">" + article.getTitle() + "<br/></p>" + article.getContent();
				articleDS.setContent(content);
				for(String picUrl : article.getPicturesUrl()){
					articleDS.addPicture(picUrl);
				}
			}else{
				author = article.getAuthor();
				if(! first){
					articleDaoImp.insert(articleDS);
					articleDS = new Article();
				}
				first = false;
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
				articleDS = new Article();
				articleDS.setTitle(article.getAuthor() + "在" + format.format(oldDate) + "的图集");
				articleDS.setTime(new Date());
				articleDS.setAuthor(article.getAuthor());
				articleDS.setUid(article.getUid());
				articleDS.setState(ArticleState.Published);
				articleDS.setTag(true);
				articleDS.addChannel(channelName);
				articleDS.setContent("<p style=\"text-align: center;\">" + article.getTitle() + "<br/></p>" + article.getContent());
				for(String picUrl : article.getPicturesUrl()){
					articleDS.addPicture(picUrl);
				}
			}
		}
		articleDaoImp.insert(articleDS);
	}
	
	public void newMethod(){
		Channel criteriaChannel = new Channel();
		criteriaChannel.setChannelName(channelName);
		List<Channel> channels = channelDaoImp.find(criteriaChannel);
		if(channels.isEmpty() || channels == null){
			return;
		}
		
		//获取一天前的时间
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(new Date());//把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
		Date oldDate = calendar.getTime();
		
		//查找当日上传的快拍pk的图片
		Query query = new Query();
		query.addCriteria(Criteria.where("tag").is(true));
		query.addCriteria(Criteria.where("channel").is("PK台"));
		query.addCriteria(Criteria.where("from").is("商报网友"));
		query.addCriteria(Criteria.where("state").is(ArticleState.Published));
		query.addCriteria(Criteria.where("time").gt(oldDate));
		query.with(new Sort("author"));
		List<Article> articles = mongoTemplate.find(query, Article.class);
		if(articles.isEmpty() || articles == null){
			return;
		}
		
		StringBuilder temp = new StringBuilder();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		File tempFile = new File(".." + File.separator + "webapps" + File.separator + "cdsb" + File.separator + "WEB-SRC" + File.separator + "pictureset" + File.separator + "temp.html");
		if(Files.notExists(tempFile.toPath())){
			return;
		}else{
			try {
				FileInputStream inputStream = new FileInputStream(tempFile);
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				String str = null;
				while((str = reader.readLine()) != null){
					temp.append(str);
				}
				inputStream.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(temp == null || temp.toString() == ""){
			return;
		}
		
		HashMap<String, List<Article>> map = new HashMap<>();
		for(Article article : articles){
			String author = article.getAuthor();
			if(author == null || author.isEmpty()){
				continue;
			}
			if(map.containsKey(author)){
				map.get(author).add(article);
			}else{
				List<Article> mapArticles = new ArrayList<>();
				mapArticles.add(article);
				map.put(author, mapArticles);
			}
		}
		for(Entry<String, List<Article>> entry : map.entrySet()){
			List<Article> mapArticles = entry.getValue();
			Article article = new Article();
			String author = entry.getKey();
			article.setAuthor(author);
			article.setTitle(author + "在" + format.format(oldDate) + "的图集");
			article.setTime(new Date());
			article.setState(ArticleState.Published);
			article.setTag(true);
			article.addChannel(channelName);
			StringBuilder articleTemp = new StringBuilder(temp);
			int start1 = articleTemp.indexOf("***Title***");
			int end1 = start1 + 11;
//			String outLine = temp.toString().replaceAll("***Title***", article.getTitle());
			StringBuilder outLine = articleTemp.replace(start1, end1, article.getTitle());
			int start2 = outLine.indexOf("***Title***");
			int end2 = start2 + 11;
			outLine = outLine.replace(start2, end2, article.getTitle());
			StringBuffer picturesDiv = new StringBuffer();
			for(Article singleArticle : mapArticles){
				List<String> singlePics = singleArticle.getPicturesUrl();
				if(singlePics != null && !singlePics.isEmpty()){
					for(String singlePicUrl : singlePics){
						article.addPicture(singlePicUrl);
						picturesDiv.append("<a href=\"" + singlePicUrl + "\" style=\"background-image:url(" + singlePicUrl + ")\" title=\"" + singleArticle.getTitle() + "\"></a>");
					}
				}
			}
			int start3 = outLine.indexOf("***Pictures***");
			int end3 = start3 + 14;
			outLine = outLine.replace(start3, end3, picturesDiv.toString());
			File file = new File(".." + File.separator + "webapps" + File.separator + "cdsb" + File.separator + "WEB-SRC" + File.separator + "pictureset" + File.separator + article.getTitle() + ".html");
			if(Files.notExists(file.toPath())){
				try {
					Files.createFile(file.toPath());
					OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
					writer.write(outLine.toString());
					writer.close();
					article.setOutSideUrl(localhost + "/WEB-SRC/pictureset/" +URLEncoder.encode(file.getName(), "UTF-8"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			articleDaoImp.insert(article);
		}
	}
	
	public void newMethod2(){
		Channel criteriaChannel = new Channel();
		criteriaChannel.setChannelName(channelName);
		List<Channel> channels = channelDaoImp.find(criteriaChannel);
		if(channels.isEmpty() || channels == null){
			return;
		}
		
		//获取一天前的时间
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(new Date());//把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
		Date oldDate = calendar.getTime();
		
		//查找当日上传的快拍pk的图片
		Query query = new Query();
		query.addCriteria(Criteria.where("tag").is(true));
		query.addCriteria(Criteria.where("channel").is("PK台"));
		query.addCriteria(Criteria.where("from").is("商报网友"));
		query.addCriteria(Criteria.where("state").is(ArticleState.Published));
		query.addCriteria(Criteria.where("time").gt(oldDate));
		query.with(new Sort("author"));
		List<Article> articles = mongoTemplate.find(query, Article.class);
		if(articles.isEmpty() || articles == null){
			return;
		}
		
		StringBuilder temp = new StringBuilder();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		File tempFile = new File(".." + File.separator + "webapps" + File.separator + "cdsb" + File.separator + "WEB-SRC" + File.separator + "pictureset" + File.separator + "test.html");
		if(Files.notExists(tempFile.toPath())){
			return;
		}else{
			try {
				FileInputStream inputStream = new FileInputStream(tempFile);
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
				String str = null;
				while((str = reader.readLine()) != null){
					temp.append(str + "\n");
				}
				inputStream.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(temp == null || temp.toString() == ""){
			return;
		}
		HashMap<String, List<Article>> map = new HashMap<>();
		for(Article article : articles){
			String author = article.getAuthor();
			if(author == null || author.isEmpty()){
				continue;
			}
			if(map.containsKey(author)){
				map.get(author).add(article);
			}else{
				List<Article> mapArticles = new ArrayList<>();
				mapArticles.add(article);
				map.put(author, mapArticles);
			}
		}
		for(Entry<String, List<Article>> entry : map.entrySet()){
			List<Article> mapArticles = entry.getValue();
			Article article = new Article();
			String author = entry.getKey();
			article.setAuthor(author);
			article.setTitle(author + "在" + format.format(oldDate) + "的图集");
			article.setTime(new Date());
			article.setState(ArticleState.Published);
			article.setTag(true);
			article.addChannel(channelName);
			StringBuilder articleTemp = new StringBuilder(temp);
			int start1 = articleTemp.indexOf("***Title***");
			int end1 = start1 + 11;
			StringBuilder outLine = articleTemp.replace(start1, end1, article.getTitle());
			int start2 = outLine.indexOf("***TimeName***");
			int end2 = start2 + 14;
			outLine = outLine.replace(start2, end2, "date:\"" + format.format(oldDate) + "\",name:" + "\"" + author + "\"");
			StringBuffer picturesDiv = new StringBuffer();
			for(Article singleArticle : mapArticles){
				List<String> singlePics = singleArticle.getPicturesUrl();
				if(singlePics != null && !singlePics.isEmpty()){
					for(String singlePicUrl : singlePics){
						article.addPicture(singlePicUrl);
//						Pattern pattern=Pattern.compile("(\r\n|\r|\n|\n\r)");
//						Matcher matcher=pattern.matcher(singleArticle.getSummary());
//						String content = matcher.replaceAll("\\n");
//						content = content.replaceAll("\"", "\\\"");
//						content = content.replaceAll("\\", "\\\\");
						String content = JavaScriptUtils.javaScriptEscape(singleArticle.getSummary());
						picturesDiv.append("{picUrl:\"" + singlePicUrl + "\",title:\"" + singleArticle.getTitle() + "\",content:\"" + content + "\"},");
					}
				}
			}
			int start3 = outLine.indexOf("***Pictures***");
			int end3 = start3 + 14;
			outLine = outLine.replace(start3, end3, picturesDiv.toString());
			Pattern pattern = Pattern.compile("( |>|<|\\\\|\\/|\\?|\\||:)");
			Matcher matcher = pattern.matcher(article.getTitle());
			String fileNameString = matcher.replaceAll("_");
			File file = new File(".." + File.separator + "webapps" + File.separator + "cdsb" + File.separator + "WEB-SRC" + File.separator + "pictureset" + File.separator + fileNameString + ".html");
			if(Files.notExists(file.toPath())){
				try {
					Files.createFile(file.toPath());
					OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
					writer.write(outLine.toString());
					writer.close();
					article.setOutSideUrl(localhost + "/WEB-SRC/pictureset/" +URLEncoder.encode(file.getName(), "UTF-8"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			articleDaoImp.insert(article);
		}
	}
}
