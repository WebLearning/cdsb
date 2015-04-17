package com.shangbao.web.control;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shangbao.model.PasswdModel;
import com.shangbao.model.persistence.User;
import com.shangbao.service.UserService;

@Controller
@RequestMapping(value="/user")
public class UserController {
	@Resource
	private UserService userService;
	@Resource
	private PasswordEncoder passwordEncoder;
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value="/users", method=RequestMethod.GET)
	@ResponseBody
	public List<User> list(){
		return userService.listUsers();
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	@ResponseBody
	public User register(@RequestBody User user){
		if(user.getName() != null && user.getPasswd() != null){
			User userCriteria = new User();
			userCriteria.setName(user.getName());
			if(userService.findOne(userCriteria) != null)
				return null;
			user.setRole("ROLE_ADMIN");
			user.setPasswd(passwordEncoder.encodePassword(user.getPasswd(), null));
			userService.addUser(user);
			return user;
		}
		return null;
	}
	
	@RequestMapping(value="/register/getregister/{name}/{passwd}", method=RequestMethod.GET)
	@ResponseBody
	public User registerSelf(@PathVariable String name, @PathVariable String passwd){
		if(name != null && passwd != null){
			User user = new User();
			user.setRole("ROLE_ADMIN");
			user.setName(name);
			user.setDuty("super");
			user.setPasswd(passwordEncoder.encodePassword(passwd, null));
			userService.addUser(user);
			return user;
		}
		return null;
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	@ResponseBody
	public boolean updateUser(@RequestBody PasswdModel passwdModel){
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(passwdModel.getNewPasswd() != null & passwdModel.getOldPasswd() != null){
			String oldPasswd = passwordEncoder.encodePassword(passwdModel.getOldPasswd(), null);
			String newPasswd = passwordEncoder.encodePassword(passwdModel.getNewPasswd(), null);
			return userService.updatePasswd(user, oldPasswd, newPasswd);
		}
		return false;
	}
	
	@RequestMapping(value="/userinfo", method=RequestMethod.GET)
	@ResponseBody
	public User getUserInfo(){
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		user.setPasswd("");
		return user;
	}
	
	@RequestMapping(value="/userauth", method=RequestMethod.GET)
	@ResponseBody
	public String getUserAuth(){
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String auth = user.getRole();
		return auth;
	}
}
