#### 작업예정.
- 스프링 프로젝트 순서
- 1. JUnit > 마이바티스(DB핸들링) > AOP(다중게시판기능) > 페이징기능 > 검색기능 > 트랜잭션기능(게시판삭제시 댓글과 첨부파일까지 삭제처리되게) > 첨부파일기능(파일업로드/다운로드) > 스프링시큐리티(로그인 인증/권한체크) 
- 2. > 댓글기능(RestAPI-백엔드,Ajax처리-프런트단) > 네이버아이디로그인(외부API사용) > 헤로쿠클라우드배포
- 3. 문서작업(화면기획서XLS 제작, 화면설계서PPT 제작)
- 1달간(프론트엔드)은 주로 UI 계속진행(VS code개발환경) 하면서,
- 피곤할때, 자바|스프링(이클립스-egov전자정부프레임워크개발환경) 기본언어실습진행.
- 피곤할때, 오라클DB(SQL디벨러퍼개발환경) Ansi-SQL(표준SQL)기본언어실습진행CRUD.
- 2달째부터(백엔드), 주로 스프링으로 실습이 진행(납품용-이력서포트폴리오용).
- VS code에서 만든 UI를 이클립스에서 JSP로 변경 한 후 스프링웹프로젝트를 진행합니다.

#### 20210604(금) 작업.
- junit테스트시 SQL에러를 디버그하는 방법은 jdbc 드라이버->log4jdbc드라이버로 바꾸면 ,SQL에러가 나오게됩니다.
- 오라클: 07장 마무리 후 13장으로 바로 실습예정.(나머지는 아래 실습 후)
- 스프링: JUnit테스트: oldQueryTest메서드 실습 예정.
- 오라클: 08,09,10,11,12,14장 실습예정.
- 스프링: JUnit테스트: 회원관리부분 CRUD 테스트 진행예정.
- 오라클: 더미데이터 일괄등록 예정. 회원관리(100명), 게시판관리(공지사항50개,겔러리50개)
- 스프링: 관리자단, 회원관리부터 스프링 작업예정.

#### 20210603(목)작업.
- 작업비중(시간)= 관리자단프로그램(70%)-사용자단 (30%)
- 스피링에서 오라클 연동순서 2가지:
- 1. jdbc(Java DataBase Connection)확장 모듈  pom에추가.
- 2. 오라클 접속 드라이버 확장 모듈으 pom에 추가x 직접 jar파일을 추가.
- root-context.xml 파일에 오라클 커넥션 빈(스프링클래스)을 추가.
- 스프링이 관리하는 클래스(스프링빈)를 추가하는 방법 2-1:@Comtroller,@Repository,@Service,@Component
- 스프링이 관리하는 클래스를 추가하는 방법 2-2: -context.xml 에서 빈(bean태그)은 추가하면,스프링클래스됨 
- 오라클 05장부터 CRUD 실습 .
- admin회원관리(jsp디자인)부터 프로그램 작업시작 예정.

#### 20210602(수)작업.
- 예외처리하는 목적: 에러상황에서도 프로그램이 중단되지 않도록 하는 것이 주 목적 (에러상황을 세련되게 넘게처리)
- 스프링에서는 개발자가 처리하는 100의 코딩에서 2-3개정도(파일업로드,다운로드)이고, 
대부분 throws Exception스프링으로넘김.(예외의전파)
- thread :실행되는 프로그램을 말함. 1개의 프로그램에는 보통1개 쓰레드가 실행(웹프로그램은 1:1쓰레드임), 특이한 경우(http웹네트워크프로그램인경우-게시물 다운로드 쓰레드가 실행되면서, 리스트 버튼을 클릭했을때 목록보기 쓰레드가 동시에 실행-안드로이드앱에서필요) 여러개 쓰레드 실행이 필요한경우가 있습니다.
- 예외처리:throwable 오브젝트가 실행시 에러가 발생하면 ,예외(내용)을 보낼때(던질때) 사용하는 클래스Throwable클래스입니다.
- 스프링앱에서는 예외(에러)정보를 스프링처리합니다.
- 자바앱에서는 예외(에러)정보를 개발자가 처리합니다.(개발자가 손이 많이 갑니다) 지금실습
- 예외처리 : 에러발생시 프로그램이 중지되는 것을 방지하고 , 계속 프로그램을 사용할수 있도록 처리하는 방법=Exception
- 패키지는 관련기능을 한곳에 모아서 개발자가 관리하기 편하게 하기 위해서 구분한 이름(폴더명)
-Controller 클래스에서 model객체로 접근.
-Controller클래스 + home/index.jsp(화면) 한쌍입니다.그래서 컨트롤러 클래스에서 만든 변수를 index.jsp에서 사용가능 하게됩니다.
- 안드로이드앱= 액티비티(java)=레이아웃-xml(화면)
- c#닷넷= test.aspx  .cs(프로그램)+ test.aspx (화면) 한쌍
- 일반홈페이지(cafe24)-URL직접접근이 가능(보안위험높음)
- MVC웹프로그램 차이점-URL직접접근이 가능x (보안위험늦음)-관공서,대학,은행 주 사용
- MVC프로젝트에도 직접접근이 가능한 resources 폴더 직접접근 가능-static콘텐츠(html,css,js)를 모아 놓은 폴더. view폴더 jsp는 직접접근이불가능.
- view폴더처럼 직접접근이 불가능한 컨텐츠는 Controller(라워터)로접근하게됨.
- view/home/index.jsp액박처리 ok, 분해(header.jsp,footer.jsp,body.jsp )
- 개발순서:ERD제작->html제작->jsp제작(현재:관리자단(10기능) 작업 후 사용자단 (5기능))
- admin폴더 만든후 분해 ok.junit 실습 후 작업합니다.
- JUnit(Java Unit Test):자바 단위 테스트(서비스 프로그램 만들기전 프로토타입 시제품 제작)- JUnit CRUD작업 후 본격작업시작.
- 로거의 레벨:DEBUG > INFO > WARN > ERROR > FATAL
- 개발할때 : DEBUG 로거 레벨을 설정
- 납품할때 : WARN 으로 로거레벨을 변경
- 4장 패키지와 예외처리 실습.

