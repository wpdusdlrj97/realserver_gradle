package com.example.realserver_gradle.service;


import com.example.realserver_gradle.entity.ResourceOwner;
import com.example.realserver_gradle.repository.ResourceOwnerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * UserDetailsService 구현체. 
 * Resource Owner 인증에 사용되는 서비스클래스이다.
 * @author yun-yeoseong
 *
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {


	@Autowired
    private ResourceOwnerRepository resposiotry;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("UserDetailsServiceImpl.loadUserByUsername :::: {}",username);
		
		ResourceOwner user = resposiotry.findByUsername(username);
		
		if(ObjectUtils.isEmpty(user)) {
			throw new UsernameNotFoundException("Invalid resource owner, please check resource owner info !");
		}
		
		user.setAuthorities(AuthorityUtils.createAuthorityList(String.valueOf(user.getRole())));

		//MariaDB에 있는 user의 정보
		System.out.println(user);
		System.out.println(user.getId());
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());


		return user;
	}


	




}
