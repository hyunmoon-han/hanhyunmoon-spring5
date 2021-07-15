package com.edu.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.edu.service.IF_MemberService;
import com.edu.util.NaverLoginController;
import com.edu.vo.MemberVO;
import com.github.scribejava.core.model.OAuth2AccessToken;

/**
 * 이 클래스는 스프링 시큐리티의 /login처리한 결과를 받아서 /login_success를 처리하는 클래스입니다.
 * 
 * @author 현문
 *
 */
@Controller
public class LoginController {
	Logger logger= LoggerFactory.getLogger(LoginController.class);
	@Inject
	private IF_MemberService memberService;
	@Inject
	private NaverLoginController naverLoginController;
	
	
	//네이버 인증체크() 후 이동할 URL=콜백URL로 네이버에서 매개변수를 주는 데이터를 @RequstParam으로 받음.
//	@RequestMapping("/naver_callback") 이렇게 하면 , GET,POST2개다 가능
	@RequestMapping(value="/naver_callback",method= {RequestMethod.GET,RequestMethod.POST})
	public String naver_callback(@RequestParam(required=false)String code,@RequestParam String state,HttpSession session,Model model,RedirectAttributes rdat) throws IOException, ParseException {
		//네아로에서 로그인 취소 버튼을 눌렀을떄 처리하는 로직
		if(code==null) {
			rdat.addFlashAttribute("msgError","네이버 인증처리를 취소했습니다.");
			return "redirect:/login_form";//로그인 폼으로 다시 이동후 끝			
		}
		OAuth2AccessToken oauthToken;//인증에 사용되는 토큰객체 생성
		oauthToken=naverLoginController.getAccessToken(session,code,state);
		//위 인증에 사용된 토큰은 네이버에서 제공된 프로필정보를 가져올떄 토큰이 필요함.
		String profile = naverLoginController.getUserProfile(oauthToken);
		//위 String형 Profile정보를 json데이터로 파싱합니다.(key,value형태로 만듬)
		//*logger.info("더비그119:"+profile.toString());더비그119:{"resultcode":"00","message":"success","response":{"id":"CMctZV_B8VHoM3C9QH-lGiAflsr12v9eyE6s82MfrgU","email":"gksans421@naver.com","name":"\ud55c\ud604\ubb38"}}
		JSONParser parser=new JSONParser();
		Object obj = parser.parse(profile);//Json데이터로 파싱
		JSONObject jsonObj=(JSONObject) obj;//반환코드,메서지 변수값,response값
		JSONObject response_obj = (JSONObject) jsonObj.get("response");//프로파일데이터
		String status = (String) jsonObj.get("message");//인증성공여부 확인
		//위 최종적으로 출력된 response_obj를 파싱시작(아래)
		String username=(String) response_obj.get("name");
		String useremail=(String) response_obj.get("email");
		
		if(status.equals("success")){// 네이버 인증처리 겨로가가 success 이면 실행
			//인증 성공 이후 스크링 시큐리티의 ROLE_USER권한을 받아야지만 ,insert,memver,opdate...URL등에 접근이 가능함.
			//시작 :여기부터가 스프링시큐리티 코드가 시작됨
			//여기서 부터 코드가 네이버와는 관계없는 스프링 코드가 시작됩니다.
			List<SimpleGrantedAuthority> authorityies = new ArrayList();//스프링 시큐리티 권한을 강제로만듭니다.
			authorityies.add(new SimpleGrantedAuthority("ROLE_USER"));//ROLE_USER라는 권한을 강제로 추가.
			//스프링 시큐리티 인증도 추가(아래)			
			Authentication authentication=new UsernamePasswordAuthenticationToken(useremail,null,authorityies);//인증 토큰을 강제로 생성
			SecurityContextHolder.getContext().setAuthentication(authentication);//위에서 발생한 인증 토큰을 시큐리티클래스에 저장합니다
			//끝:여기까지가 스프링시큐리티 로직끝
			//로그인세션 변수 생성(아래)
			session.setAttribute("session_enabled", true);
			session.setAttribute("session_userid", useremail);
			session.setAttribute("session_levels", "ROLE_USER");
			session.setAttribute("session_username", username);
			session.setAttribute("session_login_type", "sns");//마이페이지 안보이게처리용
			rdat.addFlashAttribute("msg","네이버ID 로그인");//출력결과->네이버ID로그인이 성....
		}else {
			rdat.addFlashAttribute("msgError","네이버 인증이 실패했습니다.다시 로그인해 주세요.");
			return "redirect:/login_form";// 로그인 폼으로 이동
		}		
		return "redirect:/";//로그인 성공했다면 ,홈페이지로 이동
	}
	//HomeController에 있던/login_form을 네아로 로그인 URL 생성 떄문에 여기로 이동.
	@RequestMapping(value="/login_form",method=RequestMethod.GET)
	public String login_form(Model model,HttpSession session)throws Exception{
		//네이버 인증 url 구하기.:세션은 서버에 클라이언트 접속 정보를 저장하는 공간이 세션입니다.
		String naverAuthUrl="";
		//NaverLoingController loginContrller=new
		naverAuthUrl=naverLoginController.getAuthorizationUrl(session);
		model.addAttribute("url",naverAuthUrl);
		return "home/login";//.jsp 생략
	}
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
