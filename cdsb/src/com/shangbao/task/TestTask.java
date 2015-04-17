package com.shangbao.task;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shangbao.app.model.AppModel;

@Service
public class TestTask {
	
	@Resource
	AppModel appModel;
	
	public void redeployAll(){
		appModel.redeployAll();
	}
}
