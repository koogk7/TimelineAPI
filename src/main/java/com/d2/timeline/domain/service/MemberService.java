package com.d2.timeline.domain.service;

import com.d2.timeline.domain.config.security.JwtTokenProvider;
import com.d2.timeline.domain.dao.MemberRepository;
import com.d2.timeline.domain.dto.SignUpDTO;
import com.d2.timeline.domain.exception.AuthNotAllowed;
import com.d2.timeline.domain.exception.AuthSizeException;
import com.d2.timeline.domain.exception.EmailDuplicationException;
import com.d2.timeline.domain.vo.Member;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.d2.timeline.domain.Constant.BoardConstant.OK_MSG;

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

    public String signIn(String email, String password){
        Member member = memberRepo.findByEmail(email).get();
        String encodePassword = passwordEncoder.encode(password);
        logger.info(member.toString());

        if(!passwordEncoder.matches(password, encodePassword))
            return "올바르지 않은 계정";
        return jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
    }

    public String signUp(SignUpDTO signUpDTO, String password){
        validateEmail(signUpDTO.getEmail());
        validateRole(signUpDTO.getRoles());
        Member member = signUpDTO.transMember();
        member.setPassword(passwordEncoder.encode(password));
        memberRepo.save(member);
        return OK_MSG;
    }

    private void validateEmail(String email){
        if (memberRepo.existsByEmail(email))
            throw new EmailDuplicationException("이미 존재하는 이메일입니다.");
    }

    private void validateRole(List<String> roles){
        if (roles.size() > 1)
            throw new AuthSizeException("하나의 권한만 가질 수 있습니다.");
        if (!roles.get(0).equals("USER"))
            throw new AuthNotAllowed("권한이 올바르지 않습니다.");
    }
}
