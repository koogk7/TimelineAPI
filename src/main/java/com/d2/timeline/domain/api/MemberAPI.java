package com.d2.timeline.domain.api;

import com.d2.timeline.domain.dao.MemberRepository;
import com.d2.timeline.domain.vo.Member;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Api(tags = "Member for API")
@RestController
@RequestMapping("/api")
public class MemberAPI {

    @Autowired
    private MemberRepository memberRepository;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "친구추천", notes = "친구를 추천한다.")
    @GetMapping(value = "/users/recommand")
    public List<Member> recommandUser() {
        System.out.println("Entry");
        return memberRepository.findAll();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 단건 조회", notes = "회원번호로 회원을 조회한다")
    @GetMapping(value = "/user")
    public Optional<Member> findUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return memberRepository.findByEmail(email);
    }

}
