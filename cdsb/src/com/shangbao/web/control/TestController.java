package com.shangbao.web.control;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shangbao.app.service.UserIdentifyService;
import com.shangbao.model.persistence.User;

@Controller
@RequestMapping("/dslabtest")
public class TestController {
	@Resource
	private UserIdentifyService userIdentifyService;
	
	@RequestMapping(value="/getuser/{phone}")
	@ResponseBody
	public String getRemoteUser(@PathVariable("phone") String phone){
		User user = userIdentifyService.getRemoteUserWithoutPW(phone);
		if(user == null){
			return "null";
		}
		return user.getName();
	}
	@RequestMapping(value="/exit/{phone}")
	@ResponseBody
	public int isExist(@PathVariable("phone") String phone){
		User user = new User();
		user.setPhone(phone);
		return userIdentifyService.remoteUserExist(user);
	}
	@RequestMapping(value="/delete/{phone}")
	@ResponseBody
	public int deleteRemoteUser(@PathVariable("phone") String phone){
		User user = new User();
		int i = 0;
		while(user != null){
			user = userIdentifyService.getRemoteUserWithoutPW(phone);
			if(user == null){
				return i;
			}
			userIdentifyService.deleteRemoteUser(user.getUid());
			i ++;
		}
		return i;
	}
}
