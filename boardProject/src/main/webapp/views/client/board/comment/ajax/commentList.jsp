<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<link rel="stylesheet" href='<c:url value="/resources/css/client/board/comment/ajax/commentList.css" />'>

<input type="hidden" value="${comment.currentPage}" id="currentPage">
<c:choose>
	<c:when test="${!empty list}">
		<c:forEach items="${list}" var="list">
			<div class="comList">
				<h3>${list.NICKNAME}</h3>
				<c:if test="${sessionId eq list.PID}">
					<a class="comList_a" id="Btn_${list.COM_KEY_ID}" onclick="fn_comment('modifyForm','${list.COM_KEY_ID}')">수정</a>
					<a class="comList_a" onclick="fn_comment('delete','${list.COM_KEY_ID}')">삭제</a>
				</c:if>
				<p>${list.REG_DATE}</p>
				<div id="${list.COM_KEY_ID}">${list.CONTENT}</div>
				<div align="right">
					<button class="comBtn" id="G_${list.COM_KEY_ID}" onclick="fn_likeTo('G','${list.COM_KEY_ID}')" value="${list.GOOD_CNT}">좋아요 ${list.GOOD_CNT}</button>
					<button class="comBtn" id="B_${list.COM_KEY_ID}" onclick="fn_likeTo('B','${list.COM_KEY_ID}')" value="${list.BAD_CNT}">싫어요 ${list.BAD_CNT}</button>
				</div>
					<details onToggle="searchReply(1, '${list.COM_KEY_ID}')">
						<summary id="summary_${list.COM_KEY_ID}">답글 ${list.REPLY_CNT}</summary>
						<div id="REPLY_${list.COM_KEY_ID}" class="replyDiv"></div>
						
						<c:if test="${!empty sessionId}">
						<div class="replyInsert">
	   						<div class="replyarea" id="text_${list.COM_KEY_ID}" contenteditable="true" placeholder="내용을 입력해 주세요."></div>
							<div class="re_div"><input class="btn_com" type="button" value="등록" onclick="fn_reply('insert', '${list.COM_KEY_ID}')"></div>
						</div>
						</c:if>		
					</details>
			</div>
		
		</c:forEach>
<!--paginate -->
    <div class="paginate">
        <div class="paging">
            <a class="direction prev" href="javascript:void(0);" onclick="searchComment(1)"> &lt;&lt; </a> 
                <a class="direction prev" href="javascript:void(0);"
                onclick="searchComment(${comment.currentPage}<c:if test="${comment.hasPreviousPage == true}">-1</c:if>);">
                &lt; </a>
 
            <c:forEach begin="${comment.firstPage}"
                end="${comment.lastPage}" var="idx">
                <a
                    style="color:<c:out value="${comment.currentPage == idx ? '#cc0000; font-weight:700; margin-bottom: 2px;' : ''}"/> "
                    href="javascript:void(0);"
                    onclick="searchComment(${idx});"><c:out value="${idx}" /></a>
            </c:forEach>
            <a class="direction next" href="javascript:void(0);"
                onclick="searchComment(${comment.currentPage}<c:if test="${comment.hasNextPage == true}">+1</c:if>);">
                &gt; </a> 
                <a class="direction next" href="javascript:void(0);" onclick="searchComment(${comment.totalRecordCount});"> &gt;&gt; </a>
        </div>
    </div>
	</c:when>
	
	<c:otherwise>
		댓글이 존재하지 않습니다.
	</c:otherwise>
</c:choose>

<script>
$(function() {
	<c:forEach items="${likes}" var="like">
		var likeBtn = "#${like.LIKE_CHECK}_${like.COM_KEY_ID}"
		$(likeBtn).addClass("likeCheck");
	</c:forEach>
});