#### 20210601(화) 작업.
- 에러:404(file not found 경로이상일때),500(자바프로그램에러)
- 프로젝드의 버전을 올립니다->외부라이브러리 부분의 버전을 올림-메이븐 컴파일러->pom.xml 버전을 관리  메이븐은 수정 및 추가->maven메뉴에서 maven
- Controller클래스에서 생성한 변수사용(여기까진자바) model객체전송(스프링) return home ->(전송은 스프링) home.jsp출력
- 출력하는 방식1:EL(Express Language)방식출력${변수}-25년전부터사용하던방식,
- 많이 사용하는 방식JSTL방식(*표준)사용. ->출력,반복,조건 등등 다양한 방식
- 언어를 배우는 순서:주석 > 디버그하는방법 > 변수 > 메서드(함수) > 클래스 >MVC모델
- 디버그하는 방법: 자바(System.out 이용해서 콜솔창에 출력)
- 스프링에서는 logger(로거)를 사용해서 디버그 내용을 출력.
- 스프링이 관리하는 클래스(빈)의 종류3가지:(사용법은 개발자가 만드는 클래스 상단에 입력)
- 1.@Controller 클래스빈  라우터역활하는 메서드를 만듭니다.(라우터 역활의 클래스기능)
- 2.@Service 클리스빈        비지니스로직에 관련된 메서드를 만듭니다.(개발자위주의 외부클래스기능)
- 3.@Repository 클래스빈  Model처리 메서드를 만듭니다.(DB핸들링외부클래스사용)
- 위3가지의 @를 사용하는 클래스는 DI(객체생성-의존성주입),AOP,IOC(제어의역전-자동메모리관리)

- 프런트 개발자가 작업한결과 백엔드 개발자가 확인.
- 일괄바꾸기1 "/home  -> "/resources/home
- 일괄바꾸기2 '/home  -> '/resources/home

- vs code 작업한 html를 이글립스에 배치를 합니다.(프론트개발자가 작업결과 백엔드개발자가 받아서 배치합니다.)
- resources폴더에 static컨텐츠(html,css,js,font) 배치합니다.
- ERD기준으로 게시판UI 마무리합니다.-board_write.html 부터 시작
- 오늘부터는 vs code->이클립스에서 작업합니다.
- 관리자단 AdminLTE적용-스프링시간 선택 후 아래 작업진행예정
- (회원관리CRUD-jsp, 게시판생성관리CRUD-jsp)
- html을 분해(1개의 페이지를 2개로 분해,header.jsp(메뉴공통),footer.jsp(공통))
- 이클립스로 작업한 html 내용을 -> resources 폴더(admin,home,root파일까지)로 배치
- 스프링 작업의 시작


