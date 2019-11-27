package com.example.realserver_gradle.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDataSourceConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationService;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.WhitelabelApprovalEndpoint;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;
import java.io.PrintWriter;


/**
 * Authorization Server Config
 * @author yun-yeoseong
 *
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	@Autowired
    private DataSource dataSource;
	@Autowired
    @Qualifier("clientDetailsServiceImpl") private ClientDetailsService clientDetailsService;
	@Autowired
    private UserDetailsService userDetailsService;


	/*
	AuthorizationServerSecurityConfigurer를 매개변수로 가진 설정 메소드이다.
	해당 메소드에서 설정하는 것은 AuthenticationEntryPoint, AccessDeniedHandler, PasswordEncoder 등을 설정할 수 있는 메소드이다.
	몇 개 테스트를 해보니 여기에 아무리 설정을 하여도 Spring Security 설정 클래스에 적용된 AuthenticationEntryPoint, AccessDeniedHandler, PasswordEncoder등을 사용한다.
	즉, Spring Security와 상호보완적이라고 표현한 것을 이것을 생각하여 말한것이다.

	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.accessDeniedHandler((request, response, exception)->{
										response.setContentType("application/json;charset=UTF-8");
							            response.setHeader("Cache-Control", "no-cache");
							            PrintWriter writer = response.getWriter();
							            writer.println(new AccessDeniedException("access denied !"));
									})
		.authenticationEntryPoint((request, response, exception)->{
									response.setContentType("application/json;charset=UTF-8");
						            response.setHeader("Cache-Control", "no-cache");
						            PrintWriter writer = response.getWriter();
						            writer.println(new AccessDeniedException("access denied !"));
								})
		;
	}



    /*
     ClientDetailsServiceConfigurer 매개변수를 가진 메소드이다.

     * Client에 대한 인증 처리를 위한 설정
     * 1) In-Memory 설정 - 기본 구현체 InMemoryClientDetailsService(Map에 클라이언트를 저장) ( -> clients.inMemory(...) )
     * 2) JDBC 설정 - 기본 구현체 JdbcClientDetailsService(JdbcTemplate를 이용한 DB이용) ( -> clients.jdbc(dataSource) )
     * 3) ClientDetailsService 설정 -> 직접 DB와 통신하게 되는 ClientDetailsService 인터페이스를 구현한 클래스를 등록 ( -> clients.withClientDetails(clientDetailsService) )
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		/*
		 * Client를 DB에서 관리하기 위하여 DataSource 주입.
		 * UserDetailsService와 동일한 역할을 하는 객체이다.

		 */


		clients.withClientDetails(clientDetailsService);

	}


	/*
	AuthorizationServerEndpointsConfigurer를 매개변수로 가진 메소드이다.
	사실 이 설정이 Authorization Server의 설정의 전부라고 해도 무방할정도로 중요한 설정 메소드이다.
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		
		endpoints
			.userDetailsService(userDetailsService) //refresh token 발급을 위해서는 UserDetailsService(AuthenticationManager authenticate()에서 사용)필요
			.authorizationCodeServices(new JdbcAuthorizationCodeServices(dataSource)) //authorization code를 DB로 관리 코드 테이블의 authentication은 blob데이터타입으로..
			.approvalStore(approvalStore()) //리소스 소유자의 승인을 추가, 검색, 취소하기 위한 메소드를 정의
			.tokenStore(tokenStore()) //토큰과 관련된 인증 데이터를 저장, 검색, 제거, 읽기를 정의
			.accessTokenConverter(accessTokenConverter()) //JWT 토큰
			;
	}
	
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
	
	@Bean
	public JdbcApprovalStore approvalStore() {
		return new JdbcApprovalStore(dataSource);
	}
	//(DB에서 관리하기 위하여 JdbcApprovalStore를 사용하였다.)


	//TokenStore의 종류로는 InMemoryTokenStore, JdbcTokenStore,JwtTokenStore,RedisTokenStore 등이 있지만,
	// 우리는 JwtTokenStore를 사용할 것이다.
	// JwtTokenStore를 빈으로 등록하였으며, JwtTokenStore 사용을 위한 JwtAccessTokenConverter를 빈으로 등록하였다.
	//
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey("non-prod-signature");
		
		return converter;
	}
	
	/*

	 * 새로운 클라이언트 등록을 위한 빈
	 * 예를 들어 우리의 애플리케이션이 페이스북의 기능을 사용하기 위하여 사전에 App을 등록하는 과정이 있을 것이다.
	 * 거기에 사용되는 서비스 클래스라고 보면된다.

	 */
	@Bean
	public ClientRegistrationService clientRegistrationService() {
		return new JdbcClientDetailsService(dataSource);
	}
}
