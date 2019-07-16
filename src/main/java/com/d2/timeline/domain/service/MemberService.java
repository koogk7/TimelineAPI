package com.d2.timeline.domain.service;

import com.d2.timeline.domain.config.security.JwtTokenProvider;
import com.d2.timeline.domain.dao.MemberRepository;
import com.d2.timeline.domain.dto.SignUpDTO;
import com.d2.timeline.domain.vo.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.d2.timeline.domain.Constant.BoardConstant.OK_MSG;

@Service
public class MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    @Autowired
    MemberRepository memberRepo;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<Member> findById(Long id){
        return memberRepo.findById(id);
    }


    public Optional<Member> findByEmail(String email){
        return memberRepo.findByEmail(email);
    }

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
        //검증하는 함수 구현 필요
        Member member = signUpDTO.transMember();
        member.setPassword(passwordEncoder.encode(password));
        memberRepo.save(member);
        return OK_MSG;
    }
}
