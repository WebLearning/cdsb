package com.shangbao.web.control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.HttpStatus;
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
import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.Commend;
import com.shangbao.model.persistence.CrawlerCommend;
import com.shangbao.model.persistence.NewsCommend;
import com.shangbao.model.show.SingleCommend;
import com.shangbao.service.ArticleService;
import com.shangbao.service.CommendService;
import com.shangbao.service.ImageService;
import com.shangbao.utils.CompressPicUtils;

@Controller
@RequestMapping("/crawler")
public class CrawlerGetController {
	@Resource
	private ArticleService articleServiceImp;
	@Resource
	private CommendService commendServiceImp;
	@Resource
	private CompressPicUtils compressPicUtils;
	@Resource
	private ImageService imageServiceImp;
	
	/**
	 * 接收上传的一篇爬虫新闻 返回新闻的id
	 * @param article
	 * @return
	 */
	@RequestMapping(value="/uploadArticle", method=RequestMethod.POST)
	@ResponseBody
	public Long uploadCrawlerArticle(@RequestBody Article article){
		if(article != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
			article.setState(ArticleState.Crawler);
			article.setCrawlerCommendsUnpublish(article.getCrawlerCommends());
			//article.setContent(articleToHtml(article));
			article.setContent(stringToBody(article));
			article.getLogs().add(sdf.format(new Date()) + " " + "爬虫上传");
			return articleServiceImp.addGetId(article);
		}
		return null;
	}
	
	/**
	 * 接收爬虫新闻的评论
	 * @param articleId
	 * @param commend
	 * @return
	 */
	@RequestMapping(value="/uploadComment/{articleId:[\\d]+}", method=RequestMethod.POST)
	@ResponseBody
	public CrawlerCommend uploadCrawlerComment(@PathVariable("articleId") Long articleId, @RequestBody CrawlerCommend commend){
		commend.setArticleId(articleId);
		if(!commend.getCommendList().isEmpty() && commend.getCommendList().get(1).getCommendId() == null){
			int count = commend.getCommendList().size();
			for(int i = 0; i < count; i ++){
				commend.getCommendList().get(i).setUserName("网络小伙伴");
				commend.getCommendList().get(i).setCommendId(new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) + "" + (int)(Math.random()*100) + "" + (int)(Math.random()*100));
			}
		}
		commendServiceImp.add(commend);
		return commend;
	}
	
	/**
	 * 接收爬虫新闻的图片
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/uploadpic", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String uploadPicture(@RequestParam(value = "file", required = true) MultipartFile file) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmm");
		String returnPath = "";
//		System.out.println("upload done!");
		if (!file.isEmpty()) {
			byte[] bytes;
			String fileName = sdf.format(new Date()) + file.getSize();
			try {
				bytes = file.getBytes();
				Properties props = new Properties();
				props=PropertiesLoaderUtils.loadAllProperties("config.properties");
				returnPath = props.getProperty("localhost");
				String fileURL = props.getProperty("pictureDir") + File.separator + "crawlerPic";
				String fileURLMid = fileURL + File.separator + "mid";
				String fileURLSim = fileURL + File.separator + "sim";
				String fileNameString = fileName + RandomStringUtils.randomAlphabetic(6) + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				Path path = Paths.get(fileURL);
				if(Files.notExists(path)){
					Path filePath = Files.createDirectories(path);
				}
				Path pathMid = Paths.get(fileURLMid);
				if(Files.notExists(pathMid)){
					Files.createDirectories(pathMid);
				}
				Path pathSim = Paths.get(fileURLSim);
				if(Files.notExists(pathSim)){
					Files.createDirectories(pathSim);
				}
				FileOutputStream fos = new FileOutputStream(fileURL + File.separator + fileNameString);
				fos.write(bytes); // 写入文件
				fos.close();
				//压缩 800 * ？
				compressPicUtils.compressByThumbnailator(new File(fileURL + File.separator + fileNameString), new File(fileURLMid + File.separator + fileNameString), 1080, 0, 1.0, true);
				//压缩 200 * 150
				compressPicUtils.compressByThumbnailator(new File(fileURL + File.separator + fileNameString), new File(fileURLSim + File.separator + fileNameString), 400, 300, 1.0, true);
				returnPath = returnPath + path.toString().split("cdsb")[1] + File.separator + "mid" + File.separator + fileNameString;
//				System.out.println(returnPath.replaceAll("\\\\", "/"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return returnPath.replaceAll("\\\\", "/");
		}
		return null;
	}
	
	/**
	 * 跟新一篇爬虫文章
	 * @param article
	 * @param articleId
	 * @return
	 */
	@RequestMapping(value = "/update/{articleId}", method=RequestMethod.POST)
	@ResponseBody
	public Long updateArticle(@RequestBody Article article, @PathVariable("articleId") Long articleId){
		article.setId(articleId);
		articleServiceImp.updateCrawler(article);
		return articleId;
	}
	
	/**
	 * 添加爬虫文章评论
	 * @param articleId
	 * @param singleCommends
	 * @return
	 */
	@RequestMapping(value = "/update/comment/{articleId}")
	@ResponseBody
	public Long updateComment(@PathVariable("articleId") Long articleId, @RequestBody SingleCommend[] singleCommendArray){
		Commend commend = new CrawlerCommend();
		commend.setArticleId(articleId);
		List<SingleCommend> singleCommends = Arrays.asList(singleCommendArray);
		commendServiceImp.update(commend, singleCommends);
		return null;
	}
	
	/**
	 * 删除爬虫文章
	 * @param id
	 */
	@RequestMapping(value="/delete/article/{articleId:[\\d]+}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Long deleteCrawlerArticle(@PathVariable("articleId") Long id){
		Article article = new Article();
		article.setId(id);
		article.setTag(false);
		article.setState(ArticleState.Crawler);
		Commend commend = new Commend();
		commend.setArticleId(id);
		List<Article> deleteArticles = articleServiceImp.find(article);
		if(deleteArticles != null && !deleteArticles.isEmpty()){
			articleServiceImp.deleteOne(article);
			commendServiceImp.delete(commend);
			imageServiceImp.deleteImage(deleteArticles.get(0));
			return id;
		}
		return null;
	}
	
	private String stringToBody(Article article){
		String body = "";
		if(article.getSummary() != null && !article.getSummary().isEmpty()){
			body += "<p style=\"text-indent:\"><em>" + article.getSummary() + "</em></p>"; 
		}
		if(article.getContent() != null && !article.getContent().isEmpty()){
			List<String> pictureUrls = article.getPicturesUrl();
			if(pictureUrls != null && !pictureUrls.isEmpty()){
				body += "<p style=\"text-align: center;\"><img src=" + pictureUrls.get(0) + " width=\"330\"></p>";
			}
			String [] parms = article.getContent().split("\n");
			for(String parm : parms){
				body += "<p style=\"text-indent:\">" + parm + "</p>";
			}
		}
		return body;
	}
	
	public ArticleService getArticleServiceImp() {
		return articleServiceImp;
	}
	public void setArticleServiceImp(ArticleService articleServiceImp) {
		this.articleServiceImp = articleServiceImp;
	}
	public CommendService getCommendServiceImp() {
		return commendServiceImp;
	}
	public void setCommendServiceImp(CommendService commendServiceImp) {
		this.commendServiceImp = commendServiceImp;
	}
}