#### 20210531(월)작업.
- 토드(sql디벨러퍼와 비슷상용)에서는 버튼으로 포워드앤지니어링이 처리.
- 무료sql디벨러퍼에서는 버튼x, DDL문을 실행해서 포워드엔지니어링을 처리.
- 참고, mysql용 워크벤치는 무료이지만, 버튼으로 포워드엔지니어링이 가능.
- ERD 그림 만든것을 물리 테이블로 만드는것:forward 엔지니어링 
- sql쿼리를 3가지 분류:
-(초기) DDL문 : Data Definition Language.  데터 정의 언어 create table문
-(유지)DCL문 : Data Control Language.	        데이터제어언어t commit,rollback
-(개발)DML문 : Data Manufacture Language. 데이터조작언어 CRUD쿼리.
- (데이터 딕셔너리를 모델과 동기화:자료사전(데이터의 데이터)을 DB테이블과 동기화)
- 데이터 딕셔너리는 메타 데이터와 동일합니다. : 콘텐츠x,구조,구성 정보만 존재
- 물리 테이블을 ERD그림으로 만드는 것 :reverse엔지니어링(기존DB분석시 사용)
- 스프링시큐리티는 2단계: 구현예정.
- 1 단계.로그인인증(ENABLED):AUTHENTICATION  (로그인x,글쓰기 기능x,관리자x)
- 2 단계.권한체크(LEVELS):AUTHORITY    (관리자-admin URL 접근가능, 일반회원은 사용자 홈페이지에서 	공지사항x갤러리만사용가능합니다.)
- 1:N관꼐? 게시물1개에 댓들 n개 달릴수 있음.
- ERD다이어그램은 그림일뿐, 물리DB(테이블)은 ERD기준으로 생성가능.
- PK를 AI(Auto Increment 자동증가)로 만들기:오라클에서는(시퀀스객체로기능구현),
  Mysql(AI라는 필드속성으로 처리)
- ERD에서 Relation 생성:게시판타입테이블(부)과 게시물관리테이블(자)의 관계를 생성 
- 부자의 관계는 부모의 PK를 기준,자식에게는 FK(Foreign Key)로 관계를 맺습니다.
- 장점은 게시판타입:notice,gallery 2가지가 존재한다면 ,
- Relation 관계가 필요한이유: 공지사항이나 갤러리 게시판이 아니면 ,개발자가 예외 처리하지 않아도 다른 게시판으로 	등록하는 상황이 발생이 되지 않게 됨.
- 데이터 무결성을 유지하는 역활.(외래키관계) 
- 필드 데이터형3:Timestamp(시간저장) 년월일시분초밀리초,Date(년월일까지)
- 필드데이터형2:CLOB(CHAR LOGN BYTE)글내용이 많을때 사용하는 데이터 형태
- 필드 데이터형:VARCHAR2(2바이트를 1글자-한글),VARCHAR(1바이트-1글자-영문전용)
- 테이블구성: 필드(컬럼,열)=테이블의  멤버변수(자바VO클래스의 멤버변수)
- 필드구성:PrimayKey(주키,기본키,고유키)= 테이블 영역에서 고유한 ID를 말함(중복되지 않는 값)
- PK에(개인을 식별할 수 있는 값):로그인id,개인이메일주소,주민번호 등등,게시판타입
- RDBMS:RelationDataBaseManagementSystem(관계형테이타베이스관리시스템)
- 오라클:테이블스페이스(TablesSpcae)=스키마(Scheme:Nysql)=데이터베이스(DB-MS-SQL)
- 지난주 금요일날,오라클 웹용 관리프로그램에서 XE라는 테이블스페이스를 XE사용자로 추가했음.
- ERD-객체관계가그림 :Entity= 테이블
- 데이터 모델:Model object를 형상화 시킨것을 모델이라고 함.데이터베이스를 말함.
- MVC 스프링프로젝트에서 M이 Modle이고,스프링프로젝트 구성중에 DB를 가리킴.
- 참고로,V는 view로 jsp를 말합니다.
- 참고로,C는 controller고 클래스를 말합니다. 
- 설치시 이름:system 암호:apmsetup.
-  스프링 프로젝트ERD 제작후 게시판UI디자인 적용.

