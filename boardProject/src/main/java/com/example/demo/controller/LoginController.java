package com.example.demo.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.service.LoginService;
import com.github.scribejava.core.model.OAuth2AccessToken;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Controller
@Slf4j
public class LoginController {
	private NaverLoginBO naverLoginBO;
	private String apiResult = null;

	@Resource
	private LoginService login;

	@Autowired
	private void setNaverLoginBO(NaverLoginBO naverLoginBO) {
		this.naverLoginBO = naverLoginBO;
	}

	@RequestMapping("/client/member/{type}")
	public String main(@PathVariable String type, HttpSession session, Model model) throws Exception {
		String url = "/client/inc/ajax/headerAjax";
		
		if(type.equals("login")) {
			String naverAuthUrl = naverLoginBO.getAuthorizationUrl(session);
			model.addAttribute("apiURL", naverAuthUrl);
			url =  "/client/member/ajax/login";
		}else if(type.equals("logout")) {
			session.invalidate();
		}else if(type.equals("delete")) {
			url =  "/naverLoginFail";
		}
		
		return url;
	}
	
	@RequestMapping("/kakoLogin")
	public String kakoLogin(@RequestParam HashMap<String, String> map, HttpSession session) throws Exception {
		int chk = login.ClientChk(map);
		
		if(chk < 1) {
			login.Join(map);
		}
		
		session.setAttribute("sessionId", map.get("pid"));
		
		return "/client/inc/ajax/headerAjax";
	}
	
	@RequestMapping(value = "/naver/callback")
	public String callback(Model model, @RequestParam(required = false) String code, @RequestParam String state,
			HttpSession session, HttpServletRequest request) throws Exception {
		HashMap<String, String> result = new HashMap<>();
		result.put("state", "Cancel");
		result.put("msg", "연동이 취소되었습니다.");
		String error = request.getParameter("error");
		
		if ( error != null ){
	        if(error.equals("access_denied")){
	        	model.addAttribute("result", result);
	            return "client/member/naverCallback";
	        }
	    }
		
		try {
			OAuth2AccessToken oauthToken;
			oauthToken = naverLoginBO.getAccessToken(session, code, state);
			apiResult = naverLoginBO.getUserProfile(oauthToken); // String형식의 json데이터
			/**
			 * apiResult json 구조 {"resultcode":"00", "message":"success",
			 * "response":{"id":"33666449","nickname":"shinn****","age":"20-29","gender":"M","email":"sh@naver.com","name":"\uc2e0\ubc94\ud638"}}
			 **/
			
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(apiResult);
			JSONObject jsonObj = (JSONObject) obj;
			JSONObject response_obj = (JSONObject) jsonObj.get("response");
			session.setAttribute("currentAT", oauthToken.getAccessToken());
			
			result.put("state", "Fail");
			if( response_obj.get("email").equals(null) || response_obj.get("name").equals(null) || response_obj.get("mobile").equals(null) ) {;}
			
			String pid = (String) response_obj.get("id");

			result.put("pid", pid);

			int chk = login.ClientChk(result);

			if (chk < 1) {
				login.NaverJoin(response_obj);
			}

			session.setAttribute("sessionId", pid);
			result.put("state", "Success");

		} catch (Exception e) {
			result.put("msg", "네이버 정보조회에 실패했습니다.");
		}

		model.addAttribute("result", result);
		return "client/member/naverCallback";
	}
	
	@RequestMapping("/naver/deleteToken")
	public String deleteToken(HttpSession session, HttpServletRequest request, Model model, String accessToken)
			throws Exception {
		String apiURL = naverLoginBO.deleteToken(accessToken);
		String res = requestToServer(apiURL);
		
		HashMap<String, String> result = new HashMap<>();
		result.put("state", "Delete");
		result.put("msg", "탈퇴 되었습니다.");
		
		model.addAttribute("result", result);
		session.invalidate();
		return "client/member/naverCallback";
	}

	/**
	 * 서버 통신 메소드
	 * 
	 * @param apiURL
	 * @return
	 * @throws IOException
	 */
	private String requestToServer(String apiURL) throws Exception {
		return requestToServer(apiURL, "");
	}

	/**
	 * 서버 통신 메소드
	 * 
	 * @param apiURL
	 * @param headerStr
	 * @return
	 * @throws IOException
	 */
	private String requestToServer(String apiURL, String headerStr) throws Exception {
		URL url = new URL(apiURL);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		if (headerStr != null && !headerStr.equals("")) {
			con.setRequestProperty("Authorization", headerStr);
		}

		int responseCode = con.getResponseCode();
		BufferedReader br;

		if (responseCode == 200) { // 정상 호출
			br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		} else { // 에러 발생
			br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
		}
		String inputLine;
		StringBuffer res = new StringBuffer();
		while ((inputLine = br.readLine()) != null) {
			res.append(inputLine);
		}
		br.close();
		if (responseCode == 200) {
			return res.toString();
		} else {
			return null;
		}
	}
}
