--DESC : Description 테이블구조를 설명 
DESC dept;
--select:테이블 내용 조회,where 조회조건, as(Alias)별칭으로 필드명이 길떄 사용
--concat오라클내장함수는 
SELECT 
concat(deptno,' 번')as "부서번호",
dname as"부서명",
concat(loc,'시')as "위치"
FROM dept
WHERE loc='NEW YORK';
--DUAL 가상테이블이름. 테이블이 없는 내용을 select할때
SELECT concat('한','현문')from dual;
SELECT 3+5 as "더하기" from dual;
--레코드(row) : 컬럼(필드,field)들로 구성
SELECT * FROM emp WHERE sal>3000;--3000이상
SELECT count(*) FROM emp WHERE sal>3000;--명
SELECT count(*) as "연봉이 2000인직원" FROM emp WHERE sal>2000;
SELECT concat(count(*),'명') as "연봉이 2000인직원" FROM emp WHERE sal>2000; -- +명
-- 큰따움표(필드명) , 작은따움표(문자열처리=비교 ,결합)
SELECT*FROM emp WHERE ename='KING';
SELECT sal+1000 FROM emp WHERE ename='KING';-- 연봉상승
SELECT*FROM emp WHERE ename!='KING'; --아닌사람(!=,<>))
SELECT*FROM emp WHERE hiredate >='1982/01/01'; -- 날짜비교 1982년 이상인사람
-- OR =+(합집합) ,AND=X(동시,교집합)
SELECT*FROM  emp 
WHERE deptno = 10 OR job='MANAGER';

SELECT * FROM emp WHERE sal -- 범위(2000~3000)
BETWEEN 2000 AND 3000;
SELECT * FROM emp WHERE sal -- 범위(2000~3000아닌사람)
NOT BETWEEN 2000 AND 3000;
SELECT * FROM emp WHERE hiredate
BETWEEN '1980/01/01' AND '1980/12/31'; -- 혹은 있는지
SELECT * FROM emp WHERE comm in (300,500,1400);
SELECT * FROM emp WHERE comm  not in (300,500,1400);

-- LIKE 조회 ,와일드카드=% 조회 ~으로시작하는
-- (키워드)키워드에()있으면 함수 upper(),lower(),count() 등
SELECT * FROM emp WHERE ename LIKE 'M%';
SELECT * FROM emp WHERE ename LIKE upper('m%'); --대소문자 구분x
SELECT * FROM emp WHERE ename LIKE lower('M%');
SELECT * FROM emp WHERE ename LIKE '%H'; -- 으로 끝나는

-- null 널 이 중요한 이유: null값이 있으면 검색x
-- 그러면, null값이 필드에 있을때, 검색은? 
SELECT * FROM emp WHERE comm IS NULL; -- 있는지  검색
SELECT * FROM emp WHERE comm IS NOT NULL; -- djqt
SELECT * FROM emp WHERE comm IS NULL;
--NVL(Null Value)널 값을 대치하는 함수-- null일경우 지정값출력
--사원중에 커미션을 0원 받은 사람은?
--아래 E 는 emp테이블의 알리아스 별칭으로 E.*은 emp.*과 같은 내용
SELECT * FROM emp WHERE comm=0;   
SELECT NVL(comm,0),E.* FROM emp E WHERE NVL(comm,0)=0; 
-- NVL2(필드명, 널이아닐때100,널일때0),NVL(필드명,널일때0)
--오라클은 표준 쿼리x ,ANSI쿼리 표준입니다.
SELECT DECODE(comm,null,0,100), NVL2(comm,100,0),E.* FROM emp E WHERE NVL(comm,0)=0; 

SELECT DECODE(comm,null,0,100),E.* FROM emp E WHERE NVL(comm,0)=0;-- 아래 값은 같다.

 
SELECT DECODE(comm,null,0,100)
, NVL2(comm,100,0)
,E.* FROM emp E WHERE NVL(comm,0)=0;

SELECT NVL2(comm,100,0),E.* FROM emp E WHERE NVL(comm,0)=0;

SELECT 
CASE WHEN comm is null THEN 0
WHEN comm=0 THEN 100
WHEN comm >0 THEN comm
end as "CASE쿨력문"
,DECODE(comm,null,0,100)
,nvl2(comm,100,0)
,E.* FROM emp E WHERE NVL(comm,0)=0;

SELECT NVL2(comm,100,0),E.* FROM emp E WHERE NVL(comm,0)=0;
--연봉 기준으로 정렬 sort =순서order by 필드명 오름차순(초기값)|내림차순
SELECT E.sal,E.* FROM emp E ORDER BY E.sal ASC; -- 오름
SELECT E.sal,E.* FROM emp E ORDER BY E.sal DESC; -- 내림
--위 정렬에서 1등만 구할때 limit는 mysql(마리아DB)의 명령어.오라클에는x
-- AI(AutpIncrement) 자동증가값 명령 오라클x
--(중요)서브쿼리? (select쿼리가 중복되어 있는 ...)입니다
SELECT ROWNUM,E.*FROM (--테이블명
SELECT * FROM emp ORDER BY sal DESC
)E WHERE ROWNUM=1;
--위 서브쿼리문장을 해석 할때는 안쪽부터 해석

--중복레코드(row)를 제거하는 명령어 distinct
SELECT deptno AS "부서번호" FROM emp; -- 사원수대로 부서번호가 출력
SELECT DISTINCT deptno AS "부서번호" FROM emp;
--(중요)문자열을 연결할때 concat함수 외에 ||파이프 라인 2개를 겹처서 구현 
SELECT ename ||' is a '||job AS "문자열 연결" FROM emp;
--여기까지 select 마무리 
--이후에는 CRUD중에 Insert,Update,Delete 3개의 쿼리 
--함수용어 ABS(Absolute 절대값),Floor(바닥함수1.5=1)<->Ceil(천정함수1.5=2)
--ROUND(반올림),REUNC(Truncate 바리는함수),MOD(나머지구하는함수)
--Upper(대문자변환함수),Lower(소문자변환함수),Length(길이구하는함수)
--Instr(문자열의 위치를 구하는 함수),substr(매개변수로 입력한 숫자위치만큼 문자열을 추출하는)
--Lpad(LeftPadding왼쪽 여백),Rpad(오른쪽여백),레포트 프로그램에서 출력조정시 사용
--Trim(왼쪽,오른쪽 문자열을 잘라내는 함수)
--날짜 함수 sysdate,systimestamp 로 오라클전용 함수로써 게시물입력시간 ,회원등록시간
SELECT to_char(systimestamp ,'yyyy-mm-dd hh24:mi:ss:ff')FROM dual;
--위 to_char (날짜를 문자열로 변환)형변환 함수라고 합니다.
SELECT sysdate +1 FROM dual; -- 내일날짜
SELECT sysdate -1 FROM dual; -- 어제날짜
--아래는 6개월간 회원정보 수정이 없는 회원에게 공지서비스를 처리하는용도
SELECT * FROM
TBL_MEMBER
WHERE UPDATE_DATE < ADD_MENTHS(SYSDATE<-6);