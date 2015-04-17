package com.shangbao.service.imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shangbao.dao.PendTagDao;
import com.shangbao.model.persistence.PendControlTag;
import com.shangbao.service.PendTagService;

@Service
public class PendTagServiceImp implements PendTagService{
	@Resource
	private PendTagDao pendTagDaoImp;

	@Override
	public void setTag(String name, boolean tag) {
		PendControlTag pendControlTag = new PendControlTag();
		pendControlTag.setName(name);
		pendControlTag.setTag(tag);
		pendTagDaoImp.insert(pendControlTag);
	}

	@Override
	public boolean isTag(String name) {
		PendControlTag criteriaControlTag = new PendControlTag();
		criteriaControlTag.setName(name);
		List<PendControlTag> list = pendTagDaoImp.find(criteriaControlTag);
		if(1 == list.size())
			return list.get(0).isTag();
		return true;
	}
	
}
