package com.shangbao.phone.control;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
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

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.shangbao.app.model.ActiveModel;
import com.shangbao.app.model.AppChannelModel;
import com.shangbao.app.model.AppPictureModel;
import com.shangbao.app.model.AppResponseModel;
import com.shangbao.app.model.ArticleInfo;
import com.shangbao.app.model.ColumnPageModel;
import com.shangbao.app.model.CommentPageModel;
import com.shangbao.app.model.FrontPageModel;
import com.shangbao.app.service.AppService;
import com.shangbao.model.PhoneInfo;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.User;
import com.shangbao.model.show.SingleCommend;
import com.shangbao.service.ReadLogService;
import com.shangbao.service.UserService;
import com.shangbao.utils.CompressPicUtils;

/**
 * 手机app获取数据的Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/app")
public class AppController {
	
	@Resource
	private Producer captchaProducer;//用于生产验证码
	@Resource
	private AppService appService;
	@Resource
	private CompressPicUtils compressPicUtils;
	@Resource
	private ReadLogService readLogServiceImp;
	@Resource
	private UserService userServiceImp;
	
	
	@RequestMapping(value="/authinfo/{udid}/{remember}", method=RequestMethod.GET, produces={"text/html;charset=UTF-8"})
	@ResponseBody
	public String getAuthInfo(@PathVariable("udid") String udidTag, @PathVariable("remember") String logTag){
		StringBuffer buffer = new StringBuffer();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmm");
		String randomString = "";
		if(udidTag.equals("false")){
			buffer.append("0");
			randomString = RandomStringUtils.randomAlphabetic(8);
		}else{
			buffer.append("1");
			randomString = udidTag.length() >= 8 ? udidTag.substring(udidTag.length() - 8) : (udidTag + RandomStringUtils.randomAlphabetic(8 - udidTag.length()));
		}
		if(logTag.equals("false")){
			buffer.append("0");
		}else{
			buffer.append("1");
		}
		String code = sdf.format(new Date()) + randomString + RandomStringUtils.randomAlphabetic(2);
		buffer.append(code);
		String encoded = DigestUtils.sha1Hex(buffer.toString() + "cdsb");
		buffer.append(encoded);
		return buffer.toString();
	}
	
	@RequestMapping(value="/phoneinfo", method=RequestMethod.POST)
	@ResponseBody
	public PhoneInfo getPhonInfo(@RequestBody PhoneInfo phoneinfo, HttpServletResponse response){
		if(phoneinfo.getUdid() != null){
			Cookie cookie = new Cookie("UDID", phoneinfo.getUdid());
			cookie.setPath("/");
//			cookie.setDomain(".cdsb.mobi");
			response.addCookie(cookie);
			phoneinfo.setMessage("SUCCESS");
			return phoneinfo;
		}else{
			PhoneInfo returnInfo = new PhoneInfo();
			returnInfo.setMessage("UDID IS EMPTY");
			return returnInfo;
		}
	}
	
	@RequestMapping(value="/getcookie", method=RequestMethod.GET)
	public void getCookieJsonp(HttpServletRequest request,HttpServletResponse response){
		try {  
	        response.setContentType("text/plain;charset=UTF-8");  
	        response.setHeader("Pragma", "No-cache");  
	        response.setHeader("Cache-Control", "no-cache"); 
	        response.setHeader("Access-Control-Allow-Origin","*");
	        response.setDateHeader("Expires", 0);  
	        PrintWriter out = response.getWriter();       
	        Cookie[] cookies = request.getCookies();
	        StringBuffer cookieString = new StringBuffer();
	        Map<String, String> cookieMap = new HashMap<>();
	        if(cookies != null){
	        	for(Cookie cookie : cookies){
//	        		cookieString.append("\"" + cookie.getName() + "\"" + ":" + cookie.getValue());
	        		cookieMap.put(cookie.getName(), cookie.getValue());
	        	}
	        }
	        String jsonpCallback = request.getParameter("callback");//客户端请求参数  
	        ObjectMapper mapper = new ObjectMapper();
	        out.println(jsonpCallback+"("+ mapper.writeValueAsString(cookieMap) +")");//返回jsonp格式数据  
	        out.flush();  
	        out.close();  
	      } catch (IOException e) {  
	       e.printStackTrace();  
	      }  
	}
	
	@RequestMapping(value="/phoneinfo/test", method=RequestMethod.GET)
	@ResponseBody
	public String testDomain(HttpServletResponse response){
		File testFile = new File(".." + File.separator + "webapps" + File.separator + "cdsb" + File.separator + "WEB-SRC" + File.separator + "pictureset" + File.separator + "test.txt");
		if(Files.notExists(testFile.toPath())){
			try {
				Files.createFile(testFile.toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return testFile.getAbsolutePath();
	}
	
	/**
	 * 获取宣传图片，首页信息
	 */
	@RequestMapping(value="/{phoneType}/start", method=RequestMethod.GET)
	@ResponseBody
	public FrontPageModel getStartPage(@PathVariable("phoneType") String phoneType, HttpServletResponse response){
		Cookie cookie = new Cookie("APP", "CDSB_APP");
		cookie.setPath("/");
		response.addCookie(cookie);
		return appService.getChannels(phoneType);
	}
	
	/**
	 * 获取商报原创
	 * @return
	 */
	@RequestMapping(value="/{phoneType}/{channelname}/{pageNo:[\\d]+}", method=RequestMethod.GET)
	@ResponseBody
	public ColumnPageModel getOriginal(@PathVariable("pageNo") int pageNo, @PathVariable("channelname") String channelname){
		return appService.getArticlesFromChannel(channelname, pageNo, 8);
	}
	
	/**
	 * 获取最新资讯，本地报告，快拍成都等
	 * @return
	 */
	@RequestMapping(value="/{phoneType}/{channelName:[a-z,A-Z]+}", method=RequestMethod.GET)
	@ResponseBody
	public AppChannelModel getChannelContent(@PathVariable("channelName") String channelName, HttpServletResponse response){
		Cookie cookie = new Cookie("APP", "CDSB_APP");
		cookie.setPath("/");
		response.addCookie(cookie);
		if(channelName.equals("kuaipai")){
			return appService.getChannelModel("kuaipai", 1);
		}
		return appService.getChannelModel(channelName, 8);
	}
	
	/**
	 * 分栏详细页面
	 * @return
	 */
	@RequestMapping(value="/{phoneType}/{channelName:[a-z,A-Z]+}/{columnName:[a-z,A-Z]+}/{pageNo:[\\d]+}", method=RequestMethod.GET)
	@ResponseBody
	public ColumnPageModel getColumnDetail(@PathVariable("columnName") String channelName, @PathVariable("pageNo") int pageNo){
		return appService.getArticlesFromChannel(channelName, pageNo, 10);
	}
	
	@RequestMapping(value="/{phoneType}/query")
	public ColumnPageModel getFuzzyFind(@RequestBody Article article){
		
		return null;
	}
	
	/**
	 * 获取新闻详细页面
	 * @return
	 */
	@RequestMapping(value="/{phoneType}/articledetail/{articleId:[\\d]+}", method=RequestMethod.GET, produces={"text/html;charset=UTF-8"})
	@ResponseBody
	public String getNews(@PathVariable("articleId") long articleId, HttpServletRequest request){
		SecurityContext context = (SecurityContext)request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
		String udid = null;
		String uid = null;
		if(context != null){
			User user = (User)context.getAuthentication().getPrincipal();
			if(user != null){
				uid = user.getUid() + "";
			}
		}
		Cookie[] cookies = request.getCookies();
		if(cookies != null){
			for(Cookie cookie : cookies){
				if(cookie.getName().equals("UDID")){
					udid = cookie.getValue();
				}
			}
		}
		return appService.getNewsHtml(articleId, request.getRemoteAddr(), udid, uid).html;
	}
	
	@RequestMapping(value="/{phoneType}/articleinfo/{articleId:[\\d]+}", method=RequestMethod.GET)
	@ResponseBody
	public ArticleInfo getArticlInfo(@PathVariable("articleId") long articleId){
		return appService.getArticleInfo(articleId);
	}
	
	/**
	 * 点击数加一后返回点击数
	 * @param articleId
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/js/addclick/{articleId}/{udid}", method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public int addClicks(@PathVariable("articleId") long articleId, @PathVariable("udid")String udid, HttpServletRequest request){
		String ip = request.getRemoteAddr();
		if(udid.equals("0")){
			udid = null;
		}
		int clicks = appService.addJsClick(articleId, ip, udid);
		if(clicks > 0){
			readLogServiceImp.addClick(articleId, ip);
		}
		return clicks;
	}
	
	@RequestMapping(value="/js/getclick/{articleId}", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public int getClicks(@PathVariable("articleId") long articleId){
		return appService.getJsClick(articleId);
	}
	
	@RequestMapping(value="/js/addlike/{articleId}", method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public int addLikes(@PathVariable("articleId") long articleId, HttpServletRequest request){
		String ip = request.getRemoteAddr();
		int likes = appService.addLike(articleId);
		if(likes > 0){
			readLogServiceImp.addLike(articleId, ip);
		}
		return likes;
	}
	
	@RequestMapping(value="/js/getlike/{articleId}", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public int getLikes(@PathVariable("articleId") long articleId){
		return appService.getLike(articleId);
	}
	
	/**
	 * 获取新闻评论
	 * @return  
	 */
	@RequestMapping(value="/{phoneType}/{newsId:[\\d]+}/comment/{pageNo:[\\d]+}", method=RequestMethod.GET)
	@ResponseBody
	public CommentPageModel getComment(@PathVariable("newsId") Long newsId, @PathVariable("pageNo") int pageNo){
		CommentPageModel commentPageModel = new CommentPageModel();
		List<SingleCommend> singleCommends = appService.getCommentByArticleId(newsId);
		if(singleCommends == null || singleCommends.isEmpty()){
			return commentPageModel;
		}
		int pageCount = singleCommends.size() / 10 + 1;
		commentPageModel.setPageCount(pageCount);
		if(pageNo > pageCount)
			return null;
		commentPageModel.setCurrentNo(pageNo);
		int fromIndex = (pageNo - 1) * 10;
		int toIndex = pageNo * 10 > singleCommends.size() ? singleCommends.size() : pageNo * 10;
		commentPageModel.addComment(singleCommends.subList(fromIndex, toIndex));
		return commentPageModel;
	}
	
	/**
	 * 发表评论
	 */
	//@Secured("ROLE_USER")
	@RequestMapping(value="/{phoneType}/{newsId:[\\d]+}/comment", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public AppResponseModel sendComment(@PathVariable("newsId") Long articleId, @RequestBody SingleCommend comment){
		AppResponseModel responseModel = new AppResponseModel();
		if(comment.getContent() != null){
			if(!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){
				User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				comment.setUserId(user.getId());
				comment.setUserName(user.getName());
				appService.addComment(articleId, comment);
				responseModel.setResultCode(1);
				responseModel.setResultMsg("Success");
			}
			else{
				comment.setUserId(0);
				comment.setUserName("商报网友");
				appService.addComment(articleId, comment);
				responseModel.setResultCode(1);
				responseModel.setResultMsg("Success");
			}
//			responseModel.setResultCode(0);
//			responseModel.setResultMsg("Login First");
		}
		return responseModel;
	}
	
	
	/**
	 * 点赞
	 */
	@RequestMapping(value="/{phoneType}/{newsId:[\\d]+}/like", method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void sendLike(@PathVariable("newsId") Long articleId){
		appService.sendLike(articleId);
	}
	
	/**
	 * 获取活动信息
	 * @return
	 */
	@RequestMapping(value="{phoneType}/kuaipai/activity", method=RequestMethod.GET)
	@ResponseBody
	public ActiveModel getActives(){
		return appService.getActives();
	}
	
	/**
	 * 图片详细页面
	 */
	@RequestMapping(value="/{phoneType}/kuaipai/{channelName:[a-z,A-Z]+}/{pageNo:[\\d]+}/{articleIndex:[\\d]+}", method=RequestMethod.GET)
	@ResponseBody
	public AppPictureModel getPictureDetial(@PathVariable("channelName") String channelName,
											@PathVariable("articleIndex") int articleIndex){
		return appService.getPictureDetails(channelName, articleIndex);
	}
	
	/**
	 * 上传用户图片
	 */
	@Secured("ROLE_USER")
	@RequestMapping(value="/sendarticle", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String sendUserPicture(@RequestBody Article article){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		SecurityContext context = (SecurityContext)request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
		if(context != null){
			User user = (User)context.getAuthentication().getPrincipal();
			article.setUid(user.getUid());
			article.setAuthor(user.getName());
			article.setFrom("商报网友");
			List<String> logs = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
			logs.add(sdf.format(new Date()) + "" + user.getUid() + " " + user.getName() + " 创建");
			article.setLogs(logs);
			
			String fileName = article.getPicturesUrl().get(0);
			if(fileName != null){
				String picLocation = fileName.substring(0, fileName.indexOf("/mid/"));
				picLocation = picLocation.substring(picLocation.lastIndexOf("/") + 1);
				fileName = fileName.substring(fileName.lastIndexOf("/"));
				Properties props = new Properties();
				try{
					props=PropertiesLoaderUtils.loadAllProperties("config.properties");
					Path logPath = Paths.get(props.getProperty("pictureDir") + File.separator +  "userPic" + File.separator + "Log.txt");
					if(Files.notExists(logPath)){
						if(Files.notExists(logPath.getParent())){
							Files.createDirectories(logPath.getParent());
						}
						Files.createFile(logPath);
					}
					PrintWriter writer = new PrintWriter(new FileWriter(logPath.toFile(), true));
					writer.println(article.getTitle() + " | " + sdf.format(new Date()) + " | " + user.getName() + " | " + user.getId() + " | " + picLocation + " | " + (user.getPhone() == null ? "null" : user.getPhone()) + " | " + fileName);
					writer.flush();
					writer.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}else{
				return "未检测到图片";
			}
		}else{
			return "未登录";
		}
		article.setTime(new Date());
		appService.postPictures(article);
		return "done";
	}
	
	@Secured("ROLE_USER")
	@RequestMapping(value = "/{userId:[\\d]+}/uploadpic", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String uploadPicture(@RequestParam(value = "file", required = true) MultipartFile file, @PathVariable("userId") long userId) {
		if(file.getSize() == 7810){
			return null;
		}
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		User criteriaUser = new User();
		criteriaUser.setId(userId);
		User user = userServiceImp.findOne(criteriaUser);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmm");
		String returnPath = "";
		System.out.println("upload done!");
		if (!file.isEmpty()) {
			byte[] bytes;
			String fileName = sdf.format(new Date()) + file.getSize();
			String localhostString = "";
			try {
				bytes = file.getBytes();
				Properties props = new Properties();
				props=PropertiesLoaderUtils.loadAllProperties("config.properties");
				
				String fileURL = props.getProperty("pictureDir") + File.separator +  "userPic" + File.separator
						+ userId;
				localhostString = props.getProperty("localhost");
				Random random = new Random();
				String fileNameString = fileName + RandomStringUtils.randomAlphabetic(6) + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				String fileUrlSim = fileURL + File.separator + "sim"; // 存放小图的地址
				String fileUrlMid = fileURL + File.separator + "mid"; // 存放中图的地址
				Path path = Paths.get(fileURL);
				if(Files.notExists(path)){
					Path filePath = Files.createDirectories(path);
				}
				Path pathSim = Paths.get(fileUrlSim);
				if(Files.notExists(pathSim)){
					Files.createDirectories(pathSim);
				}
				Path pathMid = Paths.get(fileUrlMid);
				if(Files.notExists(pathMid)){
					Files.createDirectories(pathMid);
				}
				FileOutputStream fos = new FileOutputStream(fileURL + File.separator + fileNameString);
				fos.write(bytes); // 写入文件
				fos.close();
				
				//压缩800*？
				compressPicUtils.compressByThumbnailator(new File(fileURL + File.separator + fileNameString), new File(fileUrlMid + File.separator + fileNameString), 1080, 0, 0.8, true);
				//压缩200 * 150
				compressPicUtils.compressByThumbnailator(new File(fileURL + File.separator + fileNameString), new File(fileUrlSim + File.separator + fileNameString), 400, 300, 0.8, true);
				
				returnPath = path.toString().split("cdsb")[1] + File.separator + "mid" + File.separator + fileNameString;
//				System.out.println(returnPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return localhostString + returnPath.replaceAll("\\\\", "/");
		}
		return null;
	}
	
	@RequestMapping(value = "/uploadpic", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String uploadPictureWithoutUserId(@RequestParam(value = "file", required = true) MultipartFile file) {
		if(file.getSize() == 7810){
			return null;
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmm");
		String returnPath = "";
		//User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){
			return "request forbiden";
		}
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long userId = user.getId();
		if (!file.isEmpty()) {
			byte[] bytes;
			String fileName = sdf.format(new Date()) + file.getSize();
			String localhostString = "";
			try {
				bytes = file.getBytes();
				Properties props = new Properties();
				props=PropertiesLoaderUtils.loadAllProperties("config.properties");
				
				String fileURL = props.getProperty("pictureDir") + File.separator +  "userPic" + File.separator
						+ userId;
				localhostString = props.getProperty("localhost");
				Random random = new Random();
				String fileNameString = fileName + RandomStringUtils.randomAlphabetic(6) + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				String fileUrlSim = fileURL + File.separator + "sim"; // 存放小图的地址
				String fileUrlMid = fileURL + File.separator + "mid"; // 存放中图的地址
				Path path = Paths.get(fileURL);
				if(Files.notExists(path)){
					Path filePath = Files.createDirectories(path);
				}
				Path pathSim = Paths.get(fileUrlSim);
				if(Files.notExists(pathSim)){
					Files.createDirectories(pathSim);
				}
				Path pathMid = Paths.get(fileUrlMid);
				if(Files.notExists(pathMid)){
					Files.createDirectories(pathMid);
				}
				FileOutputStream fos = new FileOutputStream(fileURL + File.separator + fileNameString);
				fos.write(bytes); // 写入文件
				fos.close();
				
				//压缩800*？
				compressPicUtils.compressByThumbnailator(new File(fileURL + File.separator + fileNameString), new File(fileUrlMid + File.separator + fileNameString), 1080, 0, 0.9, true);
				//压缩200 * 150
				compressPicUtils.compressByThumbnailator(new File(fileURL + File.separator + fileNameString), new File(fileUrlSim + File.separator + fileNameString), 400, 300, 0.8, true);
				
				returnPath = path.toString().split("cdsb")[1] + File.separator + "mid" + File.separator + fileNameString;
//				System.out.println(returnPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return localhostString + returnPath.replaceAll("\\\\", "/");
		}
		return null;
	}
	
	
	@RequestMapping(value = "/{phoneType}/KaptchaTest", method = RequestMethod.GET)
	public String initCaptcha() {
		return "kaptcha";
	}

//	@RequestMapping(value = "/{phoneType}/kuaipai", method = RequestMethod.POST)
//	@ResponseBody
//	public String test(String verifyCode, HttpServletRequest request) {
//		String code = (String) request.getSession().getAttribute(
//				Constants.KAPTCHA_SESSION_KEY); // 获取生成的验证码
//		System.out.println(verifyCode + "," + code);
//		if (verifyCode.equals(code)) {
//			System.out.println("验证通过 ");
//		}
//		return "sdfsdf";
//	}

	
	/**
	 * 生成验证码的图片
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/Kaptcha")
	public void getPic(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control",
				"no-store, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("image/jpeg");
		String capText = captchaProducer.createText();
		request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY,
				capText);
		BufferedImage bi = captchaProducer.createImage(capText);

		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(bi, "jpg", out);

		try { 
			out.flush();
		} finally {
			out.close();
		}
		//return (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
	}
}
