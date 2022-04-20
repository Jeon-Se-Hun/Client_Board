<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<title>네이버 연동 실패</title>
</head>
<link rel="stylesheet" href='<c:url value="/resources/css/client/naverLoginFail.css" />'>
<script src='<c:url value = "/resources/my_js/jquery-3.6.0.js"/>'></script>
<script src='<c:url value = "/resources/my_js/jquery-ui.min.js"/>'></script>
<body>

	<div id="wrapper">
		<div id="content">

			<h1>
				<a href="/"><img id="logo" src='<c:url value="/resources/img/logo.jpg"/>'/></a> 
				<span class="naSpan">네이버 계정 연동</span>
			</h1>
			
			<hr>
			
			<div id="midContent">
				<h3>고객님의 계정 정보를 확인 할 수 없습니다.</h3>
				<br>
				
				<div>Project1에서는 SNS 연동시 이름, 이메일, 핸드폰번호 정보는 필수로 제공해주셔야 합니다.</div>
				<br>
				<div>네이버계정의 정보 제공을 거부한 상태입니다. 네이버계정의 이름, 이메일, 핸드폰번호 정보 제공 거부를 변경 후 이용 가능합니다.</div>
			</div>
			<div class="rowbtn">
				<a href="/naver/deleteToken?accessToken=${currentAT}" class="bWrap">
					<span>탈퇴하기</span>
				</a>
			</div>
		</div>
	</div>
</body>
</html>
