package com.shangbao.web.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/xml")
public class XMLController {
	
	@RequestMapping(value = "/test", produces="application/xml")
	@ResponseBody
	public String test(){
		String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";  
	    xmlData += "<user><username>zhang</username><password>123</password></user>";
		return xmlData;
	}
	
}
