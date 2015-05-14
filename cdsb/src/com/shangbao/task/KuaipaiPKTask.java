package com.shangbao.task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.shangbao.dao.ArticleDao;
import com.shangbao.dao.ChannelDao;
import com.shangbao.dao.ReadLogDao;
import com.shangbao.model.ArticleState;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.Channel;
import com.shangbao.model.persistence.ReadLog;
import com.shangbao.utils.CompressPicUtils;

@Service
public class KuaipaiPKTask {
	@Resource
	private ReadLogDao readLogDaoImp;
	@Resource
	private ChannelDao channelDaoImp;
	@Resource
	private ArticleDao articleDaoImp;
	@Resource
	private MongoTemplate mongoTemplate;
	@Resource
	private CompressPicUtils compressPicUtils;
	
	private String pkChannelName = "PK台";
	
	public void findTopArticle(){
		Long newTopArticleId = null;
		List<Long> oldTopArticleIds = new ArrayList<>();
		Date date = new Date();
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = dft.format(date);
		Channel criteriaChannel = new Channel();
		criteriaChannel.setChannelName(pkChannelName);
		List<Channel> channels = channelDaoImp.find(criteriaChannel);
		if(channels.isEmpty() || channels == null){
			return;
		}
		
		//获取当前发布的快拍pk的所有文章
		
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(new Date());//把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
		Date oldDate = calendar.getTime();
		Query query = new Query();
		query.addCriteria(Criteria.where("tag").is(true));
		query.addCriteria(Criteria.where("channel").is(pkChannelName));
		query.addCriteria(Criteria.where("from").is("商报网友"));
		query.addCriteria(Criteria.where("state").is(ArticleState.Published));
		query.addCriteria(Criteria.where("time").gt(oldDate));
		query.with(new Sort("author"));
		List<Article> pkArticles = mongoTemplate.find(query, Article.class);
		if(pkArticles.isEmpty() || pkArticles == null){
			return;
		}
		
		Article criteriaArticle = new Article();
		criteriaArticle.setTag(true);
		criteriaArticle.addChannel(pkChannelName);
		criteriaArticle.setState(ArticleState.Published);
		List<Article> publishedArticles = articleDaoImp.find(criteriaArticle);
		for(Article article : publishedArticles){
			if(article.getChannelIndex().get(pkChannelName) > (Integer.MAX_VALUE / 2)){
				oldTopArticleIds.add(article.getId());
			}
		}
		//找到今日点赞最多的文章
		int likes = 0;
		for(Article article : pkArticles){
//			if(article.getChannelIndex().get(pkChannelName) > (Integer.MAX_VALUE / 2)){
//				oldTopArticleIds.add(article.getId());
//			}
			ReadLog criteriaReadLog = new ReadLog();
			criteriaReadLog.setId(article.getId());
			List<ReadLog> readLogs = readLogDaoImp.find(criteriaReadLog);
			if(readLogs.isEmpty() || readLogs == null){
				continue;
			}else{
				if(readLogs.get(0).getDateLike().get(dateString) != null && readLogs.get(0).getDateLike().get(dateString) > likes){
					newTopArticleId = readLogs.get(0).getId();
					likes = readLogs.get(0).getDateLike().get(dateString);
				}
			}
		}
		//取消已经置顶的文章
		if(likes > 0){
			if(!oldTopArticleIds.isEmpty()){
				for(Long oldTopId : oldTopArticleIds){
					articleDaoImp.unSetTopArticle(pkChannelName, oldTopId);
				}
			}
			//置顶赞最多的文章
			if(newTopArticleId != null){
				articleDaoImp.setTopArticle(pkChannelName, newTopArticleId);
				
				//画个皇冠
				Article topArticle = articleDaoImp.findById(newTopArticleId);
				if(topArticle != null){
					List<String> picUrls = topArticle.getPicturesUrl();
					if(!picUrls.isEmpty() && picUrls != null){
						String pic = picUrls.get(0);
						String simPic = pic.replace("/mid/", "/sim/");
						String simPath = simPic.substring(pic.indexOf("picture/"));
						String midPath = pic.substring(pic.indexOf("picture/"));
						String picDicPath = "D:\\apache-tomcat\\webapps\\Shangbao01\\WEB-SRC\\picture";
						String waterMark = ".." + File.separator + "webapps" + File.separator + "cdsb" + File.separator + "WEB-SRC" + File.separator + "watermark.png";
						if(Files.notExists(Paths.get(waterMark))){
							return;
						}
						try {
							Properties props = PropertiesLoaderUtils.loadAllProperties("config.properties");
							picDicPath = props.getProperty("pictureDir");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						simPath.replace("/", File.separator);
						String midFilePath = picDicPath.substring(0, picDicPath.indexOf("picture")) + midPath;
						String filePath = picDicPath.substring(0, picDicPath.indexOf("picture")) + simPath;
						if(Files.exists(Paths.get(filePath))){
							compressPicUtils.setWaterMark(new File(midFilePath), new File(filePath), new File(waterMark), 1.0f);
						}
						//换名字
						String newPic;
						String newMidFilePath;
						String newSimFilePath;
						newPic = pic.substring(0, pic.lastIndexOf(".")) + "zan" + pic.substring(pic.lastIndexOf("."));
						newMidFilePath = midFilePath.substring(0, midFilePath.lastIndexOf(".")) + "zan" + midFilePath.substring(midFilePath.lastIndexOf("."));
						newSimFilePath = filePath.substring(0, filePath.lastIndexOf(".")) + "zan" + filePath.substring(filePath.lastIndexOf("."));
//						new File(midFilePath).renameTo(new File(newMidFilePath));
						if(Files.notExists(Paths.get(newMidFilePath))){
							try {
								Files.copy(Paths.get(midFilePath), Paths.get(newMidFilePath));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						new File(filePath).renameTo(new File(newSimFilePath));
						topArticle.getPicturesUrl().set(0, newPic);
						articleDaoImp.update(topArticle);
					}
				}
			}
		}
	}
}
