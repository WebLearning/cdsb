package com.shangbao.phone.control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.shangbao.app.model.AppResponseModel;
import com.shangbao.app.model.ColumnPageModel;
import com.shangbao.app.service.UserIdentifyService;
import com.shangbao.model.PasswdModel;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.User;
import com.shangbao.model.show.Page;
import com.shangbao.service.ArticleService;
import com.shangbao.service.UserService;
import com.shangbao.utils.CompressPicUtils;

@Controller
@RequestMapping("/appuser")
public class AppUserController {

	@Resource
	private UserService userServiceImp;
	@Resource
	private ArticleService articleServiceImp;
	@Resource
	private PasswordEncoder passwordEncoder;
	@Resource
	private UserIdentifyService userIdentifyService;
	@Resource
	private CompressPicUtils compressPicUtils;
	
	/**
	 * 获取当前用户信息
	 * @return
	 */
	@Secured("ROLE_USER")
	@RequestMapping(value="/userinfo", method=RequestMethod.GET)
	@ResponseBody
	public User getUserInfo(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		SecurityContext context = (SecurityContext)request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
		User user = (User)context.getAuthentication().getPrincipal();
		User criteriaUser = new User();
		criteriaUser.setId(user.getId());
		User returnUser = userServiceImp.findOne(criteriaUser);
		returnUser.setPasswd("");
		return returnUser;
	}

	/**
	 * 获得当前用户的文章
	 * @return
	 */
	@Secured("ROLE_USER")
	@RequestMapping(value="/articles/{pageNo}", method=RequestMethod.GET)
	@ResponseBody
	public ColumnPageModel getUserArticle(@PathVariable("pageNo") int pageNo){
		ColumnPageModel columnPageModel = new ColumnPageModel();
		int pageSize = 5;
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		SecurityContext context = (SecurityContext)request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
		if(context != null){
			User user = (User)context.getAuthentication().getPrincipal();
			Article article = new Article();
			article.setUid(user.getUid());
			List<Article> userArticles = articleServiceImp.find(article, Direction.DESC, "time");
			if(userArticles != null && !userArticles.isEmpty()){
				int pageCount = userArticles.size() / pageSize + 1;
				if(pageNo <= pageCount){
					columnPageModel.setCurrentNo(pageNo);
					columnPageModel.setPageCount(pageCount);
					int fromIndex = (pageNo - 1) * pageSize;
					int toIndex = pageNo * pageSize > userArticles.size() ?  userArticles.size() : pageNo * pageSize + 1;
					int index = 1;
					for(Article userArticle : userArticles.subList(fromIndex, toIndex)){
						columnPageModel.addNewsTitle(userArticle, index);
						index ++;
					}
				}
			}
		}
		return columnPageModel;
	}

