package com.shangbao.service.imp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.shangbao.dao.ArticleDao;
import com.shangbao.dao.CommendDao;
import com.shangbao.model.CommendState;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.Commend;
import com.shangbao.model.persistence.CrawlerCommend;
import com.shangbao.model.persistence.NewsCommend;
import com.shangbao.model.show.CommendForArticle;
import com.shangbao.model.show.CommendList;
import com.shangbao.model.show.CommendPage;
import com.shangbao.model.show.Page;
import com.shangbao.model.show.SingleCommend;
import com.shangbao.service.CommendService;
import com.shangbao.service.PendTagService;

@Service
public class CommendServiceImp implements CommendService {

	@Resource
	private CommendDao commendDaoImp;
	
	@Resource
	private ArticleDao articleDaoImp;
	
	@Resource
	private PendTagService pendTagServiceImp;

	@Override
	public void add(Commend commend){
		commendDaoImp.insert(commend);
	}
	
	@Override
	public CommendList get(Commend criteriaElement, int pageId) {
		CommendList commendList = new CommendList();
		List<Commend> commends = commendDaoImp.find(criteriaElement);
		if (commends.size() == 1
				&& commends.get(0).getCommendList().size() > 10 * (pageId - 1)) {
			List<SingleCommend> singleCommends = commends.get(0)
					.getCommendList();
			int pageCount = (singleCommends.size() % 10 == 0 ? singleCommends.size() / 10 : singleCommends.size() / 10 + 1);
			commendList.setPageCount(pageCount);
			commendList.setCurrentNo(pageId);
			commendList.setCommendList(singleCommends.subList(
					(pageId - 1) * 10,
					pageId * 10 - 1 >= singleCommends.size() ? singleCommends.size() : pageId * 10));
			return commendList;
		}
		return commendList;
	}
	
	@Override
	public CommendList getPubUnpub(Commend criteriaCommend, String type, int pageId){
		CommendList commendList = new CommendList();
		List<Commend> commends = commendDaoImp.find(criteriaCommend);
		if (commends.size() == 1
				&& commends.get(0).getCommendList().size() > 10 * (pageId - 1)){
			List<SingleCommend> singleCommends = commends.get(0).getCommendList();
			List<SingleCommend> returnCommends = new ArrayList<>();
			for(SingleCommend singleCommend : singleCommends){
				if(type.equals("publish") && singleCommend.getState().equals(CommendState.published)){
					returnCommends.add(singleCommend);
				}
				if(type.equals("unpublish") && singleCommend.getState().equals(CommendState.unpublished)){
					returnCommends.add(singleCommend);
				}
			}
			int pageCount = (returnCommends.size() % 10 == 0 ? returnCommends.size() / 10 : returnCommends.size() / 10 + 1);
			if(pageId <= pageCount){
				commendList.setPageCount(pageCount);
				commendList.setCurrentNo(pageId);
				commendList.setCommendList(returnCommends.subList(
						(pageId - 1) * 10,
						pageId * 10 - 1 >= returnCommends.size() ? returnCommends.size() : pageId * 10));
			}
		}
		return commendList;
	}
	
	@Override
	public CommendList getPubUnpub(Commend criteriaCommend, String type, int pageId, String order, String direction){
		CommendList commendList = new CommendList();
		List<Commend> commends = commendDaoImp.find(criteriaCommend);
		if (commends.size() == 1
				&& commends.get(0).getCommendList().size() > 10 * (pageId - 1)){
			List<SingleCommend> singleCommends = reSort(commends.get(0)
					.getCommendList(), order);
			if(direction.equals("desc")){
				Collections.reverse(singleCommends);
			}
			List<SingleCommend> returnCommends = new ArrayList<>();
			for(SingleCommend singleCommend : singleCommends){
				if(type.equals("publish") && singleCommend.getState().equals(CommendState.published)){
					returnCommends.add(singleCommend);
				}
				if(type.equals("unpublish") && singleCommend.getState().equals(CommendState.unpublished)){
					returnCommends.add(singleCommend);
				}
			}
			int pageCount = (returnCommends.size() % 10 == 0 ? returnCommends.size() / 10 : returnCommends.size() / 10 + 1);
			if(pageId <= pageCount){
				commendList.setPageCount(pageCount);
				commendList.setCurrentNo(pageId);
				commendList.setCommendList(returnCommends.subList(
						(pageId - 1) * 10,
						pageId * 10 - 1 >= returnCommends.size() ? returnCommends.size() : pageId * 10));
			}
		}
		return commendList;
	}

