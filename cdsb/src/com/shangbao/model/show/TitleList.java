package com.shangbao.model.show;

import java.util.ArrayList;
import java.util.List;

import com.shangbao.model.persistence.Article;

/**
 * 文章标题列表类
 * @author YangYi
 *
 */
public class TitleList {	
	private int pageCount;//总页数
	private int currentNo;//当前页码
	private List<Title> titleList = new ArrayList<Title>();//当前页面内容
	
	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getCurrentNo() {
		return currentNo;
	}

	public void setCurrentNo(int currentNo) {
		this.currentNo = currentNo;
	}

	public List<Title> getTileList() {
		return titleList;
	}

	public void setTileList(List<Title> tileList) {
		this.titleList = tileList;
	} 
	
	public void addTitle(Article article){
		titleList.add(new Title(article));
	}
}
