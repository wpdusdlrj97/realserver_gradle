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
		
		return user;
	}

}
