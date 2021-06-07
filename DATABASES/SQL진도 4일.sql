-- DDL문 (create;alter;) ,DCL문 (commit;rollback;)
-- DML문 (Data Manufacture Language ) insert,update,delete
-- insert문 :테이블에 새로운 레코드(row) 추가
-- CREATE TABLE dipt02 AS SELECT  * FROM dept
-- 위 처럼 커리를 실행하면 dept 테이블과 구조와 내용이 똑같은 테이블생성
--where 조건이 붙으면,구조는 같으나 내용은 빈 테이블이 생성
CREATE TABLE dept02 AS SELECT * FROM dept WHERE 1=0;
--위 커리는 테이블을 복제하라는 명령

INSERT INTO dept02 (--필드명
deptno,dname,loc
) VALUES(--바인딩값
10,'개발부서','천안'
);
insert into dept02 values (20,'디자인부','경기도');
-- DCL명령어 인 커밋이 필수입니다.
COMMIT;--  데이터 베이스 쿼리 직접입력한 결과는 반드시 커밋을 해줘야지만 실제 저장이 됩니다.커밋을 하지 않으면,여기만 보이고 다른곳x
SELECT *FROM dept02 order by deptno;

--delete는 래코드1줄을 지우는 명령
DELETE FROM dept02;   -- 이렇게 사용 모든 레코드삭제됨  표현조건집어노
DELETE FROM dept02 WHERE deptno >= 0;  --모두 삭제해도 where 반드시 포함.

-- DROP TABLE테이블명 은 테이블자체를 물리적으로 없애는 명령
DROP TABLE dept02; --드롭 테이블은 커밋없이 바로 적용.
CREATE TABLE emp01 as SELECT *FROM emp; -- 테이블 복제명령
SELECT * FROM emp01;
--UPDATE 테이블명 SET 필드명 ='바꿀값' where empno='특정ID'
UPDATE emp01 SET ename ='한현문';
ROLLBACK; -- DCL문,롤백은 마지막 커밋 바로전까지 가능
UPDATE emp01 SET ename ='한현문' WHERE empno = 7839;
UPDATE emp01 SET sal =sal*1.1;-- 모든 직업의 연복을 10%인상
UPDATE emp01 SET hiredate = sysdate;
UPDATE emp01 SET hiredate = '1997/01/20' WHERE ename = '한현문';
-- 머지 표준쿼리(ANSI)가 아니라서 건너뜀.

-- 8장 함수 (count,upper,lower,to_char,round...)그룹함수
--라운드함수(반올림) 소수점기준. round(10,o5,2)수수점2쨰반올림
SELECT ename,round(sal,-3) FROM emp; --레코드 여러개
SELECT SUM(sal) FROM emp;  --1개의 레코드만.그룹함수라고함 다더해
SELECT AVG(sal) FROM emp;  -- 평균 1개의 레코드로 출력
SELECT round(AVG(sal)) FROM emp;  -- 평균 1개의 레코드로 출력
SELECT COUNT(*) FROM emp WHERE sal>=
(SELECT AVG(sal) FROM emp);
 --위 커리 사원중에 평균연봉보다 많이 받는 사람의 숫자| AVG함수를 where 조건에 사용 못할떄 서브쿼리이용
SELECT MAX(sal),MIN(sal) FROM emp; -- 최대 최소 연봉 출력
SELECT MAX(sal),MIN(sal)
,MAX(sal)-MIN(sal) AS "대표와사원의연봉차"
FROM emp; 
-- 자바코딩에서는 소문자로 통일합니다-select
-- 부서별 연봉의 합계를 구해서 제일 급여가 많이 지출되는 부서(아래)
-- DB셋팅에서 대소문자구분해서 사용할지, 구분하지 않을 지 셋팅
SELECT * FROM(
SELECT deptno,SUM(sal)as dete_sal
FROM emp GROUP BY deptno
)R ORDER BY dete_sal DESC; --R (as R) r의 역활은 별칭
SELECT deptno, sum(sal) from emp GROUP by deptno ORDER by sum(sal) DESC;  -- 서브쿼리사용x
SELECT deptno, sum(sal)  from emp
GROUP by deptno  --1:1매칭필요
ORDER by sum(sal) DESC;
-- having은 group by의 조건문을 적습니다.
-- 부서별 평균 연봉이 2000이상인 부서의 번호와 부서별평균급여
SELECT deptno, round(avg(sal)) from emp     --where avg(sal)>=2000 에러   검색조건
GROUP BY deptno
HAVING AVG(sal) >=2000;    -- 그룹조건

