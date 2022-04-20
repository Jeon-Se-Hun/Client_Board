package com.example.demo.controller;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.BoardDTO;
import com.example.demo.model.PageDTO;
import com.example.demo.service.BoardService;
import com.example.demo.service.FileService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class BoardController {

	@Resource
	BoardService board;

	@Resource
	FileService fs;
	
	@RequestMapping("/")
	public String home() {
		return "client/mainPage";
	}
	
	// 게시판 리스트
	@RequestMapping("/client/boardTotalListAjax")
	public String BoardTotalListAjax(@RequestParam HashMap<String, Object> map, Model model) throws Exception {
		int listCnt = board.BoardTotalCnt();
		int currentPage = Integer.parseInt(String.valueOf(map.get("currentPage")));
		int cntPerPage = Integer.parseInt(String.valueOf(map.get("cntPerPage")));
		int pageSize = Integer.parseInt(String.valueOf(map.get("pageSize")));

		PageDTO pagination = new PageDTO(currentPage, cntPerPage, pageSize);
		pagination.setTotalRecordCount(listCnt);
		// https://hanhyx.tistory.com/46
		map.put("firstRecordIndex", pagination.getFirstRecordIndex());
		map.put("lastRecordIndex", pagination.getLastRecordIndex());

		List<BoardDTO> list = board.BoardTotalListAjax(map);
		model.addAttribute("list", list);
		model.addAttribute("listCnt", listCnt);
		model.addAttribute("board", pagination);
		return "/client/board/ajax/boardList";
	}

	@RequestMapping("/client/ajax/boardForm")
	public String BoardList(@RequestParam HashMap<String, Object> map, Model model, HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		String type = String.valueOf(map.get("type"));
		if (type.equals("detail") || type.equals("modifyForm")) {
			addViewedNttIdToCookie(request, response, String.valueOf(map.get("bor_key_id"))); // 쿠기를 하루로 설정해서 수정 페이지를
																								// 들어가도 조회수가 증가하지 않음.
			List<HashMap<String, Object>> files = fs.FileDetail(map);
			HashMap<String, Object> detail = board.BoardDetail(map);
			model.addAttribute("detail", detail);
			model.addAttribute("files", files);
		}
		if (type.equals("modifyForm")) {
			map.put("type", "modify");
		}

		model.addAttribute("board", map);
		return "/client/board/ajax/boardForm";
	}

	@RequestMapping("/client/board/ajax/boardType")
	@ResponseBody
	public HashMap<String, Object> BoardInsert(@RequestParam(required = false) HashMap<String, Object> map, Model model)
			throws Exception {
		String type = String.valueOf(map.get("type"));
		HashMap<String, Object> hs = new HashMap<>();
		hs.put("chk", true);

		if (type.equals("insert")) {
			hs = board.BoardInsert(map);
		} else if (type.equals("delete")) {
			board.BoardDelete(map);
			fs.DeleteFileAll(map);
		} else if (type.equals("modify")) {
			hs = board.BoardModify(map);
		}

		return hs;
	}
	
	@RequestMapping("/client/board/passChk")
	@ResponseBody
	public boolean BoardPassChk(@RequestParam(required = false) HashMap<String, Object> map) throws Exception {
		boolean flag = board.BoardPassChk(map);
		
		return flag;
	}

	/**
	 * 이미 조회한 게시물에 대해서 해당 게시물에 대한 중복 조회수 증가를 방지하는 메소드이다.<br>
	 * 
	 * @param request
	 * @param response
	 * @param nttId    - 게시물 id 값
	 */
	private void addViewedNttIdToCookie(final HttpServletRequest request, final HttpServletResponse response,
			final String nttId) {
		Cookie accumulateNttIdCookie = Arrays.stream(request.getCookies())
				.filter(cookie -> cookie.getName().equals("alreadyViewNttId")).findFirst().orElseGet(() -> {
					Cookie cookie = createAccNttIdCookie(nttId); // 조회수 중복 방지용 쿠키 생성
					response.addCookie(cookie); // 생성한 쿠키를 response에 담는다.
					board.IncrementNttRdCnt(nttId); // 조회수 증가 쿼리 수행
					return cookie;
				});

		// 한번이라도 조회한 게시물에 대해서는 쿠키값에 해당 게시물의 nttId가 저장된다.
		// 서로 다른 nttId에 대해서는 "/" 로 구분한다.
		// ex) 000000000891/000000000890/000000000889
		String cookieValue = accumulateNttIdCookie.getValue();
		if (!cookieValue.contains(nttId)) {
			String newCookieValue = cookieValue + "/" + nttId;
			/*
			 * response.addCookie(getRemainSecondForTommorow()); // 기존에 같은 이름의 쿠키가 있다면 덮어쓴다.
			 */
			response.addCookie(createAccNttIdCookie(newCookieValue)); // 기존에 같은 이름의 쿠키가 있다면 덮어쓴다.
			board.IncrementNttRdCnt(nttId); // 조회수 증가 쿼리 수행
		}

	}

	/**
	 * 조회수 중복 증가(= 새로고침에 의한 조회수 증가)를 방지하기 위한 쿠키를 생성하는 메소드 <br>
	 * 일반 게시판(공지사항, 자료실, 질의응답, FAQ, 사용자요청) 전용
	 * 
	 * @param cookieValue
	 * @return
	 */
	private Cookie createAccNttIdCookie(String cookieValue) {
		Cookie cookie = new Cookie("alreadyViewNttId", cookieValue);
		cookie.setComment("조회수 중복 증가 방지 쿠키"); // 쿠키 용도 설명 기재
		cookie.setMaxAge(getRemainSecondForTommorow()); // 하루를 준다.
		cookie.setHttpOnly(true); // 클라이언트 단에서 javascript로 조작 불가
		return cookie;
	}

	// 다음 날 정각까지 남은 시간(초)
	private int getRemainSecondForTommorow() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime tommorow = LocalDateTime.now().plusDays(1L).truncatedTo(ChronoUnit.DAYS);
		return (int) now.until(tommorow, ChronoUnit.SECONDS);
	}
}
