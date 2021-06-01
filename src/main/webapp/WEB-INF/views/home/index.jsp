<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<title> 스프링 </title>
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- 제이쿼리 코어 임포트 가져오기(아래) -->
<script src="/resources/home/js/jquery-3.6.0.js"></script>
<!-- 상단바로가기 클릭시 부드럽게 이동하는 외부 라이브러리 JS 임포트(아래) -->
<script src="/resources/home/js/jquery.smooth-scroll.min.js"></script>
<!-- 화면을 초기화 시키는 reset 스타일 임포트:크로스브라이징 처리하기위해서 -->
<!-- 크롬, IE, 엣지, 사파리, 파이어폭스 h1, p, ul, div 태그의 크기가 조금씩 틀림니다. -->
<!-- 작업한 결과가 모든 브라우져(크로싱브라우징)에 똑같이 보이게 하기 위한 reset.css(아래) -->
<link rel="stylesheet" href="/resources/home/css/reset.css">
<!-- 여기서 부터 사용자 정의형 스타일 + 스크립트 추가(아래) -->
<link rel="stylesheet" href="/resources/home/css/mobile.css">
<!-- 태블릿용 css임포트 -->
<link rel="stylesheet" href="/resources/home/css/tablet.css">
<!-- pc용 css임포트 -->
<link rel="stylesheet" href="/resources/home/css/pc.css">
<script src="/resources/home/js/main.js"></script>
<!-- 메인 슬라이드 코어 임포트 -->
<script src="/resources/home/js/slidemain.js"></script>

<style>
/* 모바일용 로그인 스타일 */

/* //모바일용 로그인 스타일 */

/* 테블릿용 메인페이지 스타일 지정(아래)  801px~무한대까지 재정의*/
@media all and (min-width:801px){  /*가로크기 801px이상부터는*/
	
}
/* pc용 메인 페이지 스타일 지정  1066px~ 무한대까지의 재정의*/
@media all and (min-width :1066px){
	
}

</style>
<script>
//pc용  서브메뉴 사라지는 액션
var isOver1= false;//대메뉴 1이 false 일때 서브메뉴가 사라지는 조건
var isOverSub1=false;//서브메뉴 1번이 false 일때 서브메뉴가 사라지는 조건
// 1번 서브메뉴를 숨기는 함수()
function goHide1(){
	if(!isOver1 && !isOverSub1){//2개변수 값이 다 false일때 구현조건이 됩니다.
		$('.gnb_depth2_1').stop().fadeOut('fast');//서브메뉴의의2번의1이사라지게된다
	}
}
var isOver2=false;//대메뉴 2번이false일때 서브메뉴가 사라지는 조건
var isOverSub2=false;//서브메뉴2번이 false일때 서브메뉴거 사라지는 조건
//2번 서브메뉴 숨기기함수(아래)
function goHide2(){
	if(!isOver2 && !isOverSub2){
		$('.gnb_depth2_2').stop().fadeOut('fast');//페이드아웃실행되고 스탑실행  
	}
}
//PC용 서브메뉴가 나타나는액션 만들기(아래)
$(document).ready(function(){
	//대메뉴 1액션
	$('.openAll1').mouseover(function(){
		if(parseInt($( 'header').css('width')) >=1049){
			$('.gnb_depth2_1').stop().fadeIn('slow');//서브메뉴1번이 나타나게 처리
		}
		isOver1=true;//false일때 숨기는 조건<->true 일때 나타나는 조건
	});
	$('.openAll1').mouseout(function(){
		isOver1=false;
		setTimeout('goHide1()',200);//기본은 goHide1() 이것만하면 되는데 ,mouseout이벤트 놓치면 않되기 때문에 setTimeout사용
	});
	//서브메뉴1 액션
	$('.gnb_depth2_1').mouseover(function(){
		isOverSub1=true;//서브메뉴1 계속 나타납니다.
	});
	$('.gnb_depth2_1').mouseout(function(){
		isOverSub1=false;//서브메뉴1번이 사라집니다.
		setTimeout('goHide1()',200);
	});
	//대메뉴2 액션
	$('.openAll2').mouseover(function(){
		if(parseInt($( 'header').css('width')) >=1049){
			$('.gnb_depth2_2').stop().fadeIn('fast');//서브메뉴1번이 나타나게 처리
		}
		isOver2=true;//false일때 숨기는 조건<->true 일때 나타나는 조건
	});
	$('.openAll2').mouseout(function(){
		isOver2=false;
		setTimeout('goHide2()',200);
	});
	//서브메뉴 2액션
	$('.gnb_depth2_2').mouseover(function(){
		isOverSub2=true;//서브메뉴2 계속 나타납니다.
	});
	$('.gnb_depth2_2').mouseout(function(){
		isOverSub2=false;//서브메뉴2번이 사라집니다.
		setTimeout('goHide2()',200);
	});
});
// 메인페이지 전용 슬라이드 호출 부분
$(document).ready(function() {
	// 위에서 선언한 함수|변수 사용(아래)
	//여기서 함수호출(실행)
	slideAuto = setTimeout('play_w("right")', 3000);//3초마다 play_w함수 실행
	var slidePlayHide = setTimeout(function(){
		$('.rollplay').css('display','none');
	},3000);//3초 후에 rollplay클래스 플레이버튼 영역을 숨김
	// 3개의 슬라이드 버튼 클랙 액션처리
	$('.rollstop a').click(function(){
		// this는 클릭한 본인 태그를 말합니다.
		$(this).parent().hide();//현재 stop버튼 숨김.
		$('.rollplay').css('display','inline-block');
		if(slideAuto) {
			clearTimeout(slideAuto);//slideAuto변수가 없다면, play_w함수를 실행 중지.
		}
	});
	$('.rollplay a').click(function(){
		$(this).parent().hide();// a태그의 부모 rollplay영역 입니다.
		$('.rollstop').css('display','inline-block');
		play_w('right');//3초마다 슬라이드 이미지 액션일 발생합니다.

	});
	$('.rollingbtn li.seq a').each(function(index){
		$(this).click(function(){
			$('.rollplay').hide();
			$('.rollstop').css('display','inline-block');
			if(slideAuto) {
				clearTimeout(slideAuto);//슬라이드 중지
			}
			play_w(index);//슬라이드 재생 : 단 시작위치는 클릭한 index부터 무한반복
		});
	});
});