	/**
	 * 用户收藏一篇文章
	 * @param articleId
	 */
	@RequestMapping(value="/collection/articles/{articleId}", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void collectArticle(@PathVariable("articleId") Long articleId){
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(user.getName() != null && user.getId() > 0){
			userServiceImp.collectArticle(user, articleId);
		}
	}
	
	/**
	 * 删除用户的一篇收藏文章
	 * @param articleId
	 */
	@RequestMapping(value="/collection/articles/{articleId}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteArticles(@PathVariable("articleId") Long articleId){
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(user.getName() != null && user.getId() > 0){
			userServiceImp.deleteCollectionArticle(user, articleId);
		}
	}
	
	/**
	 * 获取用户收藏的文章
	 * @param pageNo
	 * @return
	 */
	@RequestMapping(value="/collection/articles/{pageNo}", method=RequestMethod.GET)
	@ResponseBody
	public ColumnPageModel getCollectionArticle(@PathVariable("pageNo") int pageNo){
		ColumnPageModel columnPageModel = new ColumnPageModel();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(user.getName() != null && user.getId() > 0){
			List<Article> articles = userServiceImp.findCollectArticle(user);
			Collections.reverse(articles);
			Page<Article> page = new Page<>(pageNo, 10, articles.size());
			columnPageModel.setCurrentNo(pageNo);
			columnPageModel.setPageCount(page.getTotalPage());
			int index = 1;
			for(Article article : articles.subList(page.getFirstResult(), page.getLastResult())){
				columnPageModel.addNewsTitle(article, index);
				index ++;
			}
		}
		return columnPageModel;
	}
	
	/**
	 * 更新用户信息
	 * @param postUser
	 * @return
	 */
	@RequestMapping(value="/update/user", method=RequestMethod.POST)
	@ResponseBody
	public User updateUser(@RequestBody User postUser){
		User user = null;
		User criteriaUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		postUser.setUid(criteriaUser.getUid());
		user = userServiceImp.updateUser(criteriaUser, postUser);
		user.setPasswd(null);
		user.setPhone(null);
		user.setName(null);
		if(user != null){
			userIdentifyService.updateUser(user);
		}
		return user;
	}
	
	/**
	 * 更新用户密码
	 * @param passwdModel
	 * @return
	 */
	@RequestMapping(value="/update/passwd", method=RequestMethod.POST)
	@ResponseBody
	public AppResponseModel updatePasswd(@RequestBody PasswdModel passwdModel){
		AppResponseModel model = new AppResponseModel();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(user != null && user.getUid() > 0 && passwdModel.getNewPasswd() != null && passwdModel.getOldPasswd() != null){
			String oldPasswd = passwordEncoder.encodePassword(passwdModel.getOldPasswd(), null);
			String newPasswd = passwordEncoder.encodePassword(passwdModel.getNewPasswd(), null);
			User updateUser = new User();
			updateUser.setUid(user.getUid());
			updateUser.setPasswd(passwdModel.getNewPasswd());
			if(!userServiceImp.updatePasswd(user, oldPasswd, newPasswd)){
				model.setResultCode(0);
				model.setResultMsg("密码错误");
				return model;
			}
			if(!userIdentifyService.updateUser(updateUser)){
				model.setResultCode(0);
				model.setResultMsg("修改失败，请重试");
				return model;
			}
			model.setResultCode(1);
			model.setResultMsg("修改成功");
			return model;
		}
		model.setResultCode(0);
		model.setResultMsg("未登录");
		return model;
	}
	
	/**
	 * 测试用
	 * @param oldpasswd
	 * @param newpasswd
	 * @return
	 */
	@RequestMapping(value="/update/passwd/{oldpass}/{newpass}", method=RequestMethod.GET)
	@ResponseBody
	public AppResponseModel updateGetPasswd(@PathVariable("oldpass") String oldpasswd, @PathVariable("newpass") String newpasswd){
		AppResponseModel model = new AppResponseModel();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(user != null && user.getUid() > 0 && newpasswd != null && oldpasswd != null){
			String oldPasswd = passwordEncoder.encodePassword(oldpasswd, null);
			String newPasswd = passwordEncoder.encodePassword(newpasswd, null);
			User updateUser = new User();
			updateUser.setUid(user.getUid());
			updateUser.setPasswd(newpasswd);
			if(user.getName() != null){
				updateUser.setName(user.getName());
			}
			if(user.getAvatar() != null){
				updateUser.setAvatar(user.getAvatar());
			}
			if(user.getEmail() != null){
				updateUser.setEmail(user.getEmail());
			}
			if(user.getSex() == 0 || user.getSex() == 1){
				updateUser.setSex(user.getSex());
			}
			if(user.getBirthday() != null){
				updateUser.setBirthday(user.getBirthday());
			}
			if(user.getQq() != null){
				updateUser.setQq(user.getQq());
			}
			if(user.getPhone() != null){
				updateUser.setPhone(user.getPhone());
			}
			if(!userServiceImp.updatePasswd(user, oldPasswd, newPasswd)){
				model.setResultCode(0);
				model.setResultMsg("OldPasswd error");
				return model;
			}
			if(!userIdentifyService.updateUser(updateUser)){
				model.setResultCode(0);
				model.setResultMsg("Very Bad Thing");
				return model;
			}
			model.setResultCode(1);
			model.setResultMsg("SUCCESS");
			return model;
		}
		model.setResultCode(0);
		model.setResultMsg("Param error");
		return model;
	}
	
	public UserService getUserServiceImp() {
		return userServiceImp;
	}


	public void setUserServiceImp(UserService userServiceImp) {
		this.userServiceImp = userServiceImp;
	}


	public ArticleService getArticleServiceImp() {
		return articleServiceImp;
	}


	public void setArticleServiceImp(ArticleService articleServiceImp) {
		this.articleServiceImp = articleServiceImp;
	}
	
	
	/**
	 * 上传用户头像
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/upload/avatar", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String uploadAvtar(@RequestParam(value = "file", required = true) MultipartFile file){
		String returnUrl = null; // 返回的图片URL
		String localhostString = null; //当前主机的地址
		String filePath = null; // 存储用户头像图片的绝对路径
		String simFilePath = null; // 存储用户头像压缩图片的绝对路径
		String fileType = null;
		byte[] bytes;
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!file.isEmpty()){
			Properties props = new Properties();
			try {
				bytes = file.getBytes();
				props=PropertiesLoaderUtils.loadAllProperties("config.properties");
				localhostString = props.getProperty("localhost");
				filePath = props.getProperty("pictureDir") + File.separator +  "userPic" + File.separator + user.getId() + File.separator + "avatar";
				simFilePath = filePath + File.separator + "sim";
				fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				Path path = Paths.get(filePath);
				Path simPath = Paths.get(simFilePath);
				if(Files.notExists(path)){
					Files.createDirectories(path);
				}
				if(Files.notExists(simPath)){
					Files.createDirectories(simPath);
				}
				FileOutputStream fos = new FileOutputStream(filePath + File.separator + "avatar" + fileType);
				fos.write(bytes); // 写入文件
				fos.close();
				compressPicUtils.compressByThumbnailator(new File(filePath + File.separator + "avatar" + fileType),
														 new File(simFilePath + File.separator + "avatar" + fileType),
														 160, 160, 0.9, true);
				returnUrl = (localhostString + simFilePath.split("cdsb")[1] + File.separator + "avatar" + fileType).replaceAll("\\\\", "/");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(returnUrl);
		return returnUrl;
	}
}
