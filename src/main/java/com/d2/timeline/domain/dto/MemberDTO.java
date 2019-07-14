package com.d2.timeline.domain.dto;

import com.d2.timeline.domain.vo.Member;
import lombok.Getter;

@Getter
public class MemberDTO {
    private Long id;
    private String nickname;
    private String email;
    private String profileImg;

    public MemberDTO(Member member){
        this.id = member.getId();
        if(this.id == -1){
            return ;
        }
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.profileImg = member.getProfileImg();
    }

    @Override
    public boolean equals(Object obj) {
        if(this.getId() != ((MemberDTO) obj).getId()) {
            return false;
        }
        return true;
    }
}
