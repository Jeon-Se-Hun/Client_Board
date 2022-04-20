package com.example.demo.mapper;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
	void join(HashMap<String, String> map);
	int clientChk(HashMap<String, String> map);
}