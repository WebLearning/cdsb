package com.shangbao.service.imp;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shangbao.dao.ClickDao;
import com.shangbao.model.persistence.Click;
import com.shangbao.service.ClickService;

@Service
public class ClickServiceImp implements ClickService{

	@Resource
	private ClickDao clickDaoImp;
	
	@Override
	public void add(long articleId, String fromIp, String udid, boolean tag) {
		Click click = new Click();
		click.setArticleId(articleId);
		click.setClickTime(new Date());
		click.setFromIp(fromIp);
		if(udid != null){
			click.setUDID(udid);
		}
		click.setTag(tag);
		click.setClickTime(new Date());
		clickDaoImp.insert(click);
	}
}
