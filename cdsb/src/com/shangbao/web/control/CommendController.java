package com.shangbao.web.control;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.shangbao.app.model.Reply;
import com.shangbao.model.ArticleState;
import com.shangbao.model.persistence.Commend;
import com.shangbao.model.persistence.CrawlerCommend;
import com.shangbao.model.persistence.NewsCommend;
import com.shangbao.model.show.CommendList;
import com.shangbao.model.show.CommendPage;
import com.shangbao.model.show.SingleCommend;
import com.shangbao.service.CommendService;

@Controller
@RequestMapping("/commend")
public class CommendController {
	@Resource
	private CommendService commendServiceImp;

	/**
	 * 返回所有文章评论信息分页
	 * 
	 * @param pageId
	 * @return
	 */
	@RequestMapping(value = "/{pageNo:[\\d]+}", method = RequestMethod.GET)
	@ResponseBody
	public CommendPage getPage(@PathVariable("pageNo") int pageNo) {
		return commendServiceImp.getCommendPage(pageNo);
	}

	/**
	 * 排序
	 * 
	 * @param pageId
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "/{pageNo:[\\d]+}/{order:[a-z,A-Z]+}/{direction:asc|desc}", method = RequestMethod.GET)
	@ResponseBody
	public CommendPage articleOrder(@PathVariable("pageNo") int pageNo,
			@PathVariable("order") String order,
			@PathVariable("direction") String direction) {
		return commendServiceImp.getCommendPage(pageNo, order, direction);
	}

	/**
	 * 点击一篇文章，根据文章id获取该文章具体评论 并跳转到对应页面
	 * 
	 * @param articleId
	 * @param type
	 * @param pageId
	 * @return
	 */
	@RequestMapping(value = "/{pageNo:[\\d]+}/{articleId:[\\d]+}/{type:crawler|news}/{commendPage:[\\d]+}", method = RequestMethod.GET)
	@ResponseBody
	public CommendList getCommends(@PathVariable("articleId") long articleId,
			@PathVariable("type") String type,
			@PathVariable("commendPage") int pageId) {
		Commend commend = null;
		if(type.equals("crawler")){
			commend = new CrawlerCommend();
		}
		if(type.equals("news")){
			commend = new NewsCommend();
		}
		commend.setArticleId(articleId);
		return commendServiceImp.get(commend, pageId);
	}
	
	@RequestMapping(value = "/{pageNo:[\\d]+}/{articleId:[\\d]+}/{type:crawler|news}/{pub:publish|unpublish}/{commendPage:[\\d]+}", method = RequestMethod.GET)
	@ResponseBody
	public CommendList getPubOrUnCommend(@PathVariable("articleId") long articleId,
										@PathVariable("type") String type,
										@PathVariable("pub") String publishType,
										@PathVariable("commendPage") int pageId){
		Commend commend = null;
		if(type.equals("crawler")){
			commend = new CrawlerCommend();
		}
		if(type.equals("news")){
			commend = new NewsCommend();
		}
		commend.setArticleId(articleId);
		return commendServiceImp.getPubUnpub(commend, publishType, pageId);
	}