#### 20210528(금) 작업.
- 추상클래스 실습은 오늘, 인터페이스 실습은 스프링에 엄청하시게 됩니다.
- extends관계클래스에서 this.(현재클래스) , super.(부모클래스)
- 다형성? 메서드 @오버라이드(상속), 메서드 오버로드(동일함수명의 파라미터의 개수, 종류틀린메서드) 구현됩니다.
- 오버라이드: 상속받아서 재정의 메서드 @오버라이드 = 다형성구현했다.
- 오라클11g ExpressEditon 설치예정. OracleXE112_Win64.zip
- SQL디벨러퍼를 다운받아서 압축풉니다. - ERD제작할 예정.
- StructuredQueryLanguage: 구조화된 질의 언어(오라클서버) -> 답변
- QueryString: URL에서 전송하는 값(서버에 질의문)을 문장으로 저장한내용 ?로 시작.
- CommendLineInterface : SQL*Plus 터미널화면으로 SQL쿼리 실행 X
- GrapicUserInter: SQL디벨러퍼 윈도우화면 에디터에서 SQL쿼리 실행 O
- SQL Developer 프로그램으로 ERD 다이어그램그림으로 프로그젝트 구조제작예정.
- EntityRelationDiagram: 테이블관계도형(아래) 
- 프로젝트 진행: 발주(클라이언트) -> 수주(개발사) -> 디자인UI(Client-Dev) -> ERD(이사,팀장) -> 코딩시작(ERD보면서+UI소스에 프로그램입히기)
- ERD에서 SQL쿼리가 생성 -> 물리테이블을 생성.
- -------------------------------------------------
- 첨부파일(자식)->게시판테이블(부모)<-댓글테이블(자식)
- 자바앱에서는 객체를 생성후 사용이 끝나면, 메모리에서 삭제하는 명령이 필수.
- 객체를 메모리에서 삭제: Object = null; Object.close();
- 스프링에서는 내장된 가비지컬렉터(garbage쓰레기수집가)가 자동실행 사용하지 않는 객체를 자동으로 지워줌.
- 위와 같이 개발자가 하던 메모리관리를 스프링의 대신 = IoC(Inversion Of Control)제어의 역전. 스프링 특징3가지(IoC, AOP, DI)
- 수업시작전, static메서드와 객체의 멤버매서드 비교설명
- Step2클래스에서 MeberService 클래스에 직접접근해서 메서드를 실행하려면 static으로 변경(컴파일시 실행가능한 상태로됨=메모리에 로딩)해야 함. 
대신, memberServie객체을 이용해서 메서드에 접근할때는 (호출시=런타임시 실행이가능한 상태로됨=메모리에 로딩)
- 클래스와 상속에 대해서 이론 및 실습
- https://github.com/miniplugin/spring5-kimilguk/blob/master/src/test/java/kr/or/test/ClassApp.java


#### 20210527(목) 작업.
- 3장 객체와 클래스 부터 시작
- 스프링에서는 클래스 extends(상속)보다는 인터페이스(implements)를 더 많이 사용합니다.
- abstract클래스(추상클래스): 구현내용이없이, 메서드명만 있는 클래스,
- 추상클래스는 매서드명만 있기(구현내용이 없기) 때문에, *객체로 만들수 없는 클래스 입니다.
- 부모로서 자식에게 상속만(메서드이름만)해서 재사용만 가능하게 됩니다.
- *객체로 만들수 없다? 실행가능한 클래스로 사용못하지만, 프로그램을 구조화 시킬때 필요.
- Step1 aaa = new Step1();//이렇게 생성자로 객체를 만들지 못함.
- final클래스: 부모가 될 수 없는 클래스(상속해서 재사용이 불가능한 클래스임)
- 접근제어자: public(패키지위치에 상관없이 접근-제일많이 사용),
- public 클래스안에서 멤버변수는 private을 제일많이 사용.(private보안성갖춤)
- public 클래스안에서 멤버메서드는 public을 많이 사용.(외부 다른 클래스의 메서드를실행가능) 대신, 변수가 private이기 때문에, 직접 수정않됨, 실행만 됩니다.
- 인스턴스(클라우드주로사용)=오브젝트(자바)=객체=실행중인클래스
- 클래스 자료형(int, long, String처럼)는 멤버변수, 멤버메서드, 서브클래스 등등 포함할 수 있습니다.=C언어 구조체 자료형
- 이클립스 커밋, 푸시가 않된때 커밋 팝업창 하단에 Force 항목을 체크하시고, 진행하면 됨.

