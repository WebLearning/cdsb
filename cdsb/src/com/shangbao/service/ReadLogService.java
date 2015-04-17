package com.shangbao.service;

public interface ReadLogService {
	void addClick(Long id, String ip);
	void addLike(Long id, String ip);
}
