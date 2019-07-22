package com.d2.timeline.domain.vo;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

//Todo 문서
/**
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        - Json 결과로 출력 안 할 데이터로 지정
    UserDetails Override 함수
        - getUsername : security에서 사용되는 회원구분 id, 여기서는 email로 사용
        - isAccountNonExpired : 계정이 만료가 안 되어 있는지
        - isAccountNonLocked: 계정이 잠기지 않았는지
        - isCredentialsNonExpired : 계정 패스워드가 만료 안되었는지
        - isEnabled : 계정이 사용 가능한지
 */

@Getter
@NoArgsConstructor
@Entity
@Table(name = "member_tb")
public class Member extends BaseEntity implements UserDetails {
    @Column(name = "email")
    private String email;

    @Setter
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password")
    private String password;

    @Setter
    @Column(name = "nickname")
    private String nickname;

    @Setter
    @Column(name = "profile_img")
    private String profileImg;

    @Column(name = "role")
    private String role;

    @Builder
    public Member(Long id, String email, String password,
                  String nickname, String profileImg,
                  String role){
        super(id);
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.role = role;
    }

    public String toString(){
        return "email( " + email + " ),  pw ( " + password + " )";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> rst = new ArrayList<>();
        rst.add(new SimpleGrantedAuthority(this.role));

        return  rst.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getUsername() {
        return this.email;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }
}