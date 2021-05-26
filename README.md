#### 20210527(목) 작업예정.
-3장  객체와 클래스 부터 시작
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