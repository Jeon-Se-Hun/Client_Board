package com.example.demo.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {
	public void upFile(HashMap<String, Object> map) throws Exception;
	
	List<HashMap<String, Object>> fileDetail(HashMap<String, Object> map) throws Exception;
	
	public void deleteFileAll(HashMap<String, Object> map) throws Exception;
	
	public int deleteFile(List<String> deleteFiles) throws Exception;
}
