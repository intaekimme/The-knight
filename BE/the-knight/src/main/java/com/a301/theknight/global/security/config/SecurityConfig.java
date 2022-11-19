package com.a301.theknight.global.security.config;

import com.a301.theknight.domain.auth.exception.RestAuthenticationEntryPoint;
import com.a301.theknight.domain.auth.filter.TokenAuthenticationFilter;
import com.a301.theknight.domain.auth.handler.CustomLogoutHandler;
import com.a301.theknight.domain.auth.handler.OAuth2AuthenticationFailureHandler;
import com.a301.theknight.domain.auth.handler.OAuth2AuthenticationSuccessHandler;
import com.a301.theknight.domain.auth.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.a301.theknight.domain.auth.service.CustomOAuth2UserService;
import com.a301.theknight.global.webmvc.properties.DomainProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final DomainProperties domainProperties;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomLogoutHandler customLogoutHandler,
                                           TokenAuthenticationFilter tokenAuthenticationFilter,
                                           RestAuthenticationEntryPoint restAuthenticationEntryPoint,
                                           OAuth2AuthorizedClientService oAuth2AuthorizedClientService,
                                           HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository,
                                           CustomOAuth2UserService customOAuth2UserService,
                                           OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
                                           OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler) throws Exception {

        http
                .headers().frameOptions().sameOrigin()
                .and()
                .cors().configurationSource(corsConfigurationSource(domainProperties))
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .logout()
                .logoutUrl("/api/logout")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                .invalidateHttpSession(false)
                .addLogoutHandler(customLogoutHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/oauth2/authorization/google", "/login/oauth2/**",
                        "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/favicon.ico",
                        "/websocket/**", "/api/hello").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .oauth2Login()
                .authorizedClientService(oAuth2AuthorizedClientService)
                .redirectionEndpoint()
                .and()
                .authorizationEndpoint()
                .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
                .and()
                .userInfoEndpoint().userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(DomainProperties domainProperties) {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin(domainProperties.getMain());
        configuration.addAllowedOrigin(domainProperties.getLocal());
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
