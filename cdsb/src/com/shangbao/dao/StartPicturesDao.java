package com.shangbao.dao;


import java.util.List;

import org.springframework.data.mongodb.core.query.Update;

import com.shangbao.model.persistence.StartPictures;

public interface StartPicturesDao{
	
	void insert(StartPictures startPictures);
	
	void update(StartPictures criteriaStartPictures, Update update);
	
	void delete(StartPictures criteriaStartPictures);
	
	List<StartPictures> find(StartPictures criteriaStartPictures);
}
