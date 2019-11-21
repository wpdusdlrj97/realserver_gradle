package com.example.realserver_gradle.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

    	//로그인 페이지로 리다이렉트 시키는 컨트롤러
        registry.addViewController("/loginPage")
                .setViewName("login");

		//로그인 후 oauth/authorize 전에 권한 승인을 요청받는 페이지로 이동
		registry.addViewController("/oauth/confirm_access")
				.setViewName("approval");

    }

}
