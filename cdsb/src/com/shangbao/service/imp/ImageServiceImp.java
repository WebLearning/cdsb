package com.shangbao.service.imp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

import com.shangbao.model.persistence.Article;
import com.shangbao.service.ImageService;

@Service
public class ImageServiceImp implements ImageService{
	private String picturePath = "/usr/upload/tomcat/webapps/Shangbao01/WEB-SRC/picture";
	
	public ImageServiceImp(){
		Properties props = new Properties();
		try {
			props=PropertiesLoaderUtils.loadAllProperties("config.properties");
			picturePath = props.getProperty("pictureDir");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean deleteImage(String url) {
		
		return false;
	}

	@Override
	public boolean deleteImage(Article article) {
		List<String> urls = article.getPicturesUrl();
		boolean tag = false;
		if(urls.isEmpty() || urls == null){
			return true;
		}
		if(picturePath == null || picturePath == ""){
			return false;
		}
		for(String midUrl : urls){
			String filePath = picturePath;
			String fileUrl = midUrl.substring(midUrl.indexOf("/picture/"));
			String[] steps = fileUrl.split("/");
			for(int i = 2; i < steps.length; i ++){
				filePath += File.separator + steps[i];
			}
			try {
				tag = Files.deleteIfExists(Paths.get(filePath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(filePath.contains(File.separator + "mid" + File.separator)){
				String bigFilePath = filePath.replace(File.separator + "mid" + File.separator, File.separator);
				String simFilePath = filePath.replace(File.separator + "mid" + File.separator, File.separator + "sim" + File.separator);
				try {
					tag = Files.deleteIfExists(Paths.get(bigFilePath));
					tag = Files.deleteIfExists(Paths.get(simFilePath));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return tag;
	}

}
