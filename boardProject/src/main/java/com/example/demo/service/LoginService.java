package com.example.demo.service;

import java.util.HashMap;

import javax.annotation.Resource;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.example.demo.idgen.service.IdgenService;
import com.example.demo.mapper.MemberMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginService {

	@Resource
	MemberMapper mm;
	
	@Resource(name = "usersIdgen")
	private IdgenService userIdgen;

	public int ClientChk(HashMap<String, String> map) throws Exception {
		int chk = mm.clientChk(map);
		return chk;
	}
	public void NaverJoin(JSONObject response_obj) throws Exception {
		HashMap<String, String> result = new HashMap<>();
		String nickName = ((String) response_obj.get("email")).substring(0,((String)response_obj.get("email")).indexOf("@"));
		
		result.put("pid", (String)response_obj.get("id"));
		result.put("pName", (String) response_obj.get("name"));
		result.put("nickName", nickName);
		result.put("email", (String) response_obj.get("email"));
		result.put("phone", (String) response_obj.get("mobile"));
		result.put("kind", "naver");
		
		mm.join(result);
	}
	
	public void Join(HashMap<String, String> map) throws Exception {
		String nick = map.get("nickName");
		
		if(nick == null || nick.equals("null")) {
			map.put("nickName", userIdgen.getNextStringId());
		}
		mm.join(map);
	}
}
