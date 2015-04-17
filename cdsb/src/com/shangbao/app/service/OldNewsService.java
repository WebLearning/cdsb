package com.shangbao.app.service;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

import com.shangbao.app.model.AppModel;

@Service
public class OldNewsService {
	private String articleUrl = "http://120.27.47.167:8080/Shangbao01/";
	
	@Resource
	private AppModel appModel;
	
	public OldNewsService(){
		try {
			Properties props=PropertiesLoaderUtils.loadAllProperties("config.properties");
			if(props.getProperty("localhost") != null){
				articleUrl = props.getProperty("localhost");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
