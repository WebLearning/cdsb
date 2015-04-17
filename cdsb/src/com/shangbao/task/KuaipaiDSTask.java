package com.shangbao.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

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
	
	private String channelName = "大师拍";
	
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
}
