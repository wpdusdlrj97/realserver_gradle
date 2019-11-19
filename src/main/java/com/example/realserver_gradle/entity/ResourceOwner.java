package com.example.realserver_gradle.entity;


import com.example.realserver_gradle.constrant.UserRole;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@ToString
@Entity
@Table(name = "RESOURCE_OWNER")
public class ResourceOwner implements UserDetails {

	private static final long serialVersionUID = -3699911426704150861L;

	@Id
	@Getter
    @Setter
	private Long id;
	
	@Getter
    @Setter
	private String username;
	
	@Column(length=400)
	@Getter
    @Setter
	private String password;
	
	@Column
    @Enumerated(EnumType.STRING)
	@Getter
    @Setter
	private UserRole role;
	
	@Transient
	@Getter
    @Setter
	private Collection<? extends GrantedAuthority> authorities;
	
	@Getter
    @Setter
	private boolean accountNonExpired = true;
	
	@Getter
    @Setter
	private boolean accountNonLocked = true;

	@Getter
    @Setter
	private boolean credentialsNonExpired = true;
	
	@Getter
    @Setter
	private boolean enabled = true;


}
