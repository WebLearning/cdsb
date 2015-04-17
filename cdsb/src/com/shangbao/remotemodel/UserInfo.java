package com.shangbao.remotemodel;

import java.util.Date;

import com.shangbao.model.persistence.User;

public class UserInfo {
	private String uid;
	private String phone;
	private String avatar;
	private String email;
	private String sex;
	private String birthday;
	private String qq;
	private String reg_time;
	private String reg_ip;
	private String status;
	private String nickname;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getReg_time() {
		return reg_time;
	}
	public void setReg_time(String reg_time) {
		this.reg_time = reg_time;
	}
	public String getReg_ip() {
		return reg_ip;
	}
	public void setReg_ip(String reg_ip) {
		this.reg_ip = reg_ip;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public User toUser(){
		User user = new User();
		if(avatar != null){
			user.setAvatar(avatar);
		}
		if(birthday != null && birthday != ""){
			user.setBirthday(new Date(Long.parseLong(birthday) * 1000));
		}
		if(uid != null){
			user.setUid(Long.parseLong(uid));
		}
		if(phone != null){
			user.setPhone(phone);
		}
		if(email != null){
			user.setEmail(email);
		}
		if(sex != null){
			user.setSex(Integer.parseInt(sex));
		}
		if(qq != null && qq != ""){
			user.setQq(qq);
		}
		if(nickname != null){
			user.setName(nickname);
		}
		return user;
	}
}
