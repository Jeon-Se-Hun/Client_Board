package com.example.demo.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {
	int commentInsert(HashMap<String, Object> map) throws Exception;
	
	int commentTotalCnt(String bor_key_id) throws Exception;
	
	List<HashMap<String, Object>> list(HashMap<String, Object> map) throws Exception;
	
	void commentDelete(HashMap<String, Object> map) throws Exception;
	
	int commentModify(HashMap<String, Object> map) throws Exception;
	
	String likeCheck(HashMap<String, Object> map) throws Exception;
	
	void likeInsert(HashMap<String, Object> map) throws Exception;
	
	void likeUpdate(HashMap<String, Object> map) throws Exception;
	
	List<HashMap<String, Object>> likeList(HashMap<String, Object> map) throws Exception;
	
	int replyTotalCnt(String bor_key_id) throws Exception;
	
	List<HashMap<String, Object>> replyList(HashMap<String, Object> map) throws Exception;
	
	int replyInsert(HashMap<String, Object> map) throws Exception;
	
	int replyModify(HashMap<String, Object> map) throws Exception;

	void replyDelete(HashMap<String, Object> map) throws Exception;
	
}
