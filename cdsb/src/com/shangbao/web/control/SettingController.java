package com.shangbao.web.control;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.shangbao.service.PendTagService;

@Controller
@RequestMapping("/setting")
public class SettingController {
	@Resource
	private PendTagService pendTagServiceImp;

	@RequestMapping(value="/istag/{name}", method=RequestMethod.GET)
	@ResponseBody
	public boolean isTag(@PathVariable("name") String name){
		return pendTagServiceImp.isTag(name);
	}
	
	@RequestMapping(value="/settag/{name}/{tag:true|false}")
	@ResponseStatus(HttpStatus.OK)
	public void setTag(@PathVariable("name") String name, @PathVariable("tag") boolean tag){
		pendTagServiceImp.setTag(name, tag);
	}
	
	public PendTagService getPendTagServiceImp() {
		return pendTagServiceImp;
	}

	public void setPendTagServiceImp(PendTagService pendTagServiceImp) {
		this.pendTagServiceImp = pendTagServiceImp;
	}
}
