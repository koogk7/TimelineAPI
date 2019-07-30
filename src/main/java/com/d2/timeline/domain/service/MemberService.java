package com.d2.timeline.domain.service;

import com.d2.timeline.domain.config.security.JwtTokenProvider;
import com.d2.timeline.domain.dao.MemberRepository;
import com.d2.timeline.domain.dto.SignUpDTO;
import com.d2.timeline.domain.exception.*;
import com.d2.timeline.domain.vo.Member;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.d2.timeline.domain.Constant.MemberConstant.*;

@RequiredArgsConstructor
@Service
public class MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    private final MemberRepository memberRepo;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public boolean existsByEmail(String email){
        return memberRepo.existsByEmail(email);
    }

    public String signIn(String email, String requestPw){
        Member member = memberRepo.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않는 이메일입니다."));

        if(!passwordEncoder.matches(requestPw, member.getPassword())) {
            logger.error(requestPw);
            throw new LoginInvalidException("비밀번호가 일치하지 않습니다");
        }

        return jwtTokenProvider.createToken(member.getEmail(), member.getRole());
    }

    public String signUp(SignUpDTO signUpDTO, String password){
        validateEmail(signUpDTO.getEmail());
        validateRole(signUpDTO.getRole());
        Member member = signUpDTO.transMember();
        member.setPassword(passwordEncoder.encode(password));
        memberRepo.save(member);
        return SIGNUP_OK_MSG;
    }

    private void validateEmail(String email){
        if (memberRepo.existsByEmail(email))
            throw new EmailDuplicationException("이미 존재하는 이메일입니다.");
    }

    private void validateRole(String roles){
        if (!roles.equals("USER"))
            throw new AuthNotAllowed("권한이 올바르지 않습니다.");
    }
}
