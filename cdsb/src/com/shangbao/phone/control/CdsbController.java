package com.shangbao.phone.control;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shangbao.app.model.CdsbArticle;
import com.shangbao.app.model.CdsbModel;
import com.shangbao.app.service.AppService;

@Controller
@RequestMapping("/iphone")
public class CdsbController {
	@Resource
	private AppService appService;
	
	@RequestMapping(value="/AdAndNewsListSearch/50/{sortTime:[\\d]+}")
	@ResponseBody
	public CdsbModel getModel(@PathVariable("sortTime") long sortTime){
		CdsbModel model = appService.getCdsbModel(sortTime);
		return model;
	}
	
	@RequestMapping(value="/NewsInfo/{newsId:[\\d]+}")
	@ResponseBody
	public CdsbArticle getArticleInfo(@PathVariable("newsId") long newsId){
		CdsbArticle cdsbArticle = appService.getCdsbArticle(newsId);
		return cdsbArticle;
	}
}
