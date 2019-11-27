package com.example.realserver_gradle.entity;


import com.example.realserver_gradle.constrant.ClientType;
import lombok.Getter;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

/**
 *
 /*
 * OAuth2.0에서 클라이언트 인증에 사용되는 객체이다.
 * ClientDetailsService에서 load하면 반환하는 객체이다.
 * Spring Security를 다루어보았다면, UserDetails와 동일한 역할을 하는 객체이다.
 */


public class Client extends BaseClientDetails {

	private static final long serialVersionUID = 5840531070411146325L;
	
	@Getter
	private ClientType clientType;


	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
		this.addAdditionalInformation("client_type", clientType.name());
	}

}
