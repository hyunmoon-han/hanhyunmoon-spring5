package kr.or.test;
/**
 * 내부변수와 배열 사용에 대해서 실습 클래스
 * @author 한현문
 *
 */
public class Step1 {
	public void step1(){ //클래스명과 같은 이름의 메서드를 생성자 메서드라고함. 자동으로만들어집니다 .
		//중요한 이유는 다른 클래스에서 step1을 객체로 만들 때 생성자 메서드가 필요.
		//step1 aaa=new step1();
		//자바앱에서는 객체를 생성자 메서드로 만들때 필수
		//단, 스프링에선@Inject으로 객체를 만들어서 사용.
	}
	
	//멤버변수(전역변수)는 step1클래스에 영향을 모두 주는 변수
	//필드변수(내부변수|메서드변수)는 main메서드 내부에서만 영향을 주는 변수
	//private String name;//멤버변수사용예,step2클래스에서 사용예정.
	public static void main(String[] args) {
		// name,age,phoneNum 필수변수사용
		String name;	//문자열 이름 입력받는 변수
		int age;		//정수형 나이 입력반는 변수
		String phoneNum;//문자열 폰번호 입력반는 변수
		//변수에 값을 입력(아래)
		name ="홍길동";
		age =10;//자바스크립트 보다는 자료형 사용에 엄격합니다.
		phoneNum ="000-0000-0000";//1문자의 끝을 명시.
		printMenber(name,age,phoneNum);//프린트멤버라는 메서드를 호출(아규먼트1,args2,args3)
		name ="성춘향";
		age =18;
		phoneNum ="111-1111-1111";
		printMenber(name,age,phoneNum);
		name ="각시탈";
		age =28;
		phoneNum ="222-2222-2222";
		printMenber(name,age,phoneNum);
		//배열을 이용해서 입력을 좀더 편리하게 변경합니다,(아래)
		String[] names = {"홍길동","성춘향","각시탈"};
		int[] ages = {10,18,28};
		String[] phoneNums = {"000-0000-0000","111-1111-1111","222-2222-2222"};
		printMenber(names,ages,phoneNums);//장점: 메서드 1번 호출하면 3명이 모두 출력
		//오류-메서드명이같습니다.단, 로드된 파라미터 타입,개수가 다른 메서드를 호출=오버로드
	}

	private static void printMenber(String[] names, int[] ages, String[] phoneNums) {
		// for 반목문으로 3명을 한번에 출력
		int dataLength = names.length;//이름배열에 있는 사람의 명수를 구하기 목적은 아래 for반복의 끝값을 구함
		for(int i=0;i<dataLength;i++) {//고전적 for반복문
			System.out.println("입력하신 회원의 이름은 "+names[i]+"님,나이는 "+ages[i]+",폰번호는"+phoneNums[i]);
		}
	}

	private static void printMenber(String name, int age, String phoneNum) {
		// printMemver(피라미터1,param2,param3)
		System.out.println("입력하신 회원의 이름은 "+name+"님,나이는 "+age+",폰번호는"+phoneNum);
		//println 메서드는 출력후 줄바꿈 명령실행 println=print+ new line
	}

}