</script>
</head>
<body>
<!-- 헤더에서푸터까지 -->
<div id="wrap">
	<!-- 헤더상단메뉴영역영역 -->
	<header id="header">
		<div class="header_area box_inner clear">
			<!-- 상단로고영역 -->
			<h1><a href="index.html">스프링 in 자바</a></h1>
			<!-- //상단로고영역 -->
			
			<!-- 상단메뉴메뉴영역 -->
			<p class="openMOgnb">
				<a href="#">
					<b class="hdd">메뉴열기</b> 
					<span></span><span></span><span></span>
				</a>
			</p>
			<div class="header_cont">
				<ul class="util clear">
					<li><a href="login.html">로그인</a></li>
					<li><a href="join.html">회원가입</a></li>
					<!-- 로그인 후 보이는 메뉴(아래) -->
					<li><a href="#">OOO님 환영합니다.</a></li>
					<li><a href="mypage.html">마이페이지</a></li>
					<li><a href="../admin/home.html">AdminLTE</a></li>
				</ul>	
				<nav>
				<ul class="gnb clear">
					<li><a href="board_list.html" class="openAll1">샘플홈페이지</a>

                        <div class="gnb_depth gnb_depth2_1">
                            <ul class="submenu_list">
                                <li><a href="board_list.html">반응형홈페이지</a></li>
                            </ul>
                        </div>
					</li>
					<li><a href="board_list.html" class="openAll2">커뮤니티</a>
				        <div class="gnb_depth gnb_depth2_2">
                            <ul class="submenu_list">
                                <li><a href="board_list.html">공지사항</a></li>
                                <li><a href="board_list.html">겔러리게시판</a></li>
                            </ul>
                        </div>
					</li>
				</ul>
                </nav>
				<p class="closePop"><a href="javascript:;">닫기</a></p>
			</div>
			<!-- //상단메뉴메뉴영역 -->
		</div>
	</header>
	<!-- //헤더상단메뉴영역영역 -->
	
	<!-- 메인콘텐츠영역 -->
	<div id="container">
		<!-- 모바일+PC 공통슬라이드영역 -->
    	<div class="main_rolling_pc">
            <div class="visualRoll">
            	<!-- 슬라이드이미지영역 -->
                <ul class="viewImgList">
                    <li class="imglist0">
                        <div class="roll_content">
                            <a href="javascript:;">
							<p class="roll_txtline">1OOOO OOOOOOOOO OOOOOOOOO OOOOO</p>
							</a>
                        </div>
                    </li>
                    <li class="imglist1">
                        <div class="roll_content">
                            <a href="javascript:;">
							<p class="roll_txtline">2OOOO OOOOOOOOO OOOOOOOOO OOOOO</p>
							</a>
                        </div>
                    </li>
                    <li class="imglist2">
                        <div class="roll_content">
                            <a href="javascript:;">
							<p class="roll_txtline">3OOOO OOOOOOOOO OOOOOOOOO OOOOO</p>
							</a>
                        </div>
                    </li>
                </ul>
                <!-- //슬라이드이미지영역 -->
                <!-- 슬라이드버튼영역 -->
                <div class="rollbtnArea">
                    <ul class="rollingbtn">
						<!-- butt0 a img -->
                        <li class="seq butt0"><a href="#butt"><img src="./img/btn_rollbutt_on.png" alt="1번" /></a></li>
                        <li class="seq butt1"><a href="#butt"><img src="./img/btn_rollbutt_off.png" alt="2번" /></a></li>
                        <li class="seq butt2"><a href="#butt"><img src="./img/btn_rollbutt_off.png" alt="3번" /></a></li>
                        <li class="rollstop"><a href="#" class="stop"><img src="./img/btn_roll_stop.png" alt="멈춤" /></a></li>
                        <li class="rollplay"><a href="#" class="play"><img src="./img/btn_roll_play.png" alt="재생" /></a></li>
                    </ul>
                </div>
                <!-- //슬라이드버튼영역 -->
            </div>
        </div>
        <!-- //모바일+PC 공통슬라이드영역 -->
	
		<!-- 갤러리최근게시물영역 -->
		<div class="about_area">
			<h2>겔러리 최근 게시물 <b>TOP 3</b></h2>
			<div class="about_box">
				<ul class="place_list box_inner clear">
					<li><a href="#" onclick="$('.popup_base').css('height',$(document).height());$('.contact_pop').show();">
							<img class="img_topplace" src="img/no_image.png" alt="OOOO OOOOO" style="opacity:0.7;"/>
							<h3>OOOO OOOOO</h3>
							<p class="txt">OOOO OOOOOOOOO OOOOOOOOO OOOOOOOOO OOOOOOOOO OOOOOOOOO OOOOOOOOO OOOOO!</p>
							<span class="view">VIEW</span></a>
					</li>
					<li><a href="#" onclick="$('.popup_base').css('height',$(document).height());$('.space_pop').show();">
							<img class="img_topplace" src="img/no_image.png" alt="OOOO OOOOO" style="opacity:0.7;"/>
							<h3>OOOO OOOOO</h3>
							<p class="txt">OOOO OOOOOOOOO OOOOOOOOO OOOOOOOOO OOOOOOOOO OOOOOOOOO OOOOOOOOO OOOOO.</p>
							<span class="view">VIEW</span></a>
					</li>
					<li><a href="#" onclick="$('.popup_base').css('height',$(document).height());$('.program_pop').show();">
							<img class="img_topplace" src="img/no_image.png" alt="OOOO OOOOO" style="opacity:0.7;"/>
							<h3>OOOO OOOOO</h3>
							<p class="txt">OOOO OOOOOOOOO OOOOOOOOO OOOOOOOOO OOOOOOOOO OOOOOOOOO OOOOOOOOO OOOOO</p>
							<span class="view">VIEW</span></a>
					</li>
				</ul>
			</div>
		</div>
		<!-- //갤러리최근게시물영역 -->

		<!-- 카카오톡상담및최근공지사항영역 -->
		<div class="appbbs_area">
			<div class="appbbs_box box_inner clear">
				<h2 class="hdd">상담과 최근게시물</h2>
				<p class="app_line">
					<a href="javascript:;">카카오톡 1:1 상담</a>
					<a href="javascript:;">전화 상담 신청</a>
				</p>
				<div class="bbs_line">
					<h3>NOTICE</h3>
					<ul class="notice_recent">
						<li><a href="javascript:;">OOOO OOOOO (스프링OOOO OOOOO)</a></li>
						<li><a href="javascript:;">OOOO OOOOOOOOO OOOOO</a></li>
						<li><a href="javascript:;">OOOO OOOOO/OOOO OOOOO</a></li>
						<li><a href="javascript:;">OOOO OOOOO OPEN! (스프링정보, OOOO OOOOO)</a></li>
						<li><a href="javascript:;">OOOO OOOOO 서비스 점검 안내</a></li>
					</ul>
				</div>
			</div>
		</div>
		<!-- //카카오톡상담및최근공지사항영역 -->
	</div>
	<!-- //메인콘텐츠영역 -->
	
	<!-- 푸터메뉴및사업자정보영역 -->
	<footer>
		<div class="foot_area box_inner">
			<ul class="foot_list clear">
				<li><a href="javascript:;">이용약관</a></li>
				<li><a href="javascript:;">개인정보취급방침</a></li>
			</ul>
			<h2>스프링</h2>
            <p class="addr">OOOO OOOOO OOOO OOOOOOOOO OOOOO <span class="gubun">/</span>        
				<span class="br_line">대표전화 <span class="space0">02-1234-5678</span> <span class="gubun">/</span>        
					<span class="br_line">E-mail : <span class="space0"> admin@OOOO OOOOO.com</span></span>
				</span>
			</p>
			<p class="copy box_inner">Copyright(c) OOOO OOOOO all right reserved</p>
			<ul class="snslink clear">
				<li><a href="javascript:;">blog</a></li>
				<li><a href="javascript:;">facebook</a></li>
				<li><a href="javascript:;">instargram</a></li>
			</ul>
		</div>
	</footer>
	<!-- //푸터메뉴및주소영역 -->
</div>
<!-- //헤더에서푸터까지 -->

<!-- 하단퀵메뉴영역 -->
<div class="quick_area">
	<p class="to_top"><a href="#wrap" class="s_point">TOP</a></p>
</div>
<!-- //하단퀵메뉴영역 -->

</body>
</html>