package com.shangbao.model.persistence;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="start")
public class StartPictures {

	@Id
	private String id;
	private List<String> pictureUrls = new ArrayList<>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getPictureUrls() {
		return pictureUrls;
	}
	public void setPictureUrls(List<String> pictureUrls) {
		this.pictureUrls = pictureUrls;
	}
	
	
}
