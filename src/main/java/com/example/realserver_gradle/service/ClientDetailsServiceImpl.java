package com.example.realserver_gradle.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.ClientAlreadyExistsException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

/**
 * 
 * @author yun-yeoseong
 *
 */
@Slf4j
@Primary
@Service
public class ClientDetailsServiceImpl extends JdbcClientDetailsService {

	//실제 비지니스로직이 담기는 서비스 클래스이다. JdbcClientDetailsService를 상속하여 사용하였다.
	public ClientDetailsServiceImpl(DataSource dataSource) {
		super(dataSource);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		log.info("ClientDetailsServiceImpl.loadClientByClientId :::: {}",clientId);
		return super.loadClientByClientId(clientId);
	}

	@Override
	public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {
		log.info("ClientDetailsServiceImpl.addClientDetails :::: {}",clientDetails.toString());
		super.addClientDetails(clientDetails);
	}

	@Override
	public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
		log.info("ClientDetailsServiceImpl.updateClientDetails :::: {}",clientDetails.toString());
		super.updateClientDetails(clientDetails);
	}

	@Override
	public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {
		log.info("ClientDetailsServiceImpl.updateClientSecret :::: {},{}",clientId,secret);
		super.updateClientSecret(clientId, secret);
	}

	@Override
	public void removeClientDetails(String clientId) throws NoSuchClientException {
		log.info("ClientDetailsServiceImpl.removeClientDetails :::: {}",clientId);
		super.removeClientDetails(clientId);
	}

	@Override
	public List<ClientDetails> listClientDetails() {
		List<ClientDetails> list = super.listClientDetails();
		log.info("ClientDetailsServiceImpl.listClientDetails :::: count = {}",list.size());
		return list;
	}

}
