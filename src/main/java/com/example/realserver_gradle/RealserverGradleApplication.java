package com.example.realserver_gradle;

import com.example.realserver_gradle.constrant.UserRole;
import com.example.realserver_gradle.entity.ResourceOwner;
import com.example.realserver_gradle.repository.ResourceOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class RealserverGradleApplication implements CommandLineRunner
{

    @Autowired
    private ResourceOwnerRepository repository;
    @Autowired private PasswordEncoder passwordEncoder;

    public static void main( String[] args )
    {
        SpringApplication.run(RealserverGradleApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {

        ResourceOwner user = new ResourceOwner();
        user.setId(2l);
        user.setUsername("zzxcho11@naver.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(UserRole.ROLE_USER);

        repository.save(user);

    }

}
