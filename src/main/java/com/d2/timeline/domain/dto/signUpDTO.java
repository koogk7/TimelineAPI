package com.d2.timeline.domain.dto;

import com.d2.timeline.domain.vo.Member;

public class JoinDTO {
    private String email;
    private String password;
    private String nickname;
    private String profileImg;

    public Member transMember(){
        return Member.builder()
                .email(this.email)
                .password(this.password)
                .nickname(this.nickname)
                .profileImg(this.profileImg)
                .build();
    }
}
