package com.example.realserver_gradle.entity;


import com.example.realserver_gradle.constrant.ClientType;
import lombok.Getter;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

/**
 * 
 * @author yun-yeoseong
 *
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
