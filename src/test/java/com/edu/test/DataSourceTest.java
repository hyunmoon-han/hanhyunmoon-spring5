package com.edu.test;




import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;


import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * 이 클래스는 오라클과 연동해서 CRUD 를 테스트하는 클래스입니다.
 * 과장(이사,팀장)Junit CRUD까지 만들어서 일반사원에게 공개 + 회원관리
 * @author 한현문
 *
 */
//RunWith 인터페이스 현재클래스 가 Junit실행클래스라고 명시
 @RunWith(SpringJUnit4ClassRunner.class)
 //경로에서 **(모든폴더명시),*(모든파일명을 명시)
 @ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/**/*.xml"})

 @WebAppConfiguration
public class DataSourceTest {
	 //디버그용 로그 객체변수생성
	 private Logger logger = Logger.getLogger(DataSourceTest.class);
	 //dataSource객체는 데이터베이스 객체를 pool로 저장해서 사용할때 DataSource클래스를 사용(아래)
	 @Inject//인젝트는 스프링에서 객체를 만드는 방법, 이전 자바에서는 new 키워드로 객체를  만들었고...
	 DataSource dataSource;// Inject로 객체를 만들면 메모리 관리를 관리를 스프링이 대신 해줌.
	 //Inject는 자바 8부터 지원, 그럼, 이전 자바7에서는 @Autowired로 객체를 만들었슴
	 
	//스프링 코딩순서:
	 //M-V-C 사이에 데이터를 입출력하는 임시저장 공간 (VO클래스-멤버변수+get/set메서드) 생성
	 //보통 ValueObject클래스는 DB테이블과 1:1로 매칭이 됩니다.
	 //그래서 , 1. MeberVO.java클래스를 생성.(필수)
	 //2.DB(마이바티스)쿼리를 만듭니다. (VO사용됨) --내일부터시작
	 @Test
	 public void selectMeber() throws Exception{
		 //회원괸리 테이블에서 더미로 입력한 100개의 레코드를 출력하는 메서드 테스트->회원관리 목록이출력
		
	 }
	
	 @Test
	 public void oldQueryTest() throws Exception{
		 //스프링빈을 사용하지 않을때 예전 방식-코딩테스트에서는 스프링설정을 안쓰고, 직접DB아이디 /암호 입력
		 Connection connection =null;
		 connection=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/XE","XE2","apmsetup");
		 
		 logger.debug("데이터베이스 직접 접속이 성공 하였습니다. DB종료는 "+connection.getMetaData().getDatabaseProductName());
		 // DriverManager:데이터연동할때 쓰는클래스
		 //직접쿼리를 날립니다.날리기전 쿼리문자  객제생성statement
		 Statement stmt = connection.createStatement();
		 //위 쿼리문장 객체를 만드는 이유? 보안 (SQL인젝션공격을 방지)
		 //stmt객체가 없으면 SQL인젝션 방지코딩을 넣어야합니다.
		 //Insert쿼리 문장 만듬(아래)
		 //stmt.executeQuery("insert into dept02 values (20,'디자인부','경기도')");
		 //예전 방식으로 더미 데이터(샘플데이터)를 100개를 입력합니다.
			/*
			 *  for(int cnt=0;cnt<100;cnt++) { //
			 * stmt.executeQuery("insert into dept02 values ("+cnt+",'디자인부','경기도')"); 
			 *  }
			 */		 //인서트,업데이트,삭제시 sql디벨러퍼에서는 커밋이 필수지만,외부 java클래스에서 인서트 할떄는 자동 		커밋됩니다.
		 
		 //테이블에 입력되어있는 레코드셋를 select 쿼리 stmt문장으로 가져옴(아래) 
		 ResultSet rs = stmt.executeQuery("select * from dept order by deptno");//전에작업방식 ,코딩테스트용공부
		 //위에서 저장된 rs객체를 반복문로 출력(아래)
		 while(rs.next()){//re객체에 레코드가 없을때까지 반복
			 logger.debug(rs.getString("deptno")+" " + rs.getString("dname")+ " " 		+rs.getString("loc"));
			 
		 }
		 stmt=null;//메모리 반환
		 rs=null;//메모리 반환
		 connection=null;//메모리 초기화
	 }
	 
		/*
		 * @Test public void dbConnectionTest() { //데이터베이스 커넥션 테스트: 설정은 root-context의
		 * 빈(스프링클래스)을 이용 try { Connection connection =dataSource.getConnection();
		 * logger.debug("데이터베이스 접속이 성공 하였습니다. DB종료는 "+connection.getMetaData().
		 * getDatabaseProductName()); } catch (SQLException e) {
		 * logger.debug("데이터베이스 접속이 실패 하였습니다."); //e.printStackTrace(); } }
		 * 
		 * @Test public void junitTest() { //로거는 장점>조건에 따라서 출력을 조정할수 있음.
		 * logger.debug("Junit테스트 시작 입니다."); }
		 */
}
