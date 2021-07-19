package kr.or.test;

import java.util.Arrays;

public class HelloWorld {

	public static void main(String[] args) {
		// 자바앱은 진입 메서드가 필요, main()
		//classpath->exclude test code unchecked 해야지만,오류 안남
		System.out.println("헬로 자바");
		int[] questions= {1,2,3,4,5};
		Arrays.sort(questions);
		System.out.println();
		for(int a:questions) {
			System.out.println(a);
		}
	}

}
