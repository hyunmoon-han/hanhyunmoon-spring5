package com.edu.controller;

import java.util.Collection;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.edu.service.IF_MemberService;
import com.edu.vo.MemberVO;

/**
 * 이 클래스는 스프링 시큐리티의 /login처리한 결과를 받아서 /login_success를 처리하는 클래스입니다.
 * 
 * @author 현문
 *
 */
@Controller
public class LoginController {
	@Inject
	private IF_MemberService memberService;
	// security-context에서 처리한 ID,암호 비교 쿼리 실행된 결과가 Authentication에 저장된 결과를 사용합니다 .
	@RequestMapping(value="/login_success",method=RequestMethod.GET)
	public String login_success(HttpServletRequest request,RedirectAttributes rdat) throws Exception{
		//request의 목적:세션 객체를 만들기 위해서 (로그인 정보를 화면이 이동하더라도 유지하기위해서)
		//rdat의 목적:modal객체로 값을 전송할수 없는 상황일떄, 값을 jsp로 전송하기위해서
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		//싱글톤 객체를 만들거나 사용하는 목적: 메모리 관리를 위해서 1개만 객체로 만들어서 사용하기위해서(new로 객체를 생성하지 못함)
		String userid="";//사용자 id
		String levels="";//권한이 들어갈 변수
		boolean enabled = false;//로그인 체크가 들어갈 변수(true=아이디/암호 비교성공)
		//principal객체는 UserDetails객치가 추출되고,enabled라는 인증결과가 존재합니다.
		Object principal = authentication.getPrincipal();
		if(principal instanceof UserDetails) {//위 결과로 principal객체에 UserDetails 있으면 
			enabled = ((UserDetails) principal).isEnabled();//true,false반환
			//위 인증결과로 로그인체크를 합니다		
		}
		//로그인 인증이true라면 내용 실행(세션값-로그인 아이디,권한,회원 이름 저장하는 목적
		if(enabled) {
			HttpSession session = request.getSession();
			//자바8이상에서 지원되는 람다식 사용(아래)
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			//auto-config-어쩌구 값이. authorities에는  회원의levels값{"ROLE_ANONYMOUS","ROLE_USER","ROLE_ADMIN",,,,}
			//우리DB에서는 levels가 1개 필드라서 여러개 권한이 있을 수 없습니다.
			//규모가 있는 DB에서는 tbl_member <- tbl_levels 테이블을 만들어서 여러개의 권한을 줍니다.
			//tbl_levels가 있으면 ,["admin":{"admin:"}
			//o받아서 o이런걸 equals(비교)해서 값을 levales에넣는구나
			if(authorities.stream().filter(o ->o.getAuthority().equals("ROLE_ANONYMOUS")).findAny().isPresent()) {
				levels = "ROLE_ANONYMOUS";//권한이긴한데 무명
			}
			if(authorities.stream().filter(o ->o.getAuthority().equals("ROLE_USER")).findAny().isPresent()) {
				levels = "ROLE_USER";//권한-일반사용자
			}
			if(authorities.stream().filter(o ->o.getAuthority().equals("ROLE_ADMIN")).findAny().isPresent()) {
				levels = "ROLE_ADMIN";//권한-관리자
			}
			//람다식은 외국 코드를 분석할떄필요합니다.
			userid=((UserDetails)principal).getUsername();
			//위에서 구한 변수 3개 enabled,levels,userid를 세션 변수로 저장(아래)
			session.setAttribute("session_enabled", enabled);//로그인 여부확인
			session.setAttribute("session_levels", levels);//로그인한 회원의 권한
			session.setAttribute("session_userid", userid);//로그인한 아이디를 출력
			MemberVO memberVO = memberService.readMember(userid);
			session.setAttribute("session_username", memberVO.getUser_name());
		}
		rdat.addFlashAttribute("msg","로그인");//로그인 성공여부를 jsp페이지로 보내주는 변수생성.
		return "redirect:/";
	}
}
