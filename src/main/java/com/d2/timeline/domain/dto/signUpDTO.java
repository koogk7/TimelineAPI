package com.d2.timeline.domain.dto;

import com.d2.timeline.domain.vo.Member;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
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
