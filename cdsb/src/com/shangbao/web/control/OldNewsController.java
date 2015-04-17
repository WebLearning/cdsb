package com.shangbao.web.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shangbao.model.OldNews;

@Controller
@RequestMapping("/old")
public class OldNewsController {
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public OldNews test(){
		OldNews oldNews = new OldNews();
		oldNews.ResultCode = 1;
		oldNews.ResultMsg = "Test";
		oldNews.add();
		return oldNews;
	}
}
