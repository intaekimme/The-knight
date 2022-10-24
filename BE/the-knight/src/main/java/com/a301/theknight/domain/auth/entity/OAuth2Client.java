package com.a301.theknight.domain.auth.entity;

import com.a301.theknight.domain.common.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "oauth2_clients")
public class OAuth2Client extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String clientRegistrationId;
    private String principalName;
    private String accessToken;
    private String refreshToken;

    @Builder
    public OAuth2Client(String clientRegistrationId, String principalName, String accessToken, String refreshToken) {
        this.clientRegistrationId = clientRegistrationId;
        this.principalName = principalName;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
