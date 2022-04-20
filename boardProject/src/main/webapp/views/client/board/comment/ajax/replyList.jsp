<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<input type="hidden" value="${com_key_id}" id="reply_com_key">
<c:choose>
	<c:when test="${!empty list}">
		<input type="hidden" value="${reply.currentPage}" id="replyPage">
		<c:forEach items="${list}" var="list">
			<div class="replyList">
				<h3>${list.NICKNAME}</h3>
				<c:if test="${sessionId eq list.PID}">
					<a class="comList_a" id="Btn_${list.REPLY_KEY_ID}" onclick="fn_reply('modifyForm','${list.REPLY_KEY_ID}')">수정</a>
					<a class="comList_a" onclick="fn_reply('delete','${list.REPLY_KEY_ID}')">삭제</a>
				</c:if>
				<p>${list.REG_DATE}</p>
				<div id="${list.REPLY_KEY_ID}">${list.CONTENT}</div>
			</div>
		
		</c:forEach>
		
<!--paginate -->
    <div class="paginate">
        <div class="paging">
            <a class="direction prev" href="javascript:void(0);" onclick="searchReply(1, '${com_key_id}')"> &lt;&lt; </a> 
                <a class="direction prev" href="javascript:void(0);"
                onclick="searchReply(${reply.currentPage}<c:if test="${reply.hasPreviousPage == true}">-1</c:if>, '${com_key_id}');">
                &lt; </a>
 
            <c:forEach begin="${reply.firstPage}"
                end="${reply.lastPage}" var="idx">
                <a
                    style="color:<c:out value="${reply.currentPage == idx ? '#cc0000; font-weight:700; margin-bottom: 2px;' : ''}"/> "
                    href="javascript:void(0);"
                    onclick="searchReply(${idx},'${com_key_id}');"><c:out value="${idx}" /></a>
            </c:forEach>
            <a class="direction next" href="javascript:void(0);"
                onclick="searchReply(${reply.currentPage}<c:if test="${comment.hasNextPage == true}">+1</c:if>, '${com_key_id}');">
                &gt; </a> 
                <a class="direction next" href="javascript:void(0);" onclick="searchReply(${reply.totalRecordCount}, '${com_key_id}');"> &gt;&gt; </a>
        </div>
    </div>
	</c:when>
	
	<c:otherwise>
		댓글이 존재하지 않습니다.
	</c:otherwise>
	
</c:choose>

<script>
var cnt = ${fn:length(list)};
$(function() {
	var _id = "#summary_${com_key_id}"
	$(_id).html("답글 " + cnt);
});

</script>
