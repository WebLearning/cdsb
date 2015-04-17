package com.shangbao.service;

import java.util.Date;
import java.util.List;

import com.shangbao.remotemodel.Pic;
import com.shangbao.remotemodel.PicTitle;



public interface DownLoadPicService {
	List<PicTitle> getPictureTitles();
	List<PicTitle> getPictureTitles(Date startDate, Date endDate);
	Pic getPictures(String id);
	void saveAsArticle();
	void saveAsArticle(Date startDate, Date endDate);
}
