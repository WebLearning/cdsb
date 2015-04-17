package com.shangbao.service;

import java.util.List;

import com.shangbao.model.persistence.StartPictures;

public interface StartPicturesService {
	
	List<StartPictures> getAll();
	
	StartPictures findStartPictures(StartPictures startPictures);
	
	void addStartPictures(StartPictures startPictures);
	
	void addPicture(StartPictures startPictures, List<String> pictureUrls);
	
	void deleteAll(StartPictures startPictures);
	
	void delete(StartPictures startPictures, int index);
	
	void delete(StartPictures startPictures);
	
}
