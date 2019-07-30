package com.d2.timeline.domain.dto;

import com.d2.timeline.domain.vo.Member;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Setter
@Getter
public class SignUpDTO {


    @ApiModelProperty("이메일")
    @NotNull
    private String email;

    @ApiModelProperty("닉네임")
    @NotNull
    private String nickname;

    @ApiModelProperty("프로필 사진")
    @NotNull
    private String profileImg;

    @ApiModelProperty("유저 권한")
    @NotNull
    private String role;


    @Builder
    public SignUpDTO(String email, String nickname,
                     String profileImg, String role){
        this.email = email;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.role = role;
    }
    public Member transMember(){
        return Member.builder()
                .email(this.email)
                .nickname(this.nickname)
                .profileImg(this.profileImg)
                .role(this.role)
                .build();
    }
}
