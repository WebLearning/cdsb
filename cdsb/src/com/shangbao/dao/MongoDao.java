package com.shangbao.dao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;

import com.shangbao.model.show.Page;

public interface MongoDao<T> {
	/**
	 * 插入一个元素
	 * @param element
	 */
	void insert(T element);
	/**
	 * 批量插入元素
	 * @param elements
	 */
	void insertAll(List<T> elements);
	/**
	 * 删除元素
	 * @param criteriaElement
	 */
	void delete(T criteriaElement);
	/**
	 * 删除所有元素
	 */
	void deleteAll();
	/**
	 * 更新
	 * @param criteriaElement
	 * @param element
	 */
	boolean update(T criteriaElement, T updateElement);
	/**
	 * 查找
	 * @param criteriaElement
	 * @return
	 */
	List<T> find(T criteriaElement);
	/**
	 * 更具Id查找元素
	 * @param id
	 * @return
	 */
	T findById(long id);
	/**
	 * 查找所有元素
	 * @return
	 */
	List<T> findAll();
	/**
	 * 按条件查找
	 * @param criteriaElement
	 * @param skip
	 * @param limit
	 * @return
	 */
	List<T> find(T criteriaElement, int skip, int limit);
	/**
	 * 查找并更新
	 * @param criteriaElement
	 * @param updateElement
	 * @return
	 */
	T findAndModify(T criteriaElement, T updateElement);
	/**
	 * 查找并删除
	 * @param criteriaElement
	 * @return
	 */
	T findAndRemove(T criteriaElement);
	/**
	 * 统计元素个数
	 * @param criteriaElement
	 * @return
	 */
	long count(T criteriaElement);
}
