package com.d2.timeline.domain.service;

import com.d2.timeline.domain.dao.MemberRepository;
import com.d2.timeline.domain.vo.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    @Autowired
    MemberRepository memberRepo;

    public Optional<Member> findByEmail(String email){
        return memberRepo.findByEmail(email);
    }

    public boolean existsByEmail(String email){
        return memberRepo.existsByEmail(email);
    }


}
