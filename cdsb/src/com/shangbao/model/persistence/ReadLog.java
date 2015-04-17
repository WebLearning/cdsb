package com.shangbao.model.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="readlog")
public class ReadLog {
	
	@Id
	private Long id;
	private Long clicks;
	private Long likes;
	private List<String> clickIP = new ArrayList<>();
	private List<String> likeIP = new ArrayList<>();
	private Map<String, Integer> dateLike = new HashMap<>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getClicks() {
		return clicks;
	}
	public void setClicks(Long clicks) {
		this.clicks = clicks;
	}
	public Long getLikes() {
		return likes;
	}
	public void setLikes(Long likes) {
		this.likes = likes;
	}
	public List<String> getClickIP() {
		return clickIP;
	}
	public void setClickIP(List<String> clickIP) {
		this.clickIP = clickIP;
	}
	public List<String> getLikeIP() {
		return likeIP;
	}
	public void setLikeIP(List<String> likeIP) {
		this.likeIP = likeIP;
	}
	public void addClick(String ip){
		this.clickIP.add(ip);
	}
	public void addLike(String ip){
		this.likeIP.add(ip);
	}
	public Map<String, Integer> getDateLike() {
		return dateLike;
	}
	public void setDateLike(Map<String, Integer> dateLike) {
		this.dateLike = dateLike;
	}
}
