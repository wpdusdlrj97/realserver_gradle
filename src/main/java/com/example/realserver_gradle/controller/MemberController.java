package com.example.realserver_gradle.controller;

import com.example.realserver_gradle.entity.ClientDto;
import com.example.realserver_gradle.entity.ResourceOwner;
import com.example.realserver_gradle.repository.ResourceOwnerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class MemberController {



    // 로그아웃 결과 페이지
    @GetMapping("/logout/result")
    // 절대경로 / 를 붙일 시 IDE에서는 오류가 나지 않지만 리눅스에서는 오류가 난다
    public String dispLogout() {
        return "logout";
    }


    // 메인
    // 로그인한 사람의 주체를 불러와서 아이디 출력해주기
    @GetMapping("/")
    public ModelAndView index(@AuthenticationPrincipal ResourceOwner user, ModelAndView mv) {

        String username = user.getUsername();

        System.out.println(username);

        mv.addObject("member", username);

        mv.setViewName("index");
        return mv;
    }

    // 메인
    // 인덱스로 리다이렉트
    @GetMapping("/index")
    public ModelAndView index2(@AuthenticationPrincipal ResourceOwner user, ModelAndView mv) {

        String username = user.getUsername();

        System.out.println(username);

        mv.addObject("member", username);

        mv.setViewName("index");
        return mv;
    }


}
