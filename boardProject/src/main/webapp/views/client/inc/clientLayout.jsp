<%@ page pageEncoding="UTF-8" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<!doctype html>
<html lang="ko">
<head>
</head>
<script src='<c:url value = "/resources/my_js/jquery-3.6.0.js"/>'></script>
<script src='<c:url value = "/resources/my_js/jquery-ui.min.js"/>'></script>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<script src='<c:url value = "/resources/my_js/ckeditor.js"/>'></script>

<script>
$(function() {
	if(${pagination == null}) {
		searchList(1,10,10);
	}else {
		searchList('${board.currentPage}', '${board.cntPerPage}', '${board.pageSize}');
	}
});

searchList = function(currentPage, cntPerPage, pageSize) {
	
	var _url = "/client/boardTotalListAjax";

	$.ajax({
		url : _url
		, type : "post"
		, dataType : "html"
		, data : {
			currentPage : currentPage
			,cntPerPage : cntPerPage
			,pageSize : pageSize
		}
		, success : function(res) {
			$("#clientAjaxDiv").empty();
			$("#clientAjaxDiv").html(res);
		}
	});
}

fn_ListType = function(type, bor_key_id) {
	$("#bor_key_id").val(bor_key_id);
	$("#type").val(type);
	var form = $("#borForm").serialize();
	var _url = "/client/ajax/boardForm";

	$.ajax({
		url : _url
		, type : "post"
		, dataType : "html"
		, data : form
		, success : function(res) {
			$("#clientAjaxDiv").empty();
			$("#clientAjaxDiv").html(res);
		}
	});
}

</script>
<body>
	<header>
		<tiles:insertAttribute name="header"/>
	</header>

	<tiles:insertAttribute name="contents"/>
	
	<footer>
		<tiles:insertAttribute name="footer"/>
	</footer>
</body>



</html>