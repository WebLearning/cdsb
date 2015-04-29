package com.shangbao.service.imp;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.shangbao.dao.ClickCountDao;
import com.shangbao.dao.ClickDao;
import com.shangbao.model.persistence.ClickCount;
import com.shangbao.model.show.ClickList;
import com.shangbao.model.show.Page;
import com.shangbao.service.ClickCountService;

@Service
public class ClickCountServiceImp implements ClickCountService{
	@Resource
	private ClickCountDao clickCountDaoImp;
	@Resource
	private ClickDao clickDaoImp;

	@Override
	public void add(long articleId, String articleTitle, Date firstClickTime) {
		ClickCount clickCount = new ClickCount();
		clickCount.setArticleId(articleId);
		clickCount.setArticleTitle(articleTitle);
		clickCount.setFirstTime(firstClickTime);
		clickCountDaoImp.init(clickCount);
	}

	@Override
	public void count24H() {
		Query coutQuery = new Query();
		Date nowDate = new Date();
		//获取48小时内发布的文章id
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(nowDate);
		calendar.add(Calendar.DAY_OF_MONTH, -2);
		Date oldDate = calendar.getTime();
		coutQuery.addCriteria(Criteria.where("firstTime").gt(oldDate));
		List<ClickCount> clickCounts = clickCountDaoImp.find(coutQuery);
		if(clickCounts != null && !clickCounts.isEmpty()){
			//统计每篇文章24小时内的各种访问量
			for(ClickCount clickCount : clickCounts){
				Date firstClick = clickCount.getFirstTime();
				calendar.setTime(firstClick);
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				Date dayDate = calendar.getTime();
				//统计app点击
				Query appQuery = new Query();
				appQuery.addCriteria(Criteria.where("articleId").is(clickCount.getArticleId()));
				appQuery.addCriteria(Criteria.where("clickTime").lt(dayDate));
				appQuery.addCriteria(Criteria.where("tag").is(true));
				long appCountNum = clickDaoImp.count(appQuery);
				//统计out点击
				Query outQuery = new Query();
				outQuery.addCriteria(Criteria.where("articleId").is(clickCount.getArticleId()));
				outQuery.addCriteria(Criteria.where("clickTime").lt(dayDate));
				outQuery.addCriteria(Criteria.where("tag").is(false));
				outQuery.addCriteria(Criteria.where("udid").exists(false));
				long outCountNum = clickDaoImp.count(outQuery);
				//统计udid点击
				Query udidQuery = new Query();
				udidQuery.addCriteria(Criteria.where("articleId").is(clickCount.getArticleId()));
				udidQuery.addCriteria(Criteria.where("clickTime").lt(dayDate));
				udidQuery.addCriteria(Criteria.where("udid").exists(true));
				long udidCountNum = clickDaoImp.count(udidQuery);
				clickCount.setDayAppClick(appCountNum);
				clickCount.setDayOutClick(outCountNum);
				clickCount.setDayUdidClick(udidCountNum);
				clickCountDaoImp.save(clickCount);
			}
		}
	}

	@Override
	public void count72H() {
		Query coutQuery = new Query();
		Date nowDate = new Date();
		//获取96小时内发布的文章id
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(nowDate);
		calendar.add(Calendar.DAY_OF_MONTH, -4);
		Date oldDate = calendar.getTime();
		coutQuery.addCriteria(Criteria.where("firstTime").gt(oldDate));
		List<ClickCount> clickCounts = clickCountDaoImp.find(coutQuery);
		if(clickCounts != null && !clickCounts.isEmpty()){
			//统计每篇文章72小时内的各种访问量
			for(ClickCount clickCount : clickCounts){
				Date firstClick = clickCount.getFirstTime();
				calendar.setTime(firstClick);
				calendar.add(Calendar.DAY_OF_MONTH, 3);
				Date dayDate = calendar.getTime();
				//统计app点击
				Query appQuery = new Query();
				appQuery.addCriteria(Criteria.where("articleId").is(clickCount.getArticleId()));
				appQuery.addCriteria(Criteria.where("clickTime").lt(dayDate));
				appQuery.addCriteria(Criteria.where("tag").is(true));
				long appCountNum = clickDaoImp.count(appQuery);
				//统计out点击
				Query outQuery = new Query();
				outQuery.addCriteria(Criteria.where("articleId").is(clickCount.getArticleId()));
				outQuery.addCriteria(Criteria.where("clickTime").lt(dayDate));
				outQuery.addCriteria(Criteria.where("tag").is(false));
				outQuery.addCriteria(Criteria.where("udid").exists(false));
				long outCountNum = clickDaoImp.count(outQuery);
				//统计udid点击
				Query udidQuery = new Query();
				udidQuery.addCriteria(Criteria.where("articleId").is(clickCount.getArticleId()));
				udidQuery.addCriteria(Criteria.where("clickTime").lt(dayDate));
				udidQuery.addCriteria(Criteria.where("udid").exists(true));
				long udidCountNum = clickDaoImp.count(udidQuery);
				clickCount.setThreeDayAppClick(appCountNum);
				clickCount.setThreeDayOutClick(outCountNum);
				clickCount.setThreeDayUdidClick(udidCountNum);
				clickCountDaoImp.save(clickCount);
			}
		}
	}

	@Override
	public void countAll() {
		// TODO Auto-generated method stub
		List<ClickCount> clickCounts = clickCountDaoImp.findAll();
		if(clickCounts != null && !clickCounts.isEmpty()){
			for(ClickCount clickCount : clickCounts){
				Query appQuery = new Query();
				appQuery.addCriteria(Criteria.where("articleId").is(clickCount.getArticleId()));
				appQuery.addCriteria(Criteria.where("tag").is(true));
				long appCountNum = clickDaoImp.count(appQuery);
				//统计out点击
				Query outQuery = new Query();
				outQuery.addCriteria(Criteria.where("articleId").is(clickCount.getArticleId()));
				outQuery.addCriteria(Criteria.where("tag").is(false));
				outQuery.addCriteria(Criteria.where("udid").exists(false));
				long outCountNum = clickDaoImp.count(outQuery);
				//统计udid点击
				Query udidQuery = new Query();
				udidQuery.addCriteria(Criteria.where("articleId").is(clickCount.getArticleId()));
				udidQuery.addCriteria(Criteria.where("udid").exists(true));
				long udidCountNum = clickDaoImp.count(udidQuery);
				clickCount.setTotalAppClick(appCountNum);
				clickCount.setTotalOutClick(outCountNum);
				clickCount.setTotalUdidClick(udidCountNum);
				clickCountDaoImp.save(clickCount);
			}
		}
	}

	@Override
	public ClickList getList(int pageNo) {
		Query query = new Query();
		query.with(new Sort(Direction.DESC, "firstTime"));
		Page<ClickCount> page = clickCountDaoImp.getPage(pageNo, 20, query);
		ClickList list = new ClickList();
		list.setCurrentNo(pageNo);
		list.setPageCount(page.getTotalPage());
		for(ClickCount clickCount : page.getDatas()){
			list.add(clickCount);
		}
		return list;
	}
	
	
}
