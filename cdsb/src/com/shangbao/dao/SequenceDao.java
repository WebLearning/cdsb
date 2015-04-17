package com.shangbao.dao;

public interface SequenceDao {
	long getNextSequenceId(String key);
}
