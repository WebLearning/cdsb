package com.shangbao.service.imp;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.shangbao.dao.ArticleDao;
import com.shangbao.dao.UserDao;
import com.shangbao.model.ArticleState;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.User;
import com.shangbao.model.show.TitleList;
import com.shangbao.service.UserService;

@Service
public class UserServiceImp implements UserService {

	@Resource
	private UserDao userDaoImp;
	@Resource 
	private ArticleDao articleDaoImp;
	
	@Override
	public void addUser(User user) {
		userDaoImp.insert(user);
	}

	@Override
	public List<User> listUsers() {
		List<User> userList = userDaoImp.findAll();
		return userList;
	}

	@Override
	public void deleteOne(User user) {
		userDaoImp.delete(user);
	}

	@Override
	public User findOne(User user) {
		List<User> findList = null;
		findList = userDaoImp.find(user);
		if(findList.isEmpty() || findList == null)
			return null;
		return userDaoImp.find(user).get(0);
	}

	@Override
	public TitleList getUserArticle(User user) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private List<Article> getUserArticles(){
		List<Article> articles = new ArrayList<>();
		
		return articles;
	}

	@Override
	public void collectArticle(User criteriaUser, Long articleId) {
		User user = userDaoImp.findById(criteriaUser.getId());
		if(user.getCollection() != null && user.getCollection().contains(articleId)){
			return;
		}else{
			user.getCollection().add(articleId);
			userDaoImp.save(user);
		}
	}

	@Override
	public List<Article> findCollectArticle(User criteriaUser) {
		List<Article> articleList = new ArrayList<>();
		User user = userDaoImp.findById(criteriaUser.getId());
		List<Long> articleIdList = user.getCollection();
		if(articleIdList != null && !articleIdList.isEmpty()){
			for(Long articleId : articleIdList){
				Article article = articleDaoImp.findById(articleId);
				if(article != null){
					if(article.getState().equals(ArticleState.Published)){
						articleList.add(article);
					}
				}else{
					deleteCollectionArticle(criteriaUser, articleId);
				}
			}
		}
		return articleList;
	}

	@Override
	public void deleteCollectionArticle(User criteriaUser, Long articleId) {
		User user = userDaoImp.findById(criteriaUser.getId());
		if(user.getCollection() != null && user.getCollection().contains(articleId)){
			user.getCollection().remove(articleId);
			userDaoImp.save(user);
		}
	}

	@Override
	public boolean updatePasswd(User criteriaUser, String oldPasswd,
			String newPasswd) {
		User user = userDaoImp.findById(criteriaUser.getId());
		if(user != null && user.getPasswd().equals(oldPasswd)){
			Update update = new Update();
			update.set("passwd", newPasswd);
//			userDaoImp.save(user);
			userDaoImp.findAndModify(user, update);
			return true;
		}
		return false;
	}

	@Override
	public User updateUser(User criteriaUser, User updateUser) {
		Update update = new Update();
		if(updateUser.getUsername() != null){
			update.set("name", updateUser.getUsername());
		}
		if(updateUser.getAvatar() != null){
			update.set("avatar", updateUser.getAvatar());
		}
		if(updateUser.getQq() != null){
			update.set("qq", updateUser.getQq());
		}
		if(updateUser.getBirthday() != null){
			update.set("birthday", updateUser.getBirthday());
		}
		User user = new User();
		user.setName(criteriaUser.getName());
		user.setId(criteriaUser.getId());
		user.setUid(criteriaUser.getUid());
		return userDaoImp.findAndModify(user, update);
	}

}
