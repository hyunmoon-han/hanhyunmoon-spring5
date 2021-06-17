package com.edu.dao;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.edu.vo.BoardTypeVO;

/**
 * 이 클래스는 게시판 생성관리 쿼리의 인터페이스를 구현메소드가있는 클래스입니다.
 * @author moon
 *
 */
@Repository  //스프링빈으로 인식
public class BoardTypeDAOImpl implements IF_BoardTypeDAO {
	//sqlSession템플릿을 의존성을 주입
	@Inject //자바8부터 신규로 나온 에노테이션입니다.
	private SqlSession sqlSession;
	
	@Override
	public void deleteBoardType(String board_type) throws Exception {
		// TODO sqlSession템플릿(틀)을 이용해서 매퍼쿼리를 실행 
		sqlSession.delete("boardTypeMapper.deleteBoardType", board_type);
		//서식 sqlSession.~템플릿메서드("SQL커리위치,데이터객체변수);
	}

	@Override
	public void updateBoardType(BoardTypeVO boardTypeVO) throws Exception {
		// TODO 아래주석동일
		sqlSession.update("boardTypeMapper.updateBoardType",boardTypeVO);
	}

	@Override
	public BoardTypeVO readBoardType(String board_type) throws Exception {
		// TODO 아래주석동일
		return sqlSession.selectOne("boardTypeMapper.readBoardType",board_type);
	}

	@Override
	public void insertBoardType(BoardTypeVO boardTypeVO) throws Exception {
		// TODO 아래 주석동일
		sqlSession.insert("boardTypeMapper.insertBoardType", boardTypeVO);//쿼리,변수
	}

	@Override
	public List<BoardTypeVO> selectBoardType() throws Exception {
		// TODO 매퍼쿼리를 실행 -sqlSession템플릿(틀)을 이용해서
		return sqlSession.selectList("boardTypeMapper.selectBoardType");//()을호출해서 위값에담어서보냄
	}

}
