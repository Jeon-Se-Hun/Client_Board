<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href='<c:url value="/resources/css/client/board/ajax/boardList.css" />'>

<div class="wd_80 tm_c">
	<table id="boardTable" class="table table-striped table-bordered table-hover">
		<thead>
	        <tr>
	            <th>번호</th>
	            <th colspan="2">제목</th>
	            <th>작성자</th>
	            <th>작성일</th>
	            <th>조회수</th>
	        </tr>
	   	</thead>
	   	
	   	<tbody>
	   		<c:choose>
	   			<c:when test="${!empty list}">
	   				<c:forEach items="${list}" var="list">
	   					<tr>
	   						<td>${list.NUM}</td>
	   						<td colspan="2">
   								<c:if test="${list.BOR_DISCLOSURE eq 'N'}">
		   							<a  href="javascript:void(0);" onclick="fn_ListType('detail','${list.BOR_KEY_ID}')">
	   									${list.TITLE}
		   							</a>
   								</c:if>
   								<c:if test="${list.BOR_DISCLOSURE eq 'Y'}">
		   							<a  href="javascript:void(0);" onclick="fn_lock('${list.NUM}', '${list.BOR_KEY_ID}')">
	   									${list.TITLE}
		   								<img src="/resources/img/Lock-icon.png" class="lock_icon"/>
		   							</a>
		   							<div id="${list.NUM}"></div>
   								</c:if>
	   						</td>
	   						<td>${list.PID}</td>
	   						<td>${list.REG_DATE}</td>
	   						<td>${list.VIEW_CNT}</td>
	   					</tr>
	   				</c:forEach>
	   			</c:when>
	   			
	   			<c:otherwise>
	   			<tr>
	   				<th colspan="6">게시글이 존재하지 않습니다.</th>
	   			</tr>
	   			</c:otherwise>
	   		</c:choose>
	   	</tbody>
	</table>
	
	<!--paginate -->
    <div class="paginate">
        <div class="paging">
            <a class="direction prev" href="javascript:void(0);" onclick="searchList(1,${board.cntPerPage},${board.pageSize});"> &lt;&lt; </a> 
                <a class="direction prev" href="javascript:void(0);"
                onclick="searchList(${board.currentPage}<c:if test="${board.hasPreviousPage == true}">-1</c:if>,${board.cntPerPage},${board.pageSize});">
                &lt; </a>
 
            <c:forEach begin="${board.firstPage}"
                end="${board.lastPage}" var="idx">
                <a
                    style="color:<c:out value="${board.currentPage == idx ? '#cc0000; font-weight:700; margin-bottom: 2px;' : ''}"/> "
                    href="javascript:void(0);"
                    onclick="searchList(${idx},${board.cntPerPage},${board.pageSize});"><c:out value="${idx}" /></a>
            </c:forEach>
            <a class="direction next" href="javascript:void(0);"
                onclick="searchList(${board.currentPage}<c:if test="${board.hasNextPage == true}">+1</c:if>,${board.cntPerPage},${board.pageSize});">
                &gt; </a> 
                <a class="direction next" href="javascript:void(0);" onclick="searchList(${board.totalRecordCount},${board.cntPerPage},${board.pageSize});"> &gt;&gt; </a>
        </div>
    </div>
	<c:if test="${sessionId ne null }">
		<input class="btn float_r" type="button" value="글쓰기" onclick="fn_ListType('insert')">
	</c:if>
	
	<c:if test="${sessionId eq null }">
		<input class="btn float_r" type="button" value="글쓰기" onclick="alert('로그인 후 가능합니다.'); fn_HeaderType('login');">
	</c:if>
	
	<select id="cntSelectBox" name="cntSelectBox" 
		onchange="changeSelectBox(${board.currentPage},${board.cntPerPage},${board.pageSize});"
        	class="form-control wd_100 float_r">
        <option value="10"<c:if test="${board.cntPerPage == '10'}">selected</c:if>>10개씩</option>
        <option value="20"<c:if test="${board.cntPerPage == '20'}">selected</c:if>>20개씩</option>
        <option value="30"<c:if test="${board.cntPerPage == '30'}">selected</c:if>>30개씩</option>
     </select>
</div>

<form id="borForm" method="post">
	<input type="hidden" name="bor_key_id" id="bor_key_id">
	<input type="hidden" name="type" id="type">
	<input type="hidden" value="${board.cntPerPage}" name="cntPerPage">
	<input type="hidden" value="${board.pageSize}" name="pageSize">
	<input type="hidden" value="${board.currentPage}" name="currentPage">
</form>

<script>
function changeSelectBox(currentPage, cntPerPage, pageSize){
    var selectValue = $("#cntSelectBox").children("option:selected").val();
    searchList(currentPage, selectValue, pageSize);
}

fn_lock = function(idx, bor_key_id) {
	var html = "<input type='password' id='" +bor_key_id +"' maxlength='10'>";
	html +='<input type="button" class="btn" onclick="fn_passChk('+ "'"+bor_key_id+ "'" +')" value="확인">';
	$("#" + idx).html(html);
}

fn_passChk = function(bor_key_id) {
 	var formData = new FormData();
	var bor_pass = $("#" + bor_key_id).val();
 	
	if(bor_pass == '') {
		return;
	}
	
	formData.append("bor_key_id", bor_key_id);
	formData.append("bor_pass", bor_pass);
	
	$.ajax({
 	      type: "POST",
 	      url: "/client/board/passChk",
     	  data : formData,
       	  processData: false,
   	      contentType: false,
 	      success: function (data) {
 	    	  if(data) {
 	    		 fn_ListType('detail', bor_key_id);
 	    	  }else {
 	    		  alert("비밀번호가 일치하지 않습니다.");
 	    		 $("#" + bor_key_id).focus();
 	    	  }
 	      },error: function (xhr, status, error) {
 	    	alert("서버오류로 지연되고있습니다. 잠시 후 다시 시도해주시기 바랍니다.");
 	      }
 	});
}

</script>