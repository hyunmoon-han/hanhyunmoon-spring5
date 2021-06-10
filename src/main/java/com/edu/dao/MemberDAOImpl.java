package com.edu.dao;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.edu.vo.MemberVO;
import com.edu.vo.PageVO;

/**
 * 이클래스는 IF_memberDAO 인터페이스를 구현하는 클래스 입니다.
 * implements 키워드로 상속을 받습니다. 
 * 단,DAO기능의 구현 클래스는 스프링빈으로 등록이 필요. 그래서 @Repository 붙임
 * @author 한현문
 *3.번
 */
@Repository
public class MemberDAOImpl implements IF_MemberDAO{
	@Inject//sqlSession 의존성(디펜더시)을 주입합니다.=고전에서는 객체를 생성
	private SqlSession sqlSession;
	
	@Override//부모 인터페이스의 메서드를 상속해서 재정의 합니다.
	public List<MemberVO> selectMember(PageVO pageVO) throws Exception {
		// sqlSession의 메서드 이용해서 메퍼 쿼리를 사용.
		List<MemberVO> listMember = sqlSession.selectList("memberMapper.selectMember",pageVO);
		return listMember;
	}

	@Override
	public int countMember() throws Exception {
		// sqlSession빈의 메서드를 이용해서 매퍼 쿼리를 실행(아래)
		int totalCount = sqlSession.selectOne("memberMapper.countMember");//"쿼리위치.ID"
		return totalCount;
	}

	@Override
	public void insertMember(MemberVO memberVO) throws Exception {
		// sqlSession빈의 메서드를 이용해서 매퍼쿼리를 실행(아래)
		sqlSession.insert("memberMapper.insertMember", memberVO);
		
	}

	@Override
	public void deleteMember(String user_id) throws Exception {
		// sqlSession스프링빈 의 메서드를 이용해서 매퍼쿼리를 실행(아래)
		sqlSession.delete("memberMapper.deleteMember", user_id);  //
		
	}

	@Override
	public MemberVO readMember(String user_id) throws Exception {
		// 데이터베이스 마이비타스 쿼리를 호출(실행)
		return sqlSession.selectOne("memberMapper.readMember",user_id);// (쿼리위치,태그)
	}

	@Override
	public void updateMember(MemberVO memberOne) throws Exception {
		// DB마이바티스 쿼리호출(아래)
		sqlSession.update("memberMapper.updateMember", memberOne); //쿼리 위치 ,쿼리값
		
	}

}
