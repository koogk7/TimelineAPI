package com.d2.timeline.domain.vo;


import com.d2.timeline.domain.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "member_tb")
public class Member extends BaseEntity {
    @Column(name = "email")
    private String email;

    @Setter
    @Column(name = "password")
    private String password;

    @Setter
    @Column(name = "nickname")
    private String nickname;

    @Setter
    @Column(name = "profile_img")
    private String profileImg;

    @Builder
    public Member(String email, String password, String nickname, String profileImg){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImg = profileImg;
    }


}