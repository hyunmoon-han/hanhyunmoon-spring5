<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!-- 게시판용 css임포트 -->
<link rel="stylesheet" href="/resources/home/css/board.css">
<!-- html5가 아닌 js로 유효성 검사 코어 2개 임포트 -->
<script src="/resources/home/js/jquery.validate.js"></script>
<script src="/resources/home/js/additional-methods.js"></script>
<style>
/* 모바일용 마이페이지 스타일 */
.myinfo {
	width: 170px;
	height: 70px;
	line-height: 70px;
	text-align: center;
	font-size: 18px;
	background: #000;
	color: #fff;
}
.radio_area {
	padding: 10px;
	box-sizing: border-box;
}
.gender {
	padding: 4px 10px;
	font-size: 14px;
}
/* //모바일용 로그인 스타일 */
/* 테블릿용 메인페이지 스타일 지정(아래) 801px~무한대까지 재정의 */
@media all and (min-width:801px) {
	
}
/* PC용 메인페이지 스타일 지정 1066px~무한대까지 재정의 */
@media all and (min-width:1066px) {
	
}
</style>
<script>
// 회원가입 전용 유효성 검사 부분
$(document).ready(function() {
	//유효성검사 확인 메세지를 한글로 출력하기
	$.extend($.validator.messages, {
		required:"필수항목입니다.",
		email:"유효하지 않은 Email 주소 입니다",
		digits:"숫자만 입력 가능합니다.",
		equalTo: "비밀번호가 일치하지 않습니다."
	});
});
</script>
<!-- 메인콘텐츠영역 만 변경됩니다. -->
<div id="container">
	<!-- 메인상단위치표시영역 -->
	<div class="location_area customer">
		<div class="box_inner">
			<h2 class="tit_page">스프링 <span class="in">in</span> 자바</h2>
			<p class="location">고객센터 <span class="path">/</span> 개인정보수정</p>
			<ul class="page_menu clear">
				<li><a href="#" class="on">개인정보수정</a></li>
			</ul>
		</div>
	</div>	
	<!-- //메인상단위치표시영역 -->

	<!-- 메인본문영역 -->
	<div class="bodytext_area box_inner">
		<div class="myinfo">내 정보</div>
		<!-- 폼영역 -->
		<form method="POST" name="join_form" action="/member/mypage" class="appForm">
			<fieldset>
				<legend>회원가입폼</legend>
				<p class="info_pilsoo pilsoo_item">필수입력</p>
				<ul class="app_list">
					<li class="clear">
						<label for="user_id_lbl" class="tit_lbl pilsoo_item">사용자ID</label>
						<div class="app_content"><input value="${memberVO.user_id}" readonly type="text" name="user_id" class="w100p" id="user_id_lbl" placeholder="아이디를 입력해주세요." required/></div>
					</li>					
					<li class="clear">
						<label for="password_lbl" class="tit_lbl pilsoo_item">암호</label>
						<div class="app_content"><input type="password" name="user_pw" class="w100p" id="user_pw_lbl" placeholder="비밀번호를 입력해주세요." /></div>
					</li>			
					<li class="clear">
						<label for="user_name_lbl" class="tit_lbl pilsoo_item">사용자이름</label>
						<div class="app_content"><input value="${memberVO.user_name}" type="text" name="user_name" class="w100p" id="user_name_lbl" placeholder="사용자 이름을 입력해주세요." required/></div>
					</li>
					<li class="clear">
						<label for="email_lbl" class="tit_lbl pilsoo_item">email</label>
						<div class="app_content"><input value="${memberVO.email}" type="email" name="email" class="w100p" id="email_lbl" placeholder="이메일을 입력해주세요." required/></div>
					</li>
					<li class="clear">
						<label for="point_lbl" class="tit_lbl pilsoo_item">point</label>
						<div class="app_content"><input readonly value="${memberVO.point}" type="digits" name="point" class="w100p" id="point_lbl" placeholder="point를 입력해주세요." required/></div>
					</li>
					<li class="clear">
						<label for="gender_lbl" class="tit_lbl pilsoo_item">권한 여부</label>
						<div class="app_content radio_area">
							<select name="enabled" class="gender" required >							
								<option value="${memberVO.enabled}">${memberVO.enabled=='true'?'허용':'금지'}</option>							
							</select>
						</div>
					</li>					
					<li class="clear">
						<label for="agree_lbl" class="tit_lbl pilsoo_item">개인정보활용동의</label>
						<div class="app_content checkbox_area"><input type="checkbox" name="agree" class="css-checkbox" id="agree_lbl" required checked/>
						<label for="agree_lbl" class="agree">동의함</label>
						</div>
					</li>
					<li class="clear">
						<label for="gender_lbl" class="tit_lbl pilsoo_item">권한여부</label>
						<div class="app_content radio_area">
						<select name="levels" class="gender" required >							
							<option value="${memberVO.levels}">${memberVO.levels}</option>							
						</select>
						</div>
					</li>
				</ul>
				<p class="btn_line">
				<button type="submit" class="btn_baseColor" style="cursor:pointer">정보수정</button>
				<button type="button" class="btn_baseColor" id="btn_leave" style="cursor:pointer">회원탈퇴</button>
				</p>	
			</fieldset>
		</form>
		<!-- //폼영역 -->
	</div>
	<!-- //메인본문영역 -->
</div>
<!-- //메인콘텐츠영역 -->



<!--  -->
<script>
$(document).ready(function(){
	$("#btn_leave").click(function(){
		if(confirm('정말로 탈퇴하시겠습니까?')){
			var form_leave=$("form[name='join_form']");
			$("option:eq(0)","select[name='enabled']").val("false");//자바스크립트| set 그냥input태그는 O
			//$("select[name='enabled']").html("<option value='false'>탈퇴</option>");//set
			//위 값을 false,0둘중 한개로 바꾼후 submit예정
			//alert($("select[name='enabled']").val());//|GET
			form_leave.attr("action","/member/mypage_leave");//크롬오동작으로 추가한 코드
			form_leave.submit();//실제 삭제는 아니고,enabled 필드값을 수정합니다.
			//탈퇴를 했다면, 로그아웃처리도 같이 되어야함
			//location.replace("/logout");//크롬에서는 오동작해서 자바스크립트처리않함.
		}
		
	});
});
</script>