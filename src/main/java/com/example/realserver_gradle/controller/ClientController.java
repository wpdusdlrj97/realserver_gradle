package com.example.realserver_gradle.controller;


import com.example.realserver_gradle.constrant.ClientType;
import com.example.realserver_gradle.entity.Client;
import com.example.realserver_gradle.entity.ClientDto;
import com.example.realserver_gradle.service.ClientDetailsServiceImpl;
import com.example.realserver_gradle.utils.Crypto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

@Controller
@RequestMapping("/client")
public class ClientController {
	
	@Autowired
    private ClientDetailsServiceImpl clientRegistrationService;
	
	@GetMapping("/register")
	public ModelAndView registerPage(ModelAndView mav) {
		mav.setViewName("client/register");
		mav.addObject("registry", new ClientDto());
		return mav;
	}
	
	@GetMapping("/dashboard")
    public ModelAndView dashboard(@ModelAttribute("clientId")String clientId
    							 , @ModelAttribute("clientSecret")String clientSecret
    							 , ModelAndView mv) {
        if(!StringUtils.isEmpty(clientId)) {
			mv.addObject("applications",
	                clientRegistrationService.loadClientByClientId(clientId));
	        mv.addObject("client_secret", clientSecret);
        }
        mv.setViewName("client/dashboard");
        return mv;
    }
	
	@Transactional
	@PostMapping("/save")
	public ModelAndView save(@Valid ClientDto clientDetails, ModelAndView mav , BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			return new ModelAndView("client/register");
		}
		String randomId = UUID.randomUUID().toString();
		String randomSecret = UUID.randomUUID().toString();
		
		Client client = new Client();
		client.addAdditionalInformation("name", clientDetails.getName());
		client.setRegisteredRedirectUri(new HashSet<>(Arrays.asList(clientDetails.getRedirectUri())));
		client.setClientType(ClientType.PUBLIC);
		client.setClientId(randomId);
		client.setClientSecret(Crypto.sha256(randomSecret));
		client.setAccessTokenValiditySeconds(3600);
		client.setScope(Arrays.asList("read","write"));
		// 자동으로 권한 승인이 필요없이 리다이렉트 시켜주는 페이지로
		// FitMe 홈페이지만 해당되게 해놓는다다
		// http://localhost:8080/oauth/authorize?client_id=FitMe_client_ID&redirect_uri=FitMe 메인홈페이지&response_type=code&scope=admin&state=xyz
		//client.setScope(Arrays.asList("read","write","admin"));
		//client.setAutoApproveScopes(Arrays.asList("admin"));

		clientRegistrationService.addClientDetails(client);
		
		mav.setViewName("redirect:/client/dashboard");
		mav.addObject("clientId", randomId);
		mav.addObject("clientSecret", randomSecret);
		
		return mav;
	}
	
	@GetMapping("/remove")
    public ModelAndView remove(
            @RequestParam(value = "client_id", required = false) String clientId) {

        clientRegistrationService.removeClientDetails(clientId);

        ModelAndView mv = new ModelAndView("redirect:/client/dashboard");
        mv.addObject("applications",
                clientRegistrationService.listClientDetails());
        return mv;
    }
}
