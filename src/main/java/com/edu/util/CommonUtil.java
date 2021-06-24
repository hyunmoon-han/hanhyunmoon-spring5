package com.edu.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.edu.service.IF_MemberService;
import com.edu.vo.MemberVO;

/**
 * 이 클래스는 이 프로젝트에서 공통으로 사용하는 유틸리티 기능을 모아놓은 클래스입니다.
 * @author 현문
 * 컨트롤러기능이면 @Controller(jsp와 바인딩이 필요할떄는 필수 에노테이션(지시어)입니다
 * 콤포턴트기능이면 @Component는 MVC가 아닌 기능들을 모아놓은 스프링빈 명시,여기서는 jsp와 바인딩이 필요해서 사용않함.
 */
@Controller
public class CommonUtil {
	//멤버변수생성(아래)
	private Logger logger =LoggerFactory.getLogger(CommonUtil.class);
	@Inject
	private IF_MemberService memberService;//스프링 빈을 주입 받아서(DI) 객체준비 
	
	
	//첨부파일 업로드/다운로드/삭제/인서트/수정에 모두 사용될 저장경로를 1개 지정해서 전역으로사용
	@Resource(name="uploadPath")
	private String uploadPath;//root-context 업로드 경로클래스빈의 ID값을 받아서 String변수 입력
	
