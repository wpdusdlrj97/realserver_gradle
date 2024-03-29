package com.example.realserver_gradle.controller;


import com.example.realserver_gradle.constrant.ClientType;
import com.example.realserver_gradle.entity.Client;
import com.example.realserver_gradle.entity.ClientDto;
import com.example.realserver_gradle.entity.ResourceOwner;
import com.example.realserver_gradle.service.ClientDetailsServiceImpl;
import com.example.realserver_gradle.utils.Crypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

@Controller
public class ClientController {

	@Autowired
	private ClientDetailsServiceImpl clientRegistrationService;


	@Transactional
	@PostMapping("/save")
	public ModelAndView save(@Valid ClientDto clientDetails, ModelAndView mav , BindingResult bindingResult, @RequestParam(value = "name")String name,
							 @RequestParam(value = "redirectUri")String redirectUri) {

		if(bindingResult.hasErrors()) {
			return new ModelAndView("register");
		}
		String randomId = UUID.randomUUID().toString();
		String randomSecret = UUID.randomUUID().toString();


		//http://49.247.136.36/developer/register_app.html로부터 받아온 클라이언트 name
		System.out.println(name);
		//http://49.247.136.36/developer/register_app.html로부터 받아온 redirect uri
		System.out.println(redirectUri);

		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format2 = new SimpleDateFormat ( "yyyy년 MM월dd일 HH시mm분ss초");

		Date time = new Date();

		String time1 = format1.format(time);
		String time2 = format2.format(time);

		System.out.println(time1);
		System.out.println(time2);


		Client client = new Client();
		// 클라이언트 이름 (앱 이름)
		client.addAdditionalInformation("name", clientDetails.getName());
		// 리다이렉트 uri
		client.setRegisteredRedirectUri(new HashSet<>(Arrays.asList(clientDetails.getRedirectUri())));
		// 클라이언트 타입
		client.setClientType(ClientType.PUBLIC);
		// 클라이언트 아이디
		client.setClientId(randomId);
		// 클라이언트 시크릿
		client.setClientSecret(Crypto.sha256(randomSecret));
		// 액세스토큰 유효 시간
		client.setAccessTokenValiditySeconds(3600);
		// 클라이언트 기능
		client.setScope(Arrays.asList("read","write"));
		// approve 화면 건너뛰기
		//client.setAutoApproveScopes(Arrays.asList("read"));

		// 추가된 사항 - 클라이언트를 등록한 유저이름 추가해주기 (현재 로그인한 사용자 정보)
		client.addAdditionalInformation("resourceowner", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		// 추가된 사항 - 암호화 전 클라이언트 시크릿 DB 기입
		client.addAdditionalInformation("client_secret", randomSecret);
		// 추가된 사항 - 정렬을 위한 현재 시간 DB 기입
		client.addAdditionalInformation("time", time1);
		//client.setResourceIds(Arrays.asList(time1));


		System.out.println("암호화전"+randomSecret);



		// 자동으로 권한 승인이 필요없이 리다이렉트 시켜주는 페이지로
		// FitMe 홈페이지만 해당되게 해놓는다다
		// http://localhost:8080/oauth/authorize?client_id=FitMe_client_ID&redirect_uri=FitMe 메인홈페이지&response_type=code&scope=admin&state=xyz
		//client.setScope(Arrays.asList("read","write","admin"));
		//client.setAutoApproveScopes(Arrays.asList("admin"));

		clientRegistrationService.addClientDetails(client);
		System.out.println("클라이언트"+client);


		//mav.setViewName("redirect:/client/dashboard");
		mav.setViewName("redirect:http://49.247.136.36/developer/after_register_app.php");



		//mav.setViewName("redirect:http://49.247.136.36/fitme_developer.php");

		//여기가 get 방식으로 받아온 원인인듯
		//mav.addObject("clientId", randomId);
		//mav.addObject("clientSecret", randomSecret);
		System.out.println("클라이언트 객체"+mav);

		return mav;
	}

}
