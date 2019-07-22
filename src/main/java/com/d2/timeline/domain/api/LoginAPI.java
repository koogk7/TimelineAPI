package com.d2.timeline.domain.api;

import com.d2.timeline.domain.dto.SignUpDTO;
import com.d2.timeline.domain.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Sign")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class LoginAPI {

    private final MemberService memberService;

    @ApiOperation(value = "로그인")
    @PostMapping(value = "/login")
    public ResponseEntity<?> signIn(String email, String password){
        String msg = memberService.signIn(email, password);
        return ResponseEntity.ok(msg);
    }

    @ApiOperation(value = "회원가입")
    @PostMapping(value = "/join")
    public ResponseEntity<?> signUp(@RequestBody SignUpDTO signUpDTO, String password){
        String msg = memberService.signUp(signUpDTO, password);
        return ResponseEntity.ok(msg);
    }

}
