package com.edu.aop;

import java.util.List;

import javax.inject.Inject;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.edu.service.IF_BoardTypeService;
import com.edu.vo.BoardTypeVO;

/**
 * 이 클래스는 AOP기능중 @Aspect 과 @ControllerAdvice 로 구현됩니다.
 * @author moon
 *
 */
//@Aspect

@ControllerAdvice
public class AspectAdvice {
	@Inject
	private IF_BoardTypeService boardTypeService;
	
	//나중에 게시물 관리 기능 만들때 @Aspect로 AOP기능 추가할예정.
	//이 메서드는 컨트롤러의 메서드가 실행되기전에 값을 생성해서 model객체에 담아서 jsp로 자료를 전송. 
	//위@컨트롤러어드바이스를 이용해서 컨트롤러의 모든 메서드가 실행되기전에 호출만되면 아래 메서드가 자동실행(콜백함수)
	@ModelAttribute("listBoardTypeVO")  //모델로 담아서보네라는 
	public List<BoardTypeVO> listboardTypeVO()throws Exception{//아무렇게나 메소드이름
	
		return boardTypeService.selectBoardType();
	}
}
