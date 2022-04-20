package com.example.demo.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.BoardDTO;

@Mapper
public interface BoardMapper {
	List<BoardDTO> list(HashMap<String, Object> map) throws Exception;
	
	int boardTotalCnt() throws Exception;
	
	HashMap<String, Object> boardDetail(HashMap<String, Object> map) throws Exception;
	
	void incrementNttRdCnt(String bor_key_id);
	
	void boardOrder(HashMap<String, Object> map) throws Exception;

	void boardDelete(HashMap<String, Object> map) throws Exception;
	
	int boardInsert(HashMap<String, Object> map) throws Exception;

	int boardModify(HashMap<String, Object> map) throws Exception;
	
	String boardPassChk(HashMap<String, Object> map) throws Exception;
	
}
