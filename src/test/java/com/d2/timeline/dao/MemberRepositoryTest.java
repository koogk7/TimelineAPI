package com.d2.timeline.dao;

import com.d2.timeline.domain.dao.MemberRepository;
import com.d2.timeline.domain.vo.Member;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@Transactional
public class MemberRepositoryTest extends RepositoryTest{

    @Autowired
    private MemberRepository memberRepository;

    private Member saveMember;
    final String testEmail = "cheese10yun@gmail.com";

    @Before
    public void setUp() throws Exception {
        saveMember = memberRepository.save(Member.builder()
                .email(testEmail).build());
    }

    @Test
    public void 이메일조회_테스트(){
        assertNotNull(memberRepository.findByEmail(testEmail));
    }


}
