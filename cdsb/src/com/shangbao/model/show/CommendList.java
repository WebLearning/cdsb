package com.shangbao.model.show;

import java.util.ArrayList;
import java.util.List;


/**
 * 一篇文章的评论页面类
 * @author YangYi
 *
 */
public class CommendList {
	private int PageCount;//页数
	private int currentNo;//当前页码
	private List<SingleCommend> commendList = new ArrayList<SingleCommend>();//当前页面内容
	
	public int getPageCount() {
		return PageCount;
	}
	public void setPageCount(int pageCount) {
		PageCount = pageCount;
	}
	public int getCurrentNo() {
		return currentNo;
	}
	public void setCurrentNo(int currentNo) {
		this.currentNo = currentNo;
	}
	public List<SingleCommend> getCommendList() {
		return commendList;
	}
	public void setCommendList(List<SingleCommend> commendList) {
		this.commendList = commendList;
	}
	
	public void addSingleComment(SingleCommend commend){
		this.commendList.add(commend);
	}
}
