package com.shangbao.service.imp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.shangbao.dao.ArticleDao;
import com.shangbao.model.ArticleState;
import com.shangbao.model.persistence.Article;
import com.shangbao.remotemodel.Pic;
import com.shangbao.remotemodel.PicTitle;
import com.shangbao.remotemodel.PicUrl;
import com.shangbao.service.ArticleService;
import com.shangbao.service.DownLoadPicService;
import com.shangbao.utils.CompressPicUtils;

@Service
public class DownLoadPicServiceImp implements DownLoadPicService {

	@Resource
	private ArticleDao articleDaoImp;
	@Resource
	private ArticleService articleServiceImp;
	
	@Resource
	private RestTemplate restTemplate;
	@Resource
	private CompressPicUtils compressPicUtils;
	
	private final String remoteUrl;
	private final String localPicDir;
	private final String localHost;
	
	public DownLoadPicServiceImp(){
		Properties props = new Properties();
		try {
			props=PropertiesLoaderUtils.loadAllProperties("config.properties");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!props.getProperty("remotePicUrl").isEmpty()){
			remoteUrl = props.getProperty("remotePicUrl");
		}else{
			remoteUrl = "http://photo.chengdu.cn/api.php?m=diary";
		}
		if(!props.getProperty("pictureDir").isEmpty()){
			localPicDir = props.getProperty("pictureDir");
		}else{
			localPicDir = "D:\\apache-tomcat\\webapps\\hangbao01\\WEB-SRC\\picture";
		}
		if(!props.getProperty("localhost").isEmpty()){
			localHost = props.getProperty("localhost");
		}else{
			localHost = "http://localhost:8080/cdsb";
		}
	}
	
