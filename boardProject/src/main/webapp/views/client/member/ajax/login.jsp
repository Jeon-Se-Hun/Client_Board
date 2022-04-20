<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" href='<c:url value="/resources/css/client/member/login.css" />'>

<div class="login">
        <a href="javascript:void(0);" onclick="window.open('${apiURL}','width=500','height=500')">
            <img src="https://developers.naver.com/doc/review_201802/CK_bEFnWMeEBjXpQ5o8N_20180202_7aot50.png" />
        </a>
        <br>
        <a href="javascript:kakaoLogin();">
            <img src='<c:url value="/resources/img/kakao.png"/>' alt="" />
        </a>
</div>
