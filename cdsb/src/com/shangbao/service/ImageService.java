package com.shangbao.service;

import com.shangbao.model.persistence.Article;

public interface ImageService {
	boolean deleteImage(String url);
	
	boolean deleteImage(Article article);
}
