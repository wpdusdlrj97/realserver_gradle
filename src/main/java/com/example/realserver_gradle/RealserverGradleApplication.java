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
public class RealserverGradleApplication
{
    //

    @Autowired
    private ResourceOwnerRepository repository;
    @Autowired private PasswordEncoder passwordEncoder;

    public static void main( String[] args )
    {
        SpringApplication.run(RealserverGradleApplication.class, args);
    }



}
