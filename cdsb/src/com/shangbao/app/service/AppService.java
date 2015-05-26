package com.shangbao.app.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

import com.shangbao.app.model.ActiveModel;
import com.shangbao.app.model.AppChannelModel;
import com.shangbao.app.model.AppModel;
import com.shangbao.app.model.AppPictureModel;
import com.shangbao.app.model.ArticleInfo;
import com.shangbao.app.model.CdsbArticle;
import com.shangbao.app.model.CdsbModel;
import com.shangbao.app.model.ColumnPageModel;
import com.shangbao.app.model.FrontPageModel;
import com.shangbao.dao.ArticleDao;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.Channel;
import com.shangbao.model.show.Page;
import com.shangbao.model.show.SingleCommend;
import com.shangbao.service.ArticleService;

@Service
public class AppService {
	@Resource
	private AppModel appModel;
	@Resource
	private ArticleService articleServiceImp;
	@Resource
	private ArticleDao articleDaoImp;
	
	private final String appUrlPrefix = "/{phoneType}/";
	private String localhost;
	private String salt;
	private String qiniu;
	
	public AppService(){
		localhost = "http://www.cdsb.mobi/";
		salt = "cdsbbsdc123321";
		qiniu = "http://7xj9jv.com2.z0.glb.qiniucdn.com";
		Properties props;
		try {
			props = PropertiesLoaderUtils.loadAllProperties("config.properties");
			if(props.getProperty("localhost") != null && !props.getProperty("localhost").isEmpty()){
				localhost = props.getProperty("localhost");
			}
			if(props.getProperty("salt") != null && props.getProperty("salt") != ""){
				salt = props.getProperty("salt");
			}
			if(props.getProperty("qiniu") != null && props.getProperty("qiniu") != ""){
				qiniu = props.getProperty("qiniu");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取app顶级栏目信息
	 * @return
	 */
	public FrontPageModel getChannels(String id){
		FrontPageModel frontPageModel = new FrontPageModel();
		List<String> pictures = appModel.getStartPictures(id);
		if(pictures != null){
			for(String picUrl : pictures){
				frontPageModel.addPictureUrl(picUrl);
			}
		}
		List<Channel> channels = appModel.getTopChannels();
		if(channels.isEmpty() || channels == null){
			return frontPageModel;
		}
		for(Channel channel : channels){
			frontPageModel.addChannel(channel.getChannelName(), appUrlPrefix + channel.getEnglishName());
		}
		return frontPageModel;
	}
	
	/**
	 * 获取含有子分栏的顶级目录的页面(如“最新资讯”，“本地报告”，“快拍成都”)
	 * @param ChannelName 顶级分类名
	 * @param titleSize 每个子分类显示的文章标题数目
	 * @return
	 */
	public AppChannelModel getChannelModel(String channelEnName, int titleSize){
		Map<String, String> channelEn_Cn = appModel.getChannelEn_Cn();
		String channelName = channelEn_Cn.get(channelEnName);
		if(channelName == null){
			return null;
		}
		AppChannelModel appChannelModel = new AppChannelModel();
		List<Channel> topChannels = appModel.getTopChannels();
		if(topChannels == null || topChannels.isEmpty())
			return appChannelModel;
		for(Channel channel : topChannels){//找父分类
			if(channel.getChannelName().equals(channelName)){
				appChannelModel.setChannelName(channelName);
				List<Channel> sonChannels = appModel.getSonChannels(channel);//找子分类
				if(sonChannels == null || sonChannels.isEmpty()){
					return appChannelModel;
				}
				for(Channel sonChannel : sonChannels){
					if(sonChannel.getChannelName().equals("爬虫滚动") || sonChannel.getChannelName().equals("成都滚动")){
						List<Article> articles = appModel.getAppMap().get(sonChannel.getChannelName());
						if(articles != null){
							if(20 >= articles.size()){
								appChannelModel.addColumn(sonChannel.getChannelName(), sonChannel.getEnglishName(), articles.subList(0, articles.size()));
							}else{
								appChannelModel.addColumn(sonChannel.getChannelName(), sonChannel.getEnglishName(), articles.subList(0, 20));
							}
						}
						continue;
					}
					List<Article> articles = appModel.getAppMap().get(sonChannel.getChannelName());
					if(articles != null){
						if(titleSize >= articles.size()){
							if(channelEnName.equals("kuaipai") && articles.size() > 1){
								List<Article> tempArticles = new ArrayList<>();
								if(sonChannel.getChannelName().equals("拍客集")){
									tempArticles.add(articles.get(1));
								}else{
									tempArticles.add(articles.get(0));
								}
								appChannelModel.addColumn(sonChannel.getChannelName(), sonChannel.getEnglishName(), tempArticles);
							}else{
								appChannelModel.addColumn(sonChannel.getChannelName(), sonChannel.getEnglishName(), articles.subList(0, articles.size()));
							}
						}else{
							if(channelEnName.equals("kuaipai") && articles.size() > 1){
								List<Article> tempArticles = new ArrayList<>();
								if(sonChannel.getChannelName().equals("PK台")){
									tempArticles.add(articles.get(1));
								}else{
									tempArticles.add(articles.get(0));
								}
								appChannelModel.addColumn(sonChannel.getChannelName(), sonChannel.getEnglishName(), tempArticles);
							}else{
								appChannelModel.addColumn(sonChannel.getChannelName(), sonChannel.getEnglishName(), articles.subList(0, titleSize));
							}
						}
					}
				}
			}
		}
		return appChannelModel;
	}
	
	/**
	 * 获取一个分类下的文章标题列表
	 * @param ChannelName
	 * @param pageNo 页码
	 * @param pageSize 页面大小
	 * @return
	 */
	public ColumnPageModel getArticlesFromChannel(String channelName, int pageNo, int pageSize){
		Map<String, String> channelEn_Cn = appModel.getChannelEn_Cn();
		channelName = channelEn_Cn.get(channelName);
		//channelName = appModel.getChannelEn_Cn().get(channelName);
		if(channelName == null){
			return null;
		}
		ColumnPageModel columnPageModel = new ColumnPageModel();
		List<Article> articles = appModel.getAppMap().get(channelName);
		if(articles == null || articles.isEmpty()){
			return columnPageModel;
		}
		Page<Article> page = new Page<Article>(pageNo, pageSize, articles.size());
		if(articles != null){
			int toIndex = page.getLastResult();
			int fromIndex = page.getFirstResult();
			if(0 <= fromIndex && fromIndex <= toIndex){
				List<Article> pageArticles = articles.subList(fromIndex, toIndex);
				columnPageModel.setCurrentNo(page.getPageNo());
				columnPageModel.setPageCount(page.getTotalPage());
				int index = fromIndex + 1;
				for(Article article : pageArticles){
					columnPageModel.addNewsTitle(article, index ++);
				}
			}
		}
		return columnPageModel;
	}
	
	/**
	 * 获取一个分类下的子分类
	 * @return
	 */
	public List<Channel> getSonChannels(String channelName){
		channelName = appModel.getChannelEn_Cn().get(channelName);
		if(channelName == null){
			return null;
		}
		for(Channel fatherChannel : appModel.getTopChannels()){
			if(channelName.equals(fatherChannel.getChannelName())){
				return appModel.getSonChannels(fatherChannel);
			}
		}
		return null;
	}
	
	/**
	 * 获取一篇文章的HTML
	 * @return
	 */
	public AppHtml getNewsHtml(Long articleId, String fromIp, String udid, String uid){
		AppHtml appHtml = new AppHtml();
		if(!appModel.getArticleMap().isEmpty()){
			if(appModel.getArticleMap().containsKey(articleId)){
				appHtml.html = articleToHtml(appModel.getArticleMap().get(articleId), udid, uid);
				appModel.addClick(articleId, fromIp);
				appHtml.articleId = articleId;
			}else{
				Article articleInMongo = articleServiceImp.findOne(articleId);
				if(articleInMongo != null){
					//appModel.getArticleMap().put(articleId, articleInMongo);
					//appHtml.html = articleInMongo.getContent();
					appHtml.html = articleToHtml(articleInMongo, udid, uid);
//					appHtml.articleId = articleId;
//					Update update = new Update();
//					update.inc("clicks", 1);
//					articleDaoImp.update(articleInMongo, update);
					//appModel.addClick(articleId);
					return appHtml;
				}
			}
		}
		return appHtml;
	}
	
	public ArticleInfo getArticleInfo(Long articleId){
		ArticleInfo articleInfo = new ArticleInfo();
		if(!appModel.getArticleMap().isEmpty()){
			if(appModel.getArticleMap().containsKey(articleId)){
				Article article = appModel.getArticleMap().get(articleId);
				articleInfo.setTitle(article.getTitle());
				List<String> picUrls = article.getPicturesUrl();
				if(picUrls != null && !picUrls.isEmpty()){
					articleInfo.setPicUrl(picUrls.get(0).replaceAll("/mid/", "/sim/"));
				}else{
					articleInfo.setPicUrl("http://www.cdsb.mobi/cdsb/WEB-SRC/icon.png");
				}
			}else {
				Article articleInMongo = articleServiceImp.findOne(articleId);
				if(articleInMongo != null){
					articleInfo.setTitle(articleInMongo.getTitle());
					List<String> picUrls = articleInMongo.getPicturesUrl();
					if(picUrls != null && !picUrls.isEmpty()){
						articleInfo.setPicUrl(picUrls.get(0).replaceAll("/mid/", "/sim/"));
					}else{
						articleInfo.setPicUrl("http://www.cdsb.mobi/cdsb/WEB-SRC/icon.png");
					}
				}
			}
		}
		return articleInfo;
	}
	
	public CdsbArticle getCdsbArticle(long articleId){
		CdsbArticle cdsbArticle = new CdsbArticle();
		if(!appModel.getArticleMap().isEmpty()){
			if(appModel.getArticleMap().containsKey(articleId)){
				Article article = appModel.getArticleMap().get(articleId);
				cdsbArticle = new CdsbArticle(article);
			}else{
				Article articleInMongo = articleServiceImp.findOne(articleId);
				if(articleInMongo != null){
					cdsbArticle = new CdsbArticle(articleInMongo);
				}
			}
		}
		return cdsbArticle;
	}
	
	/**
	 * 获取为商报遗留app提供的文章
	 * @param sortTime
	 * @return
	 */
	public CdsbModel getCdsbModel(long sortTime){
		CdsbModel model = new CdsbModel();
		List<Article> articles = appModel.getSbArticles();
		int i = 0;
		int j = 0;
		for(Article article : articles){
			if(i < 4){
				model.addHotArticle(article);
				i ++;
			}else{
				if(j > 20)
					break;
				if(sortTime == 0 && j < 20){
					model.addNormalArticle(article);
					j ++;
				}else if(article.getTime().getTime() < sortTime && j < 20){
					model.addNormalArticle(article);
					j ++;
				}
			}
		}
		return model;
	}
	
	public int addJsClick(Long articleId, String fromIp, String udid){
		return appModel.addJsClick(articleId, fromIp, udid);
	}

	
	public int getJsClick(Long articleId){
		return appModel.getJsClick(articleId);
	}
	
	public int addLike(Long articleId){
		return appModel.addLike(articleId);
	}
	
	public int getLike(Long articleId){
		return appModel.getLike(articleId);
	}
	
	/**
	 * 根据文章id返回
	 * @param articleId
	 * @return
	 */
	public List<SingleCommend> getCommentByArticleId(Long articleId){
		return appModel.findComments(articleId);
	}
	
	/**
	 * 获取所有活动信息
	 * @return
	 */
	public ActiveModel getActives(){
		ActiveModel actives = new ActiveModel();
		List<Channel> channels = appModel.getActivities();
		if(channels != null && !channels.isEmpty()){
			for(Channel activity : channels){
				actives.addActive(activity.getChannelName(), activity.getSummary());
			}
		}
		return actives;
	}
	
	/**
	 * 返回图片的详细页面
	 * @param channelName 快拍成都下级分类
	 * @param articleIndex 
	 * @return
	 */
	public AppPictureModel getPictureDetails(String channelName, int articleIndex){
		channelName = appModel.getChannelEn_Cn().get(channelName);
		if(channelName == null){
			return null;
		}
		AppPictureModel pictureModel = new AppPictureModel();
		List<Article> articles;
		if((articles = appModel.getAppMap().get(channelName)) != null){
			if(articleIndex > 0 && articleIndex <= articles.size() + 1){
				pictureModel = new AppPictureModel(articles.get(articleIndex - 1));
			}
		}
		return pictureModel;
	}
	
	/**
	 * 添加用户评论
	 * @return
	 */
	public void addComment(Long articleId, SingleCommend singleCommend){
		appModel.addComment(articleId, singleCommend);
	}
	
	/**
	 * 添加用户点赞
	 */
	public void sendLike(Long articleId){
		appModel.addLike(articleId);
	}
	
	/**
	 * 用户上传图片
	 */
	public void postPictures(Article pictureArticle){
		appModel.postPictures(pictureArticle);
	}
	
	/*
	 * 下面方法为后台Controller调用
	 */
	
	/**
	 * 返回当前所有Channel以及文章列表
	 * @return
	 */
	public List<BackChannelModel> getAllChannels(){
		List<BackChannelModel> channels = new ArrayList<BackChannelModel>();
		List<Channel> orderedChannels = appModel.getChannelOrdered();
		if(!this.appModel.getAppMap().isEmpty() && !orderedChannels.isEmpty()){
			for(Channel tempChannel : orderedChannels){
				String channelEnName = tempChannel.getEnglishName();
				String channelChName = tempChannel.getChannelName();
				BackChannelModel backChannelModel = new BackChannelModel();
				if(appModel.getAppMap().get(channelChName) != null && !appModel.getAppMap().get(channelChName).isEmpty()){
					backChannelModel.ChannelEnglishName = channelEnName;
					backChannelModel.ChannelName = channelChName;
					List<Article> articles = appModel.getAppMap().get(channelChName);
					int i = 1;
					for(Article article : articles){
						String articleTitle = article.getTitle();
						if(article.getChannelIndex().isEmpty() || !article.getChannelIndex().containsKey(channelChName)){
							articleDaoImp.fixArticle(article.getId());
							continue;
						}
						backChannelModel.addTitle(articleTitle, article.getId(), i, article.getChannelIndex().get(channelChName) > (Integer.MAX_VALUE/2));
						i ++;
						if(i >= 30)
							break;
					}
					channels.add(backChannelModel);
				}
			}
			
//			for(Map.Entry<String, List<Article>> entry : this.appModel.getAppMap().entrySet()){
//				String channelName = entry.getKey();
//				List<Article> articles = entry.getValue();
//				BackChannelModel backChannelModel = new BackChannelModel();
//				backChannelModel.ChannelName = channelName;
//				
//				if(articles != null && !articles.isEmpty()){
//					int i = 1;
//					for(Article article : articles){
//						String articleTitle = article.getTitle();
//						backChannelModel.addTitle(articleTitle, i);
//						i ++;
//					}
//				}
//				channels.add(backChannelModel);
//			}
		}
		return channels;
	}
	
	public BackChannelModel getChannelByName(String channelName){
		channelName = appModel.getChannelEn_Cn().get(channelName);
		if(channelName == null){
			return null;
		}
		BackChannelModel backChannelModel = new BackChannelModel();
		if(appModel.getAppMap().containsKey(channelName)){
			backChannelModel.ChannelName = channelName;
			List<Article> articles = appModel.getAppMap().get(channelName);
			if(articles != null && !articles.isEmpty()){
				int i = 1;
				for(Article article : articles){
					backChannelModel.addTitle(article.getTitle(), article.getId(), i, article.getChannelIndex().get(channelName) > (Integer.MAX_VALUE/2));
					i ++;
				}
			}
		}
		return backChannelModel;
	}
	
	/**
	 * 设置文章位置
	 * @param channelName 分类名字
	 * @param index 文章在appModel中的位置
	 * @param tag  true为上移一位，false为下移一位
	 */
	public synchronized BackChannelModel setArticleLocation(String channelName, int index, boolean tag){
		channelName = appModel.getChannelEn_Cn().get(channelName);
		if(channelName == null){
			return null;
		}
		index --;
		if(this.appModel.getAppMap().containsKey(channelName)){
			if(index >= 0 && index < appModel.getAppMap().get(channelName).size()){
				if(tag){//上移一位
					if(index > 0){
						Article articleA = appModel.getAppMap().get(channelName).get(index - 1);
						Article articleB = appModel.getAppMap().get(channelName).get(index);
						appModel.swapArticle(channelName, articleA.getId(), articleB.getId());
					}
				}else{//下移一位
					if(index < appModel.getAppMap().get(channelName).size() - 1){
						Article articleA = appModel.getAppMap().get(channelName).get(index + 1);
						Article articleB = appModel.getAppMap().get(channelName).get(index);
						appModel.swapArticle(channelName, articleA.getId(), articleB.getId());
						appModel.redeployChannelArticles(channelName);
					}
				}
			}
		}
		return getChannelByName(channelName);
	}
	
	/**
	 * 将文章置顶
	 * @param channelName 分类名字
	 * @param index 文章在appModel中的位置
	 */
	public synchronized void setArticleTop(String channelName, int index){
		channelName = appModel.getChannelEn_Cn().get(channelName);
		if(channelName == null){
			return;
		}
		index --;
		if(this.appModel.getAppMap().containsKey(channelName)){
			if(index > 0 && index < appModel.getAppMap().get(channelName).size()){
				//Article article = appModel.getAppMap().get(channelName).get(index);
				appModel.setTopArticle(channelName, index);
				//appModel.redeployChannelArticles(channelName);
			}
		}
	}
	
	public synchronized void unSetArticleTop(String channelName, int index){
		channelName = appModel.getChannelEn_Cn().get(channelName);
		if(channelName == null){
			return;
		}
		index --;
		if(this.appModel.getAppMap().containsKey(channelName)){
			appModel.unSetTopArticle(channelName, index);
		}
	}
	
	public void refresh(){
		appModel.redeployAll();
	}
	
	public AppModel getAppModel() {
		return appModel;
	}

	public void setAppModel(AppModel appModel) {
		this.appModel = appModel;
	}
	
	
	public ArticleService getArticleServiceImp() {
		return articleServiceImp;
	}

	public void setArticleServiceImp(ArticleService articleServiceImp) {
		this.articleServiceImp = articleServiceImp;
	}


	public ArticleDao getArticleDaoImp() {
		return articleDaoImp;
	}

	public void setArticleDaoImp(ArticleDao articleDaoImp) {
		this.articleDaoImp = articleDaoImp;
	}

	
	private String articleToHtml(Article article, String udid, String uid){
		if(uid == null){
			uid = "0";
		}
		if(udid == null){
			udid = "0";
		}
		if(article.getOutSideUrl() == null || article.getOutSideUrl().equals("")){
			//不是外链文章
			String localhostString = "";
			try {
				Properties properties = PropertiesLoaderUtils.loadAllProperties("config.properties");
				localhostString = properties.getProperty("localhost");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String css = "app.css";
//			if(article.isTag()){
//				css = "kuaipai.css";
//			}
			SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");
			String duxq = "<div ng-app=\"\" ng-controller=\"readAndZanCtrl\"><div data-ng-init=\"load()\"></div><div ng-show=\"visible\" style=\"position:fixed; z-index:10000;width: 99.5%;height: 60px;top:0px\"><a href=\"http://app.cdsb.com/download.php\"><div style=\"width: 100%;height: 50px;float: left; background:#E0E0E0;color:#000000;float: left; text-align:left; line-height:48px;\"><div style=\"margin: 5px 5px 5px 20px;;float: left;\"><img src=\"../../../WEB-SRC/icon.png\" width=\"40\" height=\"40\" /></div><span style=\"line-height:12px; float: left; margin: 20px 20px 25px 0px;  font-size:14px;\">扎根成都的客户端</span><div style=\"margin: 15px 20px;float: right; background:#E0E0E0; color:#000000;text-align:center;\"><span style=\"line-height:12px; display:block;  font-size:10px;\">立即<br>下载</span></div></div></a></div><div ng-show=\"visible\"><br> <br> <br> <br></div>";
			String duxq2 = "<div class=\"single-post-meta-top\">阅读{{clickNum}} &nbsp;&nbsp;&nbsp;&nbsp;<a ng-click=\"zanAdd(zanNum,pictureUrl)\"><img alt=\"\" src={{pictureUrl}}>{{zanNum}}</a></div></div>";
			StringBuilder html = new StringBuilder();
			html.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"zh-CN\"><head profile=\"http://gmpg.org/xfn/11\"> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><meta name=\"viewport\" content=\"width=device-width\" /> <title>");
			html.append(article.getTitle().replace("\\n", "") +"  | 成都商报新闻客户端</title>" + "<link rel=\"stylesheet\" href=\"" + localhostString + "/WEB-SRC/" + css + "\" type=\"text/css\" /> <script src=\"" + localhostString + "/WEB-SRC/src/js/angular.min.js\"></script> <script src=\"" + localhostString + "/WEB-SRC/click.js\"></script>");
			html.append("<link href=\" " + localhostString +  "/WEB-SRC/videojs/video-js.css\" rel = \"stylesheet\" type=\"text/css\"><script src = \" " + localhostString + "/WEB-SRC/videojs/video.js\"></script><script>videojs.options.flash.swf = \" " + localhostString + "/WEB-SRC/videojs/video-js.swf\";</script>");//vedio-js
			html.append("</head><body class=\"classic-wptouch-bg\"> " + duxq +  " <input type=\"hidden\" name=\"id\" value=" + article.getId() + "/> <div class=\"content single\"> <div class=\"post\"> <a class=\"sh2\">");
			html.append(article.getTitle().replace("\\n", "<br/>") + "</a><div style=\"font-size:15px; padding: 5px 0;\"></div><div class=\"single-post-meta-top\">");
			html.append((article.getAuthor() == null ? "" : article.getAuthor()) + "&nbsp&nbsp" + (article.getTime() == null ? "" : format.format(article.getTime())));
			html.append("</div><div style=\"margin-top:10px; border-top:1px solid #d8d8d8; height:1px; background-color:#fff;\"></div> <div id=\"singlentry\" class=\"full-justified\">");
			html.append(addHerf(article.getContent()));
			html.append("<p>&nbsp;</p> " + duxq2 + "</div></div></div> <div id=\"footer\"><p>成都商报</p></div></body></html>");
			return html.toString();
		}else{
			//是外联文章
			String timeStamp = new Date().getTime() / 1000 + "";
			StringBuilder html = new StringBuilder();
			html.append("<html><head><title></title></head><body>");
			html.append("<script language=\"javascript\">document.location = \"");
			html.append((article.getOutSideUrl().startsWith("http://") || article.getOutSideUrl().startsWith("https://")) ? article.getOutSideUrl() : ("http://" + article.getOutSideUrl()));
			html.append("?udid=" + udid + "&uid=" + uid + "&time=" + timeStamp + "&authcode=" + DigestUtils.sha1Hex(udid + uid + timeStamp + salt));
			html.append("\"</script></body></html>");
			//System.out.println(html);
			return html.toString();
		}
	}
	
	private String addHerf(String content){
		if(content == null){
			return "";
		}
		StringBuilder sBuilder = new StringBuilder(content);
		Pattern pat = Pattern.compile("(?:<img.*?src=)((\".*?\")*)(?:[^>]*?>)");  
		Matcher matcher = pat.matcher(sBuilder);
		while(matcher.find()){
			StringBuilder img = new StringBuilder(matcher.group());
			StringBuilder src = new StringBuilder(matcher.group(1));
			if(!src.toString().contains(localhost)){
				continue;
			}
			StringBuilder replaceString = null;
			if(src.toString().contains("/cdsb/") && qiniu != null && qiniu != ""){
				StringBuilder newImg = new StringBuilder(img.toString().replace(src.toString(), "\"" + qiniu + src.substring(src.indexOf("/cdsb/"))));
				replaceString = new StringBuilder("<a href=" + src + ">" + newImg.toString() + "</a>");
			}else{
				replaceString = new StringBuilder("<a href=" + src + ">" + img.toString() + "</a>");
			}
			content = content.replace(img, replaceString);
		}
		return content;
	}

	/**
	 * 后台在一览众显示分类和文章的模板
	 */
	public class BackChannelModel{
		public String ChannelName;
		public String ChannelEnglishName;
		public List<Title> content = new ArrayList<Title>();
		class Title{
			public String title;
			public long articleId;
			public int index;
			public boolean top;
		}
		public void addTitle(String articleTitle, long articleId, int index, boolean top){
			Title title = new Title();
			title.title = articleTitle;
			title.articleId = articleId;
			title.index = index;
			title.top = top;
			this.content.add(title);
		}
		
	}
	
	public class AppHtml{
		public String html;
		public Long articleId;
	}
}
