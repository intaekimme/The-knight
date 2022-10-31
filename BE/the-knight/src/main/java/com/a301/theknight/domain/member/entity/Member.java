package com.a301.theknight.domain.member.entity;

import com.a301.theknight.domain.common.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 45)
    private String nickname;

    @Column(length = 200)
    private String refreshToken;

    @Column
    private String image;

    private String role;

    @Builder
    public Member(String email, String password, String nickname, String image, String role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.image = image;
        this.role = role;
    }

    //TODO  test 용도 추후 삭제
    public Member(long id, String email, String password, String nickname, String image, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.image = image;
        this.role = role;
    }

    public void saveRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void removeRefreshToken() {
        refreshToken = "";
    }

    public void updateInfo(String nickname, String image) {
        this.nickname = nickname;
        this.image = image;
    }
}
