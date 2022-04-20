package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.BoardDTO;
import com.example.demo.model.PageDTO;
import com.example.demo.service.CommentService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class CommentController {
	
	@Resource
	CommentService comm;
	
	// 댓글 등록 및 삭제
	@RequestMapping("/client/comment/commentType")
	@ResponseBody
	public HashMap<String, Object> CommentType(@RequestParam(required = false) HashMap<String, Object> map) throws Exception{
		HashMap<String, Object> hs = new HashMap<String, Object>();
		String type = String.valueOf(map.get("type"));
		
		if(type.equals("insert")) {
			hs = comm.CommentInsert(map);
		}else if(type.equals("modify")) {
			hs = comm.CommentModify(map);
		}else if(type.equals("delete")) {
			comm.CommentDelete(map);
			hs.put("chk", true);
		}
		
		return hs;
	}
	
	// 댓글 리스트
	@RequestMapping("/client/commentListAjax")
	public String CommentListAjax(@RequestParam HashMap<String, Object> map, Model model) throws Exception {
		int listCnt = comm.CommentTotalCnt(String.valueOf(map.get("bor_key_id")));
		int currentPage = Integer.parseInt(String.valueOf(map.get("currentPage")));
		int cntPerPage = 10;
		int pageSize = 10;

		PageDTO pagination = new PageDTO(currentPage, cntPerPage, pageSize);
		pagination.setTotalRecordCount(listCnt);

		map.put("firstRecordIndex", pagination.getFirstRecordIndex());
		map.put("lastRecordIndex", pagination.getLastRecordIndex());

		List<HashMap<String, Object>> list = comm.CommentListAjax(map);
		model.addAttribute("list", list);
		model.addAttribute("comment", pagination);
		
		
		String pid = String.valueOf(map.get("pid")); 
		
		if(!pid.equals("")) { 
			List<HashMap<String, Object>> likes = comm.CommentLikeList(map); 
			model.addAttribute("likes", likes);
		}
		
		return "/client/board/comment/ajax/commentList";
	}
	
	@RequestMapping("/client/commentLikeTo")
	@ResponseBody
	public HashMap<String, Object> CommentLikeTo(@RequestParam HashMap<String, Object> map) throws Exception{
		HashMap<String, Object> hs = comm.CommentLikeTo(map);
		
		return hs;
	}
	
	// 대댓글 리스트
	@RequestMapping("/client/replyListAjax")
	public String ReplyListAjax(@RequestParam HashMap<String, Object> map, Model model) throws Exception {
		String com_key_id = String.valueOf(map.get("com_key_id"));
		int listCnt = comm.ReplyTotalCnt(com_key_id);
		int currentPage = Integer.parseInt(String.valueOf(map.get("currentPage")));
		int cntPerPage = 5;
		int pageSize = 10;

		PageDTO pagination = new PageDTO(currentPage, cntPerPage, pageSize);
		pagination.setTotalRecordCount(listCnt);
		
		map.put("firstRecordIndex", pagination.getFirstRecordIndex());
		map.put("lastRecordIndex", pagination.getLastRecordIndex());

		List<HashMap<String, Object>> list = comm.ReplyListAjax(map);
		model.addAttribute("list", list);
		model.addAttribute("reply", pagination);
		model.addAttribute("com_key_id", com_key_id);

		return "/client/board/comment/ajax/replyList";
	}
	
	// 대댓글 등록 및 삭제
	@RequestMapping("/client/comment/replyType")
	@ResponseBody
	public HashMap<String, Object> ReplyType(@RequestParam(required = false) HashMap<String, Object> map) throws Exception{
		HashMap<String, Object> hs = new HashMap<String, Object>();
		String type = String.valueOf(map.get("type"));
		
		if(type.equals("insert")) {
			hs = comm.ReplyInsert(map);
		}else if(type.equals("modify")) {
			hs = comm.ReplyModify(map);
		}else if(type.equals("delete")) {
			comm.ReplyDelete(map);
			hs.put("chk", true);
		}
		
		return hs;
	}
}
