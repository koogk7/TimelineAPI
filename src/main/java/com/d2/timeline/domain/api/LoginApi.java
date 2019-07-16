package com.d2.timeline.domain.api;

import com.d2.timeline.domain.dto.SignUpDTO;
import com.d2.timeline.domain.service.MemberService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Sign")
@RestController
@RequestMapping("/api")
public class LoginAPI {

    private static final Logger logger = LoggerFactory.getLogger(LoginAPI.class);

    @Autowired
    private MemberService memberService;


    @PostMapping(value = "/login")
    public String signIn(String email, String password){
        return memberService.signIn(email, password);
    }

    @PostMapping(value = "/join")
    public String signUp(@RequestBody SignUpDTO signUpDTO, String password){
        return memberService.signUp(signUpDTO, password);
    }


}
