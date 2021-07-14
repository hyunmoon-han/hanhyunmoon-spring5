package com.edu.util;

import com.github.scribejava.core.builder.api.BaseApi;
import com.github.scribejava.core.builder.api.DefaultApi20;

public class NaverLoginApi extends DefaultApi20 {

	@Override
	public String getAccessTokenEndpoint() {
		// 토큰 체크용 EndPoint는 네이버의  Rest-Api URL을 명시합니다.(예, 중복 ID체크  RestApi URL->/ID_check?~)
		//엑서스 체크용 토큰 구하기RestApi URL
		return "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code";
	}

	@Override
	protected String getAuthorizationBaseUrl() {
		// 네이버 인증 체크용 RestApi URL
		return "https://nid.naver.com/oauth2.0/authorize";
	}

	//싱글톤 인스턴스 객체를 생성하기 위해서 인스턴스 홀더 클래스 상수(static,final)를 생성합니다.
	private static class InstanceHolder{
		private static final NaverLoginApi INSTANCE=new NaverLoginApi();
	}
	public static NaverLoginApi instance() {
		//싱글톤으로 인스턴스 객체를 생성하는 방식:메모리에 올라가는 객체를 1회만 생성하기 위해서
		return InstanceHolder.INSTANCE;
	}

	
}
