package com.shangbao.web.control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.shangbao.model.ArticleState;
import com.shangbao.model.ChannelState;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.Channel;
import com.shangbao.model.persistence.User;
import com.shangbao.model.show.ChannelList;
import com.shangbao.model.show.TitleList;
import com.shangbao.service.DownLoadPicService;
import com.shangbao.service.PendTagService;
import com.shangbao.service.PictureService;
import com.shangbao.utils.CompressPicUtils;

@Controller
@RequestMapping("/picture")
public class PictureController {
	@Resource
	private PictureService pictureServiceImp;
	@Resource
	private DownLoadPicService downLoadPicServiceImp;
	@Resource
	private CompressPicUtils compressPicUtils;
	@Resource
	private PendTagService pendTagServiceImp;
	
	/**
	 * 新建图片
	 * 保存新建的图片
	 * @param article
	 */
	@RequestMapping(value="/newPicture", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Article addPicture(@RequestBody Article article){
		article.setState(ArticleState.Temp);//状态设置为暂存
		article.setTag(true);//设置为图片新闻
		String message = getLog("创建");
		if(message != null){
			article.getLogs().add(message);
		}
		Article temp = new Article();
		temp.setId(this.pictureServiceImp.addGetId(article));
		return temp;
	}
	
	/**
	 * 新建图片
	 * 提交审核图片
	 * @param article
	 */
	@RequestMapping(value="/newPicture/pend", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Article addPicturePending(@RequestBody Article article){
		if(pendTagServiceImp.isTag("article")){
			article.setState(ArticleState.Pending);//状态设置为待审
			String message = getLog("新建并提交审核");
			if(message != null){
				article.getLogs().add(message);
			}
		}else{
			article.setState(ArticleState.Published);
			String message = getLog("新建并直接发布");
			if(message != null){
				article.getLogs().add(message);
			}
		}
		article.setTag(true);//设置为图片新闻
		Article temp = new Article();
		temp.setId(this.pictureServiceImp.addGetId(article));
		return temp;
	}
	
	/**
	 * 在新建模块下的定时发布
	 * @param article
	 * @param time
	 */
	@RequestMapping(value = "/newPicture/timingpublish/{time:[\\d]+}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void addAndTimePublish(@RequestBody Article article, @PathVariable("time") Long time){
		article.setState(ArticleState.Temp);
		article.setTag(true);
		String message = getLog("新建并定时发布");
		if(message != null){
			article.getLogs().add(message);
		}
		Long id = pictureServiceImp.addGetId(article);
		List<Long> articleIds = new ArrayList<>();
		articleIds.add(id);
		publishTask(articleIds, time, null);
	}
	
	/**
	 * 获得图片标题列表
	 * @param articleState
	 * @param pageId
	 * @return
	 */
	@RequestMapping(value="/{articleState}/{pageNo}", method=RequestMethod.GET)
	@ResponseBody
	public TitleList getTitleList(@PathVariable ArticleState articleState,
			@PathVariable int pageNo){
		return this.pictureServiceImp.getTiltList(articleState, pageNo);
	}
	
	/**
	 * 获得排序后的标题列表
	 * @param articleState
	 * @param pageNo
	 * @param order
	 * @return
	 */
	@RequestMapping(value="/{articleState}/{pageId}/{order:[a-z,A-Z]+}/{direction:asc|desc}", method=RequestMethod.GET)
	@ResponseBody
	public TitleList getOrderedTitleList(@PathVariable("articleState") ArticleState articleState,
			@PathVariable("pageId") int pageNo, @PathVariable("order") String order, @PathVariable("direction") String direction){
		return this.pictureServiceImp.getOrderedList(articleState, pageNo, order, direction);
	}
	
	/**
	 * 查询
	 * @param articleState
	 * @param pageNo
	 * @param value
	 * @return
	 */
	@RequestMapping(value="/{articleState}/{pageNo}/query", method=RequestMethod.POST)
	@ResponseBody
	public TitleList fuzzyFind(@PathVariable("articleState") ArticleState articleState, @PathVariable("pageNo") int pageNo, @RequestBody Article article){
		TitleList list = new TitleList();
		if(article.getContent() != null){
			list = pictureServiceImp.fuzzyFind(article.getContent(), articleState, pageNo, 20);
		}
		return list;
	}
	
	@RequestMapping(value="/{articleState}/{pageNo}/query/{order:[a-z,A-Z]+}/{direction:asc|desc}", method=RequestMethod.POST)
	@ResponseBody
	public TitleList fuzzyFind(@PathVariable ArticleState articleState,
							   @PathVariable int pageNo,
							   @PathVariable String order,
							   @PathVariable String direction,
							   @RequestBody Article article){
		TitleList list = new TitleList();
		if(article.getContent() != null){
			list = pictureServiceImp.fuzzyFindOrder(article.getContent(), articleState, pageNo, 20, order, direction);
		}
		return list;
	}
	
	/**
	 * 获取一篇图片文章
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{articleState}/{pageNo}/{id:[\\d]+}", method = RequestMethod.GET)
	@ResponseBody
	public Article findOne(@PathVariable("id") Long id) {
		Article article = pictureServiceImp.findOne(id);
		return article;
	}

	/**
	 * 获取一篇文章的操作日志
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{articleState}/{pageNo}/{id:[\\d]+/log}", method = RequestMethod.GET)
	@ResponseBody
	public List<String> getArticleLogs(@PathVariable("id") Long id){
		Article article = pictureServiceImp.findOne(id);
		return article.getLogs();
	}
	
	
	/**
	 * 修改一篇图片文章
	 * 
	 * @param state 只有暂存，已发布，撤销的文章能够修改
	 * @param id
	 * @param article
	 */
	@RequestMapping(value = "/{articleState}/{pageNo}/{id:[\\d]+}", method = RequestMethod.PUT)
	@ResponseBody
	public boolean modifyOne(@PathVariable("articleState") ArticleState state,
			@PathVariable("id") Long id, @RequestBody Article article) {
		if (state.equals(ArticleState.Crawler)
				|| state.equals(ArticleState.Revocation)
				|| state.equals(ArticleState.Temp)
				|| state.equals(ArticleState.Published)) {
			article.setId(id);
			String message = getLog("修改");
			if(message != null){
				article.getLogs().add(message);
			}
			pictureServiceImp.update(article);
			return true;
		}
		return false;
	}
	
	/**
	 * 状态转换
	 * 
	 * @param articleState
	 * @param pageNo
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{articleState}/{pageNo}/statechange/{ids:[\\d]+(?:_[\\d]+)*}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public TitleList stateTranslatePut(
			@PathVariable("articleState") ArticleState articleState,
			@PathVariable("pageNo") int pageNo, @PathVariable("ids") String id) {
		String[] idsString = id.split("_");
		List<Long> idList = new ArrayList<Long>();
		String message = getLog("状态转换");
		for (String idString : idsString) {
			idList.add(Long.parseLong(idString));
		}
		pictureServiceImp.setPutState(articleState, idList, message);
		return pictureServiceImp.getTiltList(articleState, pageNo);
	}

	@RequestMapping(value = "/{articleState}/{pageNo}/{order:[a-z,A-Z]+}/{direction:asc|desc}/{ids:[\\d]+(?:_[\\d]+)*}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public TitleList stateTranslatePut(
			@PathVariable("articleState") ArticleState articleState,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("order") String order, 
			@PathVariable("ids") String id,
			@PathVariable("direction") String direction) {
		String[] idsString = id.split("_");
		List<Long> idList = new ArrayList<Long>();
		String message = getLog("状态转换");
		for (String idString : idsString) {
			idList.add(Long.parseLong(idString));
		}
		pictureServiceImp.setPutState(articleState, idList, message);
		return pictureServiceImp.getOrderedList(articleState, pageNo, order, direction);
	}
	
	/**
	 * 定时发布
	 * @param ids
	 * @param time
	 */
	@RequestMapping(value="/{articleState}/{pageNo}/timingpublish/{ids:[\\d]+(?:_[\\d]+)*}/{time:[\\d]+}")
	@ResponseStatus(HttpStatus.OK)
	public void timingPublish(@PathVariable("ids") String ids, @PathVariable("time") Long time){
		String[] idStrings = ids.split("_");
		List<Long> idList = new ArrayList<>();
		String message = getLog("定时发布：");
		for(String id : idStrings){
			idList.add(Long.parseLong(id));
		}
		publishTask(idList, time, message);
	}

	@RequestMapping(value = "/{articleState}/{pageNo}/statechange/{ids:[\\d]+(?:_[\\d]+)*}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public TitleList stateTranslateDelete(
			@PathVariable("articleState") ArticleState articleState,
			@PathVariable("pageNo") int pageNo, @PathVariable("ids") String id) {
		String[] idsString = id.split("_");
		List<Long> idList = new ArrayList<Long>();
		String message = getLog("状态转换");
		for (String idString : idsString) {
			idList.add(Long.parseLong(idString));
		}
		pictureServiceImp.setDeleteState(articleState, idList, message);
		return pictureServiceImp.getTiltList(articleState, pageNo);
	}

	@RequestMapping(value = "/{articleState}/{pageNo}/{order:[a-z,A-Z]+}/{direction:asc|desc}/{ids:[\\d]+(?:_[\\d]+)*}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public TitleList stateTranslateDelete(
			@PathVariable("articleState") ArticleState articleState,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("order") String order, 
			@PathVariable("ids") String id,
			@PathVariable("direction") String direction) {
		String[] idsString = id.split("_");
		List<Long> idList = new ArrayList<Long>();
		String message = getLog("状态转换");
		for (String idString : idsString) {
			idList.add(Long.parseLong(idString));
		}
		pictureServiceImp.setDeleteState(articleState, idList, message);
		return pictureServiceImp.getOrderedList(articleState, pageNo, order, direction);
	}
	
	private void publishTask(final List<Long> idList, final Long date, final String message){
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(message == null){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
					pictureServiceImp.setPutState(ArticleState.Pending, idList, sdf.format(new Date()));
				}else{
					pictureServiceImp.setPutState(ArticleState.Pending, idList, message);
				}
			}
		}, date);
	}
	
	/**
	 * 显示活动
	 */
	@RequestMapping(value="/activity/{pageNo:[\\d]+}", method=RequestMethod.GET)
	@ResponseBody
	public ChannelList showActivity(@PathVariable("pageNo") int pageNo){
		return this.pictureServiceImp.getActivity(pageNo, 20);
	}
	
	@RequestMapping(value="/activity/{pageNo:[\\d]+}/{order:[a-z,A-Z]+}", method=RequestMethod.GET)
	@ResponseBody
	public ChannelList showOderedActivity(@PathVariable("pageNo") int pageNo, @PathVariable("order") String order){
		return this.pictureServiceImp.getActivity(pageNo, 20);
	}
	
	/**
	 * 新建活动
	 */
	@RequestMapping(value="/activity", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void addActivity(@RequestBody Channel activity){
		activity.setState(ChannelState.Activity);
		pictureServiceImp.add(activity);
	}
	
	/**
	 * 删除活动
	 */
	@RequestMapping(value="/activity", method=RequestMethod.DELETE)
	public void deleteActivity(@RequestBody List<Channel> channels){
		pictureServiceImp.delete(channels);
	}

	/**
	 * 下载当日的成都图片网的新闻
	 */
	@RequestMapping(value="/download", method=RequestMethod.GET)
	public void downloadPic(){
		downLoadPicServiceImp.saveAsArticle();
	}
	
	
	/**
	 * 上传文件
	 * @param file
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String uploadPicture(@RequestParam(value = "file", required = true) MultipartFile file) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmm");
		String returnString = "";
		String localhostString = "";
		Random random = new Random();
		String fileName = sdf.format(new Date()) + file.getSize() + "" + random.nextInt(1000) + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));//保存到本地的文件名
		Properties props = new Properties();
		try {
			props=PropertiesLoaderUtils.loadAllProperties("config.properties");
			String filePath = props.getProperty("pictureDir") + File.separator +"kuaipaiPic";//目录的路径
			String filePathMid = filePath + File.separator + "mid";
			String filePathSim = filePath + File.separator + "sim";
			localhostString = props.getProperty("localhost");
			Path path = Paths.get(filePath);
			if(Files.notExists(path)){
				Path filPath = Files.createDirectories(path);
			}
			Path pathMid = Paths.get(filePathMid);
			if(Files.notExists(pathMid)){
				Files.createDirectories(pathMid);
			}
			Path pathSim = Paths.get(filePathSim);
			if(Files.notExists(pathSim)){
				Files.createDirectories(pathSim);
			}
			if(!file.isEmpty()){
				byte[] bytes;
				bytes = file.getBytes();
				FileOutputStream fos = new FileOutputStream(filePath + File.separator + fileName);
				fos.write(bytes); // 写入文件
				fos.close();
				//压缩 800 * ？
				if(fileName.endsWith(".gif")){
					Files.copy(new File(filePath + File.separator + fileName).toPath(), new File(filePathMid + File.separator + fileName).toPath());
				}else{
					compressPicUtils.compressByThumbnailator(new File(filePath + File.separator + fileName), new File(filePathMid + File.separator + fileName), 800, 0, 0.8 ,true);
				}
				//压缩 200 * 150
				compressPicUtils.compressByThumbnailator(new File(filePath + File.separator + fileName), new File(filePathSim + File.separator + fileName), 200, 150, 0.8 ,true);
				returnString = path.toString().split("cdsb")[1] + File.separator + "mid" + File.separator + fileName;
//				System.out.println(returnString);
				return localhostString + returnString.replaceAll("\\\\", "/");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println("upload done!");
		return null;
	}
	
	public PictureService getPictureServiceImp() {
		return pictureServiceImp;
	}

	public void setPictureServiceImp(PictureService pictureServiceImp) {
		this.pictureServiceImp = pictureServiceImp;
	}

	public DownLoadPicService getDownLoadPicServiceImp() {
		return downLoadPicServiceImp;
	}

	public void setDownLoadPicServiceImp(DownLoadPicService downLoadPicServiceImp) {
		this.downLoadPicServiceImp = downLoadPicServiceImp;
	}
	
	private String getLog(String message){
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(user == null){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		return sdf.format(new Date()) + " " + user.getId() + " " + user.getName() + " " + message;
	}
}
