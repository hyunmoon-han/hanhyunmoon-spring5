package com.edu.util;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

/**
 * 이 클래스는 네이버REST_API서버 URL을 생성하는 기능의 클래스 입니다.
 * @author문
 *
 */
 
//아래sns~만드는 목적:로컬과 헤로쿠의 인증 ID와 Secret를 소스에서 변경하기 보단, 전역변수로 만들면 폄함.
@PropertySource("classpath:properties/sns.properties")
@Controller// 스프링 빈으로 등록되면, 인젝션으로 객체를 사용가능하게 됨.
public class NaverLoginController {
	@Value("${SnsClientID}")//스프링 빈의 전역변수를 가져올떄 @Resource와 비교하면 도움
	private String CLIENT_ID; //properties에 있는 전역변수값을 클래스 변수값으로 사용
	@Value("${SnsClientSecret}")
	private String CLIENT_SECRET;
	@Value("${SnsCallbackUrl}")
	private String REDIRECT_URL;//위 변수를 이용해서 만든 RestApi URL이후 인증성공후 이동할 URL
	//아래는 네이버에서 지정한 상수값을 사용해야 함.대문자로 타이핑하는 변수는 파이널(변수는 요기서만 사용하고 끝)이거나 static일떄 사용==이 클래스에서만 사용하고(final), 끝내는 변수(static)
	private final static String SESSION_STATE="oauth_state";
	////네이버에서 제공해주는 이름+이메일 정보등을 가져올수있는RestAPI URL설정
	private final static String PROFILE_API_URL= "https://openapi.naver.com/v1/nid/me";
	
	
	//네아로에서 Access Token(토큰:네이워크용데이터단위) 구하기
	
	//네이버에서 제공하는 인증URL구하는 메서드(사용자 로그인 폼에 ${url}로 제공하게됨)
	public String getAuthorizationUrl(HttpSession session) {
		//세션에 유효성 검증을 위하여 난수를 생성(아래)
		String state= generateRandomString();
		//생성한 난수값(state)을session변수에 저장
		setSession(session,state);
		//Scribe가입을 담당하는 외부 모듈 pom.xml에서 추가(OAuth2.0서비스)에서 제공하는 인증 URL생성(url제공할려고)
		OAuth20Service oauthService=new ServiceBuilder()
				.apiKey(CLIENT_ID)
				.apiSecret(CLIENT_SECRET)
				.callback(REDIRECT_URL)
				.state(state)
				.build(NaverLoginApi.instance());
		return oauthService.getAuthorizationUrl();
		//위 return값으로 네아로 인증 URL(RestApi서비스)이 생성됨.
	}

	private void setSession(HttpSession session, String state) {
		// Httpsession 클래스에 데이터를 저장
		session.setAttribute(SESSION_STATE,state);
		
	}

	private String generateRandomString() {
		// 새션 유효성 검증을 위한 난수 생성기
		return UUID.randomUUID().toString();
	}
	
	public OAuth2AccessToken getAccessToken(HttpSession session, String code, String state) throws IOException {
			// 네이버 인증RestApi에서 인증 데이터인 토큰 값 가져오기
			//  동작 전:현재 컨트롤러에서 발생된  세션의 state값, 동작후:콜백URL로 반환값에서 발생한 난수값 비교
			String sessionState=getSession(session);
			if(StringUtils.pathEquals(sessionState, state))	{		
				//동작전,후의 값이 같다며느 인증 토큰을 구현합니다.
				OAuth20Service oauthService = new ServiceBuilder()
						.apiKey(CLIENT_ID)
						.apiSecret(CLIENT_SECRET)
						.callback(REDIRECT_URL)
						.state(state)
						.build(NaverLoginApi.instance());
				//Scribe pom의 외뷰 모듈에서 제공하는 기능으로 토큰을 생성
				OAuth2AccessToken accessToken = oauthService.getAccessToken(code);//code는 네이버에서 반환값으로 주는 인증 성공 /실패(아이디 불,암호 불 틀리는 코드들)
				return accessToken;//인증정보=토큰 return 반환
			}			
			return null;
	}
	private String getSession(HttpSession session) {
		// http에서 session값 가져오기
		return (String) session.getAttribute(SESSION_STATE);
	}

	public String getUserProfile(OAuth2AccessToken oauthToken) throws IOException  {
		// 위 인증 데이터인 토큰값으로 네이버에서 프로필내용 가져오기
		OAuth20Service oauthService= new ServiceBuilder().
				apiKey(CLIENT_ID)
				.apiSecret(CLIENT_SECRET)
				.callback(REDIRECT_URL)
				.build(NaverLoginApi.instance());
			OAuthRequest request =new OAuthRequest(Verb.GET,PROFILE_API_URL,oauthService);
			oauthService.signRequest(oauthToken,request);
			Response response = request.send();//프로필 가져오는 객체를 실행
			return response.getBody();
		
	}

}
