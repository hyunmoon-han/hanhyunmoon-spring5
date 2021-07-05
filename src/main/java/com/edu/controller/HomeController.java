package com.edu.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//외부라이브러리(모듈) 사용 =import
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.edu.service.IF_MemberService;
import com.edu.vo.MemberVO;

/**
 * 이 클래스는 MVC웹프로젝트를 최초로 생성시 자동으로 생성되는 클래스.
 * 웹프라우저의 요청사항을 view단(jsp)에 연결시켜주는 클래스@controller.
 * 스프링에서 관리하는 클래스를 스피링빈(콩) = 스프링빈을 명시한는 게 @controller 에노테이션.
 * Beans(콩들) 그래프로 이 프로젝트의 규모를 확인가능.
 * 스프링이 관리하는 클래스(=스프링빈)는 파일아이콘에S가 붙습니다.
 */


@Controller
public class HomeController {
	//스프링빈(클래스)에서는 로거로 디버그를 합니다. =로거객체를 만듭니다. 
	//로고중 slf4j(Spring Log For Java)

	private static final Logger Logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * 사용자요청(웹브라우저)을 받아서=@RequestMapping 인터페이스를사용해서 메서드명을 스프링이 구현합니다.
	 *  route(루트(rootX))
	 * 	 * return 값으로 view(jsp)를 선택해서 작업한 결과를 변수로 담아서 화면에 전송 후 결과를 (표시)렌더링합니다.
	 * 폼(자료) 전송시 post(자료숨김),get(자료노출-URL쿼리스트링?있는자료전송,)
	 */
	//이제부터는 일반적인 개발방식 VO-쿼리-DAO-service(관리자단에서 여기까지끝)
	//관리자단에서 작성한 Service사용자단에서 그대로 이용, 컨트롤러부터분리해작업-jsp
	@Inject
	private IF_MemberService memberService;
	
	//404파일 에러 처리하는 GET 호출 추가
	@RequestMapping(value="/home/error/error_404",method=RequestMethod.GET)
	public String error_404() {
		return "home/error/error_404";//.jsp생략
	}
	//회원 가입처리 호출 POST방식
	@RequestMapping(value="/join",method=RequestMethod.POST)
	public String join(MemberVO memberVO,RedirectAttributes rdat)throws Exception{
		//rawPassword 암호를 스프링 시큐리티로 인코딩합니다(아래)
		String rawPassword = memberVO.getUser_pw();
		BCryptPasswordEncoder passwprdEncoder=new BCryptPasswordEncoder();
		memberVO.setUser_pw(passwprdEncoder.encode(rawPassword));//암호화 실행
		
		memberService.insertMember(memberVO);
		rdat.addFlashAttribute("msg","회원가입");//화원가입   성공했습니다 출력용
		return "redirect:/login_form";//페이지 리다이렉트로이동
	}
	//회원가입폼 호출 GET방식
	@RequestMapping(value="/join_form",method=RequestMethod.GET)
	public String join_form()throws Exception{
		
		return "home/join";//.jsp생략
	}
	//마이페이지에서 회원탈퇴POST방식처리만.
	@RequestMapping(value="/member/mypage_leave",method=RequestMethod.POST)
	public String mypage_leave(MemberVO memberVO)throws Exception{
		memberService.updateMember(memberVO);		
		//rdat.addFlashAttribute("msg","회원탈퇴");// 스프링 내장된logout을 사용시x
		return "redirect:/logout";
	}
	
	//마이페이지 회원정보수정POST방식.처리후 msg를 히든값으로 jsp로 전송
	@RequestMapping(value="/member/mypage",method=RequestMethod.POST)
	public String mypage(MemberVO memberVO,RedirectAttributes rdat)throws Exception{
		//암호를 인코딩처리합니다.조건,암호를 변경값이 있을떄
		if(!memberVO.getUser_pw().isEmpty()) {
			BCryptPasswordEncoder passwprdEncoder=new BCryptPasswordEncoder();
			String rawPassword = memberVO.getUser_pw();
			memberVO.setUser_pw(passwprdEncoder.encode(rawPassword)); 
		}
		memberService.updateMember(memberVO);		
		rdat.addFlashAttribute("msg","회원정보수정");//회웑어보수정(가)이 성공했습니다.출력용
		return "redirect:/member/mypage_form";
	}
	
	//mypage form호출 get방식,회원 수정폼이기 때문에 model에 담아서 변수값을 전송이 필요함.
	@RequestMapping(value="/member/mypage_form",method=RequestMethod.GET)
	public String mypage_form(HttpServletRequest request,Model model)throws Exception{
		//로그인한 사용자 seSseion_userid 로 memberService의 readMember를 호출하면 됨.
		//jsp에서 발생된 세션을 가져오려고 하기 때문에 HttpServletRequest객체가 사용됩니다.
		HttpSession session = request.getSession();//싱클톤 객체
		String user_id=(String) session.getAttribute("session_userid");
		//memberService에서 1개의 레코드를 가져옵니다 .model담아서 jsp로 보냅니다.
		model.addAttribute("memberVO",memberService.readMember(user_id));
		return "home/member/mypage";//.jsp생략
	}
	//사용자단 로그인 폼 호출 get방식,로그인 POST처리는 컨트롤러에서하지않고 스프링시큐리티로 처리
	@RequestMapping(value="/login_form",method=RequestMethod.GET)
	public String login_form()throws Exception {
		
		return "home/login";//.jsp생략
	}
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String homepage(Model model) {//콜백메서드-자동실행됨
		String jspVar = "@서비스(DB)에서처리한결과";
		
		model.addAttribute("jspObject", jspVar );
		//home.jsp파일로 자료를 전송(스프링)하는 기능=model인터페이스객체(스프링이처리)에 내용만 채우면 됨
		Logger.info("디버그 스프링로고사용:"+jspVar);//System.out 대신 logger객체를 사용
		
		return "home/index";//확장자가 생략.jsp가 생략되어 있음.
	}
	
}
