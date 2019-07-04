package com.d2.timeline.domain.service;

import com.d2.timeline.domain.dao.MemberRepository;
import com.d2.timeline.domain.vo.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class MemberFindService {

    private static final Logger logger = LoggerFactory.getLogger(MemberFindService.class);

    @Autowired
    MemberRepository memberRepository;

    public Member findByEmail(String email){
        logger.info("findByEmail");
        return memberRepository.findByEmail(email).orElseGet(()->{
            logger.error(email + "을 가진 계정이 존재하지 않습니다.");
            return Member.builder().email("NULL").build();
        });
    }

}
