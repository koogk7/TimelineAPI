package com.d2.timeline.vo;

import com.d2.timeline.domain.vo.Member;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

public class MemberVoTest {
    @Test
    public void 회원객체_생성(){
        String email = "test@test.com";
        //when
        final Member member = Member.builder()
                .email(email)
                .nickname("네이버사랑해요")
                .password("1234")
                .profileImg("www.naver.com/my.jpg")
                .build();
        //then
        assertEquals(member.getEmail(), email);
    }
}
