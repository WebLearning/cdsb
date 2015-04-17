package com.shangbao.dao.Imp;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.shangbao.dao.SequenceDao;
import com.shangbao.model.persistence.SequenceId;

@Repository
public class SequenceDaoImp implements SequenceDao {
	@Resource
	private MongoTemplate mongoTemplate;
	
	@Override
	public long getNextSequenceId(String key) {
		Query query = new Query(Criteria.where("_id").is(key));
		if(mongoTemplate.find(query, SequenceId.class).isEmpty()){
			SequenceId sequenceId = new SequenceId();
			sequenceId.setId(key);
			sequenceId.setSeq(0);
			mongoTemplate.insert(sequenceId);
		}
		Update update = new Update();
	    update.inc("seq", 1);
	 
	    //return new increased id
	    FindAndModifyOptions options = new FindAndModifyOptions();
	    options.returnNew(true);
	    SequenceId seqId = mongoTemplate.findAndModify(query, update, options, SequenceId.class);
	    return seqId.getSeq();
	}

}
