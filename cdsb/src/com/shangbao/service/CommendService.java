package com.shangbao.service;

import java.util.List;

import com.shangbao.model.persistence.Commend;
import com.shangbao.model.show.CommendList;
import com.shangbao.model.show.CommendPage;
import com.shangbao.model.show.SingleCommend;

public interface CommendService {
	
	void add(Commend commend);
	/**
	 * 查找一篇文章的所有评论
	 * @param articleId
	 */
	CommendList get(Commend commend, int pageId);
	
	CommendList getPubUnpub(Commend criteriaCommend, String type, int pageId);
	
	CommendList getPubUnpub(Commend criteriaCommend, String type, int pageId, String order, String direction);
	/**
	 * 查找一篇文章的所有评论并按排序
	 * @param articleId
	 * @param order
	 * @return
	 */
	CommendList get(Commend commend, int pageId, String order, String direction);
	/**
	 * 添加一个评论
	 * @param singleCommend
	 */
	void add(Commend commend, SingleCommend singleCommend);
	
	/**
	 * 更新一个评论
	 * @param singleCommend
	 */
	void update(Commend commend, SingleCommend singleCommend);
	
	/**
	 * 更新多个评论
	 * @param singleCommends
	 */
	void update(Commend commend, List<SingleCommend> singeCommends);
	
	/**
	 * 添加一条回复
	 * @param singleCommend
	 * @param reply
	 */
	void reply(Commend commend, String commendId, String reply);
	
	/**
	 * 发布评论
	 * @param singleCommends
	 */
	void publish(Commend commend, List<String> singleCommendIds);
	
	/**
	 * 删除评论
	 * @param articleId
	 * @param singleCommendIds
	 */
	void delete(Commend commend, List<String> singleCommendIds);
	
	void delete(Commend commend);
	
	/**
	 * 获取所有文章评论信息
	 * @param pageNo 页码
	 * @return
	 */
	CommendPage getCommendPage(int pageNo);
	/**
	 * 获取所有文章评论信息
	 * @param pageNo 页码
	 * @param order 排序
	 * @return
	 */
	CommendPage getCommendPage(int pageNo, String order, String direction);
}
