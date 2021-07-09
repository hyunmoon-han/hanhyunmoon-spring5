package com.edu.aop;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.edu.service.IF_BoardService;
import com.edu.service.IF_BoardTypeService;
import com.edu.vo.BoardTypeVO;
import com.edu.vo.BoardVO;
import com.edu.vo.PageVO;

/**
 * 이 클래스는 AOP기능중 @Aspect 과 @ControllerAdvice 로 구현됩니다.
 * @author moon
 *
 */
@Aspect

@ControllerAdvice
public class AspectAdvice {
	@Inject
	private IF_BoardTypeService boardTypeService;
	@Inject
	IF_BoardService boardService;
	private Logger logger = LoggerFactory.getLogger(AspectAdvice.class);

	//나중에 게시물 관리 기능 만들때 @Aspect로 AOP기능 추가할예정.목적:board_type값을 항상 가져가도록 처리(세션)
	//세션?서버-pc 구조상에서 클라이언트가 서버에 접속할떄 [서버에 발생되는 정보를 세션이라고 함.(서버에 저장됨)]
	//쿠키?서버-pc 구조상에서 클라이언트가 서버에 접속할떄 [클라이언트에 발생되는 정보를 쿠키라고함.(pc에 저장됨)]
	//이전에는 쿠키로 로그인 체크를 했습니다.->보안상 pc에 로그인 정보가 저장되기 때문에 위험(인터넷광고에만 사용)->세션만변경
	//Aspect로 AOP를 구현할떄는 포인트컷(Advice가 들어갈위치)이 필요합니다. 
	//@Around =@Before+@After  =@Around( 포인트컷 전+후. *(..)모든 메서드)
	//@Around는 콜백함수의 매게변수로 조인포인트객체(포인트컷에서 실행되는 메서드들) 필수로 받습니다.
	//아래 조인포인트는(진입점) board_delete,board_update..메서드를 실행할때,본인이 작성한 글인지 확인한는 기능 단,관리자는 제외
	@Around("execution(* com.edu.controller.HomeController.board_delete(..)) || execution(* com.edu.controller.HomeController.board_update*(..))")
	public Object check_board_crud(ProceedingJoinPoint pjp)throws Throwable {
		//request객체는 이전페이지 URL+session_userid(세션값)을 가져오기 위해서 필요
		HttpServletRequest request = ((ServletRequestAttributes) 		RequestContextHolder.currentRequestAttributes()).getRequest();
		//사용할 변수 초기화(null)
		String user_id=null;//게시물 작성자 ID
		BoardVO boardVO=null;//boardVO.getWriter()게시물의 작성자 아이디 구할려면 필요한 객체변수
		logger.info("디버그 호출된 메서드 명은:"+pjp.getSignature().getName());
		//향상된 for문을 이용해서 매개변수중 dno,boardVO 2가지를 체크해서 user_id를 구함
		for(Object object:pjp.getArgs()) {
			//메서드의 매개변수가 BoardVO,bno일떄만 로직을 실행하기 위해서 
			if(object instanceof BoardVO) {//BoardVO는 클래스형 데이터형임
				user_id=((BoardVO) object).getWriter();	//업데이트 처리시 매개변수로 받은 객체			
			}
			//조인 포인트의 메서드의 매개변수가 object인데, 이 값과 instanceof 데이터형을 비교
			if(object instanceof Integer) {//bno일떄는 Integer형 데이터형임
				boardVO=boardService.readBoard((int) object);
				user_id=boardVO.getWriter();
			}
		}
		if(request != null) {
			HttpSession session=request.getSession();
			//위 메서드의 매개변수를 이용해서 생성된 user_id와 세선 userid를비교 + ROLE_USER일떄조건추가
			if(!user_id.equals(session.getAttribute("session_userid"))&&"ROLE_USER".equals(session.getAttribute("session_levels"))) {
				//메세지를 redirect로.addFlash~로 변수명을 지정해서 보냇음.
				//위 RedirectAttruibutes를 사용하는 대신 Flash(휘발생객체 .1번사용하고 내용없어짐)클래스 사용
				FlashMap flashMap = new FlashMap();
                flashMap.put("msgError", "게시물은 본인글만 수정/삭제 가능합니다.");
                FlashMapManager flashMapManager = RequestContextUtils.getFlashMapManager(request);
                flashMapManager.saveOutputFlashMap(flashMap, request, null);

				return "redirect:"+request.getHeader("Referer");
			}
		}
		Object result = pjp.proceed();//실제 저인 포인트 실행이 여기서됨
		return result;
	}
	
