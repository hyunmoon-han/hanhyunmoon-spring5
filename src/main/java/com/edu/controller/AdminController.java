package com.edu.controller;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.edu.service.IF_MemberService;
import com.edu.vo.MemberVO;
import com.edu.vo.PageVO;

/**
 * 이 클래스는 Admin관리자단을 접근하는 클래스
 * 변수 Object를 만들어서 jsp로 전송 <-> jsp 폼값을 받아서  Object로처리
 * @author 한현문
 *
 */
@Controller
public class AdminController {
	//컨트롤러 수정하면 자동로딩(auto컴파일)
	//디버그용 로그객체생성(아래)
	private Logger logger = LoggerFactory.getLogger(AdminController.class);
	//이 메서드는 회원 목록을 출려하는 jsp와 매핑이 됩니다.
	@Inject
	private IF_MemberService memberService;
	
	//아래경로는 회원신규 등록 폼을 호출하는 URL쿼리스트링으로 보넨것을 받을떄는 GET방식으로 받습니다.
	@RequestMapping(value ="/admin/member/member_insert_form",method=RequestMethod.GET)
	public String insertmemberForm(@ModelAttribute("pageVO")PageVO pageVO)throws Exception{//폼호출했을떄아래값
		
		return "admin/member/member_insert";//jsp생략 ,상대경로
	}
	//아래경로는 회원 신규 등록을 처리하는 서비스를 호출하는 URL
	@RequestMapping(value ="/admin/member/member_insert",method=RequestMethod.POST)
	public String insertmember(PageVO pageVO,MemberVO memberVO)throws Exception{//인서트에서온값	
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String rawPassword = memberVO.getUser_pw();//원시 페스워드값
		String encPassword = passwordEncoder.encode(rawPassword);
		memberVO.setUser_pw(encPassword);
		memberService.insertMember(memberVO);
		return "redirect:/admin/member/member_list";//.jsp는 생략
	} 
	//아래 경로는 수정처리를 호출=DB를 변경처리
	@RequestMapping(value="/admin/member/member_update",method=RequestMethod.POST)
	public String updateMember(MemberVO memberVO , PageVO pageVO)throws Exception{// (받은값)
		//update 서비스만 처리하면 끝
		
		//업데이트 쿼리 서비스 호출하기 전 스프링시큐리티 암호화 적용합니다.
		String rawPassword = memberVO.getUser_pw();
		if(!rawPassword.isEmpty()) {//수정폼에서 암호 입력값이 비어있지 않을떄만 아래로직실행
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encPassword =passwordEncoder.encode(rawPassword);
		memberVO.setUser_pw(encPassword);
		}
		//이 메서드는 수정 처리 이후 보인 페이지에 있습니다
		memberService.updateMember(memberVO);//반환값이 없습니다.
		//redirect로 페이지를 이동하면,model로 담아서 보낼수 없습니다.아래처럼 쿼리스트링(URL?)으로 보냅니다.
		String queryString = "user_id="+memberVO.getUser_id()+"&page="+pageVO.getPage()+"&search_type="+pageVO.getSearch_type()+"&search_keyword="+pageVO.getSearch_keyword();
		return "redirect:/admin/member/member_update_form?"+queryString;//
		}
	
