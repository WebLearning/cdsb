package com.shangbao.dao.Imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.WriteResult;
import com.shangbao.dao.ArticleDao;
import com.shangbao.dao.SequenceDao;
import com.shangbao.model.ArticleState;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.show.Page;

@Repository
public class ArticleDaoImp implements ArticleDao {

	@Resource
	private MongoTemplate mongoTemplate;

	@Resource
	private SequenceDao sequenceDaoImp;

	private static final String ARTICLE_SEQ_KEY = "article";

	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public void insert(Article article) {
		if (article.getState() == null) {

		} else if (article.getState().equals(ArticleState.Published)) {
			// 查找当前文章所属于分类
			List<String> channels = article.getChannel();
			if (channels != null && !channels.isEmpty()) {
				if (article.getChannelIndex() == null) {
					article.setChannelIndex(new HashMap<String, Integer>());
				}
				for (String channelName : channels) {
					// 找出该channel所属文章的最大的index
					Query channelQuery = new Query();
					Criteria channelCriteria = Criteria.where("state").is(
							ArticleState.Published.toString());
					channelQuery.addCriteria(channelCriteria);
					channelQuery.addCriteria(Criteria.where(
							"channelIndex." + channelName).lt(
							Integer.MAX_VALUE / 2));
					channelQuery.with(new Sort(Direction.DESC, "channelIndex."
							+ channelName));
					channelQuery.limit(2);
					List<Article> articleList = mongoTemplate.find(
							channelQuery, Article.class);
					if (articleList != null
							&& !articleList.isEmpty()
							&& articleList.get(0).getChannelIndex()
									.get(channelName) != null) {// 有文章
					// if(articleList.get(0).getChannelIndex().get(channelName).equals(Integer.MAX_VALUE)){//有置顶文章
					// if(articleList.get(1) != null &&
					// articleList.get(1).getChannelIndex().get(channelName) !=
					// null){
					// article.getChannelIndex().put(channelName,
					// articleList.get(1).getChannelIndex().get(channelName) +
					// 1);
					// }
					// }else{//没有置顶文章
						article.getChannelIndex().put(
								channelName,
								articleList.get(0).getChannelIndex()
										.get(channelName) + 1);
						// }
					} else {// 没有文章
						article.getChannelIndex().put(channelName, 1);
					}
				}
			}
		}
		Long idLong = sequenceDaoImp.getNextSequenceId(ARTICLE_SEQ_KEY);
		article.setId(idLong);
		mongoTemplate.insert(article);
	}

	@Override
	public Long insertAndGetId(Article article) {
		if (article.getState() == null) {

		} else if (article.getState().equals(ArticleState.Published)) {
			// 查找当前文章所属于分类
			List<String> channels = article.getChannel();
			if (channels != null && !channels.isEmpty()) {
				if (article.getChannelIndex() == null) {
					article.setChannelIndex(new HashMap<String, Integer>());
				}
				for (String channelName : channels) {
					// 找出该channel所属文章的最大的index
					Query channelQuery = new Query();
					Criteria channelCriteria = Criteria.where("state").is(
							ArticleState.Published.toString());
					channelQuery.addCriteria(channelCriteria);
					channelQuery.addCriteria(Criteria.where(
							"channelIndex." + channelName).lt(
							Integer.MAX_VALUE / 2));
					channelQuery.with(new Sort(Direction.DESC, "channelIndex."
							+ channelName));
					channelQuery.limit(2);
					List<Article> articleList = mongoTemplate.find(
							channelQuery, Article.class);
					if (articleList != null
							&& !articleList.isEmpty()
							&& articleList.get(0).getChannelIndex()
									.get(channelName) != null) {// 有文章
						article.getChannelIndex().put(
								channelName,
								articleList.get(0).getChannelIndex()
										.get(channelName) + 1);
					} else {// 没有文章
						article.getChannelIndex().put(channelName, 1);
					}
				}
			}
		}
		Long idLong = sequenceDaoImp.getNextSequenceId(ARTICLE_SEQ_KEY);
		article.setId(idLong);
		mongoTemplate.insert(article);
		return idLong;
	}

