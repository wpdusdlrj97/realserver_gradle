package com.example.realserver_gradle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RealserverGradleApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealserverGradleApplication.class, args);
    }

    //  DB에 값이 없을 경우 해당 메소드로 DB에 값을 주입한뒤 실행한다다	//
	/*

@SpringBootApplication
public class RealserverGradleApplication implements CommandLineRunner {

    @Autowired
    private ResourceOwnerRepository repository;
    //@Autowired private PasswordEncoder passwordEncoder;
    //패스워드 인코더 BCrypt로 지정
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static void main(String[] args) {
        SpringApplication.run(RealserverGradleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        ResourceOwner user = new ResourceOwner();
        user.setId(1l);
        user.setUsername("1223yys@naver.com");
        user.setPassword(encoder.encode("password"));
        user.setRole(UserRole.ROLE_USER);

        repository.save(user);

    }

}
	 */
}
