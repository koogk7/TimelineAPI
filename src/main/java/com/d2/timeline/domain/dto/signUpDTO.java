package com.d2.timeline.domain.dto;

import com.d2.timeline.domain.vo.Member;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
public class SignUpDTO {

    @ApiModelProperty("이메일")
    private String email;

    @ApiModelProperty("닉네임")
    private String nickname;

    @ApiModelProperty("프로필 사진")
    private String profileImg;

    @ApiModelProperty("유저 권한")
    private List<String> roles;


    public Member transMember(){
        return Member.builder()
                .email(this.email)
                .nickname(this.nickname)
                .profileImg(this.profileImg)
                .roles(this.roles)
                .build();
    }
}
