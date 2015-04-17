package com.shangbao.service.imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.shangbao.dao.StartPicturesDao;
import com.shangbao.model.persistence.StartPictures;
import com.shangbao.service.StartPicturesService;

@Service
public class StartPicturesServiceImp implements StartPicturesService{
	@Resource
	private StartPicturesDao startPicturesDaoImp;

	public StartPicturesDao getStartPicturesDaoImp() {
		return startPicturesDaoImp;
	}

	public void setStartPicturesDaoImp(StartPicturesDao startPicturesDaoImp) {
		this.startPicturesDaoImp = startPicturesDaoImp;
	}

	@Override
	public List<StartPictures> getAll() {
		return startPicturesDaoImp.find(new StartPictures());
	}

	@Override
	public StartPictures findStartPictures(StartPictures startPictures) {
		List<StartPictures> sList = startPicturesDaoImp.find(startPictures);
		if(sList != null && !sList.isEmpty()){
			return sList.get(0);
		}
		return null;
	}

	@Override
	public void addStartPictures(StartPictures startPictures){
		if(findStartPictures(startPictures) == null && startPictures.getId() != null){
			startPicturesDaoImp.insert(startPictures);
		}
	}
	
	@Override
	public void addPicture(StartPictures startPictures, List<String> pictureUrls) {
		if(findStartPictures(startPictures) != null){
			Update update = new Update();
			update.pushAll("pictureUrls", pictureUrls.toArray());
			startPicturesDaoImp.update(startPictures, update);
		}
	}

	@Override
	public void deleteAll(StartPictures startPictures) {
		startPicturesDaoImp.delete(startPictures);
	}

	@Override
	public void delete(StartPictures startPictures, int index) {
		index --;
		StartPictures tempPictures = findStartPictures(startPictures);
		if(tempPictures != null && !tempPictures.getPictureUrls().isEmpty() && tempPictures.getPictureUrls().get(index) != null){
			Update update = new Update();
			update.pull("pictureUrls", tempPictures.getPictureUrls().get(index));
			startPicturesDaoImp.update(startPictures, update);
		}
	}
	
	@Override
	public void delete(StartPictures startPictures){
		Update update = new Update();
		if(!startPictures.getPictureUrls().isEmpty()){
			update.pullAll("pictureUrls", startPictures.getPictureUrls().toArray());
			startPicturesDaoImp.update(startPictures, update);
		}
	}
}
