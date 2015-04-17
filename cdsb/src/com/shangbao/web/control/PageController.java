package com.shangbao.web.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
	
	@RequestMapping(value="/houtai")
	public String MainPage(){
		return "login";
	}
	
	@RequestMapping(value="/")
	public String defaultPage(){
		return "login";
	}
}