	public String getUploadPath() {
		return uploadPath;
	}
	//페이지 이동이 아닌 같은 페이지에 결과값만 반환하는 @ResponseBody
	@RequestMapping(value="/image_preview",method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> imagePreview(@RequestParam("save_file_name")String save_file_name,HttpServletResponse response) throws Exception {
		//파일을 입출력할떄는  파일을byte(이진010101011010)형식으로 입출력할때 발생되는 통로 스트림이 발생
		FileInputStream fis =null;//입력통로
		ByteArrayOutputStream baos=new ByteArrayOutputStream();//출력통로
		fis=new FileInputStream(uploadPath+"/"+save_file_name);
		int readCount=0;
		byte[] buffer =new byte[1024]; //임시저장소크기지정1K
		byte[] fileArray=null;//출력스트림결과 저장하는 공간
		//반복문 : 목적,fis입력받는 save_file_name 바이트값이(배열) -1일때까지 반복(없을때까지)
		while((readCount =fis.read(buffer))!=-1) {
			//입력통로fis에서 출력통로 통로로 baos보냅니다.이유는 파일 입출력은 byte 단위로만 가능.
			baos.write(buffer, 0, readCount);//(rawDatea,종료조건,반복횟수)
			//결과는 baos에 누적된 결과가 들어갑니다.-jsp로 보내주면 됩니다.		
		}
		
		fileArray= baos.toByteArray();//baos클래스를  byte[]배열로 형변환(파싱)합니다.
 		fis.close();//메모리 초기화
 		baos.close();//메모리 초기화
 		//fileArray값을 jsp로 보내주는 준비작업 ,final 이메서드에서만 사용하겠다고 명시.
 		final HttpHeaders headers=new HttpHeaders();//크롬 개발자도구>네트워크>image_preview클릭>헤더탭확인
 		String ext = FilenameUtils.getExtension(save_file_name);
 		//이미지 확장자에 따라서 매칭되는 헤더값이 변해야지만, 이미지 미리보기가 정상으로 보입니다.
 		switch(ext.toLowerCase()) {//선택조건:확장자를 소문자로 바꿔서 비교
 		case "png":
 			headers.setContentType(MediaType.IMAGE_PNG);
 			break;//스위치문 빠져나가기
 		case "jpg":
 			headers.setContentType(MediaType.IMAGE_JPEG);
 			break;
 		case "gif":
 			headers.setContentType(MediaType.IMAGE_GIF);
 			break;
 		case "jpeg":
 			headers.setContentType(MediaType.IMAGE_JPEG);
 			break;
 		case "bmp":
 			headers.setContentType(MediaType.parseMediaType("image/bmp"));
 			break;
 		default:break;	
 		}
 		
//		return new ResponseEntity<byte[]>(fileArray);//객체생성시 초기값(rawData,)
	}
	//XSS 크로스사이트스크립트 방지용 코드로 파싱하는 메서드(아래)
	public String unScript(String data) {
//		if(data==null ||data.trim().equals("")) {}
		if(data.isEmpty()) {
			return "";
		}
		String ret=data;
		ret = ret.replaceAll("<(S|s)(C|c)(R|r)(I|i)(P|p)(T|t)", "&lt;script");/*&lt = <*/
        ret = ret.replaceAll("</(S|s)(C|c)(R|r)(I|i)(P|p)(T|t)", "&lt;/script");
        ret = ret.replaceAll("<(O|o)(B|b)(J|j)(E|e)(C|c)(T|t)", "&lt;object");
        ret = ret.replaceAll("</(O|o)(B|b)(J|j)(E|e)(C|c)(T|t)", "&lt;/object");
        ret = ret.replaceAll("<(A|a)(P|p)(P|p)(L|l)(E|e)(T|t)", "&lt;applet");
        ret = ret.replaceAll("</(A|a)(P|p)(P|p)(L|l)(E|e)(T|t)", "&lt;/applet");
        ret = ret.replaceAll("<(E|e)(M|m)(B|b)(E|e)(D|d)", "&lt;embed");
        ret = ret.replaceAll("</(E|e)(M|m)(B|b)(E|e)(D|d)", "&lt;embed");
        ret = ret.replaceAll("<(F|f)(O|o)(R|r)(M|m)", "&lt;form");
        ret = ret.replaceAll("</(F|f)(O|o)(R|r)(M|m)", "&lt;form");

		return ret;
	}
	//첨부파일이 이미지인지 아닌지 확인하는 데이터 생성
	private ArrayList<String>checkImgArray =new ArrayList<String>() {
		{
		add("aif");
		add("jgp");
		add("jpeg");
		add("png");
		add("bmp");
		}
	};
	
	public ArrayList<String> getCheckImgArray() {
		return checkImgArray;
	}
	
	//RestAPI서버 맛보기 ID중복체크(제대로 만들면  @RestController사용)
	@RequestMapping(value="/id_check", method=RequestMethod.GET)
	@ResponseBody//반환받은 값의 헤더값을 제외하고 ,내용(body)만 반환하겠다는 명시
	public String id_check(@RequestParam("user_id")String user_id) throws Exception {//하나받을때씀
		//중복아이디를 체크하는 로직(아래)
		String memberCnt = "1";//중복 ID가 있을떄 ,기본값1
		if(!user_id.isEmpty()) {//user_id가 공백이 아니라면,
		MemberVO memberVO = memberService.readMember(user_id);
		logger.info("디버그: " +memberVO);//user_id를 공백을 전송해도 null이기 때문에 조건 추가필요
		if(memberVO ==null) {//중복ID가 존재하지않으면{}안을 실행.
			memberCnt ="0";
			}
		}
		return memberCnt;//0,jsp 이렇게 작동하지 안습니다. 이유는 @ResponseBody떄문이고,RestAPI는 값만 반환 
	}

	//파일업로드 공통 메서드(지금은 Admin컨트롤러에서사용,->home컨트롤러에서도 사용)
	public String fileUpload(MultipartFile file) throws IOException  {
		// TODO UUID클래스로 자장될 고유식별(PK) 파일명을 생성후 물리적으로 저장
		String realFileName = file.getOriginalFilename();
		//폴더에 저장할 PK파일명을 생성(아래)
		UUID uid= UUID.randomUUID();//유니크ID값 생성
		String saveFileName = uid.toString() + "."+StringUtils.getFilenameExtension(realFileName);
		//file의 의 데이터 타입은MultipartFile클래스형 객체.클래스형 자료(맴버변수,메서드,..)는 직접 저장을 할수 없음
		//그래서,바이트형으로 파싱(변환)해서 저장해야함.->bit형(010101001)이진,비트형자료로 파싱(변환).
		//자바자료형 중 정수:byte(bit로구성) <short <int< long, 실수형(소수점):float<double
		byte[] fileData=file.getBytes();//getBytes메서드를 데이터를 bit형으로 파싱해서 저장
		File target = new File(uploadPath,saveFileName);
		FileCopyUtils.copy(fileData, target);//파일이 물리적으로 폴더에 저장됨
		return saveFileName;//UUID로 생성된 식별값의 파일명
	}
	
}
