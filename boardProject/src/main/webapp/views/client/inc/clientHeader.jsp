<%@ page pageEncoding="UTF-8" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
<div id="outout">
</div>
<script>
$(function() {
	fn_HeaderType('main');
});

fn_HeaderType = function(type) {
	var url = "/client/member/" + type;
	var _div = "#outout";
	var form;
	
	if(type == 'kakao') {
		url = "/kakoLogin";
		form = $("#kakaoLoginForm").serialize();
	}
	if(type == 'login' || type == 'myPage' || type == 'delete') {
		_div = "#clientAjaxDiv";
	}	
	$.ajax({
		url : url
		, type : "post"
		, dataType : "html"
		, data : form
		, success : function(res) {
			$(_div).empty();
			$(_div).html(res);
			if(type == 'kakao' || type =='logout') {
				searchList(1,10,10);
			}
		}
	});
}

window.Kakao.init('JavaScript Key');

function kakaoLogin() {
    window.Kakao.Auth.login({
        scope: 'profile_nickname, account_email', //동의항목 페이지에 있는 개인정보 보호 테이블의 활성화된 ID값을 넣습니다.
        success: function(response) {
            window.Kakao.API.request({ // 사용자 정보 가져오기 
                url: '/v2/user/me',
                success: (res) => {
                	const kakao_account = res.kakao_account;
                    var _email = kakao_account.email;
                    var _nickName = kakao_account.profile.nickname;
                    
                    if(_email == undefined) {	// 이메일 수집의 경우 필수로 받기 위해서 검수를 받아야 하므로 창을 다시 열어서 필수로 빋기 위해서
                    	alert("이메일 수집 동의 후 로그인이 가능합니다.");
                    	kakaoLogin();
                    }else {
						$("#pName").val(_nickName);     	
						$("#kakaoPid").val(_email);
						$("#email").val(_email);
						fn_HeaderType('kakao');
                    }
                }
            });
        },
        fail: function(error) {
            console.log(error);
        }
    });
}

</script>
