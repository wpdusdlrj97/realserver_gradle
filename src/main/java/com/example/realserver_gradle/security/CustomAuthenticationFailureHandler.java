package com.example.realserver_gradle.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 
 * @author yun-yeoseong
 *
 */
@Slf4j
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
		log.info("CustomAuthenticationFailureHandler.onAuthenticationFailure ::::");

		String username = request.getParameter("username");
		String errormsg = exception.getMessage();

		request.setAttribute("username", username);
		request.setAttribute("errormsg", errormsg);

		System.out.println("요청받은 이름"+username);
		System.out.println("요청받은 비밀번호"+username);
		System.out.println("에러 메시지"+errormsg);

		super.onAuthenticationFailure(request, response, exception);

	}
	
	
}
