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