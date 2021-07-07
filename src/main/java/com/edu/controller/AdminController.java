package com.edu.controller;

import java.io.File;
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
import org.springframework.web.multipart.MultipartFile;

import com.edu.dao.IF_BoardDAO;
import com.edu.service.IF_BoardService;
import com.edu.service.IF_BoardTypeService;
import com.edu.service.IF_MemberService;
import com.edu.util.CommonUtil;
import com.edu.vo.AttachVO;
import com.edu.vo.BoardTypeVO;
import com.edu.vo.BoardVO;
import com.edu.vo.MemberVO;
import com.edu.vo.PageVO;

/**
 * 이 클래스는 Admin관리자단을 접근하는 콘트롤러 클래스<-디스패처 서블렛(게이트웨이)기능을 합니다
 * 디스패처 서블렛 클래스는 톰캣이 생행될때 제일 먼저 실행되는 클래스,그래서,게이트웨이라고합니다.
 * //디스페어 서블릿 실행될떄,컨트롤러의 Request매핑경로를 재등록합니다
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
	@Inject
	private IF_BoardTypeService boardTypeService;
	@Inject
	private IF_BoardService boardService;//DI으로 스프링빈을 주입해서 객체로 생성
	@Inject
	private CommonUtil commonUtil;
	@Inject
	private IF_BoardDAO boardDAO;
	//게시물 등록을 POST로 처리합니다
	@RequestMapping(value="/admin/board/board_insert",method=RequestMethod.POST)
	public String board_insert(BoardVO boardVO,@RequestParam("file")MultipartFile[] files)throws Exception{
		//위 메서드의 BoardVO boardVO =>@RequestParam("fitle")String title 옛날에는 이런형식으로-boardVO객체에 설정한 값들로 jsp파일에name을 지정해서 비교하고 boardVO객체로 인식해서 컨트롤러로 보내게됨
		//신규 등록이라서 기존 첨부파일 불러오는 로직은 필요없음.
		//AttachVO테이블에 가로데이터를 세로데이터로 입력하기위해서(아래2개)
		String[] save_file_names=new String[files.length];
		String[] real_file_names=new String[files.length];
		int index=0;//첨부파일이 1개 이상일떄 반복변수로사용
		for(MultipartFile file:files) {
			if(file.getOriginalFilename()!="") {//첨부파일이 있으면 실행
				save_file_names[index]=commonUtil.fileUpload(file);//물리적인실제 파일저장
				real_file_names[index]=file.getOriginalFilename();//UI용 파일이름				
			}
			index=index+1;
		}
		//신규등록jsp폼에서 보내온 boardVO값에 아래file에 대한 임시 변수값을 저장하는 로직
		boardVO.setSave_file_names(save_file_names);
		boardVO.setReal_file_names(real_file_names);
		boardService.insertBoard(boardVO);//DB에 저장하는 서비스 호출
		return "redirect:/admin/board/board_list";//게시판 테러방지용 redirect사용(새로고침시 무한등록을 방지),모델로못보냄
		//게시판 신규등록시 자동으로 page가 1로 이동됩니다.
	}
	//게시물 등록 폼을 GET으로 호출
	@RequestMapping(value="/admin/board/board_insert_form",method=RequestMethod.GET)
	public String board_insert_form(@ModelAttribute("pageVO")PageVO pageVO)throws Exception{
		if(pageVO.getPage()==null) {
			pageVO.setPage(1);
		}
		return "admin/board/board_insert";//.jsp생략
	}
	//게시물 수정처리는POst로만 접근가능
	@RequestMapping(value="/admin/board/board_update",method=RequestMethod.POST)
	public String board_update(@RequestParam("file")MultipartFile[] files,BoardVO boardVO,PageVO pageVO) throws Exception{
		//기존 등록된 첨부파일 목록 구하기 .list(2차원배열)객체의 크기는 .size()로구함.  기존파일이 있을댸사용
		List<AttachVO> delFiles = boardService.readAttach(boardVO.getBno());
		//1차원 배열의 크기는.length
		String[] save_file_names = new String[files.length];
		String[] real_file_names = new String[files.length];
		int index=0;//jsp폼에서 보내온 파일에 대한 인덱스 초기값 변수.
		for(MultipartFile file:files) {//files[0]=file,files[1]..
			if(file.getOriginalFilename() !="") {//전송된 첨부파일명이 있다면 실행
				int sun = 0;//DB테이블에 저장된 순서에 대한 인덱스 초기값 변수.
				//아래 for의 목적:jsp폼에서 기존에 1번 위치에 기존파일이 있으면 ,기존파일을 지우고 ,신규파일을 덮어쓰는 로직(아래)
				for(AttachVO file_name:delFiles) {//기존파일을 가져와서 반복하면서 지우는 로직
					if(index==sun) { //jsp폼의 파일의 순서와 DB에 저장된 파일의 순서가 일치할때
						//File클래스는("파일의 업로드된 위치","삭제할파일명");
						File target = new File(commonUtil.getUploadPath(),file_name.getSave_file_name());
						if(target.exists()) {
						target.delete();//물리적인 파일 지우는 명령 -파일에 저장값 삭제
						//실제 삭제시 테이블삭제-> 파일삭제순으로이루어짐
						//DB지우는 부분 추가
						boardDAO.deleteAttach(file_name.getSave_file_name());
						}//if(target.exists())
					}//if(index==sun)
					sun=sun+1;
				}//for문 종료-sun을가진,for(AttachVO file_name:delFiles)
				//신규파일업로드
				save_file_names[index]=commonUtil.fileUpload(file);//jsp폼에서 전송된파일
				real_file_names[index]=file.getOriginalFilename();//UI용 이름임시저장
			}else{//if끝if(file.getOriginalFilename()
				save_file_names[index]=null;
				real_file_names[index]=null;
			}
			index=index+1;
		}//for-index,for(MultipartFile file:files)
		boardVO.setSave_file_names(save_file_names);
		boardVO.setReal_file_names(real_file_names);
		//시큐어코딩추가(아래)
		String rawContent=boardVO.getContent();
		String secContent=commonUtil.unScript(rawContent);
		boardVO.setContent(secContent);
		String rawTitle=boardVO.getTitle();
		String secTitle=commonUtil.unScript(rawTitle);
		boardVO.setTitle(secTitle);
		//시큐어끝
		
		boardService.updateBoard(boardVO);//게시물수정
		//첨부파일 작업전, 시큐어코딩:입력/수정시 시큐어코딩적용,뷰화면에서만 시큐어->뷰화면에서 시큐어x
		
		String queryString="bno="+boardVO.getBno()+"&page="+pageVO.getPage()+"&search_type="+pageVO.getSearch_type();
		return "redirect:/admin/board/board_view?"+queryString;//수정한 이후에는 board_view페이지로 이동:새로고침 방지하기 위해서 redirect사용-은 모델로 못보냄
	}
	//게시물 수정 폼은 URL쿼리스트링으로 접근
	@RequestMapping(value="/admin/board/board_update_form", method=RequestMethod.GET)
	public String board_update_form(Model model, @RequestParam("bno")Integer bno, @ModelAttribute("pageVO") PageVO pageVO) throws Exception {
		//첨부파일용 save_file_names, real_file_names 2개 배열값을 구해서 boardVO입력이 필요
		BoardVO boardVO = new BoardVO();
		boardVO = boardService.readBoard(bno);
		//여기서 첨부파일 배열을 추가(아래)
		
		List<AttachVO> listAttachVO = boardService.readAttach(bno);
		String[] save_file_names=new String[listAttachVO.size()];
		String[] real_file_names=new String[listAttachVO.size()];
		int idx=0;
		//향상된 for문 사용
		for(AttachVO file_name:listAttachVO) {//세로데이터를 가로데이터로 변경하는 로직
			save_file_names[idx] = file_name.getSave_file_name();
			real_file_names[idx] = file_name.getReal_file_name();
			idx = idx + 1;//idx++
		}
		boardVO.setSave_file_names(save_file_names);
		boardVO.setReal_file_names(real_file_names);
		model.addAttribute("boardVO", boardVO);//1개코드 저장
		
		return "admin/board/board_update";//.jsp생략
	}
	
	//게시물 삭제는 URL쿼리스트링으로 접근하지않고 ,post방식으로 처리
	@RequestMapping(value="/admin/board/board_delete",method=RequestMethod.POST)
	public String board_delete(@RequestParam("bno")Integer bno,PageVO pageVO)throws Exception{
		//디버그 삭제할 전역변수 경로확인
		logger.info("디버그전역업로드경로:"+commonUtil.getUploadPath());
		//DB테이블삭제한 이후,첨부파일부터 있으면 삭제처리. 자바에서 파일핸들링 처리 
		//기존 등록된 첨푸파일 폴더에서 삭제할UUID(고유한식별값생성클래스)이름을 추출합니다.(아래)
		List<AttachVO> delFiles = boardService.readAttach(bno);//해당게시물의 모든 첨부파일  delFiles 에 임시로 담아놓습니다
		
		boardService.deleteBoard(bno);//첨부파일 테이블 삭제 후 게시물 테이블 삭제  
		//물리적으로 파일 삭제 처리 시작,향상된for문사용
		for(AttachVO file_name:delFiles) {
			//File클래스는("파일의 업로드된 위치","삭제할파일명");
			File target = new File(commonUtil.getUploadPath(),file_name.getSave_file_name());
			if(target.exists()) {
			target.delete();//물리적인 파일 지우는 명령 
			//실제 삭제시 테이블삭제-> 파일삭제순으로이루어짐
			}
		}
		
		String queryString ="page="+pageVO.getPage()+"&search_type="+pageVO.getSearch_type();
		return "redirect:/admin/board/board_list?"+queryString;
	}
	//게시물 상세보기 폼으로 접근하지 않고 URL쿼리 스트링으로 접근(GET) @RequestParam("가져올 데이터의 이름")[데이터타입][가져온데이터를 담을 변수]
	@RequestMapping(value="/admin/board/board_view",method =RequestMethod.GET)
	public String board_view(@RequestParam("bno")Integer bno,@ModelAttribute("pageVO")PageVO pageVO,Model model)throws Exception{
		BoardVO boardVO =boardService.readBoard(bno);
		
		//첨부파일 부분 attach데이터도 board_view.jsp로 이동해야함 (아래)
		List<AttachVO> files = boardService.readAttach(bno);
		//배열객체 생성 구조:String[] 배열명 =new String[배열크기];
		//개발자가 만든 클래스형 객체,예, boardVO는 개발자가 만든 메서드 사용
		//반면,List<AttachVO>files List클래스형 객체 files는 내장용 메서드= size()
		String[] save_file_names = new String[files.size()];
		String[] real_file_names = new String[files.size()];
		//attach테이블안의 해당 bno개시물의 첨부파일 이름 파싱해서 jsp로 보내주는 과정(아래)
		int cnt=0;
		for(AttachVO file_name:files) {//files다수레코드에서 1개의 레코드씩 추출
			save_file_names[cnt]= file_name.getSave_file_name();
			real_file_names[cnt]= file_name.getReal_file_name();
			cnt = cnt+1;
		}
		//위 for은 세로데이터를 (다수레코드)를 가로데이터(1개레코드이면서 배열)에 담아서 1개 레코드 boardVO로 만드는게 목적
		boardVO.setSave_file_names(save_file_names);//파싱한 결과 set // 다운로드 로직에필요
		boardVO.setReal_file_names(real_file_names);//boardVO에 set //화면에보이는데필요
		model.addAttribute("boardVO",boardVO);//게시물+첨부파일명2개 이상
		//업로드한 파일이 이미지인지 아닌지 확인하는 용도의 데이터입니다.아래(목적:이미지일때 미리보기 img태그를 사용하기위해서 )
		model.addAttribute("checkImeArray",commonUtil.getCheckImgArray());
		
		return "admin/board/board_view";//.jsp.생략
	}
	//게시물 목록은 폼으로 접근하지 않고 URL로 접근하기 떄문에 GET방식으로 처리 
	@RequestMapping(value="/admin/board/board_list",method=RequestMethod.GET)
	public String board_list(@ModelAttribute("pageVO")PageVO pageVO,Model model)throws Exception{
		//게시판 타입이 null일때 기본값으로notice를 추가
		if(pageVO.getBoard_type()==null) {
			pageVO.setBoard_type("notice");
		}
		//페이징 처리를 위한 기본값추가
		if (pageVO.getPage()==null) {//초기값
			pageVO.setPage(1);
		}
		pageVO.setPerPageNum(5);//UI하단에서 보여줄 페이징 번호 크기	
		pageVO.setQueryPerPageNum(5);//토탈카운트를 구하기전 필수값으로 1개의 값이 필요
		pageVO.setTotalCount(boardService.countBoard(pageVO));//검색어포함
		
		//@ModelAttribute("pageVO")모델로 안보낼려면 사용  이렇게 보네나 아래로보내나 동일(listBoardVO)
		model.addAttribute("listBoardVO", boardService.selectBoard(pageVO));
		
		return "admin/board/board_list";//.jsp생략
	}
	//jsp에서 게시판 생성관리에 GET/POST이든 접근할떄URL을 bbs_type로 지정합니다.
	//왜board_type하지않고,bbs_type하는 이유는 왼쪽메뉴 고정시키는 로직에서 경로가 board와 겹치지 않도록.
	@RequestMapping(value="/admin/bbs_type/bbs_type_list", method=RequestMethod.GET)
	public String selectBoardTypeList(Model model)throws Exception{//목록폼1
		//아래모델은 AOP기능중 ControllerAdvice인터페이스로 구현했기 때문에 아래는 실행안함
		//		model.addAttribute("listBoardTypeVO",boardTypeService.selectBoardType()); 참견으로만들어놈
		return "admin/bbs_type/bbs_type_list";//상대경로일대는 view폴더가 root(최상위)
	}
	//bbs_type_list.jsp에서 게시판생성버튼을 클랙했을떄 이동하는 폼  경로
	@RequestMapping(value="/admin/bbs_type/bbs_type_insert", method=RequestMethod.GET)
	public String insertBoardTypeFrom()throws Exception{//입력폼1
		return "admin/bbs_type/bbs_type_insert";//.jsp생략
	}
	//bbs_type_insert.jsp의 입력폼에서 전송된 값을  boardTypeeVO 자동으로 담겨서  {구현}  단, 자동으로 값이 바인딩되려면 ,폼name과,VO벰버변수명 동일해야함
	@RequestMapping(value="/admin/bbs_type/bbs_type_insert", method=RequestMethod.POST)
	public String insertBoardType(BoardTypeVO boardTypeVO)throws Exception{//입력처리1
		boardTypeService.insertBoardType(boardTypeVO);
		return "redirect:/admin/bbs_type/bbs_type_list";
		//redirect는 절대경로 (뒤로가기-데이터가사라짐).forward도 이동이 가능(뒤로가기-데이터가 살아있음)
		//쇼핑몰에서 결제화면을 처리 후 뒤로가기를 누르면, 리다이렉트는 데이터가 사라지기 떄문에 재결제 불가
		//forward로 결제화면을 처리후 뒤로가기를 누르면,재결제가 발생됩니다.이러면 않되기 떄문에 사용않함.
	}
	//게시판 생서관리는 이 기능은 사용자단에서 UI를 사용할 일이 없기 떄문에,Read,Update를 1개로 사용
	@RequestMapping(value="/admin/bbs_type/bbs_type_update", method=RequestMethod.GET)
	public String updateBoardTypeFrom(@RequestParam("board_type")String board_type,Model model)throws Exception{//수정폼1
		model.addAttribute("boardTypeVO",boardTypeService.readBoardType(board_type));///서식-model.addAttributes("jsp변수","데이터객체");
		//서식model.add~("jsp변수-변수로담아서view화면으로 보넴",서비스에서 쿼리실행한 데이터객체);
		return "admin/bbs_type/bbs_type_update";//.jsp생략
	}
	@RequestMapping(value="/admin/bbs_type/bbs_type_update", method=RequestMethod.POST)
	public String updateBoardType(BoardTypeVO boardTypeVO)throws Exception{//수정처리1
		boardTypeService.updateBoardType(boardTypeVO);
		return "redirect:/admin/bbs_type/bbs_type_update?board_type="+boardTypeVO.getBoard_type();//수정한 이후 수정폼을 GET방식으로 이동
	}
	@RequestMapping(value="/admin/bbs_type/bbs_type_delete", method=RequestMethod.POST)
	public String deldeteBoardType(@RequestParam("board_type")String board_type)throws Exception{//삭제처리1
		boardTypeService.deleteBoardType(board_type);//삭제서비스호출(실행) 끝
		return "redirect:/admin/bbs_type/bbs_type_list";//.jsp생략
	}
	//-------------------------------------------------------------
	
	//아래경로는 회원신규 등록 폼을 호출하는 URL쿼리스트링으로 보넨것을 받을떄는 GET방식으로 받습니다.
	@RequestMapping(value ="/admin/member/member_insert_form",method=RequestMethod.GET)
	public String insertmemberForm(@ModelAttribute("pageVO")PageVO pageVO)throws Exception{//폼호출했을떄아래값
		
		return "admin/member/member_insert";//jsp생략 ,상대경로
	}
	
	//아래경로는 회원 신규 등록을 처리하는 서비스를 호출하는 URL
	@RequestMapping(value ="/admin/member/member_insert",method=RequestMethod.POST)
	public String insertmember(PageVO pageVO,MemberVO memberVO)throws Exception{//인서트에서온값	
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String rawPassword = memberVO.getUser_pw();//원시 패스워드값
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
		//스프링시큐리티ㅣ 내장클래서 에서user.pw(admin1234)와 password(해시값)비교함수
		//passwordEncoder.matches(admin1234, Password); 참이면 로그인성~공
		}
		//이 메서드는 수정 처리 이후 보인 페이지에 있습니다
		memberService.updateMember(memberVO);//반환값이 없습니다.
		//redirect로 페이지를 이동하면,model로 담아서 보낼수 없습니다.아래처럼 쿼리스트링(URL?)으로 보냅니다.
		String queryString = "user_id="+memberVO.getUser_id()+"&page="+pageVO.getPage()+"&search_type="+pageVO.getSearch_type();
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
		PageVO pageVO =new PageVO();//최소 2개의 기본값이 필수
		pageVO.setQueryPerPageNum(4);
		pageVO.setPage(1);
		List<MemberVO> latestMembers=memberService.selectMember(pageVO);
		model.addAttribute("latestMembers",latestMembers);
		return "admin/home";//리턴경로= 접근 경로는 반드시 상대경로로 표시 
	}
	//메인 페이지 또는 대시보드에 최신 테이블 리스트를 출력하는 방법 2개지(위-model,고전
	//아래-<c:import방식: 최신 게시물용도로 사용||페이지 안에서 컴파일된 다른 페이지를 불러올수있음
	@RequestMapping(value="/admin/latest/latest_board", method=RequestMethod.GET)
	public String latest_board(@RequestParam(value="board_name",required=false)String board_name,Model model,@RequestParam(value="board_type",required=false)String board_type)throws Exception{
		PageVO pageVO =new PageVO();
		pageVO.setPage(1);
		pageVO.setQueryPerPageNum(5);
		pageVO.setBoard_type(board_type);
		List<BoardVO> latestBoard= boardService.selectBoard(pageVO);
		model.addAttribute("board_name", board_name);
		model.addAttribute("board_type",board_type);
		model.addAttribute("latestBoard", latestBoard);
		return "admin/latest/latest_board";//.jsp생략.최신 게시물을 출력하는 결과 페이지 생성
	}
	
}