#### 20210526(수)
- 붕어빵: 붕어빵틀(클래스) ->붕어빵 구어져서나오면 오브젝트(객체)
- OOP :자바를 객체지향(클래스를 실행 가능하게 하는) 프로그래밍
- 객체=오브젝트=인스턴스=실행가능한클래스
- 객체(object)와 클래스?클래스형 자료를 실행가능하게 만든 것을 오브젝트(객체)라고함.
- 추상화(abstract):오프라인업무-> 대표 업무만 뽑아낸 클래스 = 추상클래스
- 클래스는 멤버변수 +멤버메서드(실행)+서브 클래스 포함할수있다.
- 변하지 않는 변수 = 상수 = 변수명을 대문자(원주율)PI=3.141569...
- 보통 상수변수는 클래스이 제일 상단 위치합니다.
- 기본형 (정수자료형-소문자 시작): byte<short<int<long ,  10L(롱타입변수)
- 기본형 (실수자료형-소수점, 소문자시작): float<double , 12.35f(실수형숫자)
- 기본형 (문자형-소문자 시작,1개문자,''단따움표 값을 입력해야 에러가 나지 않음):char,1개의 문자만 입력
- 문자형에서 유니코드는 \u숫자 입니다.
- 기본형 (블린형-참,거짓):boolean, (0|1)
- 참조형 (대문자로시작):참조형 변수의 대표적인 변수 클래스 입니다.
- 클래스변수(저장된상태)->실행가능하게 되었을떄,인스턴스 변수라고함(메모리에로드된상태)
- 인스턴스라는 말보다는 오브젝트라는 말을 더 많이 사용합니다.
- string(문자열 클래스변수):대문자로 시작.
- 상수변수를 명시적으로 만들때: final로씀 int MAX = 100;
- for-each 반복문전까지 실습

#### 20210525(화)
-스프링 MVC프로젝트:ModelViewContrller?약자 MVC구조(웹프로그램구조)
- src/test/java폴더를 만들었습니다.:테스트작업은 이 폴에서 하라는 약속.
- src/main/java폴더가 진짜 프로그램작업을 하는 폴더.
- javac HelloWorld.jsva -encoding UTF-8 (한글 내용도 컴파일 됨)
- 위 java컴파일러로 실행한 결과->HelloWorld.class 실행파일이 생성됨.
- 주),클래스파일은 실행 패키지의 루트(최상위)에서 실행해야함.
- kr.or.test패키지 root폴더->srt/test/java폴더에서 실행을해야함
- java kr.or.test.HelloWorld 클래스를 실행하게 됨.
- 이클립스 나오기 전, 25년전 javac 컴파일에서 class프로그램을 만들었습니다.
- 지금은 터미널에서 javac 사용하지 않고 ,이클립스에서 바로 실행합니다.
- javac? 자바앱 컴파일러-> 클래스 실행파일을 만듬 .class(바이트코드)(자바환경JVM 에서실행)
- 메이븐?Maven : 웹프로그램 컴파일러->웹프로그램(앱) 실행파일을 만듬 .war(톰캣에서 실행)
- 메이븐이 컴파일할때, 자바소스만 컴파일하는 것이 아니고, 외부 라이브러리도 가져와서 바인딩(묶어줌)하게 됨=패키징 이라고 합니다. -.war(와르)파일로 패키징됨.
- 메이븐이 관리하는 외부 라이브러리파일(lib) 목록을 저장하는 파일 pom.xml 입니다
- pom.xml 에서 자바버전을 1.6 -> 1.8로 변경 후 메이븐을 업데이트 합니다.
- 외부 라이브러리 파일을 참조하는 방식을 영어로 =dependency 디펜던시
- jar파일? JavaARchive=jar 자바클래스를 패키징한 파일입니다.

#### 20210525(화)작업
- 자바 기초 실습
- 자바.java클래스파일을  컴파일해서 생성된 .class파일 실행하는 구조 .
- 파이썬 /자바스크립트는 인터프리터 방식을 실행
- 자바스크립트는 함수구조의 프로그래밍.=Function(함수)
- 자바는 객체지향 프로그래밍.(object oriented program)
- 오브젝트(객체)=실행가능한 class(클래스)
-아스키, 유니코드(UnicodeTypeFormat-8)
-아스키 코트IoT에서 데이터 전송시 필수로 확인해야합니다.0,1을전송->48,49값을 받습니다
-IOT프로그램시 하드웨어 값을 주고 받을때  if (var1 ==48){구현내용}
- 공유기가 하위 가질수 있는 사설 IP는 공유기본인IP + 255개(여유뷴)=256개 인터넷이 가능.
- 영어 인코딩은 아시크코드로 다표현가능
-단, 한글인코딩,영어인코딩,중국어/일본어 인코딩 등등은 아스키코드로 다 표현못함 그래서,유니코드등장.UTF-8
- Hex(16진수),Dec(10잔수),Char(문자)=127개 =아스키문자의 크기(컴문자-사람문자 1:1매칭)
-Oct(8진수),Bit(2진수)
-아스키코드- 7비트코드(127글자)->ANSI코드 8비트(256글자) -> Unicode(65536글자)-UTF-8
-UTF8mb4(이모지까지 포함:이모티콘 +이미지)
- 자바언어를 한다는 것은 컴파일 후 실행이 된다는 의미.-->실습예정
- 자바스크립트(파이썬)는 그냥 실행해서 프로그램을 만들어 집니다 -->실습예정