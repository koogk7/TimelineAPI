package com.d2.timeline.domain.vo;

import com.d2.timeline.global.AbstractEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.time.LocalDateTime;

//@Builder
@Getter
@NoArgsConstructor
@Entity
@Table(name = "member_tb")
public class Member extends AbstractEntity {
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
    private String profile_img;

    @Builder
    public Member(String email, String password, String nickname, String profile_img){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profile_img = profile_img;
    }


}