	//아래 경로는 수정폼을 호출=화면에 출력만=렌더링만
	@RequestMapping(value="/admin/member/member_update_form",method=RequestMethod.GET) //아래 보넬떄,받을떄
	public String updateMemberForm(MemberVO memberVO,Model model,@ModelAttribute("pageVO")PageVO pagoVO)throws Exception{
		//이 메서드는 수정폼에 pageVO,memberVO2개의 데이터객체를 jsp로 보넵니다.
		//사용자 1명의 레코드를 가져오는 멤버서비스(쿼리)를 실행(아래)
		MemberVO memberView =memberService.readMember(memberVO.getUser_id());
		//사용자1명의 레코드를 model에 담아서 +@Model.attribute에 담아서 jsp로 보냅니다.
		model.addAttribute("memberVO",memberView);//네임,value ->네임이라는 이름으로 value객체를추가
		return "admin/member/member_update";//상대경로
	}
	@RequestMapping(value="/admin/member/member_delete",method=RequestMethod.POST)//호출
	public String deleteMember(MemberVO memberVO)throws Exception{//매개변수 받은것은 인자값-<
		//MemberVO memberVO는 클래스형 변수 String user_id는 스트링형
		logger.info("디버그:" + memberVO.toString());
		String user_id=memberVO.getUser_id();//같은 의미
		//이 메서드는 회원상세보기 페이지에서 삭제버튼을 클릭시 전송받은 memberVO값을 이용해서 삭제를 구현(아래)
		memberService.deleteMember(user_id);//삭제 쿼리가 실행됨.
		//return "admin/memeber/member_list";//삭제후 이동할 jsp경로지정
		//위 방식대로하면,새로고침하면,/admin/member/member_delete 가 계속실행됩니다.
		//게시판테러상황을 방지히기 위해서, 퀴리를 작업 후 이동할때는 redirect(다시접속)라는 명령을 사용합니다.-사용자단에서 실습예정
		return "redirect:/admin/member/member_list"; //단,redirect는 절대경로를 사용.
	}
	@RequestMapping(value="/admin/member/member_view",method=RequestMethod.GET)
	public String viewMemberForm(Model model,@RequestParam("user_id")String user_id,@ModelAttribute("pageVO")PageVO pageVO) throws Exception {
		//페이ㅈ지 진입시 받은 클래스 변수값 PageVO pageVO
		/**
		 *  이 메서드는 리스트 페이지에서 상세보기로 이동할때 보여주는 1개 레코드값을 보여주는 구현을 합니다.
		 *  JUnit에서 테스트 했던 readMember 방식을 이용.
		 *  다른점은 Junit에서는 식별자 ID를 강제로 지정했지만,이 메서드에서는 @RequsetParam인터페이스를 이용해서 식별자값을 받음.
		 */
		
		//위 출력값 memberVO 1개의 레코드를 model을 이용해서 member_view.jsp로 보냅니다(아래)
		model.addAttribute("memberVO",memberService.readMember(user_id));
		//아래 페이지 반환시 (렌더링)@ModelAtteribute("pageVO")에 의해서 pageVO.page변수값으로 jsp보냅니다.
		return "admin/member/member_view";//상대경로 폴더파일위치
	}
	@RequestMapping(value="/admin/member/member_list",method=RequestMethod.GET)
	public String selectMember(@ModelAttribute("pageVO")PageVO pageVO,Model model) throws Exception{
		//Model model =new Model() 이런식으로도 모델객체가능
		/**이 메서드는 2개 객체를 생성하는 로직이 필요.결과를 JSP로 보네는 기능을수행. 
		1객체:memberList객체를 생성해서 model을통해서 jsp로 전송.
		2객체:pageVO객체(prev,next,startPage,endPage)를 생성해서 model을 통해서 jsp로 전송
		2번 객체부터 로직이 필요 ->memberList 구하는 쿼리변수가 만들어지기 떄문에 이것부터구현.
		*/	
		if(pageVO.getPage()==null){//jsp에서 클릭값이 없을떄만 초기값 입력
			pageVO.setPage(1);//초기값 1페이지입력
		}
		//*학습포인트: calcPage()로직 보다(이해)< 변수(객체)값의 이동확인(코딩에사용)
		//pageVO의 calcPage메서드를 실행하려면 , 필수 변수값입력해야됨(아래)
		pageVO.setQueryPerPageNum(5);//memberList객체+endPage구할떄 필요
		pageVO.setPerPageNum(5);//하단 UI에 보여줄 페이지번호개수,startPage구할떄 필요
		//위 2개의 변수값을 이용해서 아래 setTotalCount메서드에서 calcPage()호출됨.
		pageVO.setTotalCount(memberService.countMember(pageVO));//검색된 결과의 전체 카운트값(단,페이징과 관련없는 개수)
		//calcPage실행되면, prev,next변수값이 입력됩니다.
		List<MemberVO>listMember = memberService.selectMember(pageVO);//selectMember쓸려고->인젝션
		//위 setPerPageNum 이 20이면 next가 false(비활성화),5이면next가 true(활성화)
		logger.info("디버그"+pageVO.toString());//지금까지 jsp->컨트롤러 방향으로 일방향 자료이동.
		//컨트롤러에서jsp로 자료를 Modal에 담아서 보내게 됩니다.
		model.addAttribute("listMember",listMember);//쓸이름,객체이름
		//model.addAttribute("pageVO", pageVO);//나중에 @ModleAttribute로 대체 @ModleAttribute("pageVO") PageVO pageVO
		return "admin/member/member_list";// jsp파일 상대경로
	}
	//URL요청 경로는 @RequestMapping 반드시 절대경로로 표시
	@RequestMapping(value="/admin", method=RequestMethod.GET)
	public String admin(Model model) throws Exception {//에러발생시 Exception클래스의 정보를 스프링으로 보내게 됩니다.
		
		//아래 상대경로에서/WEB-INF/views폴더가 루트(최상위)(생략prefix 접두어)입니다.
		//아래 상대경로에서 home.jsp에서 .jsp(생략suffix접미어)입니다.
		return "admin/home";//리턴경로= 접근 경로는 반드시 상대경로로 표시 
	}
}