	@RequestMapping(value="/{pageNo:[\\d]+}/{articleId:[\\d]+}/{type:crawler|news}/{pub:publish|unpublish}/{commendPage:[\\d]+}/{order:[a-z,A-Z]+}/{direction:asc|desc}",method = RequestMethod.GET)
	@ResponseBody
	public CommendList getPubOrUnCommend(@PathVariable("articleId") long articleId,
										@PathVariable("type") String type,
										@PathVariable("pub") String publishType,
										@PathVariable("commendPage") int pageId,
										@PathVariable("order") String order,
										@PathVariable("direction") String direction){
		Commend commend = null;
		if(type.equals("crawler")){
			commend = new CrawlerCommend();
		}
		if(type.equals("news")){
			commend = new NewsCommend();
		}
		commend.setArticleId(articleId);
		return commendServiceImp.getPubUnpub(commend, publishType, pageId, order, direction);
	}
	/**
	 * 点击属性，按照选择的属性排序
	 * 
	 * @param articleId
	 * @param type
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "/{pageId:[\\d]+}/{articleId:[\\d]+}/{type:crawler|news}/{commendPage:[\\d]+}/{order:[a-z,A-Z]+}/{direction:asc|desc}", method = RequestMethod.GET)
	@ResponseBody
	public CommendList commendOrder(@PathVariable("articleId") long articleId,
			@PathVariable("type") String type,
			@PathVariable("commendPage") int pageId,
			@PathVariable("order") String order,
			@PathVariable("direction") String direction) {
		Commend commend = null;
		if(type.equals("crawler")){
			commend = new CrawlerCommend();
		}
		if(type.equals("news")){
			commend = new NewsCommend();
		}
		commend.setArticleId(articleId);
		return commendServiceImp.get(commend, pageId, order, direction);
	}

	/**
	 * 新建一个评论
	 * 
	 * @param articleId
	 * @param type
	 * @param singleCommend
	 */
	@RequestMapping(value = "/{pageId:[\\d]+}/{articleId:[\\d]+}/{type:crawler|news}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void addCommend(@PathVariable("articleId") long articleId,
			@PathVariable("type") String type,
			@RequestBody SingleCommend singleCommend) {
		Commend commend = null;
		if(type.equals("crawler")){
			commend = new CrawlerCommend();
		}
		if(type.equals("news")){
			commend = new NewsCommend();
		}
		commend.setArticleId(articleId);
		commendServiceImp.add(commend, singleCommend);
	}

	/**
	 * 新建一个回复
	 * 
	 * @param articleId
	 * @param type
	 * @param commendId
	 */
	@RequestMapping(value = "/{pageId:[\\d]+}/{articleId:[\\d]+}/{type:crawler|news}/{commendId:[\\d]+}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void reply(@PathVariable("articleId") long articleId,
			@PathVariable("type") String type,
			@PathVariable("commendId") String commendId,
			@RequestBody Reply reply
			) {
		System.out.println("reply inite!!!");
		Commend commend = null;
		if(type.equals("crawler")){
			commend = new CrawlerCommend();
		}
		if(type.equals("news")){
			commend = new NewsCommend();
		}
		commend.setArticleId(articleId);
		commendServiceImp.reply(commend, commendId, reply.getReply());
	}

	/**
	 * 发表评论
	 * 
	 * @param articleId
	 * @param type
	 * @param commendId
	 */
	@RequestMapping(value = "/{pageId:[\\d]+}/{articleId:[\\d]+}/{type:crawler|news}/{commendIds:[\\d]+(?:_[\\d]+)*}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void publish(@PathVariable("articleId") long articleId,
			@PathVariable("type") String type,
			@PathVariable("commendIds") String commendIds) {
		Commend commend = null;
		if(type.equals("crawler")){
			commend = new CrawlerCommend();
		}
		if(type.equals("news")){
			commend = new NewsCommend();
		}
		commend.setArticleId(articleId);
		List<String> commendIdList = new ArrayList<String>();
		for (String idString : commendIds.split("_")) {
			commendIdList.add(idString);
		}
		commendServiceImp.publish(commend, commendIdList);
	}

	/**
	 * 删除评论
	 * 
	 * @param articleId
	 * @param type
	 * @param commendIds
	 */
	@RequestMapping(value = "/{pageId:[\\d]+}/{articleId:[\\d]+}/{type:crawler|news}/{commendIds:[\\d]+(?:_[\\d]+)*}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable("articleId") long articleId,
			@PathVariable("type") String type,
			@PathVariable("commendIds") String commendIds) {
		Commend commend = null;
		if(type.equals("crawler")){
			commend = new CrawlerCommend();
		}
		if(type.equals("news")){
			commend = new NewsCommend();
		}
		commend.setArticleId(articleId);
		List<String> commendIdList = new ArrayList<String>();
		for (String idString : commendIds.split("_")) {
			commendIdList.add(idString);
		}
		commendServiceImp.delete(commend, commendIdList);
	}
	
	@RequestMapping(value="/test",method=RequestMethod.POST)
	public void test(@RequestParam("reply") String reply){
		System.out.println(reply);
	}

	
//	public class Reply{
//		private String reply;
//
//		public String getReply() {
//			return reply;
//		}
//
//		public void setReply(String reply) {
//			this.reply = reply;
//		}
//		
//	}
}