	@Override
	public void insertAll(List<Article> articles) {
		if (!articles.isEmpty()) {
			List<Article> tempArticles = new ArrayList<>();
			for (Article article : articles) {
				article.setId(sequenceDaoImp.getNextSequenceId(ARTICLE_SEQ_KEY));
				tempArticles.add(article);
			}
			mongoTemplate.insertAll(tempArticles);
		}
	}

	@Override
	public void delete(Article criteriaArticle) {
		mongoTemplate.remove(getQuery(criteriaArticle), Article.class);
	}

	@Override
	public void deleteAll() {
		mongoTemplate.dropCollection(Article.class);
	}

	@Override
	public void update(Article article) {
		// 检查文章的分类是否变化
		if (article.getState().equals(ArticleState.Published)) {
			Article criteriaArticle = findById(article.getId());
			List<String> criteriaChannels = criteriaArticle.getChannel();
			List<String> channels = article.getChannel();

			for (String oldChannel : criteriaChannels) {
				if (!channels.contains(oldChannel)) {
					article.getChannelIndex().remove(oldChannel);
				}
			}
			if (channels != null && !channels.isEmpty()) {
				for (String channel : channels) {
					if (criteriaChannels.contains(channel)) {

					} else {
						Query channelQuery = new Query();
						Criteria channelCriteria = Criteria.where("state").is(
								ArticleState.Published.toString());
						channelQuery.addCriteria(channelCriteria);
						channelQuery.addCriteria(Criteria.where(
								"channelIndex." + channel).lt(
								Integer.MAX_VALUE / 2));
						channelQuery.with(new Sort(Direction.DESC,
								"channelIndex." + channel));
						channelQuery.limit(1);
						List<Article> articleList = mongoTemplate.find(
								channelQuery, Article.class);
						if (articleList != null
								&& !articleList.isEmpty()
								&& articleList.get(0).getChannelIndex()
										.get(channel) != null) {
							article.getChannelIndex().put(
									channel,
									articleList.get(0).getChannelIndex()
											.get(channel) + 1);
						} else {
							article.getChannelIndex().put(channel, 1);
						}
					}
				}
			}
		}
		Article tempArticle = mongoTemplate.findById(article.getId(), Article.class);
		if(tempArticle != null){
			article.setJs_clicks(tempArticle.getJs_clicks());
			article.setClicks(tempArticle.getClicks());
			article.setLikes(tempArticle.getLikes());
			article.setNewsCommends(tempArticle.getNewsCommends());
			article.setNewsCommendsPublish(tempArticle.getNewsCommendsPublish());
			article.setNewsCommendsUnpublish(tempArticle.getNewsCommendsUnpublish());
			article.setCrawlerCommends(tempArticle.getCrawlerCommends());
			article.setCrawlerCommendsPublish(tempArticle.getCrawlerCommendsPublish());
			article.setCrawlerCommendsUnpublish(tempArticle.getCrawlerCommendsUnpublish());
		}
		mongoTemplate.save(article);
	}
	
	@Override
	public void fixArticle(long articleid){
		Article article = mongoTemplate.findById(articleid, Article.class);
		if(!article.getState().equals(ArticleState.Published) || article.getChannel().isEmpty()){
			return;
		}
		for(String channel : article.getChannel()){
			if(!article.getChannelIndex().containsKey(channel)){
				Query channelQuery = new Query();
				Criteria channelCriteria = Criteria.where("state").is(
						ArticleState.Published.toString());
				channelQuery.addCriteria(channelCriteria);
				channelQuery.addCriteria(Criteria.where("channel").is(channel));
				channelQuery.addCriteria(Criteria.where(
						"channelIndex." + channel).lt(
						Integer.MAX_VALUE / 2));
				channelQuery.with(new Sort(Direction.DESC,
						"channelIndex." + channel));
				channelQuery.limit(1);
				List<Article> articleList = mongoTemplate.find(
						channelQuery, Article.class);
				if (articleList != null
						&& !articleList.isEmpty()
						&& articleList.get(0).getChannelIndex()
								.get(channel) != null) {
					article.getChannelIndex().put(
							channel,
							articleList.get(0).getChannelIndex()
									.get(channel) + 1);
				} else {
					article.getChannelIndex().put(channel, 1);
				}
				mongoTemplate.save(article);
			}
		}
	}

