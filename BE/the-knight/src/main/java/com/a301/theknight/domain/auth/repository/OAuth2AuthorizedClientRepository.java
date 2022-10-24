package com.a301.theknight.domain.auth.repository;

import com.a301.theknight.domain.auth.entity.OAuth2Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuth2AuthorizedClientRepository extends JpaRepository<OAuth2Client, Long> {
    OAuth2Client findByClientRegistrationIdEqualsAndPrincipalNameEquals(String clientRegistrationId, String principalName);
}
