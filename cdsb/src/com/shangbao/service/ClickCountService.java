package com.shangbao.service;

import java.util.Date;

import com.shangbao.model.show.ClickList;

public interface ClickCountService {
	void add(long articleId, String articleTitle, Date firstClickTime);
	void count24H();
	void count72H();
	void countAll();
	ClickList getList(int pageNo);
}
