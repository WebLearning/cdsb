package com.shangbao.web.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.shangbao.dao.UserDao;
import com.shangbao.model.persistence.User;


@Service
public class MyUserDetailService implements UserDetailsService {

	@Resource
	private UserDao userDaoImp;
	
	@Override
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException {
		User user = new User();
		user.setName(userName);
		List<User>ulist = userDaoImp.find(user);
		if(ulist.size() == 1){
			return (UserDetails) (ulist.toArray())[0];
		}
		return user;
	}

	public UserDetails loadUserByEmail(String email){
		User user = new User();
		user.setEmail(email);
		List<User>ulist = userDaoImp.find(user);
		if(ulist.size() == 1){
			return (UserDetails) (ulist.toArray())[0];
		}
		return null;
	}
	
	public UserDetails loadUserByPhone(String phone){
		User user = new User();
		user.setPhone(phone);
		List<User>ulist = userDaoImp.find(user);
		if(ulist.size() == 1){
			return (UserDetails) (ulist.toArray())[0];
		}
		return null;
	}
}
