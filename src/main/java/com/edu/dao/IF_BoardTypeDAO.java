package com.edu.dao;

import java.util.List;

import com.edu.vo.BoardTypeVO;

/**
 * 이 인터페이스는 게시판 생성관리 쿼리를 접근하기 위한 명세서(영수증-가격,상품명|
 * 이름만있다라는뜼이다어거야~)  파일입니다.
 * @author moon
 *
 */
public interface IF_BoardTypeDAO {
	//CRUD 메소드 명세만 생성(아래5개)
	public void deleteBoardType(String board_type) throws Exception;
	public void updateBoardType(BoardTypeVO boardTypeVO) throws Exception;
	public BoardTypeVO readBoardType(String board_type) throws  Exception;
	public void insertBoardType(BoardTypeVO boardTypeVO) throws Exception;// 상품명
	//BoardTypeVO 1개의 레코드를 저장한 클래스를 다중로코드 List<제네릭타입>형 묶어서 받습니다.
	public List<BoardTypeVO> selectBoardType () throws Exception;//가격
}
