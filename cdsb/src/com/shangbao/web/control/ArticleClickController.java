package com.shangbao.web.control;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shangbao.model.show.ClickList;
import com.shangbao.service.ClickCountService;

@Controller
@RequestMapping("/articleclick")
public class ArticleClickController {
	@Resource
	private ClickCountService clickCountServiceImp;
	
	@RequestMapping(value="/{pageNo}", method=RequestMethod.GET)
	@ResponseBody
	public ClickList getTitles(@PathVariable("pageNo") int pageNo){
		return clickCountServiceImp.getList(pageNo);
	}
}