--9장은 패스(레프트용 함수사용)
-- 10장 .테이블 조인  2개의 테이블을 연결해서 결과를 구하는 예약어
-- 댓글 개수 구할떄필요,
-- 카티시안프러덕트 조인(합집합-게시물이 10개 ,딸린 댓글100개 ->조인하면110개~1000개)사용x
-- (인너)조인(교집합)을 제일 많이 사용.
-- 아래 조인 방식이 oracale방식
SELECT dept.dname,emp.* FROM emp,dept 
WHERE emp.deptno = dept.deptno
AND emp.ename ='SCOTT';
--표준쿼리(ANSI)방식(아래) inner키워드 디폴드값(기본값) inner아래생략
SELECT * FROM emp e  inner JOIN dept d
ON e.deptno=d.deptno;

SELECT d.dname,e.* FROM emp e JOIN dept d
ON e.deptno=d.deptno
WHERE e.ename= 'SCOTT';
-- 조인과 그룹을 이용해서 댓글 카운터도 출려하는 게시판 리스트만들기
SELECT bod.bno, title||'['||count(*)||']', writer, bod.reg_date,view_count
FROM tbl_board BOD 
INNER JOIN tbl_reply REP ON bod.bno = rep.bno
GROUP BY bod.bno,title,writer,bod.reg_date,view_count
ORDER BY bod.bno;
--11 서브쿼리 :
-- 단일행서브쿼리 필드값을 비교할떄 , 비교하는 값이 단일한(필드값) 
-- 다중행서브쿼리 테이블값을 select쿼리로 생성(레코드형식)
-- 12.테이블 구조 생성(creatr) ,변경(alter;) ,삭제(drop;)
-- ERD 관계형 다이어그램으로 물리적 테이블 생성 (포워드엔지니어링)->아래 강제로 만들어봄 오케?
CREATE TABLE TBL_MEMBER_DEL
(
USER_ID VARCHAR(50) PRIMARY KEY
,USER_PW VARCHAR(255)
,USER_NAME VARCHAR(255)
,EMAIL VARCHAR(255)
,POINT NUMBER(11)
,ENABLED NUMBER(1)
,LEVELS VARCHAR(255)
,REG_DATE TIMESTAMP
,UPDATE_DATE TIMESTAMP
);
--ALTER 테이블로 필드명 변경 (아래)
DESC tbl_member_del;
ALTER TABLE tbl_member_del RENAME COLUMN email To user_email;
--DEPT 테이블의 deptno 숫자 2자리때문에 에러 -> 4자리로 크기변경
DESC dept; --단,작은자리 큰자리로 변경 문제 없음.
ALTER TABLE dept MODIFY(deptno NUMBER(4));
DROP TABLE tbl_member_del;--12장끝13장넘어가
-- 14장 트래잭션 DB단에서 사용하지 않고, 스프링단에서 트랜잭션을 사용 @Transactional 인페페이스를 사용
-- commit과 rollback;(DML문:insert,update,delete)
-- rollback은 제일 마지막 커밋된 상태로 되돌립니다.
--15장 pk생성시 자동으로 2가지 생성  -NOT NULL(빈값방지), UNIQUE(no중복)
--제약조건(contraint)이 자동생성, index(테이블)도 자동생성(검색시중요)
-- ERD로 게시판 테이블-[댓글|첨부파일] foreign key(외래키) 부자관계생성
--19장 사용자 추가(CreateWorkSpace)시 오라클데스크탑x,SQL플러스x, 
--대신 웹프로그램을 사용 (http://127.0.0.1:9000/apex/f?p=4950)
