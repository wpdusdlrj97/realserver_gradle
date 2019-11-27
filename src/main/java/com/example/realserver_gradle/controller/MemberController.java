package com.example.realserver_gradle.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class MemberController {


    // 로그아웃 결과 페이지
    @GetMapping("/logout/result")
    // 절대경로 / 를 붙일 시 IDE에서는 오류가 나지 않지만 리눅스에서는 오류가 난다
    public String dispLogout() {
        return "logout";
    }




}
