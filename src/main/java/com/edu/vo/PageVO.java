package com.edu.vo;
/**
 * 이 클래스는 공통(회원관리,게시물관리,)으로 사용하는 페이징처리+검색기능의 클래스.
 * @author 한현문
 * 이 클래스는 오라클이든, MySql(마리아DB)어디서든 공통으로 사용Get/Set.
 * gueryStartNO ,queryPerPageNum,page,perPageNum,startPage,endPage
 *검색에 사용되는 변수(쿼리변수만): 검색어 (search_keyword),검색조건(search_type)
 */
public class PageVO {
	private int queryStartNo;//쿼리전용 변수
	private int queryPerPageNum;//쿼리전용
	private Integer page; //jsp에서 발생  자바전용
	private int perPageNum;//UI하단에 보여줄 페이징 개수 계산
	private int startPage;//워 perPageNum으로 구하는 UI하단페이지시작번호
	private int endPage;//위 perPageNum으로 구하는 UI하단 페이지 끝번호
	
	private String search_keyword;//jsp에서 받은 검색어 쿼리 전용변수
	private String seacch_type;//검색조건에 해당하는 쿼리전용변수
	
}
