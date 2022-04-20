<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href='<c:url value="/resources/css/client/inc/ajax/headerAjax.css" />'>

<div class="outer" align="right">
	<nav>
		<ul>
			<c:choose>
				<c:when test="${sessionId != null}">
					<li><a href="javascript:void(0);" onclick="fn_HeaderType('myPage')"> 마이페이지 </a></li>
					<li><a href="javascript:void(0);" onclick="fn_HeaderType('logout')"> 로그아웃 </a></li>
				</c:when>
				
				<c:otherwise>
					<li><a href="/"> 목록으로 </a></li>
					<li><a href="javascript:void(0);" onclick="fn_HeaderType('login')"> 로그인 </a></li>
				</c:otherwise>
			</c:choose>
		</ul>
	</nav>	
</div>

<form id="kakaoLoginForm" method="post">
	<input type="hidden" id="kakaoPid" name="pid" value="">
	<input type="hidden" id="pName" name="pName" value="">
	<input type="hidden" id="email" name="email" value="">
	<input type="hidden" id="kind" name="kind" value="kakao">
</form>