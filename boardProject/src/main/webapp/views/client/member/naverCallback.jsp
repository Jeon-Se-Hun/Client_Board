<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script>
$(function() {
	var state = '${result.state}';
	
	if(state == "Cancel") {
		alert('${result.msg}');
	}else if(state == "Fail"){
		alert('${result.msg}');
		window.opener.fn_HeaderType('delete');
	}else if(state == 'Delete') {
		location.href = "/";	
	}else {
		window.opener.fn_HeaderType('main');
		window.opener.searchList(1,10,10);
	}
		window.close();
});
</script>