package com.shangbao.service;

import java.util.List;

import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.User;
import com.shangbao.model.show.TitleList;

public interface UserService {
	/**
	 * 添加一个用户
	 */
	public void addUser(User user);
	
	/**
	 * 列出所有用户
	 * @return
	 */
	public List<User> listUsers();
	
	/**
	 * 删除用户
	 * @param user
	 */
	public void deleteOne(User user);
	
	/**
	 * 查找用户
	 * @param user
	 * @return
	 */
	public User findOne(User user);
	
	TitleList getUserArticle(User user);
	
	void collectArticle(User criteriaUser, Long articleId);
	
	List<Article> findCollectArticle(User criteriaUser);
	
	void deleteCollectionArticle(User criteriaUser, Long articleId);
	
	boolean updatePasswd(User criteriaUser, String oldPasswd, String newPasswd);
	
	User updateUser(User criteriaUser, User updateUser);
}