	/**
	 * 获取图片列表
	 */
	@Override
	public List<PicTitle> getPictureTitles(){
		String url = remoteUrl + "&a=query";
		String picTitle = restTemplate.getForObject(url, String.class);
		System.out.println(picTitle);
		ObjectMapper mapper = new ObjectMapper();
		JavaType type = mapper.getTypeFactory().constructParametricType(ArrayList.class, PicTitle.class);
		List<PicTitle> picTitles = new ArrayList<>();
		try {
			mapper.enableDefaultTyping();
			picTitles = (List<PicTitle>)mapper.readValue(picTitle, type);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return picTitles;
	}
	
	/**
	 * 获取图片列表
	 * @parm startDate 开始时间
	 * @parm endDate 结束时间
	 */
	@Override
	public List<PicTitle> getPictureTitles(Date startDate, Date endDate) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String startDateString = sdf.format(startDate);
		String endDateString = sdf.format(endDate);
		String url = remoteUrl + "&a=query&stime=" + startDateString + "&etime=" +endDateString;
		String picTitle = restTemplate.getForObject(url, String.class);
		ObjectMapper mapper = new ObjectMapper();
		JavaType type = mapper.getTypeFactory().constructParametricType(ArrayList.class, PicTitle.class);
		List<PicTitle> picTitles = new ArrayList<>();
		try {
			mapper.enableDefaultTyping();
			picTitles = (List<PicTitle>)mapper.readValue(picTitle, type);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return picTitles;
	}

	/**
	 * 获取一篇图片
	 */
	@Override
	public Pic getPictures(String id) {
		String url = remoteUrl + "&a=detail&id=" + id;
		String picString = restTemplate.getForObject(url, String.class);
		System.out.println(picString);
		Pic pic = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.enableDefaultTyping();
			pic = mapper.readValue(picString, Pic.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pic;
	}

	@Override
	public void saveAsArticle() {
		List<PicTitle> picTitles = getPictureTitles();
		if(picTitles != null && !picTitles.isEmpty()){
			for(PicTitle title : picTitles){
				Pic pic = getPictures(title.id);
				List<String> urls = new ArrayList<>();
				if(pic.list != null){
					for(PicUrl url : pic.list){
						urls.add(url.picurl);
					}
				}
				List<String> localUrls = downloadPic(urls);
//				for(String urlString : localUrls){
//					System.out.println(urlString);
//				}
				Article article = new Article();
				article.setUid(Long.parseLong(title.uid));
				article.setPicturesUrl(localUrls);
				article.setTime(new Date());
				article.setTitle(title.title);
				article.setTag(true);//是图片新闻
				article.setState(ArticleState.Crawler);
				article.setAuthor(title.nickname);
				StringBuilder content = new StringBuilder();
				for(String picUrl : localUrls){
					content.append("<p style=\"text-align:center\"><img src=\"" + picUrl + "\"/></p>");
				}
				article.setContent(content.toString());
				articleServiceImp.add(article);
			}
		}
	}

	@Override
	public void saveAsArticle(Date startDate, Date endDate) {
		List<PicTitle> picTitles = getPictureTitles(startDate, endDate);
		if(picTitles != null && !picTitles.isEmpty()){
			for(PicTitle title : picTitles){
				Pic pic = getPictures(title.id);
				List<String> urls = new ArrayList<>();
				if(pic.list != null){
					for(PicUrl url : pic.list){
						urls.add(url.picurl);
					}
				}
				List<String> localUrls = downloadPic(urls);
				Article article = new Article();
				article.setUid(Long.parseLong(title.uid));
				article.setPicturesUrl(localUrls);
				article.setTitle(title.title);
				article.setTime(new Date());
				article.setAuthor(title.nickname);
				articleServiceImp.add(article);
			}
		}
	}
	
	/**
	 * 下载图片存储放到本地
	 * @param picUrls
	 * @return
	 */
	private List<String> downloadPic(List<String> picUrls){
		List<String> localPicUrls = new ArrayList<>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String dateString = sdf.format(new Date());
		Path path = Paths.get(localPicDir + File.separator + dateString);
		Path pathSim = Paths.get(localPicDir + File.separator + dateString + File.separator + "sim");
		Path pathMid = Paths.get(localPicDir + File.separator + dateString + File.separator + "mid");
		try{
			if(Files.notExists(path)){
				Path filPath = Files.createDirectories(path);
			}
			if(Files.notExists(pathSim)){
				Files.createDirectories(pathSim);
			}
			if(Files.notExists(pathMid)){
				Files.createDirectories(pathMid);
			}
		}catch(IOException e){
			System.out.println(e.getStackTrace());
		}
		for(String singlePicUrl : picUrls){
			byte[] bytes;
			bytes = restTemplate.getForObject(singlePicUrl, byte[].class);
			String returnUrl = "";
			String returnMid = "";
			String returnSim = "";
			//FileOutputStream fos;
			try(FileOutputStream fos = 
					new FileOutputStream(localPicDir + File.separator + dateString + File.separator +
							singlePicUrl.substring(singlePicUrl.lastIndexOf("/")))) {	
				fos.write(bytes); 
				returnUrl = localPicDir.split("cdsb")[1] + File.separator + dateString
						 + singlePicUrl.substring(singlePicUrl.lastIndexOf("/"));
				returnMid = localPicDir.split("cdsb")[1] + File.separator + dateString + File.separator + "mid"
						 + singlePicUrl.substring(singlePicUrl.lastIndexOf("/"));
				returnSim = localPicDir.split("cdsb")[1] + File.separator + dateString + File.separator + "sim"
						 + singlePicUrl.substring(singlePicUrl.lastIndexOf("/"));
//				compressPicUtils.compressPic(new File(path.toFile().getAbsolutePath() + File.separator + singlePicUrl.substring(singlePicUrl.lastIndexOf("/"))), 
//											 new File(pathSim.toFile().getAbsoluteFile() + File.separator + singlePicUrl.substring(singlePicUrl.lastIndexOf("/"))),
//											 800, 0, true);
				//压缩800 * ？
				compressPicUtils.compressByThumbnailator(new File(path.toFile().getAbsolutePath() + File.separator + singlePicUrl.substring(singlePicUrl.lastIndexOf("/"))),
														 new File(pathMid.toFile().getAbsoluteFile() + File.separator + singlePicUrl.substring(singlePicUrl.lastIndexOf("/")))
				 										 , 1080, 0, 0.9, true);
				//压缩 200 * 150
				compressPicUtils.compressByThumbnailator(new File(path.toFile().getAbsolutePath() + File.separator + singlePicUrl.substring(singlePicUrl.lastIndexOf("/"))),
						 new File(pathSim.toFile().getAbsoluteFile() + File.separator + singlePicUrl.substring(singlePicUrl.lastIndexOf("/")))
						 , 200, 150, 0.8, false);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			localPicUrls.add(localHost + returnMid.replaceAll("\\\\", "/"));
		}
		return localPicUrls;
	}
	
}
