package com.example.realserver_gradle.config;


import com.example.realserver_gradle.security.CustomAuthenticationFailureHandler;
import com.example.realserver_gradle.security.CustomAuthenticationSuccessHandler;
import com.example.realserver_gradle.security.ResourceOwnerAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.PrintWriter;
import java.util.Arrays;

/**
 * @author yun-yeoseong
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    //@Autowired
    //private PasswordEncoder passwordEncoder;

    //인코더를 BCryptPasswordEncoder로 변환
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /*
     * 스프링 시큐리티가 사용자를 인증하는 방법이 담긴 객체.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    /*
     * 스프링 시큐리티 룰을 무시하게 하는 Url 규칙.
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/css/**")
                .antMatchers("/vendor/**")
                .antMatchers("/js/**")
                .antMatchers("/favicon*/**")
                .antMatchers("/img/**")
        ;
    }

    /*
     * 스프링 시큐리티 룰.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers("/login*/**").permitAll()
                .antMatchers("/logout*/**").permitAll()
                .antMatchers("/error**").permitAll()
                .anyRequest().authenticated()
                //로그아웃 관련 핸들러
                .and().logout()
                .logoutUrl("/logout")
                //.logoutSuccessHandler(logoutSuccessHandler()
                //HTTP 세션을 초기화하는 작업입니다.
                //로그아웃의 기본 URL(/logout) 이 아닌 다른 URL로 재정의합니다.
                //.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/logout/result")
                //HTTP 세션을 초기화하는 작업입니다.
                .invalidateHttpSession(true)
                .and().csrf()
                .disable()
                .addFilter(authenticationFilter())
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler((request, response, exception) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setHeader("Cache-Control", "no-cache");

                    PrintWriter writer = response.getWriter();
                    writer.println(new AccessDeniedException("access denied !"));
                });
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        //authenticationProvider.setPasswordEncoder(passwordEncoder);
        //위에 있는 BCryptPasswordEncoder로 변환
        //로그인시 입력한 패스워드가 BCryptPasswordEncoder로 변환되어서 DB에 있는 패스워드와 해시를 비교해서 인증한다
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    public ResourceOwnerAuthenticationFilter authenticationFilter() throws Exception {
        ResourceOwnerAuthenticationFilter filter = new ResourceOwnerAuthenticationFilter(authenticationManager());
        filter.setFilterProcessesUrl("/login");
        filter.setUsernameParameter("username");
        filter.setPasswordParameter("password");

        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        filter.setAuthenticationFailureHandler(authenticationFailureHandler());

        filter.afterPropertiesSet();

        return filter;
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint("/loginPage");
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        CustomAuthenticationSuccessHandler successHandler = new CustomAuthenticationSuccessHandler();
        // localhost:8080으로 접속하면 로그인 페이지("http://localhost:8080/loginPage")로 리다이렉트되고
        // 로그인 성공 시 시작하는 디폴트 경로
        successHandler.setDefaultTargetUrl("/index");
        ////만약 localhost:8080/pppp로 접속하면 일단 로그인 페이지로 이동해 로그인을 한 뒤에
        ////localhost:8080/pppp로 이동하는데 이때 pppp.html파일이 없다면 error.html이 출력된다

        return successHandler;
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        CustomAuthenticationFailureHandler failureHandler = new CustomAuthenticationFailureHandler();
        failureHandler.setDefaultFailureUrl("/loginPage?error=loginfali");

        return failureHandler;
    }
}
