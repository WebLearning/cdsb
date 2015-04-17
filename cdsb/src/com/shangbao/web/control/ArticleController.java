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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.shangbao.app.service.AppPushService;
import com.shangbao.model.ArticleState;
import com.shangbao.model.PushModel;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.User;
import com.shangbao.model.show.TitleList;
import com.shangbao.service.ArticleService;
import com.shangbao.service.PendTagService;
import com.shangbao.utils.CompressPicUtils;

@Controller
@RequestMapping("/article")
public class ArticleController {
	@Resource
	private ArticleService articleServiceImp;
	@Resource
	private CompressPicUtils compressPicUtils;
	@Resource
	private AppPushService appPushService;
	@Resource
	private PendTagService pendTagServiceImp;

	/**
	 * 新建文章
	 * 保存文章
	 * @param article
	 */
	@RequestMapping(value = "/newArticle", method = RequestMethod.POST)
	@ResponseBody
	public Article add(@RequestBody Article article) {
		article.setState(ArticleState.Temp);
		String message = getLog("创建");
		if(message != null){
			article.getLogs().add(message);
		}
		long id = articleServiceImp.addGetId(article);
		Article temp = new Article();
		temp.setId(id);
		return temp;
	}

	/**
	 * 新建文章
	 * 提交审核
	 * @param article
	 */
	@RequestMapping(value = "/newArticle/pend", method = RequestMethod.POST)
	@ResponseBody
	public Article addPending(@RequestBody Article article){
		if(pendTagServiceImp.isTag("article")){
			article.setState(ArticleState.Pending);
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
//		articleServiceImp.add(article);
		Article temp = new Article();
		temp.setId(articleServiceImp.addGetId(article));
		return article;
	}
	
	/**
	 * 在新建模块下的定时发布
	 * @param article
	 * @param time
	 */
	@RequestMapping(value = "/newArticle/timingpublish/{time:[\\d]+}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void addAndTimePublish(@RequestBody Article article, @PathVariable("time") Long time){
		article.setState(ArticleState.Temp);
		String message = getLog("新建并定时发布");
		if(message != null){
			article.getLogs().add(message);
		}
		Long id = articleServiceImp.addGetId(article);
		System.out.println("定时发布文章id：" + id);
		List<Long> articleIds = new ArrayList<>();
		articleIds.add(id);
		publishTask(articleIds, time, null);
	}
	
	/**
	 * 获取是否需要待审
	 * @return
	 */
	@RequestMapping(value = "/ispending", method=RequestMethod.GET)
	@ResponseBody
	public boolean isPending(){
		return pendTagServiceImp.isTag("article");
	}
	
	/**
	 * 标题列表分页
	 * 
	 * @param articleState
	 * @param pageId
	 * @return
	 */
	@RequestMapping(value = "/{articleState}/{pageId}", method = RequestMethod.GET)
	@ResponseBody
	public TitleList pageTest(@PathVariable ArticleState articleState,
			@PathVariable int pageId) {
		TitleList titleList = articleServiceImp.getTiltList(articleState, pageId);
		return titleList;
	}

	@RequestMapping(value="/channel/{channelEnName}/{articleState}/{pageId:[\\d]+}", method = RequestMethod.GET)
	@ResponseBody
	public TitleList page(@PathVariable ArticleState articleState, @PathVariable String channelEnName,
			@PathVariable int pageId){
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String role = user.getRole();
		TitleList titleList = articleServiceImp.getTitleList(articleState, channelEnName, pageId);
		return titleList;
	}
	
	/**
	 * 按照Order排序
	 * 
	 * @param articleState
	 * @param pageId
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "/{articleState}/{pageNo}/{order:[a-z,A-Z]+}/{direction:asc|desc}", method = RequestMethod.GET)
	@ResponseBody
	public TitleList order(@PathVariable ArticleState articleState,
			@PathVariable int pageNo, @PathVariable String order, @PathVariable String direction) {
		TitleList titleList = articleServiceImp.getOrderedList(articleState,
				pageNo, order, direction);
		return titleList;
	}
	
	@RequestMapping(value = "/channel/{channelEnName}/{articleState}/{pageNo:[\\d]+}/{order:[a-z,A-Z]+}/{direction:asc|desc}", method = RequestMethod.GET)
	@ResponseBody
	public TitleList order(@PathVariable String channelEnName, @PathVariable ArticleState articleState,
			@PathVariable int pageNo, @PathVariable String order, @PathVariable String direction){
		TitleList titleList = articleServiceImp.getOrderedList(articleState,
				channelEnName, pageNo, order, direction);
		return titleList;
	}
	

	/**
	 * 模糊查找
	 * @param articleState
	 * @param pageNo
	 * @param article
	 * @return
	 */
	@RequestMapping(value = "/{articleState}/{pageNo}/query", method=RequestMethod.POST)
	@ResponseBody
	public TitleList fuzzeFind(@PathVariable ArticleState articleState, @PathVariable int pageNo, @RequestBody Article article){
		TitleList list = new TitleList();
		if(article.getContent() != null){
			list = articleServiceImp.fuzzyFind(article.getContent(), articleState, pageNo, 20);
		}
		return list;
	}
	
	@RequestMapping(value = "/channel/{channelEnName}/{articleState}/{pageId:[\\d]+}/query", method=RequestMethod.POST)
	@ResponseBody
	public TitleList fuzzeFind(@PathVariable String channelEnName, @PathVariable ArticleState articleState, @PathVariable int pageNo, @RequestBody Article article){
		TitleList list = new TitleList();
		if(article.getContent() != null){
			list = articleServiceImp.fuzzyFind(article.getContent(), articleState, channelEnName, pageNo, 20);
		}
		return list;
	}
	
	@RequestMapping(value = "/{articleState}/{pageNo}/query/{order:[a-z,A-Z]+}/{direction:asc|desc}", method=RequestMethod.POST)
	@ResponseBody
	public TitleList fuzzeFindOrder(@PathVariable ArticleState articleState,
								    @PathVariable int pageNo,
								    @PathVariable String order,
								    @PathVariable String direction,
								    @RequestBody Article article){
		TitleList list = new TitleList();
		if(article.getContent() != null){
			list = articleServiceImp.fuzzyFindOrder(article.getContent(), articleState, pageNo, 20, order, direction);
		}
		return list;
	}
	
	@RequestMapping(value = "/channel/{channelEnName}/{articleState}/{pageNo}/query/{order:[a-z,A-Z]+}/{direction:asc|desc}", method=RequestMethod.POST)
	@ResponseBody
	public TitleList fuzzeFindOrder(@PathVariable String channelEnName,
									@PathVariable ArticleState articleState,
								    @PathVariable int pageNo,
								    @PathVariable String order,
								    @PathVariable String direction,
								    @RequestBody Article article){
		TitleList list = new TitleList();
		if(article.getContent() != null){
			list = articleServiceImp.fuzzyFindOrder(article.getContent(), articleState, channelEnName, pageNo, 20, order, direction);
		}
		return list;
	}
	
	
	/**
	 * 获取一篇文章
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{articleState}/{pageNo}/{id:[\\d]+}", method = RequestMethod.GET)
	@ResponseBody
	public Article findOne(@PathVariable("id") Long id) {
		Article article = articleServiceImp.findOne(id);
		return article;
	}
	
	/**
	 * 获取一篇文章的操作日志
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{articleState}/{pageNo}/{id:[\\d]+}/log", method = RequestMethod.GET)
	@ResponseBody
	public List<String> getArticleLogs(@PathVariable("id") Long id){
		Article article = articleServiceImp.findOne(id);
		return article.getLogs();
	}
	
	/**
	 * 修改一篇文章
	 * 
	 * @param state
	 *            只有暂存，爬虫，撤销的文章能够修改
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
			articleServiceImp.update(article);
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
		articleServiceImp.setPutState(articleState, idList, message);
		return articleServiceImp.getTiltList(articleState, pageNo);
	}

	@RequestMapping(value = "/{articleState}/{pageNo}/{order:[a-z,A-Z]+}/{direction:asc|desc}/{ids:[\\d]+(?:_[\\d]+)*}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public TitleList stateTranslatePut(
			@PathVariable("articleState") ArticleState articleState,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("order") String order, @PathVariable("ids") String id,
			@PathVariable("direction") String direction) {
		String[] idsString = id.split("_");
		List<Long> idList = new ArrayList<Long>();
		String message = getLog("状态转换");
		for (String idString : idsString) {
			idList.add(Long.parseLong(idString));
		}
		articleServiceImp.setPutState(articleState, idList, message);
		return articleServiceImp.getOrderedList(articleState, pageNo, order, direction);
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
		articleServiceImp.setDeleteState(articleState, idList, message);
		return articleServiceImp.getTiltList(articleState, pageNo);
	}

	@RequestMapping(value = "/{articleState}/{pageNo}/{order:[a-z,A-Z]+}/{direction:asc|desc}/{ids:[\\d]+(?:_[\\d]+)*}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public TitleList stateTranslateDelete(
			@PathVariable("articleState") ArticleState articleState,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("order") String order, @PathVariable("ids") String id,
			@PathVariable("direction") String direction) {
		String[] idsString = id.split("_");
		List<Long> idList = new ArrayList<Long>();
		String message = getLog("状态转换");
		for (String idString : idsString) {
			idList.add(Long.parseLong(idString));
		}
		articleServiceImp.setDeleteState(articleState, idList, message);
		return articleServiceImp.getOrderedList(articleState, pageNo, order, direction);
	}

	/**
	 * 定时发布
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
	
	@RequestMapping(value="/push", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void pushtest(@RequestBody PushModel pushModel){
		if(pushModel.getArticleId() != 0 && pushModel.getMessage() != null){
			appPushService.push(pushModel.getMessage(), pushModel.getArticleId());
		}
	}
	
	private void publishTask(final List<Long> idList, final Long date, final String message){
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				if(message == null){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
					articleServiceImp.setPutState(ArticleState.Pending, idList, sdf.format(new Date()));
				}else{
					articleServiceImp.setPutState(ArticleState.Pending, idList, message);
				}
			}
		}, date);
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
			String filePath = props.getProperty("pictureDir") + File.separator +"articlePic";//目录的路径
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
				//compressPicUtils.compress(new File(filePath + File.separator + fileName), new File(filePathSim + File.separator + fileName), 200, 150, 0.5);
				//压缩800 * ？
				if(fileName.endsWith(".gif")){
					//拷贝成中图
					Files.copy(new File(filePath + File.separator + fileName).toPath(), new File(filePathMid + File.separator + fileName).toPath());
				}else{
					compressPicUtils.compressByThumbnailator(new File(filePath + File.separator + fileName), new File(filePathMid + File.separator + fileName), 800, 0, 0.8, true);
				}
				//压缩200 * 150
				compressPicUtils.compressByThumbnailator(new File(filePath + File.separator + fileName), new File(filePathSim + File.separator + fileName), 200, 150, 0.8, true);
				returnString = path.toString().split("cdsb")[1] + File.separator + "mid"  + File.separator + fileName;
//				System.out.println(returnString);
				return localhostString + returnString.replaceAll("\\\\", "/");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println("upload done!");
		return null;
	}
	
	@RequestMapping(value ="/upload", method = RequestMethod.GET)
	public String uploadPage(){
		return "upload";
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