	@Override
	public boolean update(Article criteriaArticle, Article article) {
		return false;
	}

	@Override
	public void update(Article criteriaArticle, Update update) {
		Query query = getQuery(criteriaArticle);
		WriteResult result = mongoTemplate.updateFirst(query, update,
				Article.class);
	}

	@Override
	public List<Article> find(Article article) {
		return mongoTemplate.find(getQuery(article), Article.class);
	}

	@Override
	public Article findById(long id) {
		return mongoTemplate.findById(id, Article.class);
	}

	@Override
	public List<Article> findAll() {
		return mongoTemplate.findAll(Article.class);
	}

	@Override
	public List<Article> find(Article criteriaArticle, int skip, int limit) {
		Query query = getQuery(criteriaArticle);
		query.skip(skip);
		query.limit(limit);
		return mongoTemplate.find(query, Article.class);
	}

	@Override
	public List<Article> find(Article criteriaArticle, Direction direction,
			String property) {
		Query query = getQuery(criteriaArticle);
		Sort sort = new Sort(direction, property);
		query.with(sort);
		// System.out.println(query.getQueryObject());
		return mongoTemplate.find(query, Article.class);
	}

	@Override
	public Article findAndModify(Article criteriaArticle, Article updateArticle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Article findAndRemove(Article criteriaArticle) {
		Query query = getQuery(criteriaArticle);
		return mongoTemplate.findAndRemove(query, Article.class);
	}

	@Override
	public long count(Article criteriaArticle) {
		return mongoTemplate.count(getQuery(criteriaArticle), Article.class);
	}

	private Query getQuery(Article criteriaArticle) {
		if (criteriaArticle == null) {
			criteriaArticle = new Article();
		}
		Query query = new Query();
		if (criteriaArticle.getId() > 0) {
			Criteria criteria = Criteria.where("id")
					.is(criteriaArticle.getId());
			query.addCriteria(criteria);
		}
		if (criteriaArticle.getUid() != null && criteriaArticle.getUid() > 0) {
			Criteria criteria = Criteria.where("uid").is(
					criteriaArticle.getUid());
			query.addCriteria(criteria);
		}
		if (criteriaArticle.getTitle() != null) {
			Criteria criteria = Criteria.where("title").is(
					criteriaArticle.getTitle());
			query.addCriteria(criteria);
		}
		if (criteriaArticle.getAuthor() != null) {
			Criteria criteria = Criteria.where("author").is(
					criteriaArticle.getAuthor());
			query.addCriteria(criteria);
		}
		if (criteriaArticle.getTime() != null) {
			Criteria criteria = Criteria.where("time").is(
					criteriaArticle.getTime());
			query.addCriteria(criteria);
		}
		if (criteriaArticle.getChannel() != null
				&& !criteriaArticle.getChannel().isEmpty()) {
			Criteria criteria = Criteria.where("channel").in(
					criteriaArticle.getChannel());
			query.addCriteria(criteria);
		}
		if (criteriaArticle.getActivity() != null
				&& !criteriaArticle.getActivity().isEmpty()) {
			Criteria criteria = Criteria.where("activity").is(
					criteriaArticle.getActivity());
			query.addCriteria(criteria);
		}
		if (criteriaArticle.getState() != null) {
			Criteria criteria = Criteria.where("state").is(
					criteriaArticle.getState().toString());
			query.addCriteria(criteria);
		}
		if (criteriaArticle.isTag()) {
			Criteria criteria = Criteria.where("tag").is(true);
			query.addCriteria(criteria);
		}
		return query;
	}

	@Override
	public Page<Article> getPage(int pageNo, int pageSize, Query query) {
		long totalCount = mongoTemplate.count(query, Article.class);
		Page<Article> page = new Page<Article>(pageNo, pageSize, totalCount);
		query.skip(page.getFirstResult());// skip相当于从那条记录开始
		query.limit(pageSize);
		// System.out.println(query.getQueryObject());
		// System.out.println(query.getSortObject());
		List<Article> datas = mongoTemplate.find(query, Article.class);
		page.setDatas(datas);
		return page;
	}

	@Override
	public void setState(ArticleState state, Article criteriaArticle) {
		Query query = getQuery(criteriaArticle);
		Update update = new Update();
		update.set("state", state.toString());
		if (state.equals(ArticleState.Published)) {
			// 如果是发表，需要设置文章在每个channel里的顺序
			Article article = findById(criteriaArticle.getId());
			List<String> channels = article.getChannel();
			if (channels != null && !channels.isEmpty()) {
				for (String channel : channels) {
					// 针对每一个channel设置该文章的顺序
					Query channelQuery = new Query();
					Criteria channelCriteria = Criteria.where("state").is(
							ArticleState.Published.toString());
					// 找出该channel所属文章的最大的index
					channelQuery.addCriteria(channelCriteria);
					channelQuery.addCriteria(Criteria.where("channel").in(
							channel));
					channelQuery.addCriteria(Criteria.where(
							"channelIndex." + channel)
							.lt(Integer.MAX_VALUE / 2));
					channelQuery.with(new Sort(Direction.DESC, "channelIndex."
							+ channel));
					List<Article> articleList = mongoTemplate.find(
							channelQuery, Article.class);
					if (articleList == null || articleList.isEmpty()) {
						// 这篇文章是该栏目第一个文章
						update.set("channelIndex." + channel, 1);
					} else {
						int index = 1;
						if (articleList.get(0).getChannelIndex().get(channel) != null) {
							index = articleList.get(0).getChannelIndex()
									.get(channel) + 1;
						}
						update.set("channelIndex." + channel, index);
					}
				}
			}
		}
		
		WriteResult result = mongoTemplate.updateFirst(query, update,
				Article.class);
	}

	@Override
	public void addMessage(String message, Article criteriaArticle) {
		// TODO Auto-generated method stub
		Query query = getQuery(criteriaArticle);
		Update update = new Update();
		update.push("logs", message);
		mongoTemplate.updateFirst(query, update, Article.class);
	}

	@Override
	public void setTopArticle(String channelName, Long articleId) {
		Article article = mongoTemplate.findById(articleId, Article.class);
		if (article == null
				|| article.getChannelIndex().get(channelName) > (Integer.MAX_VALUE / 2)) {
			// 该文章不存在或者已经被置顶了
			return;
		}
		if (article.getChannel().contains(channelName)) {
			Query channelQuery = new Query();
			Criteria channelCriteria = Criteria.where("state").is(
					ArticleState.Published.toString());
			// 找出该channel所属文章的所有置顶文章
			channelQuery.addCriteria(channelCriteria);
			channelQuery.with(new Sort(Direction.DESC, "channelIndex."
					+ channelName));
			channelQuery.addCriteria(Criteria.where(
					"channelIndex." + channelName).gt(Integer.MAX_VALUE / 2));
			List<Article> articleList = mongoTemplate.find(channelQuery,
					Article.class);

			if (articleList != null && !articleList.isEmpty()) {
				// 有置顶文章
				for (Article topArticle : articleList) {
					// 置顶文章下移一位
					Query query = getQuery(topArticle);
					Update update = new Update();
					update.inc("channelIndex." + channelName, -1);
					mongoTemplate.updateFirst(query, update, Article.class);
				}
				// 将文章置顶
				Query topQuery = getQuery(article);
				Update topUpdate = new Update();
				topUpdate.set("channelIndex." + channelName, Integer.MAX_VALUE);
				mongoTemplate.updateFirst(topQuery, topUpdate, Article.class);
			} else {
				// 没有置顶文章
				Query topQuery = getQuery(article);
				Update topUpdate = new Update();
				topUpdate.set("channelIndex." + channelName, Integer.MAX_VALUE);
				mongoTemplate.updateFirst(topQuery, topUpdate, Article.class);
			}
			/*
			 * if(articleList != null && !articleList.isEmpty()){
			 * if(articleList.get(0).getId() == articleId) return;
			 * if(articleList
			 * .get(0).getChannelIndex().get(channelName).equals(Integer
			 * .MAX_VALUE)){ //有置顶文章 if(articleList.get(1) == null){ return;
			 * }else{ Integer index =
			 * articleList.get(1).getChannelIndex().get(channelName) + 1; Update
			 * maxUpdate = new Update(); Query maxQuery = new Query();
			 * maxQuery.addCriteria
			 * (Criteria.where("id").is(articleList.get(0).getId()));
			 * maxUpdate.set("channelIndex." + channelName, index);
			 * mongoTemplate.updateFirst(maxQuery, maxUpdate, Article.class); }
			 * } //将id为articleId的文章置顶 Update update = new Update(); Query query
			 * = new Query();
			 * query.addCriteria(Criteria.where("id").is(articleId));
			 * update.set("channelIndex." + channelName, Integer.MAX_VALUE);
			 * WriteResult result = mongoTemplate.updateFirst(query, update,
			 * Article.class); }
			 */
		}
	}

	@Override
	public void unSetTopArticle(String channelName, Long articleId) {
		Article article = mongoTemplate.findById(articleId, Article.class);
		if (article.getChannelIndex().get(channelName) < (Integer.MAX_VALUE / 2)
				|| article == null) {
			// 文章不是置顶文章或者不存在
			return;
		}
		if (article.getChannel().contains(channelName)) {
			Query channelQuery = new Query();
			Criteria channelCriteria = Criteria.where("state").is(
					ArticleState.Published.toString());
			// 找出该channel所属文章的所有置顶文章
			channelQuery.addCriteria(channelCriteria);
			channelQuery.with(new Sort(Direction.DESC, "channelIndex."
					+ channelName));
			channelQuery.addCriteria(Criteria.where(
					"channelIndex." + channelName).gt(Integer.MAX_VALUE / 2));
			List<Article> articleList = mongoTemplate.find(channelQuery,
					Article.class);
			for (Article topArticle : articleList) {
				if (topArticle.getChannelIndex().get(channelName) >= article
						.getChannelIndex().get(channelName)) {
					continue;
				} else {
					// 将排在该文章后的置顶文章的顺序上移一位
					Query query = getQuery(topArticle);
					Update update = new Update();
					update.inc("channelIndex." + channelName, 1);
					mongoTemplate.updateFirst(query, update, Article.class);
				}
			}
			// 将该文章排在非置顶文章的第一位
			Query articleQuery = new Query();
			articleQuery.addCriteria(Criteria.where("state").is(
					ArticleState.Published.toString()));
			articleQuery.addCriteria(Criteria.where(
					"channelIndex." + channelName).lt(Integer.MAX_VALUE / 2));
			articleQuery.with(new Sort(Direction.DESC, "channelIndex."
					+ channelName));
			List<Article> normalArtricles = mongoTemplate.find(articleQuery,
					Article.class);
			if (normalArtricles.isEmpty() || normalArtricles == null) {
				Update articleUpdate = new Update();
				articleUpdate.set("channelIndex." + channelName, 1);
				mongoTemplate.updateFirst(getQuery(article), articleUpdate,
						Article.class);
			} else {
				int index = normalArtricles.get(0).getChannelIndex()
						.get(channelName) + 1;
				Update articleUpdate = new Update();
				articleUpdate.set("channelIndex." + channelName, index);
				mongoTemplate.updateFirst(getQuery(article), articleUpdate,
						Article.class);
			}
		}
	}

	@Override
	public void swapArticle(String channelName, Long articleAId, Long articleBId) {
		Article articleA = mongoTemplate.findById(articleAId, Article.class);
		Article articleB = mongoTemplate.findById(articleBId, Article.class);
		if (articleA == null || articleB == null
				|| !articleA.getChannel().contains(channelName)
				|| !articleB.getChannel().contains(channelName)) {
			return;
		}
		if (articleA.getChannelIndex().get(channelName) > (Integer.MAX_VALUE / 2)
				&& articleB.getChannelIndex().get(channelName) < (Integer.MAX_VALUE / 2)) {
			return;
		}
		if (articleA.getChannelIndex().get(channelName) < (Integer.MAX_VALUE / 2)
				&& articleB.getChannelIndex().get(channelName) > (Integer.MAX_VALUE / 2)) {
			return;
		}
		int indexA = articleB.getChannelIndex().get(channelName);
		int indexB = articleA.getChannelIndex().get(channelName);
		if (indexA > 0 && indexB > 0) {
			Update updateA = new Update();
			Query queryA = new Query();
			queryA.addCriteria(Criteria.where("id").is(articleAId));
			updateA.set("channelIndex." + channelName, indexA);
			WriteResult resultA = mongoTemplate.updateFirst(queryA, updateA,
					Article.class);
			Update updateB = new Update();
			Query queryB = new Query();
			queryB.addCriteria(Criteria.where("id").is(articleBId));
			updateB.set("channelIndex." + channelName, indexB);
			WriteResult resultB = mongoTemplate.updateFirst(queryB, updateB,
					Article.class);
		}
	}

	@Override
	public List<Article> fuzzyFind(String words, ArticleState state, boolean tag) {
		Query query = new Query();
		query.addCriteria(Criteria.where("state").is(state.toString()));
		query.addCriteria(Criteria.where("tag").is(tag));
		if (state.equals(ArticleState.Crawler) && !tag && words.matches("\\d+")) {
			if(words.length() == 1){
				query.addCriteria(new Criteria().orOperator(
								new Criteria().andOperator(Criteria.where("level").regex(Pattern.compile("\\d{1}")),Criteria.where("level").gt(words)),
								Criteria.where("level").is("100"),
								Criteria.where("level").regex(Pattern.compile("\\d{2}"))));
			}else if(words.length() == 2){
				query.addCriteria(new Criteria().orOperator(new Criteria().andOperator(Criteria.where("level").regex(Pattern.compile("\\d{2}")), Criteria.where("level").gt(words)), Criteria.where("level").is("100")));
			}else if(words.equals("100")){
				query.addCriteria(Criteria.where("level").is("100"));
			}
		} else {
			query.addCriteria(Criteria.where("title").regex(words));
			query.addCriteria(Criteria.where("content").regex(words));
		}
		query.addCriteria(new Criteria().regex(words));
		query.with(new Sort(Direction.DESC, "time"));
		return mongoTemplate.find(query, Article.class);
	}

	@Override
	public Page<Article> fuzzyFind(String words, ArticleState state,
			boolean tag, int pageNo, int pageSize) {
		Query query = new Query();
		query.addCriteria(Criteria.where("state").is(state.toString()));
		query.addCriteria(Criteria.where("tag").is(tag));
		if (state.equals(ArticleState.Crawler) && !tag && words.matches("\\d+")) {
			if(words.length() == 1){
				query.addCriteria(new Criteria().orOperator(
								new Criteria().andOperator(Criteria.where("level").regex(Pattern.compile("\\d{1}")),Criteria.where("level").gt(words)),
								Criteria.where("level").is("100"),
								Criteria.where("level").regex(Pattern.compile("\\d{2}"))));
			}else if(words.length() == 2){
				query.addCriteria(new Criteria().orOperator(new Criteria().andOperator(Criteria.where("level").regex(Pattern.compile("\\d{2}")), Criteria.where("level").gt(words)), Criteria.where("level").is("100")));
			}else if(words.equals("100")){
				query.addCriteria(Criteria.where("level").is("100"));
			}
		} else {
			query.addCriteria(new Criteria().orOperator(
					Criteria.where("content").regex(words),
					Criteria.where("title").regex(words)));
		}
		// query.addCriteria(Criteria.where("content").regex(words));
		// System.out.println(query.getQueryObject());
		long count = mongoTemplate.count(query, Article.class);
		Page<Article> page = new Page<Article>(pageNo, pageSize, count);
		query.with(new Sort(Direction.DESC, "time"));
		query.skip(page.getFirstResult());// skip相当于从那条记录开始
		query.limit(pageSize);
		List<Article> datas = mongoTemplate.find(query, Article.class);
		page.setDatas(datas);
		return page;
	}

	@Override
	public Page<Article> fuzzyFind(String words, ArticleState state,
			List<String> channelNames, boolean tag, int pageNo, int pageSize) {
		Query query = new Query();
		query.addCriteria(Criteria.where("state").is(state.toString()));
		query.addCriteria(Criteria.where("tag").is(tag));
		query.addCriteria(Criteria.where("channel").in(channelNames));
		query.addCriteria(new Criteria().orOperator(Criteria.where("content")
				.regex(words), Criteria.where("title").regex(words)));
		long count = mongoTemplate.count(query, Article.class);
		Page<Article> page = new Page<Article>(pageNo, pageSize, count);
		query.with(new Sort(Direction.DESC, "time"));
		query.skip(page.getFirstResult());// skip相当于从那条记录开始
		query.limit(pageSize);
		List<Article> datas = mongoTemplate.find(query, Article.class);
		page.setDatas(datas);
		return page;
	}

	@Override
	public Page<Article> fuzzyFind(String words, ArticleState state,
			boolean tag, int pageNo, int pageSize, String order,
			Direction direction) {
		Query query = new Query();
		query.addCriteria(Criteria.where("state").is(state.toString()));
		query.addCriteria(Criteria.where("tag").is(tag));
		if (state.equals(ArticleState.Crawler) && !tag && words.matches("\\d+")) {
			if(words.length() == 1){
				query.addCriteria(new Criteria().orOperator(
								new Criteria().andOperator(Criteria.where("level").regex(Pattern.compile("\\d{1}")),Criteria.where("level").gt(words)),
								Criteria.where("level").is("100"),
								Criteria.where("level").regex(Pattern.compile("\\d{2}"))));
			}else if(words.length() == 2){
				query.addCriteria(new Criteria().orOperator(new Criteria().andOperator(Criteria.where("level").regex(Pattern.compile("\\d{2}")), Criteria.where("level").gt(words)), Criteria.where("level").is("100")));
			}else if(words.equals("100")){
				query.addCriteria(Criteria.where("level").is("100"));
			}
		} else {
			query.addCriteria(new Criteria().orOperator(
					Criteria.where("content").regex(words),
					Criteria.where("title").regex(words)));
		}
		long count = mongoTemplate.count(query, Article.class);
		Page<Article> page = new Page<Article>(pageNo, pageSize, count);
		query.with(new Sort(direction, order));
		query.skip(page.getFirstResult());// skip相当于从那条记录开始
		query.limit(pageSize);
		List<Article> datas = mongoTemplate.find(query, Article.class);
		page.setDatas(datas);
		return page;
	}

	@Override
	public Page<Article> fuzzyFind(String words, ArticleState state,
			List<String> channelNames, boolean tag, int pageNo, int pageSize,
			String order, Direction direction) {
		Query query = new Query();
		query.addCriteria(Criteria.where("state").is(state.toString()));
		query.addCriteria(Criteria.where("tag").is(tag));
		query.addCriteria(new Criteria().orOperator(Criteria.where("content")
				.regex(words), Criteria.where("title").regex(words)));
		long count = mongoTemplate.count(query, Article.class);
		Page<Article> page = new Page<Article>(pageNo, pageSize, count);
		query.with(new Sort(direction, order));
		query.skip(page.getFirstResult());// skip相当于从那条记录开始
		query.limit(pageSize);
		List<Article> datas = mongoTemplate.find(query, Article.class);
		page.setDatas(datas);
		return page;
	}
}
