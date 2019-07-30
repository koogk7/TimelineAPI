package com.d2.timeline.service;

import com.d2.timeline.domain.config.security.JwtTokenProvider;
import com.d2.timeline.domain.dao.MemberRepository;
import com.d2.timeline.domain.dto.SignUpDTO;
import com.d2.timeline.domain.exception.AuthNotAllowed;
import com.d2.timeline.domain.exception.EmailDuplicationException;
import com.d2.timeline.domain.exception.EntityNotFoundException;
import com.d2.timeline.domain.exception.LoginInvalidException;
import com.d2.timeline.domain.service.MemberService;
import com.d2.timeline.domain.vo.Member;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

import static com.d2.timeline.domain.Constant.MemberConstant.*;


@WebAppConfiguration
public class MemberServiceTest extends MockTest {
    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private Member member;
    private String email;
    private String password;
    private String encodedPW;
    private String role;
    private String token;
    private SignUpDTO signUpDTO;

    @Before
    public void setUp() throws Exception{

        email = "test@test.com";
        password = "1234";
        encodedPW = "encoded";
        role = "USER";
        token = "1312ndankwlq";

        member = Member.builder()
                .id(1L)
                .email(email)
                .password(encodedPW)
                .role(role)
                .build();
        signUpDTO = SignUpDTO.builder()
                .email(email)
                .role(role)
                .build();

        given(passwordEncoder.encode(password)).willReturn(encodedPW);
        given(jwtTokenProvider.createToken(email, role)).willReturn(token);

    }


    @Test
    public void 로그인_성공(){//OK
        //given
        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));
        given(passwordEncoder.matches(password, encodedPW)).willReturn(true);

        //when
        final String returnToken = memberService.signIn(email, password);

        //then
        assertEquals(token ,returnToken);
    }

    @Test(expected = LoginInvalidException.class)
    public void 패스워드불일치_로그인_실패(){//OK
        //given
        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));
        given(passwordEncoder.matches(password, encodedPW)).willReturn(false);

        //when
        final String returnToken = memberService.signIn(email, "4321");
    }

    @Test(expected = EntityNotFoundException.class)
    public void 존재하지않는이메일_로그인_실패(){//OK
        //given
        given(memberRepository.findByEmail("testFail@test.com")).willReturn(Optional.empty());

        //when
        final String returnToken = memberService.signIn("tesFail@test.com", password);

        //then
    }

    @Test
    public void 회원가입_성공(){//OK
        //given
        given(memberRepository.existsByEmail(email)).willReturn(false);
        given(memberRepository.save(member)).willReturn(member);
        //when
        final String returnMessage = memberService.signUp(signUpDTO, password);
        //then
        assertEquals(SIGNUP_OK_MSG, returnMessage);
    }

    @Test(expected = EmailDuplicationException.class)
    public void 중복이메일_회원가입_실패(){//OK
        //given
        given(memberRepository.existsByEmail(email)).willReturn(true);
        //when
        final String returnMessage = memberService.signUp(signUpDTO, password);
        //then
    }
    @Test(expected = AuthNotAllowed.class)
    public void 권한오류_회원가입_실페(){//OK
        //given
        final SignUpDTO failSignUpDTO = SignUpDTO.builder()
                .role("NOT USER")
                .build();
        given(memberRepository.existsByEmail(email)).willReturn(false);
        //when
        final String returnMessage = memberService.signUp(failSignUpDTO, password);
        //then

    }
}