fn_reply = function(type, key_id) {
	
	if(type == 'modifyForm') {
		$("#" + key_id).attr("contenteditable", true);
		$("#" + key_id).focus();
		$("#" + key_id).attr("class", "contentArea");
		$("#Btn_" + key_id).removeAttr("onclick");
		$("#Btn_" + key_id).attr("onclick", "fn_reply('modify', '"+key_id+"')");
		return;
	}
	
 	var formData = new FormData();
	
	var url = "/client/comment/replyType";
 	var msg = "등록";
 	
	formData.append("type", type);
	formData.append("pid", '${sessionId}');
	
	var page = $("#replyPage").val();
	var reply_com_key = $("#reply_com_key").val();
	
 	if(type == 'insert') {
		page = 1;
		var content = $("#text_" + key_id).text();
		
		if(content == '') {
			alert("댓글을 입력 해주세요.");
			$("#text_" + key_id).focus();
			return;
		}
		formData.append("content", content);
 		formData.append("com_key_id", key_id);
		
 	}else if(type == 'modify') {
 		msg = "수정";
		var content = $("#" + key_id).text();
 		
		if(content == '') {
			alert("댓글을 입력 해주세요.");
			return;
		}
		console.log(key_id);
		formData.append("content", content);
 		formData.append("reply_key_id", key_id);
		
 	}else if(type == 'delete') {
 		formData.append("reply_key_id", key_id);
 		msg = "삭제";
 	}
 	
	$.ajax({
 	      type: "POST",
 	      url: url,
     	  data : formData,
       	  processData: false,
   	      contentType: false,
 	      success: function (data) {
 	    	if(data.chk) {
				alert(msg + " 완료");
				$("#text_" + key_id).text("");
				searchReply(page, reply_com_key);
 	    	}else {
	 	    	alert("서버오류로 지연되고있습니다. 잠시 후 다시 시도해주시기 바랍니다.");
 	    	}
 	      },error: function (xhr, status, error) {
 	    	alert("서버오류로 지연되고있습니다. 잠시 후 다시 시도해주시기 바랍니다.");
 	      }
 	    });
}

searchReply = function(currentPage, com_key_id) {
	var _url = "/client/replyListAjax";
	var formData = new FormData();
	formData.append("currentPage", currentPage);
	formData.append("com_key_id", com_key_id);
	
	$.ajax({
		url : _url
		, type : "post"
		, dataType : "html"
		, data : formData
     	, processData: false
	   	, contentType: false
		, success : function(res) {
			$("#REPLY_" + com_key_id).empty();
			$("#REPLY_" + com_key_id).html(res);
		}
	});
}


fn_likeTo = function(type, com_key_id) {
	if(${sessionId == null}) {
		alert("로그인 후 가능합니다.");
		return;
	}
	var url = "/client/commentLikeTo"
	var formData = new FormData();
	var likeBtn = "#" + type + "_" + com_key_id;
	
	formData.append("like_check", type);
	formData.append("com_key_id", com_key_id);
	formData.append("pid", '${sessionId}');
	
	var html = "좋아요 ";
	var cnt = parseInt($(likeBtn).val());
	if(type == 'B') {
		html = "싫어요 "
	}
	
 	$.ajax({
 	      type: "POST",
 	      url: url,
     	  data : formData,
       	  processData: false,
   	      contentType: false,
 	      success: function (res) {
 	    	if(res.chk == 'Insert' || res.chk == 'Change') {
				$(likeBtn).addClass("likeCheck");
				html += cnt+1;
				$(likeBtn).val(cnt+1);
				$(likeBtn).html(html);
 	    	}else if(res.chk == 'Cancel') {
				$(likeBtn).removeClass("likeCheck");
				html += cnt-1;
				$(likeBtn).val(cnt-1);
				$(likeBtn).html(html);
 	    	}else if(res.chk == 'Fail') {
 	     		alert(res.msg);
 	    	}else {
	 	    	alert("서버오류로 지연되고있습니다. 잠시 후 다시 시도해주시기 바랍니다.");
 	     	}
 	      },error: function (xhr, status, error) {
 	    	alert("서버오류로 지연되고있습니다. 잠시 후 다시 시도해주시기 바랍니다.");
 	      }
 	    });
}

</script>
