-- DDL문 (create;alter;) ,DCL문 (commit;rollback;)
-- DML문 (Data Manufacture Language ) insert,update,delete
-- insert문 :테이블에 샤로우 레코드(row) 추가
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