	@Override
	public CommendList get(Commend criteriaElement, int pageId, String order, String direction) {
		CommendList commendList = new CommendList();
		List<Commend> commends = commendDaoImp.find(criteriaElement);
		if (commends.size() == 1
				&& commends.get(0).getCommendList().size() > 10 * (pageId - 1)) {
			List<SingleCommend> singleCommends = reSort(commends.get(0)
					.getCommendList(), order);
			if(direction.equals("desc")){
				Collections.reverse(singleCommends);
			}
			if (singleCommends.size() > 0) {
				int pageCount = (singleCommends.size() % 10 == 0 ? singleCommends.size() / 10 : singleCommends.size() / 10 + 1);
				commendList.setPageCount(pageCount);
				commendList.setCurrentNo(pageId);
				commendList.setCommendList(singleCommends.subList(
						(pageId - 1) * 10,
						pageId * 10 - 1 >= singleCommends.size() ? singleCommends.size() : pageId * 10));
				return commendList;
			}
		}
		return commendList;
	}

	@Override
	public void add(Commend commend, SingleCommend singleCommend) {
		List<Commend> commends = commendDaoImp.find(commend);
		if(commends == null || commends.isEmpty()){
			Article article = new Article();
			article.setId(commend.getArticleId());
			List<Article> articles = articleDaoImp.find(article);
			if(articles == null || articles.isEmpty()){
				return;
			}
			commend.setArticleTitle(articles.get(0).getTitle());
			commend.setState(articles.get(0).getState());
			if(pendTagServiceImp.isTag("comment") || commend instanceof CrawlerCommend){
				singleCommend.setState(CommendState.unpublished);
			}else{
				singleCommend.setState(CommendState.published);
			}
			singleCommend.setCommendId(new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) + (int)(Math.random()*1000) + "" + (int)(Math.random()*1000));
			commend.getCommendList().add(singleCommend);
			commendDaoImp.insert(commend);
		}else{
			Update updateElement = new Update();
			if(pendTagServiceImp.isTag("comment") || commend instanceof CrawlerCommend){
				singleCommend.setState(CommendState.unpublished);
			}else{
				singleCommend.setState(CommendState.published);
			}
			singleCommend.setCommendId(new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) + (int)(Math.random()*1000) + "" + (int)(Math.random()*1000));
			updateElement.push("commendList", singleCommend);
			commendDaoImp.update(commend, updateElement);
		}
		//将文章评论数加一
		Article article = new Article();
		article.setId(commend.getArticleId());
		Update update = new Update();
		if(commend instanceof CrawlerCommend){
			update.inc("crawlerCommends", 1);
			update.inc("crawlerCommendsUnpublish", 1);
			articleDaoImp.update(article, update);
		}else{
			update.inc("newsCommends", 1);
			if(!pendTagServiceImp.isTag("comment")){
				update.inc("newsCommendsPublish", 1);
			}else{
				update.inc("newsCommendsUnpublish", 1);
			}
			articleDaoImp.update(article, update);
		}
	}

	@Override
	public void update(Commend commend, SingleCommend singleCommend) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Commend commend, List<SingleCommend> singeCommends) {
		List<Commend> commends = commendDaoImp.find(commend);
//		Update update = new Update();
		List<SingleCommend> singleUpdateCommends = new ArrayList<>();
		if(singeCommends != null && !singeCommends.isEmpty()){
			for(SingleCommend singleCommend : singeCommends){
				singleCommend.setCommendId(new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) + (int)(Math.random()*100));
				singleCommend.setState(CommendState.unpublished);
				singleUpdateCommends.add(singleCommend);
			}
			if(commends == null || commends.isEmpty()){
				commend.setCommendList(singleUpdateCommends);
				commendDaoImp.insert(commend);
				return;
			}
			for(SingleCommend singleCommend : singleUpdateCommends){
				Update update = new Update();
				update.push("commendList", singleCommend);
				commendDaoImp.update(commend, update);
			}
		}
	}

	@Override
	public void reply(Commend commend, String commendId, String reply) {
		Update updateElement = new Update();
		Query query = new Query();
		query.addCriteria(Criteria.where("commendList.commendId").is(commendId));
		updateElement.set("commendList.$.reply", reply);
		commendDaoImp.update(commend, query, updateElement);
	}

	@Override
	public void publish(Commend commend, List<String> singleCommendIds) {
		int commendCount = 0;
		Update updateElement = new Update();
		updateElement.set("commendList.$.state", CommendState.published.toString());
		for(String commendId : singleCommendIds){
			Query query = new Query();
			query.addCriteria(Criteria.where("commendList.commendId").is(commendId));
			commendDaoImp.update(commend, query, updateElement);
			commendCount ++;
		}
		Article article = new Article();
		article.setId(commend.getArticleId());
		List<Article> articles = articleDaoImp.find(article);
		if(articles != null && !articles.isEmpty()){
			Update update = new Update();
			if(commend instanceof CrawlerCommend){
				update.inc("crawlerCommendsPublish", commendCount);
				update.inc("crawlerCommendsUnpublish", -commendCount);
			}else{
				update.inc("newsCommendsPublish", commendCount);
				update.inc("newsCommendsUnpublish", -commendCount);
			}
			articleDaoImp.update(article, update);
		}
	}

	@Override
	public void delete(Commend commend, List<String> singleCommendIds) {
		int totalCount = 0;
		int published = 0;
		for(String commendId : singleCommendIds){
			Update updateElement = new Update();
			Query query = new Query();
			DBObject object = new BasicDBObject();
			object.put("commendId", commendId);
			updateElement.pull("commendList", object);
			query.addCriteria(Criteria.where("commendList.commendId").is(commendId));
			commendDaoImp.update(commend, query, updateElement);
		}
		List<Commend> commendList = commendDaoImp.find(commend);
		if(!commendList.isEmpty() && commendList != null){
			if(commendList.get(0).getCommendList().isEmpty()){
				
			}else{
				for(SingleCommend singleCommend : commendList.get(0).getCommendList()){
					if(singleCommend.getState().equals(CommendState.published)){
						totalCount ++;
						published ++;
					}else{
						totalCount ++;
					}
				}
			}
			Article article = new Article();
			article.setId(commend.getArticleId());
			List<Article> articles = articleDaoImp.find(article);
			if(articles != null && !articles.isEmpty()){
				Update update = new Update();
				if(commend instanceof CrawlerCommend){
					update.set("crawlerCommendsPublish", published);
					update.set("crawlerCommends", totalCount);
					update.set("crawlerCommendsUnpublish", totalCount - published);
				}else{
					update.set("newsCommendsPublish", published);
					update.set("newsCommends", totalCount);
					update.set("newsCommendsUnpublish", totalCount - published);
				}
				articleDaoImp.update(article, update);
			}
		}
	}
	
	@Override
	public void delete(Commend commend){
		commendDaoImp.delete(commend);
	}

	@Override
	public CommendPage getCommendPage(int pageNo) {
		CommendPage commendPage = new CommendPage();
		List<CommendForArticle> commendList = new ArrayList<CommendForArticle>();
		Page<Article> page = commendDaoImp.getPage(pageNo, 20, new Query());
		commendPage.setCurrentNo(pageNo);
		commendPage.setPageCount(page.getTotalPage());
		for(Article article : page.getDatas()){
			CommendForArticle commendForArticle = new CommendForArticle(article);
			commendList.add(commendForArticle);
		}
		commendPage.setCommendList(commendList);
		return commendPage;
	}

	@Override
	public CommendPage getCommendPage(int pageNo, String order, String direction) {
		CommendPage commendPage = new CommendPage();
		List<CommendForArticle> commendList = new ArrayList<CommendForArticle>();
		Query query = new Query();
		if(direction.equals("asc")){
			query.with(new Sort(Direction.ASC, order));
		}else{
			query.with(new Sort(Direction.DESC, order));
		}
		Page<Article> page = commendDaoImp.getPage(pageNo, 20, query);
		commendPage.setCurrentNo(pageNo);
		commendPage.setPageCount(page.getTotalPage());
		for(Article article : page.getDatas()){
			CommendForArticle commendForArticle = new CommendForArticle(article);
			commendList.add(commendForArticle);
		}
		commendPage.setCommendList(commendList);
		return commendPage;
	}

	private List<SingleCommend> reSort(List<SingleCommend> singleCommends,
			String order) {
		Comparator comparator = new SingleCommendCompara(order);
		Collections.sort(singleCommends, comparator);
		return singleCommends;
	}
	
	class SingleCommendCompara implements Comparator<SingleCommend>{
		private String order;
		public SingleCommendCompara(String order){
			this.order = order;
		}
		@Override
		public int compare(SingleCommend o1, SingleCommend o2) {
			if(order.equals("time")){
				if(o1.getTimeDate() == null)
					return 0;
				if(o2.getTimeDate() == null)
					return 1;
				return o1.getTimeDate().after(o2.getTimeDate()) ? 1 : 0;
			}else if(order.equals("level")){
				if(o1.getLevel() == null)
					return 0;
				if(o2.getLevel() == null)
					return 0;
				return o1.getLevel().compareTo(o2.getLevel());
			}else if(order.equals("state")){
				if(o1.getState() == null)
					return 0;
				if(o2.getState() == null)
					return 0;
				return o1.getState().toString().compareTo(o2.getState().toString());
			}else if(order.equals("from")){
				if(o1.getFrom() == null)
					return 0;
				if(o2.getFrom() == null)
					return 1;
				return o1.getFrom().compareTo(o2.getFrom());
			}
			return 0;
		}
	}
}
