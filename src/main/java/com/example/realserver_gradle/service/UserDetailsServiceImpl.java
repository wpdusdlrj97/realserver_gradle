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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

		/*
		try {
			sendPost(user.getUsername());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		 */



		return user;
	}


	/*
	private void sendPost(String getemail) throws Exception {
		URL url = new URL("http://49.247.136.36/fitme_session.php"); // 호출할 url
		Map<String,Object> params = new LinkedHashMap<>(); // 파라미터 세팅
		params.put("email", getemail);

		StringBuilder postData = new StringBuilder();
		for(Map.Entry<String,Object> param : params.entrySet()) {
			if(postData.length() != 0) postData.append('&');
			postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
			postData.append('=');
			postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
		}
		byte[] postDataBytes = postData.toString().getBytes("UTF-8");

		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		conn.setDoOutput(true);
		conn.getOutputStream().write(postDataBytes); // POST 호출

		int responseCode = conn.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

		String inputLine;
		while((inputLine = in.readLine()) != null) { // response 출력
			System.out.println(inputLine);
		}

		in.close();
	}


	 */




}
