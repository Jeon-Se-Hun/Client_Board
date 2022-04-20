package com.example.demo.service;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.example.demo.idgen.service.IdgenService;
import com.example.demo.mapper.CommentMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommentService {
	
	@Resource
	CommentMapper comment;
	
	@Resource(name ="commentIdgen")
	private IdgenService commentIdgen;
	
	@Resource(name ="likeIdgen")
	private IdgenService likeIdgen;
	
	
	@Resource(name ="replyIdgen")
	private IdgenService replyIdgen;
	
	
	public HashMap<String, Object> CommentInsert(HashMap<String, Object> map) throws Exception{
		HashMap<String, Object> hs = new HashMap<String, Object>();
		boolean chk = true;
		
		map.put("com_key_id", commentIdgen.getNextStringId());
		
		int cnt = comment.commentInsert(map);
		
		if(cnt < 1)
			chk = false;
		
		hs.put("chk", chk);
				
		return hs;
	}
	
	public HashMap<String, Object> CommentModify(HashMap<String, Object> map) throws Exception{
		HashMap<String, Object> hs = new HashMap<String, Object>();
		boolean chk = true;
		
		int cnt = comment.commentModify(map);
		
		if(cnt < 1)
			chk = false;
		
		hs.put("chk", chk);
		
		return hs;
	}
	
	public void CommentDelete(HashMap<String, Object> map) throws Exception {
		comment.commentDelete(map);
	}
	
	public int CommentTotalCnt(String bor_key_id) throws Exception{
		return comment.commentTotalCnt(bor_key_id);
	}
	
	public List<HashMap<String, Object>> CommentListAjax(HashMap<String, Object> map) throws Exception {
		List<HashMap<String, Object>> list = comment.list(map);
		return list;
	}
	
	
	public HashMap<String, Object> CommentLikeTo(HashMap<String, Object> map) throws Exception{
		HashMap<String, Object> hs = new HashMap<String, Object>();
		
		String like_check = comment.likeCheck(map);
		
		// 체크X
		if(like_check == null) {
			map.put("like_key_id", likeIdgen.getNextStringId());
			comment.likeInsert(map);
			hs.put("chk", "Insert"); // Insert 등록
		}else{
			// 체크한 값
			String type = String.valueOf(map.get("like_check"));
			
			// 체크취소 후 체크
			if(like_check.equals("N")) {
				map.put("like_check", type);
				hs.put("chk", "Change"); // change 변경
				comment.likeUpdate(map);
			}else {
				if(type.equals(like_check)) {
					map.put("like_check", "N");
					comment.likeUpdate(map);
					hs.put("chk", "Cancel"); // Cancel 취소
				}else {
					String s = "'좋아요'";
					if(like_check.equals("B")) {
						s = "'싫어요'";
					}
					hs.put("chk", "Fail");
					hs.put("msg", s+"한 댓글입니다.\n취소 후 수정해주세요.");
				}
			}
		}
		
		return hs;
	}
	public List<HashMap<String, Object>> CommentLikeList(HashMap<String, Object> map) throws Exception {
		return comment.likeList(map);
	}
	
	public int ReplyTotalCnt(String com_key_id) throws Exception{
		return comment.replyTotalCnt(com_key_id);
	}
	
	public List<HashMap<String, Object>> ReplyListAjax(HashMap<String, Object> map) throws Exception {
		return comment.replyList(map);
	}
	

	public HashMap<String, Object> ReplyInsert(HashMap<String, Object> map) throws Exception{
		HashMap<String, Object> hs = new HashMap<String, Object>();
		boolean chk = true;
		
		map.put("reply_key_id", replyIdgen.getNextStringId());
		
		int cnt = comment.replyInsert(map);
		
		if(cnt < 1)
			chk = false;
		
		hs.put("chk", chk);
				
		return hs;
	}
	
	public HashMap<String, Object> ReplyModify(HashMap<String, Object> map) throws Exception{
		HashMap<String, Object> hs = new HashMap<String, Object>();
		boolean chk = true;
		
		int cnt = comment.replyModify(map);
		
		if(cnt < 1)
			chk = false;
		
		hs.put("chk", chk);
		
		return hs;
	}
	
	public void ReplyDelete(HashMap<String, Object> map) throws Exception {
		comment.replyDelete(map);
	}
	
}
