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

    @Column(length = 100)
    private String refreshToken;

    @Column
    private String image;

    @Builder
    public Member(String email, String password, String nickname, String image){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.image = image;
    }
}