	//아래 조인포인트는 (진입점) 검색어와 게시판타입의 값을 세션으로 유지시키는 기능
	@Around("execution(* com.edu.controller.*Controller.*(..))") //여기있는걸 실행될때
	public Object sessionManager(ProceedingJoinPoint pjp)throws Throwable{//뭐가 나와도 던져
		//board_type 변수값을 세션에 저장하려고함. 클라이언트별 세션이 발생됨.
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		//일반적인 컨트롤러에서는 매개변수로 HttpServletRequest를 사용 가능함. 위처럼 복잡하게 구하지 않음.
		//컨트롤러 클래스에서 매개변수로 받을 값 (board_type) < pageVO, 
		
		PageVO pageVO = null;
		String board_type=null;//jsp에서 전송되는 값을 임시로 저장,목적은 세션변수 발생조건으로 사용
		String search_keyword =null; //한글검색시 IE에서 400에러발생하기떄문에 추가
		//조인 포인트리스트의 객체의 메서드의 Arguments(매개변수)를  뽑아냄
		for(Object object:pjp.getArgs())	{
			if(object instanceof PageVO) {//AOP실행 메서드중 매개변수중 PageVO pageVO객체있는지판단
				//결과는 게시판, 멤버서비스에서 PageVO 사용하는 서비스에만 적용됨.게시판생성관리에는 정용x
				pageVO = (PageVO) object;
				board_type= pageVO.getBoard_type();
				search_keyword = pageVO.getSearch_keyword();
			}	
		}
		if(request !=null) {//jsp에서 get,post가 있을때,
			//세션값을 paveVO.board_type 값으로 저장하는 로직(아래)
			HttpSession session=request.getSession();//pc가 스프링 프로젝트에 접근시 세션객체생성
			//검색폼이 있는 jsp에서 발생됨.결과는 검색폼이 없는 입력/수정/삭제에는 실행안됨.
			if(search_keyword !=null) {//검색어가 발생하면 최초로 세션을 만듭니다.
				session.setAttribute("session_search_keyword", search_keyword);
			}
			if(session.getAttribute("session_search_keyword")!=null) {
				//세션값이 있다면,실행
				search_keyword = (String) session.getAttribute("session_search_keyword");
				if(pageVO!=null) {//get,set중에 set하는 pageVO에 널이면 에러발생히기 때문에 추가한 코드
					pageVO.setSearch_keyword(search_keyword);
					
				}
			}
			if(board_type != null) {//최초로 세션변수가 발생
				session.setAttribute("session_board_type", board_type);
			}
			if(session.getAttribute("session_board_type") != null) {
				board_type=(String) session.getAttribute("session_board_type");
				if(pageVO !=null) {
					pageVO.setBoard_type(board_type);//검색달성:여기서 항상 값을 가져가도록 구현됩니다 .dao까지전달 selectboard,pageVO까지.
				}
			}
			logger.info("디버그19: "+(String) session.getAttribute("session_board_type"));
		}
		//Aspect > 포인트컷(Around) > 조인포인트(메서드) > 매개변수로 구현 결과를 리턴
		Object result = pjp.proceed();//여기서 조인포인트가 실행됩니다.
		return result;
	}
	
	//이 메서드는 컨트롤러에서  EXception이 발생했을때 여기서 인터셉트(가로채기)해서 에러메세지를 개발자가 작성한 jsp화면에 뿌려주는 기능을 추가 
	//prevPage변수1,exceptoi변수 2전송함
	@ExceptionHandler(Exception.class)
	public ModelAndView errorModelAndView(Exception ex,HttpServletRequest request) {
		//Model(jsp로Data담아서 보내주는 객체)+view(페이지 이동할 주소)
		ModelAndView modelAndView =new ModelAndView();
		//이전 페이지로 돌아가기용 데이터 생성
		String referer= request.getHeader("Referer");//크롬>네트워크>파일>Referer>이전페이지URL이 존재
		request.getSession().setAttribute("session_prevPage", referer);//prevPage세션변수만듭니다
		//컨트롤러에서 받은 exception을 modelAndView로 전달(아래)
		modelAndView.addObject("exception",ex);//model.addAttribute와 같음
		modelAndView.setViewName("home/error/error_spring");//return String   .jsp생략
		return modelAndView;
	}
	
	//이 메서드는 컨트롤러의 메서드가 실행되기전에 값을 생성해서 model객체에 담아서 jsp로 자료를 전송. 
	//위@컨트롤러어드바이스를 이용해서 컨트롤러의 모든 메서드가 실행되기전에 호출만되면 아래 메서드가 자동실행(콜백함수)
	@ModelAttribute("listBoardTypeVO")  //모델로 담아서보네라는 
	public List<BoardTypeVO> listboardTypeVO()throws Exception{//아무렇게나 메소드이름  .Exception나오면던져
	
		return boardTypeService.selectBoardType();
	}
}
