package com.shangbao.app.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.shangbao.model.show.SingleCommend;

public class CommentPageModel {
	private int PageCount;//页数
	private int currentNo;//当前页码
	private List<Comment> content = new ArrayList<Comment>();
	
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

	public List<Comment> getContent() {
		return content;
	}

	public void setContent(List<Comment> content) {
		this.content = content;
	}
	
	public void addComment(Long articleId, String userName, Long userId,
			 Date time, String level, String from, String content, String reply){
		Comment comment = new Comment(articleId, userName, userId, time, level, from, content, reply);
		this.content.add(comment);
	}
	
	public void addComment(List<SingleCommend> singleCommends){
		if(singleCommends != null && !singleCommends.isEmpty()){
			for(SingleCommend singleCommend : singleCommends){
				Comment comment = new Comment(singleCommend);
				content.add(comment);
			}
		}
	}

	class Comment{
		public Long articleId;
		public String userName;
		public Long userId;
		public Date time;
		public String level;
		public String from;
		public String content;
		public String reply;
		
		public Comment(){
			
		}
		
		public Comment(SingleCommend singleCommend){
			//this.articleId = singleCommend.geta;
			this.userName = singleCommend.getUserName();
			this.userId = singleCommend.getUserId();
			this.time = singleCommend.getTimeDate();
			this.level = singleCommend.getLevel();
			this.from = singleCommend.getFrom();
			this.content = singleCommend.getContent();
			this.reply = singleCommend.getReply();
		}
		
		public Comment(Long articleId, String userName, Long userId,
				 Date time, String level, String from, String content, String reply){
			this.articleId = articleId;
			this.userName = userName;
			this.userId = userId;
			this.time = time;
			this.level = level;
			this.from = from;
			this.content = content;
			this.reply = reply;
		}
	}
}
