package com.shangbao.service;

public interface ClickService {
	void add(long articleId, String fromIp, String udid, boolean tag);
}
